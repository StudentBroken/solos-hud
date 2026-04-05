package com.digits.sdk.android;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.SectionIndexer;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;

/* JADX INFO: loaded from: classes18.dex */
class CountryListAdapter extends ArrayAdapter<CountryInfo> implements SectionIndexer {
    private final HashMap<String, Integer> alphaIndex;
    private final HashMap<String, Integer> countryPosition;
    private String[] sections;

    public CountryListAdapter(Context context) {
        super(context, R.layout.dgts__country_row, android.R.id.text1);
        this.alphaIndex = new LinkedHashMap();
        this.countryPosition = new LinkedHashMap();
    }

    public void setData(List<CountryInfo> countries) {
        int index = 0;
        for (CountryInfo countryInfo : countries) {
            String key = countryInfo.country.substring(0, 1).toUpperCase(Locale.getDefault());
            if (!this.alphaIndex.containsKey(key)) {
                this.alphaIndex.put(key, Integer.valueOf(index));
            }
            this.countryPosition.put(countryInfo.country, Integer.valueOf(index));
            index++;
            add(countryInfo);
        }
        this.sections = new String[this.alphaIndex.size()];
        this.alphaIndex.keySet().toArray(this.sections);
        notifyDataSetChanged();
    }

    @Override // android.widget.SectionIndexer
    public Object[] getSections() {
        return this.sections;
    }

    @Override // android.widget.SectionIndexer
    public int getPositionForSection(int index) {
        if (this.sections == null || index <= 0) {
            return 0;
        }
        if (index >= this.sections.length) {
            index = this.sections.length - 1;
        }
        return this.alphaIndex.get(this.sections[index]).intValue();
    }

    @Override // android.widget.SectionIndexer
    public int getSectionForPosition(int position) {
        return 0;
    }

    public int getPositionForCountry(String country) {
        Integer position = this.countryPosition.get(country);
        if (position == null) {
            return 0;
        }
        return position.intValue();
    }
}
