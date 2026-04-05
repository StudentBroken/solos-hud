package com.kopin.solos.settings;

import android.content.Context;
import android.content.Intent;
import android.preference.Preference;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import com.digits.sdk.vcard.VCardConfig;
import com.kopin.solos.R;
import com.kopin.solos.SetupActivity;
import com.kopin.solos.storage.Bike;
import com.kopin.solos.storage.SQLHelper;

/* JADX INFO: loaded from: classes24.dex */
public class BikePreference extends Preference {
    BikeAdapter mBikeAdapter;

    public BikePreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public BikePreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public BikePreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BikePreference(Context context) {
        super(context);
    }

    @Override // android.preference.Preference
    protected View onCreateView(ViewGroup parent) {
        super.onCreateView(parent);
        this.mBikeAdapter = new BikeAdapter(getContext(), SQLHelper.getActiveBikesCursor(), true) { // from class: com.kopin.solos.settings.BikePreference.1
            @Override // com.kopin.solos.settings.BikeAdapter
            protected boolean deleteBike(Bike bike) {
                BikePreference.this.callChangeListener(null);
                return super.deleteBike(bike);
            }
        };
        View view = View.inflate(getContext(), R.layout.layout_manage_bikes, null);
        view.findViewById(R.id.btnAddBike).setOnClickListener(new View.OnClickListener() { // from class: com.kopin.solos.settings.BikePreference.2
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                BikePreference.this.onPrefAddBikeClick();
            }
        });
        ListView listView = (ListView) view.findViewById(android.R.id.list);
        listView.setAdapter((ListAdapter) this.mBikeAdapter);
        listView.setOnTouchListener(new View.OnTouchListener() { // from class: com.kopin.solos.settings.BikePreference.3
            @Override // android.view.View.OnTouchListener
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case 0:
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        break;
                    case 1:
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }
                v.onTouchEvent(event);
                return true;
            }
        });
        return view;
    }

    public void onPrefAddBikeClick() {
        Intent intent = new Intent(getContext(), (Class<?>) SetupActivity.class);
        intent.putExtra(SetupActivity.SETUP_INTENT_EXTRA_KEY, 7);
        intent.setFlags(VCardConfig.FLAG_APPEND_TYPE_PARAM);
        getContext().startActivity(intent);
    }
}
