package com.kopin.solos;

import android.app.Fragment;
import android.os.Bundle;
import com.kopin.solos.Fragments.TrainingDetailFragment;

/* JADX INFO: loaded from: classes24.dex */
public class TrainingSummaryActivity extends WorkoutSummaryActivity {
    public static final String TRAINING_EXTERNAL_ID = "training_external_id";

    @Override // com.kopin.solos.WorkoutSummaryActivity
    protected boolean cannotShow() {
        return false;
    }

    @Override // com.kopin.solos.WorkoutSummaryActivity
    protected void setContentView() {
        setContentView(R.layout.activity_training);
    }

    @Override // com.kopin.solos.WorkoutSummaryActivity
    protected void prepareActionBarTabs() {
        Bundle args = getIntent().getExtras();
        Fragment trainingDetailFragment = new TrainingDetailFragment();
        trainingDetailFragment.setArguments(args);
        this.mFragments.add(trainingDetailFragment);
        addActionBarTab(R.string.segment);
        if (this.mYourRideFragment != null) {
            this.mFragments.add(this.mYourRideFragment);
            addActionBarTab(getStringFromTheme(R.attr.strYourWorkout));
            setActionBarTitle(R.string.completed_training_summary);
            return;
        }
        setActionBarTitle(R.string.training_summary);
    }

    @Override // com.kopin.solos.WorkoutSummaryActivity
    protected int getFragmentContentViewId() {
        return R.id.fragment;
    }
}
