package com.facebook.share.internal;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.facebook.AccessToken;
import com.facebook.FacebookRequestError;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.R;
import com.facebook.internal.Validate;
import com.facebook.share.model.ShareContent;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.ShareOpenGraphContent;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes41.dex */
public class DeviceShareDialogFragment extends DialogFragment {
    private static final String DEVICE_SHARE_ENDPOINT = "device/share";
    private static final String REQUEST_STATE_KEY = "request_state";
    public static final String TAG = "DeviceShareDialogFragment";
    private static ScheduledThreadPoolExecutor backgroundExecutor;
    private volatile ScheduledFuture codeExpiredFuture;
    private TextView confirmationCode;
    private volatile RequestState currentRequestState;
    private Dialog dialog;
    private ProgressBar progressBar;
    private ShareContent shareContent;

    @Override // android.support.v4.app.Fragment
    @Nullable
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        RequestState requestState;
        View view = super.onCreateView(inflater, container, savedInstanceState);
        if (savedInstanceState != null && (requestState = (RequestState) savedInstanceState.getParcelable(REQUEST_STATE_KEY)) != null) {
            setCurrentRequestState(requestState);
        }
        return view;
    }

    @Override // android.support.v4.app.DialogFragment
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        this.dialog = new Dialog(getActivity(), R.style.com_facebook_auth_dialog);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.com_facebook_device_auth_dialog_fragment, (ViewGroup) null);
        this.progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
        this.confirmationCode = (TextView) view.findViewById(R.id.confirmation_code);
        Button cancelButton = (Button) view.findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(new View.OnClickListener() { // from class: com.facebook.share.internal.DeviceShareDialogFragment.1
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                DeviceShareDialogFragment.this.dialog.dismiss();
            }
        });
        TextView instructions = (TextView) view.findViewById(R.id.com_facebook_device_auth_instructions);
        instructions.setText(Html.fromHtml(getString(R.string.com_facebook_device_auth_instructions)));
        TextView title = (TextView) view.findViewById(R.id.com_facebook_device_dialog_title);
        title.setText(getString(R.string.com_facebook_share_button_text));
        this.dialog.setContentView(view);
        startShare();
        return this.dialog;
    }

    @Override // android.support.v4.app.DialogFragment, android.content.DialogInterface.OnDismissListener
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (this.codeExpiredFuture != null) {
            this.codeExpiredFuture.cancel(true);
        }
        Intent resultIntent = new Intent();
        finishActivity(-1, resultIntent);
    }

    @Override // android.support.v4.app.DialogFragment, android.support.v4.app.Fragment
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (this.currentRequestState != null) {
            outState.putParcelable(REQUEST_STATE_KEY, this.currentRequestState);
        }
    }

    private void finishActivity(int resultCode, Intent data) {
        if (isAdded()) {
            Activity activity = getActivity();
            activity.setResult(resultCode, data);
            activity.finish();
        }
    }

    private void detach() {
        if (isAdded()) {
            getFragmentManager().beginTransaction().remove(this).commit();
        }
    }

    public void setShareContent(ShareContent shareContent) {
        this.shareContent = shareContent;
    }

    private Bundle getGraphParametersForShareContent() {
        ShareContent content = this.shareContent;
        if (content == null) {
            return null;
        }
        if (content instanceof ShareLinkContent) {
            return WebDialogParameters.create((ShareLinkContent) content);
        }
        if (content instanceof ShareOpenGraphContent) {
            return WebDialogParameters.create((ShareOpenGraphContent) content);
        }
        return null;
    }

    private void startShare() {
        Bundle parameters = getGraphParametersForShareContent();
        if (parameters == null || parameters.size() == 0) {
            finishActivityWithError(new FacebookRequestError(0, "", "Failed to get share content"));
        }
        String accessToken = Validate.hasAppID() + "|" + Validate.hasClientToken();
        parameters.putString("access_token", accessToken);
        GraphRequest graphRequest = new GraphRequest(null, DEVICE_SHARE_ENDPOINT, parameters, HttpMethod.POST, new GraphRequest.Callback() { // from class: com.facebook.share.internal.DeviceShareDialogFragment.2
            @Override // com.facebook.GraphRequest.Callback
            public void onCompleted(GraphResponse response) {
                FacebookRequestError error = response.getError();
                if (error != null) {
                    DeviceShareDialogFragment.this.finishActivityWithError(error);
                    return;
                }
                JSONObject jsonObject = response.getJSONObject();
                RequestState requestState = new RequestState();
                try {
                    requestState.setUserCode(jsonObject.getString("user_code"));
                    requestState.setExpiresIn(jsonObject.getLong(AccessToken.EXPIRES_IN_KEY));
                    DeviceShareDialogFragment.this.setCurrentRequestState(requestState);
                } catch (JSONException e) {
                    DeviceShareDialogFragment.this.finishActivityWithError(new FacebookRequestError(0, "", "Malformed server response"));
                }
            }
        });
        graphRequest.executeAsync();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void finishActivityWithError(FacebookRequestError error) {
        detach();
        Intent intent = new Intent();
        intent.putExtra("error", error);
        finishActivity(-1, intent);
    }

    private static synchronized ScheduledThreadPoolExecutor getBackgroundExecutor() {
        if (backgroundExecutor == null) {
            backgroundExecutor = new ScheduledThreadPoolExecutor(1);
        }
        return backgroundExecutor;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setCurrentRequestState(RequestState currentRequestState) {
        this.currentRequestState = currentRequestState;
        this.confirmationCode.setText(currentRequestState.getUserCode());
        this.confirmationCode.setVisibility(0);
        this.progressBar.setVisibility(8);
        this.codeExpiredFuture = getBackgroundExecutor().schedule(new Runnable() { // from class: com.facebook.share.internal.DeviceShareDialogFragment.3
            @Override // java.lang.Runnable
            public void run() {
                DeviceShareDialogFragment.this.dialog.dismiss();
            }
        }, currentRequestState.getExpiresIn(), TimeUnit.SECONDS);
    }

    private static class RequestState implements Parcelable {
        public static final Parcelable.Creator<RequestState> CREATOR = new Parcelable.Creator<RequestState>() { // from class: com.facebook.share.internal.DeviceShareDialogFragment.RequestState.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            public RequestState createFromParcel(Parcel in) {
                return new RequestState(in);
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            public RequestState[] newArray(int size) {
                return new RequestState[size];
            }
        };
        private long expiresIn;
        private String userCode;

        RequestState() {
        }

        public String getUserCode() {
            return this.userCode;
        }

        public void setUserCode(String userCode) {
            this.userCode = userCode;
        }

        public long getExpiresIn() {
            return this.expiresIn;
        }

        public void setExpiresIn(long expiresIn) {
            this.expiresIn = expiresIn;
        }

        protected RequestState(Parcel in) {
            this.userCode = in.readString();
            this.expiresIn = in.readLong();
        }

        @Override // android.os.Parcelable
        public int describeContents() {
            return 0;
        }

        @Override // android.os.Parcelable
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.userCode);
            dest.writeLong(this.expiresIn);
        }
    }
}
