package com.kopin.solos;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.kopin.solos.RideControl;
import com.kopin.solos.analytics.Analytics;
import com.kopin.solos.common.DialogUtils;
import com.kopin.solos.share.Platforms;
import com.kopin.solos.storage.Bike;
import com.kopin.solos.storage.Ride;
import com.kopin.solos.storage.SQLHelper;
import com.kopin.solos.storage.SavedRide;
import com.kopin.solos.storage.SavedRides;
import com.kopin.solos.storage.SavedRun;
import com.kopin.solos.storage.SavedWorkout;
import com.kopin.solos.storage.Workout;
import com.kopin.solos.storage.util.Conversion;
import com.kopin.solos.storage.util.Utility;
import com.kopin.solos.util.PaceUtil;
import com.kopin.solos.util.RideActivity;
import com.kopin.solos.util.RunType;
import com.kopin.solos.view.SafeButton;
import java.util.Arrays;

/* JADX INFO: loaded from: classes24.dex */
public class FinishRideActivity extends ThemeActivity {
    private static final int MAX_RIDE_DESC_LENGTH = 80;
    private static final int MAX_RIDE_NAME_LENGTH = 25;
    private static final String TAG = "FinishRide";
    private SafeButton btnSaveRide;
    TextView mActivityTextView;
    TextView mBikeTextView;
    TextView mDescriptionTextView;
    private FirebaseAnalytics mFirebaseAnalytics;
    TextView mRideTitleTextView;
    TextView txtDistance;
    TextView txtDuration;
    TextView txtMetric3;
    TextView txtMetric4;
    private long mBikeId = -1;
    private int mActivityType = -1;
    private String tempRideName = "";
    private String tempDescription = "";
    private boolean newRide = false;
    private boolean dialogShowing = false;
    private final RideControl.RideObserver mRideObserver = new RideControl.RideObserver() { // from class: com.kopin.solos.FinishRideActivity.8
        @Override // com.kopin.solos.RideControl.RideObserver
        public void onRideConfig(Workout.RideMode mode, long rideOrRouteId) {
        }

        @Override // com.kopin.solos.RideControl.RideObserver
        public void onRideIdle() {
            FinishRideActivity.this.runOnUiThread(new Runnable() { // from class: com.kopin.solos.FinishRideActivity.8.1
                @Override // java.lang.Runnable
                public void run() {
                    if (!FinishRideActivity.this.isFinishing()) {
                        FinishRideActivity.this.finish();
                    }
                }
            });
        }

        @Override // com.kopin.solos.RideControl.RideObserver
        public void onRideReady(RideControl.StartMode startMode) {
        }

        @Override // com.kopin.solos.RideControl.RideObserver
        public void onRideStarted(Workout.RideMode mode) {
        }

        @Override // com.kopin.solos.RideControl.RideObserver
        public boolean okToStop() {
            return false;
        }

        @Override // com.kopin.solos.RideControl.RideObserver
        public void onRideStopped(SavedWorkout ride) {
        }

        @Override // com.kopin.solos.RideControl.RideObserver
        public void onRidePaused(boolean userOrAuto) {
        }

        @Override // com.kopin.solos.RideControl.RideObserver
        public void onRideResumed(boolean userOrAuto) {
        }

        @Override // com.kopin.solos.RideControl.RideObserver
        public void onNewLap(double lastDistance, long lastTime) {
        }

        @Override // com.kopin.solos.RideControl.RideObserver
        public void onCountdownStarted() {
        }

        @Override // com.kopin.solos.RideControl.RideObserver
        public void onCountdown(int counter) {
        }

        @Override // com.kopin.solos.RideControl.RideObserver
        public void onCountdownComplete(boolean wasCancelled) {
        }
    };

    @Override // com.kopin.solos.ThemeActivity, com.kopin.solos.common.BaseActivity, android.app.Activity
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish_ride);
        getActionBar().setDisplayShowTitleEnabled(true);
        this.mRideTitleTextView = (TextView) findViewById(R.id.txtRideTitle);
        this.mBikeTextView = (TextView) findViewById(R.id.txtBike);
        this.mActivityTextView = (TextView) findViewById(R.id.txtSport);
        this.mDescriptionTextView = (TextView) findViewById(R.id.txtDescr);
        this.txtDistance = (TextView) findViewById(R.id.txtDistance);
        this.txtDuration = (TextView) findViewById(R.id.txtDuration);
        this.txtMetric3 = (TextView) findViewById(R.id.txtMetric3);
        this.txtMetric4 = (TextView) findViewById(R.id.txtMetric4);
        this.btnSaveRide = (SafeButton) findViewById(R.id.btnSaveRide);
        this.btnSaveRide.setBackgroundResource(R.color.save_ride_btn_normal);
        this.btnSaveRide.setOnClickListener(new View.OnClickListener() { // from class: com.kopin.solos.FinishRideActivity.1
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                FinishRideActivity.this.saveWorkout(null);
            }
        });
        boolean rideValid = false;
        Intent intent = getIntent();
        if (intent != null && this.mSavedRide != null) {
            if (this.mSavedRide.getWorkoutMode() == Workout.RideMode.TRAINING) {
                findViewById(R.id.textShare).setVisibility(4);
                findViewById(R.id.layoutShare).setVisibility(4);
            }
            this.tempRideName = this.mSavedRide.getOrGenerateTitle();
            this.mRideTitleTextView.setText(this.tempRideName);
            setTitle(this.tempRideName);
            this.mDescriptionTextView.setText(this.mSavedRide.getComment());
            this.tempDescription = this.mSavedRide.getComment();
            this.newRide = getIntent().getBooleanExtra(ThemeActivity.EXTRA_NEW_RIDE, false);
            if (this.mSavedRide instanceof SavedRide) {
                updateRide();
            } else if (this.mSavedRide instanceof SavedRun) {
                updateRun();
            }
            String distance = String.format(this.mSavedRide.getDistanceForLocale() < 10.0d ? "%.1f" : "%.0f", Double.valueOf(this.mSavedRide.getDistanceForLocale()));
            this.txtDistance.setText("" + distance + " " + Utility.getUserDefinedUnitShort(this, 1, false));
            this.txtDuration.setText(Utility.formatTime(this.mSavedRide.getDuration()));
            rideValid = true;
        }
        if (!rideValid) {
            finish();
        }
        this.mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
    }

    private void updateRun() {
        findViewById(R.id.layoutBikeInput).setVisibility(8);
        findViewById(R.id.divider1).setVisibility(8);
        findViewById(R.id.divider2).setVisibility(8);
        findViewById(R.id.layoutSportInput).setTag(Integer.valueOf(R.array.sport_types_run));
        findViewById(R.id.btnBar).setEnabled(false);
        this.mActivityType = RunType.DEFAULT_TYPE_ID;
        this.mActivityTextView.setText(RunType.getLabel(this.mActivityType));
        this.txtMetric3.setText(PaceUtil.formatPace(!Ride.hasData(this.mSavedRide.getAveragePace()) ? 0.0d : this.mSavedRide.getAveragePaceForLocale(), true, this));
        this.txtMetric3.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_pace, 0, 0, 0);
        String unitOfCadence = getString(R.string.unit_cadence_run_short);
        this.txtMetric4.setText(!Ride.hasData(this.mSavedRide.getAverageCadence()) ? "0 " + unitOfCadence : String.format("%.0f", Double.valueOf(this.mSavedRide.getAverageCadence())) + " " + unitOfCadence);
        this.txtMetric4.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_cadence_run, 0, 0, 0);
    }

    private void updateRide() {
        Bike bike;
        findViewById(R.id.layoutSportInput).setTag(Integer.valueOf(R.array.sport_types));
        this.mBikeId = this.mSavedRide.getBikeId();
        if (this.mBikeId != -1 && (bike = SQLHelper.getBike(this.mBikeId)) != null && bike.getName() != null) {
            this.mBikeTextView.setText(bike.getName());
        }
        this.mActivityType = this.mSavedRide.getActivity();
        if (this.mActivityType != -1) {
            this.mActivityTextView.setText(RideActivity.RideType.getLabel(this.mActivityType));
        }
        this.txtMetric3.setText(!Ride.hasData(this.mSavedRide.getAverageSpeed()) ? "0 " + Conversion.getUnitOfSpeed(this) : String.format("%.1f", Double.valueOf(this.mSavedRide.getAverageSpeedForLocale())) + " " + Conversion.getUnitOfSpeed(this));
        this.txtMetric3.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_ic_speed_icon, 0, 0, 0);
        String unitOfCadence = getString(R.string.cadence_unit);
        this.txtMetric4.setText(!Ride.hasData(this.mSavedRide.getAverageCadence()) ? "0 " + unitOfCadence : String.format("%.0f", Double.valueOf(this.mSavedRide.getAverageCadence())) + " " + unitOfCadence);
        this.txtMetric4.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_cadence_small, 0, 0, 0);
    }

    @Override // com.kopin.solos.common.BaseActivity, android.app.Activity
    public void onBackPressed() {
        discardRide();
    }

    void showListDialog(final int titleResId, final int strArrayResId) {
        if (!this.dialogShowing) {
            this.dialogShowing = true;
            final String[] strArray = getResources().getStringArray(strArrayResId);
            Arrays.sort(strArray);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(titleResId).setItems(strArray, new DialogInterface.OnClickListener() { // from class: com.kopin.solos.FinishRideActivity.2
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialog, int which) {
                    int index = Utility.indexOf(strArray[which], strArrayResId, FinishRideActivity.this);
                    if (titleResId == R.string.finish_ride_bike_type_msg) {
                        FinishRideActivity.this.mBikeId = index;
                        FinishRideActivity.this.mBikeTextView.setText(strArray[which]);
                    } else {
                        FinishRideActivity.this.mActivityType = index;
                        FinishRideActivity.this.mActivityTextView.setText(strArray[which]);
                    }
                }
            });
            Dialog dialog = builder.create();
            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() { // from class: com.kopin.solos.FinishRideActivity.3
                @Override // android.content.DialogInterface.OnDismissListener
                public void onDismiss(DialogInterface dialog2) {
                    FinishRideActivity.this.dialogShowing = false;
                }
            });
            dialog.setCanceledOnTouchOutside(true);
            dialog.show();
            DialogUtils.setDialogTitleDivider(dialog);
        }
    }

    void showEditTextDialog(final int titleResId, String defaultValue, final int maxLength) {
        if (!this.dialogShowing) {
            this.dialogShowing = true;
            LayoutInflater inflater = getLayoutInflater();
            View view = inflater.inflate(R.layout.layout_custom_edittext, (ViewGroup) null);
            final EditText editText = (EditText) view.findViewById(android.R.id.edit);
            editText.setSingleLine(true);
            editText.setMaxLines(1);
            if (defaultValue == null) {
                defaultValue = "";
            }
            editText.append(defaultValue);
            DialogInterface.OnClickListener clickListener = new DialogInterface.OnClickListener() { // from class: com.kopin.solos.FinishRideActivity.4
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialog, int which) {
                    if (which == -1) {
                        Editable text = editText.getText();
                        String value = text.toString();
                        if (titleResId == R.attr.strWorkoutNameDialogTitle) {
                            FinishRideActivity.this.mRideTitleTextView.setText(value);
                            FinishRideActivity.this.tempRideName = value;
                            FinishRideActivity.this.setTitle(FinishRideActivity.this.tempRideName);
                        } else {
                            FinishRideActivity.this.mDescriptionTextView.setText(value);
                            FinishRideActivity.this.tempDescription = value;
                        }
                    }
                }
            };
            AlertDialog.Builder builder = new AlertDialog.Builder(this).setTitle(getStringFromTheme(titleResId)).setView(view).setPositiveButton(android.R.string.ok, clickListener).setNegativeButton(android.R.string.cancel, clickListener);
            final AlertDialog dialog = builder.create();
            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() { // from class: com.kopin.solos.FinishRideActivity.5
                @Override // android.content.DialogInterface.OnDismissListener
                public void onDismiss(DialogInterface dialog2) {
                    FinishRideActivity.this.dialogShowing = false;
                }
            });
            dialog.getWindow().setSoftInputMode(4);
            dialog.show();
            DialogUtils.setDialogTitleDivider(dialog);
            final TextView txtCharsRemaining = (TextView) view.findViewById(R.id.txtCharsRemaining);
            txtCharsRemaining.setVisibility(maxLength > 0 ? 0 : 8);
            TextWatcher mTextEditorWatcher = new TextWatcher() { // from class: com.kopin.solos.FinishRideActivity.6
                @Override // android.text.TextWatcher
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override // android.text.TextWatcher
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    int i = maxLength - s.length();
                    txtCharsRemaining.setText(FinishRideActivity.this.getResources().getQuantityString(R.plurals.label_chars_remaining, i, Integer.valueOf(i)));
                    if (s.length() == 0) {
                        dialog.getButton(-1).setEnabled(false);
                    } else {
                        dialog.getButton(-1).setEnabled(true);
                    }
                }

                @Override // android.text.TextWatcher
                public void afterTextChanged(Editable s) {
                }
            };
            if (maxLength > 0) {
                editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});
                int i = maxLength - editText.getText().toString().length();
                txtCharsRemaining.setText(getResources().getQuantityString(R.plurals.label_chars_remaining, i, Integer.valueOf(i)));
                editText.addTextChangedListener(mTextEditorWatcher);
            }
        }
    }

    public void onViewClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.layoutTitleInput /* 2131951826 */:
                showEditTextDialog(R.attr.strWorkoutNameDialogTitle, this.tempRideName, 25);
                break;
            case R.id.layoutSportInput /* 2131951830 */:
                showListDialog(R.string.finish_ride_activity_msg, ((Integer) v.getTag()).intValue());
                break;
            case R.id.layoutBikeInput /* 2131951834 */:
                showListDialog(R.string.finish_ride_bike_type_msg, R.array.bike_types);
                break;
            case R.id.layoutDescInput /* 2131951837 */:
                showEditTextDialog(R.attr.strWorkoutDescDialogTitle, this.tempDescription, 80);
                break;
            case R.id.imgFacebook /* 2131951847 */:
                shareRide(Platforms.Facebook);
                break;
            case R.id.imgTrainingPeaks /* 2131951848 */:
                shareRide(Platforms.TrainingPeaks);
                break;
            case R.id.imgTwitter /* 2131951849 */:
                shareRide(Platforms.Twitter);
                break;
            case R.id.imgStrava /* 2131951850 */:
                shareRide(Platforms.Strava);
                break;
            case R.id.imgMapMyRide /* 2131951851 */:
                shareRide(Platforms.UnderArmour);
                break;
            case R.id.btnDiscardRide /* 2131951853 */:
                discardRide();
                break;
        }
    }

    private void discardRide() {
        DialogInterface.OnClickListener clickListener = new DialogInterface.OnClickListener() { // from class: com.kopin.solos.FinishRideActivity.7
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int which) {
                if (which == -1) {
                    SavedRides.deleteWorkout(FinishRideActivity.this.mSavedRide);
                    SQLHelper.removeIncompleteWorkouts();
                    RideControl.reset();
                    FinishRideActivity.this.finish();
                }
            }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(this).setTitle(R.string.discard_ride).setMessage(getResourceIdFromTheme(R.attr.strWorkoutDiscardMsg)).setPositiveButton(android.R.string.ok, clickListener).setNegativeButton(android.R.string.cancel, clickListener);
        AlertDialog dialog = builder.create();
        dialog.show();
        DialogUtils.setDialogTitleDivider(dialog);
    }

    private void shareRide(Platforms platform) {
        if (Utility.isNetworkAvailable(this)) {
            saveWorkout(platform);
        } else {
            DialogUtils.showNoNetworkDialog(this);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void saveWorkout(Platforms platform) {
        Bundle params = new Bundle();
        params.putString("workout_mode", this.mSavedRide.getWorkoutMode().name());
        params.putString(Analytics.Param.WORKOUT_TYPE, this.mSavedRide.getSportType().name());
        this.mFirebaseAnalytics.logEvent(Analytics.Events.WORKOUT, params);
        this.mSavedRide.setTitle(this.mRideTitleTextView.getText().toString());
        this.mSavedRide.setComment(this.mDescriptionTextView.getText().toString());
        this.mSavedRide.setActivity(this.mActivityType);
        if (this.mSavedRide instanceof SavedRide) {
            this.mSavedRide.setBikeId(this.mBikeId);
        }
        showRideActivity(platform);
        RideControl.reset();
        finish();
    }

    private void showRideActivity(Platforms platform) {
        Intent intent = WorkoutSummaryActivity.intentToLaunch(this, this.mSavedRide, this.newRide, platform);
        startActivity(intent);
    }

    @Override // com.kopin.solos.common.BaseActivity, android.app.Activity
    protected void onStart() {
        super.onStart();
        if (this.newRide && this.mSavedRide.getTitle() != null) {
            finish();
        } else {
            RideControl.registerObserver(this.mRideObserver);
        }
    }

    @Override // com.kopin.solos.common.BaseActivity, android.app.Activity
    protected void onStop() {
        super.onStop();
        RideControl.unregisterObserver(this.mRideObserver);
    }
}
