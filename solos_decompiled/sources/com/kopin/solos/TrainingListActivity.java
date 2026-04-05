package com.kopin.solos;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.MenuItem;
import com.kopin.solos.Fragments.CompletedTrainingListFragment;
import com.kopin.solos.Fragments.UpcomingTrainingListFragment;

/* JADX INFO: loaded from: classes24.dex */
public class TrainingListActivity extends ThemeActivity {
    private Fragment[] mFragments = {new UpcomingTrainingListFragment(), new CompletedTrainingListFragment()};
    private ActionBar.TabListener mTabListener = new ActionBar.TabListener() { // from class: com.kopin.solos.TrainingListActivity.1
        @Override // android.app.ActionBar.TabListener
        public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
            if (!TrainingListActivity.this.isFinishing() && TrainingListActivity.this.getFragmentManager() != null && tab != null) {
                int pos = tab.getPosition();
                if (TrainingListActivity.this.mFragments[pos] != null && !TrainingListActivity.this.mFragments[pos].isAdded()) {
                    TrainingListActivity.this.getFragmentManager().beginTransaction().replace(R.id.fragment, TrainingListActivity.this.mFragments[pos], "" + pos).commit();
                }
            }
        }

        @Override // android.app.ActionBar.TabListener
        public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
        }

        @Override // android.app.ActionBar.TabListener
        public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
        }
    };

    @Override // com.kopin.solos.ThemeActivity, com.kopin.solos.common.BaseActivity, android.app.Activity
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training);
        ActionBar actionBar = getActionBar();
        actionBar.setTitle(R.string.training_workouts);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setNavigationMode(2);
        actionBar.addTab(actionBar.newTab().setText(getString(R.string.upcoming)).setTabListener(this.mTabListener));
        actionBar.addTab(actionBar.newTab().setText(R.string.completed).setTabListener(this.mTabListener));
        getFragmentManager().beginTransaction().replace(R.id.fragment, this.mFragments[0], "0").commit();
    }

    @Override // com.kopin.solos.common.BaseActivity, android.app.Activity
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() != 16908332) {
            return super.onOptionsItemSelected(item);
        }
        finish();
        return true;
    }
}
