package com.digits.sdk.android;

import java.text.Collator;
import java.util.Locale;

/* JADX INFO: loaded from: classes18.dex */
class CountryInfo implements Comparable<CountryInfo> {
    private final Collator collator = Collator.getInstance(Locale.getDefault());
    public final String country;
    public final int countryCode;

    public CountryInfo(String country, int countryCode) {
        this.collator.setStrength(0);
        this.country = country;
        this.countryCode = countryCode;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        CountryInfo that = (CountryInfo) obj;
        if (this.countryCode == that.countryCode) {
            if (this.country != null) {
                if (this.country.equals(that.country)) {
                    return true;
                }
            } else if (that.country == null) {
                return true;
            }
        }
        return false;
    }

    public int hashCode() {
        int result = this.country != null ? this.country.hashCode() : 0;
        return (result * 31) + this.countryCode;
    }

    public String toString() {
        return this.country + " +" + this.countryCode;
    }

    @Override // java.lang.Comparable
    public int compareTo(CountryInfo info) {
        return this.collator.compare(this.country, info.country);
    }
}
