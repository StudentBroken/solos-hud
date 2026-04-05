package com.kopin.solos;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Pair;
import android.view.MenuItem;
import com.kopin.solos.Fragments.TrainingSegmentSummaryFragment;
import com.kopin.solos.common.BaseFragmentActivity;
import com.kopin.solos.common.SportType;
import com.kopin.solos.graphics.TrainingGraph;
import com.kopin.solos.storage.SavedRides;
import com.kopin.solos.storage.SavedTraining;
import com.kopin.solos.storage.SavedTrainingWorkouts;
import com.kopin.solos.storage.SavedWorkout;

/* JADX INFO: loaded from: classes24.dex */
public class TrainingSegmentSummaryActivity extends BaseFragmentActivity implements TrainingGraph.GraphGestureListener {
    public static final String TRAINING_FLAT_STEP_POSITION_KEY = "training_flat_step_position_key";
    private final FragmentStatePagerAdapter mPagerAdapter = new FragmentStatePagerAdapter(getSupportFragmentManager()) { // from class: com.kopin.solos.TrainingSegmentSummaryActivity.2
        @Override // android.support.v4.app.FragmentStatePagerAdapter
        public Fragment getItem(int i) {
            return TrainingSegmentSummaryFragment.newInstance(TrainingSegmentSummaryActivity.this.mWorkoutId, TrainingSegmentSummaryActivity.this.mSavedTraining.getSport(), i);
        }

        @Override // android.support.v4.view.PagerAdapter
        public int getCount() {
            return TrainingSegmentSummaryActivity.this.mSavedTraining.getSegmentMap().size();
        }
    };
    private SavedTraining mSavedTraining;
    private SavedWorkout mSavedWorkout;
    private TrainingGraph mTrainingGraph;
    private ViewPager mViewPager;
    private long mWorkoutId;

    @Override // com.kopin.solos.common.BaseFragmentActivity, android.support.v4.app.FragmentActivity, android.support.v4.app.SupportActivity, android.app.Activity
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training_segment_summary);
        ActionBar actionBar = getActionBar();
        actionBar.setTitle(R.string.segment_summary);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
        Bundle args = getIntent().getExtras();
        this.mWorkoutId = args.getLong("ride_id", -1L);
        SportType sport = (SportType) args.getSerializable(ThemeActivity.EXTRA_WORKOUT_TYPE);
        this.mSavedWorkout = SavedRides.getWorkout(sport, this.mWorkoutId);
        this.mSavedTraining = SavedTrainingWorkouts.get(this.mSavedWorkout.getVirtualWorkoutId());
        this.mTrainingGraph = (TrainingGraph) findViewById(R.id.trainingGraph);
        int totalLaps = this.mSavedWorkout.getLaps().size();
        this.mTrainingGraph.setTraining(this.mSavedTraining, totalLaps, this);
        this.mViewPager = (ViewPager) findViewById(R.id.pager);
        this.mViewPager.setAdapter(this.mPagerAdapter);
        int selectedStepPosition = args.getInt(TRAINING_FLAT_STEP_POSITION_KEY, 0);
        selectPageWithStepPosition(selectedStepPosition);
        this.mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() { // from class: com.kopin.solos.TrainingSegmentSummaryActivity.1
            @Override // android.support.v4.view.ViewPager.OnPageChangeListener
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override // android.support.v4.view.ViewPager.OnPageChangeListener
            public void onPageSelected(int position) {
                TrainingSegmentSummaryActivity.this.selectPageIndicator(position);
            }

            @Override // android.support.v4.view.ViewPager.OnPageChangeListener
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void selectPageIndicator(int position) {
        Pair<Integer, Integer> pair = this.mSavedTraining.getSegmentMap().get(position);
        int segmentPosition = ((Integer) pair.first).intValue();
        int loopIndex = ((Integer) pair.second).intValue();
        SavedTraining.Segment segment = this.mSavedTraining.getSegments().get(segmentPosition);
        int stepCount = segment.getStepCount();
        int offset = segment.getFlatStepListPosition() + (segment.getStepCount() * loopIndex);
        this.mTrainingGraph.highlightSteps(offset, stepCount);
    }

    private void selectPageWithStepPosition(int flatStepPosition) {
        int i = 0;
        for (Pair<Integer, Integer> pair : this.mSavedTraining.getSegmentMap()) {
            int segmentPosition = ((Integer) pair.first).intValue();
            int loopIndex = ((Integer) pair.second).intValue();
            SavedTraining.Segment segment = this.mSavedTraining.getSegments().get(segmentPosition);
            int stepCount = segment.getStepCount();
            int offset = segment.getFlatStepListPosition() + (segment.getStepCount() * loopIndex);
            if (flatStepPosition >= offset && flatStepPosition < offset + stepCount) {
                this.mTrainingGraph.highlightSteps(offset, stepCount);
                this.mViewPager.setCurrentItem(i, true);
                return;
            }
            i++;
        }
    }

    @Override // com.kopin.solos.common.BaseFragmentActivity, android.app.Activity
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() != 16908332) {
            return super.onOptionsItemSelected(item);
        }
        finish();
        return true;
    }

    @Override // com.kopin.solos.graphics.TrainingGraph.GraphGestureListener
    public void onStepClick(int stepPosition) {
        selectPageWithStepPosition(stepPosition);
    }

    @Override // com.kopin.solos.graphics.TrainingGraph.GraphGestureListener
    public void onFlingLeft() {
        int currentIndex = this.mViewPager.getCurrentItem() + 1;
        if (currentIndex < this.mPagerAdapter.getCount()) {
            this.mViewPager.setCurrentItem(currentIndex, true);
            selectPageIndicator(currentIndex);
        }
    }

    @Override // com.kopin.solos.graphics.TrainingGraph.GraphGestureListener
    public void onFlingRight() {
        int currentIndex = this.mViewPager.getCurrentItem() - 1;
        if (currentIndex >= 0) {
            this.mViewPager.setCurrentItem(currentIndex, true);
            selectPageIndicator(currentIndex);
        }
    }
}
