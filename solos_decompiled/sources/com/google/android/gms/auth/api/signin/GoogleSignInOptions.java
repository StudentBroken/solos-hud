package com.google.android.gms.auth.api.signin;

import android.accounts.Account;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import com.google.android.gms.auth.api.signin.internal.zzn;
import com.google.android.gms.auth.api.signin.internal.zzo;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.internal.ReflectedParcelable;
import com.google.android.gms.common.internal.zzbr;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes3.dex */
public class GoogleSignInOptions extends com.google.android.gms.common.internal.safeparcel.zza implements Api.ApiOptions.Optional, ReflectedParcelable {
    private int versionCode;
    private Account zzajd;
    private boolean zzalj;
    private String zzalk;
    private final ArrayList<Scope> zzama;
    private final boolean zzamb;
    private final boolean zzamc;
    private String zzamd;
    private ArrayList<zzn> zzame;
    private Map<Integer, zzn> zzamf;
    public static final Scope zzalX = new Scope(Scopes.PROFILE);
    public static final Scope zzalY = new Scope("email");
    public static final Scope zzalZ = new Scope("openid");
    private static Scope SCOPE_GAMES = new Scope(Scopes.GAMES);
    public static final GoogleSignInOptions DEFAULT_SIGN_IN = new Builder().requestId().requestProfile().build();
    public static final GoogleSignInOptions DEFAULT_GAMES_SIGN_IN = new Builder().requestScopes(SCOPE_GAMES, new Scope[0]).build();
    public static final Parcelable.Creator<GoogleSignInOptions> CREATOR = new zzd();
    private static Comparator<Scope> zzalW = new zzc();

    public static final class Builder {
        private Account zzajd;
        private boolean zzalj;
        private String zzalk;
        private boolean zzamb;
        private boolean zzamc;
        private String zzamd;
        private Set<Scope> zzamg;
        private Map<Integer, zzn> zzamh;

        public Builder() {
            this.zzamg = new HashSet();
            this.zzamh = new HashMap();
        }

        public Builder(@NonNull GoogleSignInOptions googleSignInOptions) {
            this.zzamg = new HashSet();
            this.zzamh = new HashMap();
            zzbr.zzu(googleSignInOptions);
            this.zzamg = new HashSet(googleSignInOptions.zzama);
            this.zzamb = googleSignInOptions.zzamb;
            this.zzamc = googleSignInOptions.zzamc;
            this.zzalj = googleSignInOptions.zzalj;
            this.zzalk = googleSignInOptions.zzalk;
            this.zzajd = googleSignInOptions.zzajd;
            this.zzamd = googleSignInOptions.zzamd;
            this.zzamh = GoogleSignInOptions.zzw(googleSignInOptions.zzame);
        }

        private final String zzbR(String str) {
            zzbr.zzcF(str);
            zzbr.zzb(this.zzalk == null || this.zzalk.equals(str), "two different server client ids provided");
            return str;
        }

        public final Builder addExtension(GoogleSignInOptionsExtension googleSignInOptionsExtension) {
            if (this.zzamh.containsKey(1)) {
                throw new IllegalStateException("Only one extension per type may be added");
            }
            this.zzamh.put(1, new zzn(googleSignInOptionsExtension));
            return this;
        }

        public final GoogleSignInOptions build() {
            if (this.zzalj && (this.zzajd == null || !this.zzamg.isEmpty())) {
                requestId();
            }
            return new GoogleSignInOptions(3, new ArrayList(this.zzamg), this.zzajd, this.zzalj, this.zzamb, this.zzamc, this.zzalk, this.zzamd, this.zzamh, null);
        }

        public final Builder requestEmail() {
            this.zzamg.add(GoogleSignInOptions.zzalY);
            return this;
        }

        public final Builder requestId() {
            this.zzamg.add(GoogleSignInOptions.zzalZ);
            return this;
        }

        public final Builder requestIdToken(String str) {
            this.zzalj = true;
            this.zzalk = zzbR(str);
            return this;
        }

        public final Builder requestProfile() {
            this.zzamg.add(GoogleSignInOptions.zzalX);
            return this;
        }

        public final Builder requestScopes(Scope scope, Scope... scopeArr) {
            this.zzamg.add(scope);
            this.zzamg.addAll(Arrays.asList(scopeArr));
            return this;
        }

        public final Builder requestServerAuthCode(String str) {
            return requestServerAuthCode(str, false);
        }

        public final Builder requestServerAuthCode(String str, boolean z) {
            this.zzamb = true;
            this.zzalk = zzbR(str);
            this.zzamc = z;
            return this;
        }

        public final Builder setAccountName(String str) {
            this.zzajd = new Account(zzbr.zzcF(str), "com.google");
            return this;
        }

        public final Builder setHostedDomain(String str) {
            this.zzamd = zzbr.zzcF(str);
            return this;
        }
    }

    GoogleSignInOptions(int i, ArrayList<Scope> arrayList, Account account, boolean z, boolean z2, boolean z3, String str, String str2, ArrayList<zzn> arrayList2) {
        this(i, arrayList, account, z, z2, z3, str, str2, zzw(arrayList2));
    }

    private GoogleSignInOptions(int i, ArrayList<Scope> arrayList, Account account, boolean z, boolean z2, boolean z3, String str, String str2, Map<Integer, zzn> map) {
        this.versionCode = i;
        this.zzama = arrayList;
        this.zzajd = account;
        this.zzalj = z;
        this.zzamb = z2;
        this.zzamc = z3;
        this.zzalk = str;
        this.zzamd = str2;
        this.zzame = new ArrayList<>(map.values());
        this.zzamf = map;
    }

    /* synthetic */ GoogleSignInOptions(int i, ArrayList arrayList, Account account, boolean z, boolean z2, boolean z3, String str, String str2, Map map, zzc zzcVar) {
        this(3, (ArrayList<Scope>) arrayList, account, z, z2, z3, str, str2, (Map<Integer, zzn>) map);
    }

    @Nullable
    public static GoogleSignInOptions zzbQ(@Nullable String str) throws JSONException {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        JSONObject jSONObject = new JSONObject(str);
        HashSet hashSet = new HashSet();
        JSONArray jSONArray = jSONObject.getJSONArray("scopes");
        int length = jSONArray.length();
        for (int i = 0; i < length; i++) {
            hashSet.add(new Scope(jSONArray.getString(i)));
        }
        String strOptString = jSONObject.optString("accountName", null);
        return new GoogleSignInOptions(3, (ArrayList<Scope>) new ArrayList(hashSet), !TextUtils.isEmpty(strOptString) ? new Account(strOptString, "com.google") : null, jSONObject.getBoolean("idTokenRequested"), jSONObject.getBoolean("serverAuthRequested"), jSONObject.getBoolean("forceCodeForRefreshToken"), jSONObject.optString("serverClientId", null), jSONObject.optString("hostedDomain", null), new HashMap());
    }

    private final JSONObject zzmx() {
        JSONObject jSONObject = new JSONObject();
        try {
            JSONArray jSONArray = new JSONArray();
            Collections.sort(this.zzama, zzalW);
            ArrayList<Scope> arrayList = this.zzama;
            int size = arrayList.size();
            int i = 0;
            while (i < size) {
                Scope scope = arrayList.get(i);
                i++;
                jSONArray.put(scope.zzpn());
            }
            jSONObject.put("scopes", jSONArray);
            if (this.zzajd != null) {
                jSONObject.put("accountName", this.zzajd.name);
            }
            jSONObject.put("idTokenRequested", this.zzalj);
            jSONObject.put("forceCodeForRefreshToken", this.zzamc);
            jSONObject.put("serverAuthRequested", this.zzamb);
            if (!TextUtils.isEmpty(this.zzalk)) {
                jSONObject.put("serverClientId", this.zzalk);
            }
            if (!TextUtils.isEmpty(this.zzamd)) {
                jSONObject.put("hostedDomain", this.zzamd);
            }
            return jSONObject;
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static Map<Integer, zzn> zzw(@Nullable List<zzn> list) {
        HashMap map = new HashMap();
        if (list == null) {
            return map;
        }
        for (zzn zznVar : list) {
            map.put(Integer.valueOf(zznVar.getType()), zznVar);
        }
        return map;
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        try {
            GoogleSignInOptions googleSignInOptions = (GoogleSignInOptions) obj;
            if (this.zzame.size() > 0 || googleSignInOptions.zzame.size() > 0 || this.zzama.size() != googleSignInOptions.zzmy().size() || !this.zzama.containsAll(googleSignInOptions.zzmy())) {
                return false;
            }
            if (this.zzajd == null) {
                if (googleSignInOptions.zzajd != null) {
                    return false;
                }
            } else if (!this.zzajd.equals(googleSignInOptions.zzajd)) {
                return false;
            }
            if (TextUtils.isEmpty(this.zzalk)) {
                if (!TextUtils.isEmpty(googleSignInOptions.zzalk)) {
                    return false;
                }
            } else if (!this.zzalk.equals(googleSignInOptions.zzalk)) {
                return false;
            }
            if (this.zzamc == googleSignInOptions.zzamc && this.zzalj == googleSignInOptions.zzalj) {
                return this.zzamb == googleSignInOptions.zzamb;
            }
            return false;
        } catch (ClassCastException e) {
            return false;
        }
    }

    public final Account getAccount() {
        return this.zzajd;
    }

    public Scope[] getScopeArray() {
        return (Scope[]) this.zzama.toArray(new Scope[this.zzama.size()]);
    }

    public final String getServerClientId() {
        return this.zzalk;
    }

    public int hashCode() {
        ArrayList arrayList = new ArrayList();
        ArrayList<Scope> arrayList2 = this.zzama;
        int size = arrayList2.size();
        int i = 0;
        while (i < size) {
            Scope scope = arrayList2.get(i);
            i++;
            arrayList.add(scope.zzpn());
        }
        Collections.sort(arrayList);
        return new zzo().zzo(arrayList).zzo(this.zzajd).zzo(this.zzalk).zzP(this.zzamc).zzP(this.zzalj).zzP(this.zzamb).zzmH();
    }

    public final boolean isIdTokenRequested() {
        return this.zzalj;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        int iZze = com.google.android.gms.common.internal.safeparcel.zzd.zze(parcel);
        com.google.android.gms.common.internal.safeparcel.zzd.zzc(parcel, 1, this.versionCode);
        com.google.android.gms.common.internal.safeparcel.zzd.zzc(parcel, 2, zzmy(), false);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 3, (Parcelable) this.zzajd, i, false);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 4, this.zzalj);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 5, this.zzamb);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 6, this.zzamc);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 7, this.zzalk, false);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 8, this.zzamd, false);
        com.google.android.gms.common.internal.safeparcel.zzd.zzc(parcel, 9, this.zzame, false);
        com.google.android.gms.common.internal.safeparcel.zzd.zzI(parcel, iZze);
    }

    public final String zzmA() {
        return zzmx().toString();
    }

    public final ArrayList<Scope> zzmy() {
        return new ArrayList<>(this.zzama);
    }

    public final boolean zzmz() {
        return this.zzamb;
    }
}
