package com.google.android.gms.internal;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import bolts.MeasurementEvent;
import com.google.android.gms.measurement.AppMeasurement;

/* JADX INFO: loaded from: classes36.dex */
public final class zzcgv extends zzciv {
    private static String[] zzbqM = new String[AppMeasurement.Event.zzboj.length];
    private static String[] zzbqN = new String[AppMeasurement.Param.zzbol.length];
    private static String[] zzbqO = new String[AppMeasurement.UserProperty.zzboq.length];

    zzcgv(zzchx zzchxVar) {
        super(zzchxVar);
    }

    /* JADX WARN: Code restructure failed: missing block: B:18:0x004c, code lost:
    
        return r4;
     */
    @android.support.annotation.Nullable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private static java.lang.String zza(java.lang.String r4, java.lang.String[] r5, java.lang.String[] r6, java.lang.String[] r7) {
        /*
            r1 = 1
            r2 = 0
            com.google.android.gms.common.internal.zzbr.zzu(r5)
            com.google.android.gms.common.internal.zzbr.zzu(r6)
            com.google.android.gms.common.internal.zzbr.zzu(r7)
            int r0 = r5.length
            int r3 = r6.length
            if (r0 != r3) goto L4d
            r0 = r1
        L10:
            com.google.android.gms.common.internal.zzbr.zzaf(r0)
            int r0 = r5.length
            int r3 = r7.length
            if (r0 != r3) goto L4f
        L17:
            com.google.android.gms.common.internal.zzbr.zzaf(r1)
        L1a:
            int r0 = r5.length
            if (r2 >= r0) goto L4c
            r0 = r5[r2]
            boolean r0 = com.google.android.gms.internal.zzckx.zzR(r4, r0)
            if (r0 == 0) goto L54
            monitor-enter(r7)
            r0 = r7[r2]     // Catch: java.lang.Throwable -> L51
            if (r0 != 0) goto L49
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> L51
            r0.<init>()     // Catch: java.lang.Throwable -> L51
            r1 = r6[r2]     // Catch: java.lang.Throwable -> L51
            r0.append(r1)     // Catch: java.lang.Throwable -> L51
            java.lang.String r1 = "("
            r0.append(r1)     // Catch: java.lang.Throwable -> L51
            r1 = r5[r2]     // Catch: java.lang.Throwable -> L51
            r0.append(r1)     // Catch: java.lang.Throwable -> L51
            java.lang.String r1 = ")"
            r0.append(r1)     // Catch: java.lang.Throwable -> L51
            java.lang.String r0 = r0.toString()     // Catch: java.lang.Throwable -> L51
            r7[r2] = r0     // Catch: java.lang.Throwable -> L51
        L49:
            r4 = r7[r2]     // Catch: java.lang.Throwable -> L51
            monitor-exit(r7)     // Catch: java.lang.Throwable -> L51
        L4c:
            return r4
        L4d:
            r0 = r2
            goto L10
        L4f:
            r1 = r2
            goto L17
        L51:
            r0 = move-exception
            monitor-exit(r7)     // Catch: java.lang.Throwable -> L51
            throw r0
        L54:
            int r2 = r2 + 1
            goto L1a
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.zzcgv.zza(java.lang.String, java.lang.String[], java.lang.String[], java.lang.String[]):java.lang.String");
    }

    private static void zza(StringBuilder sb, int i) {
        for (int i2 = 0; i2 < i; i2++) {
            sb.append("  ");
        }
    }

    private final void zza(StringBuilder sb, int i, zzcla zzclaVar) {
        if (zzclaVar == null) {
            return;
        }
        zza(sb, i);
        sb.append("filter {\n");
        zza(sb, i, "complement", zzclaVar.zzbuY);
        zza(sb, i, "param_name", zzdY(zzclaVar.zzbuZ));
        int i2 = i + 1;
        zzcld zzcldVar = zzclaVar.zzbuW;
        if (zzcldVar != null) {
            zza(sb, i2);
            sb.append("string_filter");
            sb.append(" {\n");
            if (zzcldVar.zzbvi != null) {
                String str = "UNKNOWN_MATCH_TYPE";
                switch (zzcldVar.zzbvi.intValue()) {
                    case 1:
                        str = "REGEXP";
                        break;
                    case 2:
                        str = "BEGINS_WITH";
                        break;
                    case 3:
                        str = "ENDS_WITH";
                        break;
                    case 4:
                        str = "PARTIAL";
                        break;
                    case 5:
                        str = "EXACT";
                        break;
                    case 6:
                        str = "IN_LIST";
                        break;
                }
                zza(sb, i2, "match_type", str);
            }
            zza(sb, i2, "expression", zzcldVar.zzbvj);
            zza(sb, i2, "case_sensitive", zzcldVar.zzbvk);
            if (zzcldVar.zzbvl.length > 0) {
                zza(sb, i2 + 1);
                sb.append("expression_list {\n");
                for (String str2 : zzcldVar.zzbvl) {
                    zza(sb, i2 + 2);
                    sb.append(str2);
                    sb.append("\n");
                }
                sb.append("}\n");
            }
            zza(sb, i2);
            sb.append("}\n");
        }
        zza(sb, i + 1, "number_filter", zzclaVar.zzbuX);
        zza(sb, i);
        sb.append("}\n");
    }

    private final void zza(StringBuilder sb, int i, String str, zzclb zzclbVar) {
        if (zzclbVar == null) {
            return;
        }
        zza(sb, i);
        sb.append(str);
        sb.append(" {\n");
        if (zzclbVar.zzbva != null) {
            String str2 = "UNKNOWN_COMPARISON_TYPE";
            switch (zzclbVar.zzbva.intValue()) {
                case 1:
                    str2 = "LESS_THAN";
                    break;
                case 2:
                    str2 = "GREATER_THAN";
                    break;
                case 3:
                    str2 = "EQUAL";
                    break;
                case 4:
                    str2 = "BETWEEN";
                    break;
            }
            zza(sb, i, "comparison_type", str2);
        }
        zza(sb, i, "match_as_float", zzclbVar.zzbvb);
        zza(sb, i, "comparison_value", zzclbVar.zzbvc);
        zza(sb, i, "min_comparison_value", zzclbVar.zzbvd);
        zza(sb, i, "max_comparison_value", zzclbVar.zzbve);
        zza(sb, i);
        sb.append("}\n");
    }

    private static void zza(StringBuilder sb, int i, String str, zzclm zzclmVar) {
        int i2 = 0;
        if (zzclmVar == null) {
            return;
        }
        int i3 = i + 1;
        zza(sb, i3);
        sb.append(str);
        sb.append(" {\n");
        if (zzclmVar.zzbwj != null) {
            zza(sb, i3 + 1);
            sb.append("results: ");
            long[] jArr = zzclmVar.zzbwj;
            int length = jArr.length;
            int i4 = 0;
            int i5 = 0;
            while (i4 < length) {
                Long lValueOf = Long.valueOf(jArr[i4]);
                int i6 = i5 + 1;
                if (i5 != 0) {
                    sb.append(", ");
                }
                sb.append(lValueOf);
                i4++;
                i5 = i6;
            }
            sb.append('\n');
        }
        if (zzclmVar.zzbwi != null) {
            zza(sb, i3 + 1);
            sb.append("status: ");
            long[] jArr2 = zzclmVar.zzbwi;
            int length2 = jArr2.length;
            int i7 = 0;
            while (i2 < length2) {
                Long lValueOf2 = Long.valueOf(jArr2[i2]);
                int i8 = i7 + 1;
                if (i7 != 0) {
                    sb.append(", ");
                }
                sb.append(lValueOf2);
                i2++;
                i7 = i8;
            }
            sb.append('\n');
        }
        zza(sb, i3);
        sb.append("}\n");
    }

    private static void zza(StringBuilder sb, int i, String str, Object obj) {
        if (obj == null) {
            return;
        }
        zza(sb, i + 1);
        sb.append(str);
        sb.append(": ");
        sb.append(obj);
        sb.append('\n');
    }

    private final void zza(StringBuilder sb, int i, zzclh[] zzclhVarArr) {
        if (zzclhVarArr == null) {
            return;
        }
        for (zzclh zzclhVar : zzclhVarArr) {
            if (zzclhVar != null) {
                zza(sb, 2);
                sb.append("audience_membership {\n");
                zza(sb, 2, "audience_id", zzclhVar.zzbuM);
                zza(sb, 2, "new_audience", zzclhVar.zzbvy);
                zza(sb, 2, "current_data", zzclhVar.zzbvw);
                zza(sb, 2, "previous_data", zzclhVar.zzbvx);
                zza(sb, 2);
                sb.append("}\n");
            }
        }
    }

    private final void zza(StringBuilder sb, int i, zzcli[] zzcliVarArr) {
        if (zzcliVarArr == null) {
            return;
        }
        for (zzcli zzcliVar : zzcliVarArr) {
            if (zzcliVar != null) {
                zza(sb, 2);
                sb.append("event {\n");
                zza(sb, 2, "name", zzdX(zzcliVar.name));
                zza(sb, 2, "timestamp_millis", zzcliVar.zzbvB);
                zza(sb, 2, "previous_timestamp_millis", zzcliVar.zzbvC);
                zza(sb, 2, "count", zzcliVar.count);
                zzclj[] zzcljVarArr = zzcliVar.zzbvA;
                if (zzcljVarArr != null) {
                    for (zzclj zzcljVar : zzcljVarArr) {
                        if (zzcljVar != null) {
                            zza(sb, 3);
                            sb.append("param {\n");
                            zza(sb, 3, "name", zzdY(zzcljVar.name));
                            zza(sb, 3, "string_value", zzcljVar.zzaIH);
                            zza(sb, 3, "int_value", zzcljVar.zzbvE);
                            zza(sb, 3, "double_value", zzcljVar.zzbuF);
                            zza(sb, 3);
                            sb.append("}\n");
                        }
                    }
                }
                zza(sb, 2);
                sb.append("}\n");
            }
        }
    }

    private final void zza(StringBuilder sb, int i, zzcln[] zzclnVarArr) {
        if (zzclnVarArr == null) {
            return;
        }
        for (zzcln zzclnVar : zzclnVarArr) {
            if (zzclnVar != null) {
                zza(sb, 2);
                sb.append("user_property {\n");
                zza(sb, 2, "set_timestamp_millis", zzclnVar.zzbwl);
                zza(sb, 2, "name", zzdZ(zzclnVar.name));
                zza(sb, 2, "string_value", zzclnVar.zzaIH);
                zza(sb, 2, "int_value", zzclnVar.zzbvE);
                zza(sb, 2, "double_value", zzclnVar.zzbuF);
                zza(sb, 2);
                sb.append("}\n");
            }
        }
    }

    @Nullable
    private final String zzb(zzcgi zzcgiVar) {
        if (zzcgiVar == null) {
            return null;
        }
        return !zzyu() ? zzcgiVar.toString() : zzA(zzcgiVar.zzyr());
    }

    private final boolean zzyu() {
        return this.zzboi.zzwE().zzz(3);
    }

    @Override // com.google.android.gms.internal.zzciu
    public final /* bridge */ /* synthetic */ Context getContext() {
        return super.getContext();
    }

    @Nullable
    protected final String zzA(Bundle bundle) {
        if (bundle == null) {
            return null;
        }
        if (!zzyu()) {
            return bundle.toString();
        }
        StringBuilder sb = new StringBuilder();
        for (String str : bundle.keySet()) {
            if (sb.length() != 0) {
                sb.append(", ");
            } else {
                sb.append("Bundle[{");
            }
            sb.append(zzdY(str));
            sb.append("=");
            sb.append(bundle.get(str));
        }
        sb.append("}]");
        return sb.toString();
    }

    @Nullable
    protected final String zza(zzcgg zzcggVar) {
        if (zzcggVar == null) {
            return null;
        }
        if (!zzyu()) {
            return zzcggVar.toString();
        }
        return "Event{appId='" + zzcggVar.mAppId + "', name='" + zzdX(zzcggVar.mName) + "', params=" + zzb(zzcggVar.zzbpJ) + "}";
    }

    protected final String zza(zzckz zzckzVar) {
        if (zzckzVar == null) {
            return "null";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("\nevent_filter {\n");
        zza(sb, 0, "filter_id", zzckzVar.zzbuQ);
        zza(sb, 0, MeasurementEvent.MEASUREMENT_EVENT_NAME_KEY, zzdX(zzckzVar.zzbuR));
        zza(sb, 1, "event_count_filter", zzckzVar.zzbuU);
        sb.append("  filters {\n");
        for (zzcla zzclaVar : zzckzVar.zzbuS) {
            zza(sb, 2, zzclaVar);
        }
        zza(sb, 1);
        sb.append("}\n}\n");
        return sb.toString();
    }

    protected final String zza(zzclc zzclcVar) {
        if (zzclcVar == null) {
            return "null";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("\nproperty_filter {\n");
        zza(sb, 0, "filter_id", zzclcVar.zzbuQ);
        zza(sb, 0, "property_name", zzdZ(zzclcVar.zzbvg));
        zza(sb, 1, zzclcVar.zzbvh);
        sb.append("}\n");
        return sb.toString();
    }

    protected final String zza(zzclk zzclkVar) {
        StringBuilder sb = new StringBuilder();
        sb.append("\nbatch {\n");
        if (zzclkVar.zzbvF != null) {
            for (zzcll zzcllVar : zzclkVar.zzbvF) {
                if (zzcllVar != null && zzcllVar != null) {
                    zza(sb, 1);
                    sb.append("bundle {\n");
                    zza(sb, 1, "protocol_version", zzcllVar.zzbvH);
                    zza(sb, 1, "platform", zzcllVar.zzbvP);
                    zza(sb, 1, "gmp_version", zzcllVar.zzbvT);
                    zza(sb, 1, "uploading_gmp_version", zzcllVar.zzbvU);
                    zza(sb, 1, "config_version", zzcllVar.zzbwf);
                    zza(sb, 1, "gmp_app_id", zzcllVar.zzboU);
                    zza(sb, 1, "app_id", zzcllVar.zzaK);
                    zza(sb, 1, "app_version", zzcllVar.zzbha);
                    zza(sb, 1, "app_version_major", zzcllVar.zzbwc);
                    zza(sb, 1, "firebase_instance_id", zzcllVar.zzbpc);
                    zza(sb, 1, "dev_cert_hash", zzcllVar.zzbvY);
                    zza(sb, 1, "app_store", zzcllVar.zzboV);
                    zza(sb, 1, "upload_timestamp_millis", zzcllVar.zzbvK);
                    zza(sb, 1, "start_timestamp_millis", zzcllVar.zzbvL);
                    zza(sb, 1, "end_timestamp_millis", zzcllVar.zzbvM);
                    zza(sb, 1, "previous_bundle_start_timestamp_millis", zzcllVar.zzbvN);
                    zza(sb, 1, "previous_bundle_end_timestamp_millis", zzcllVar.zzbvO);
                    zza(sb, 1, "app_instance_id", zzcllVar.zzbvX);
                    zza(sb, 1, "resettable_device_id", zzcllVar.zzbvV);
                    zza(sb, 1, "limited_ad_tracking", zzcllVar.zzbvW);
                    zza(sb, 1, "os_version", zzcllVar.zzbb);
                    zza(sb, 1, "device_model", zzcllVar.zzbvQ);
                    zza(sb, 1, "user_default_language", zzcllVar.zzbvR);
                    zza(sb, 1, "time_zone_offset_minutes", zzcllVar.zzbvS);
                    zza(sb, 1, "bundle_sequential_index", zzcllVar.zzbvZ);
                    zza(sb, 1, "service_upload", zzcllVar.zzbwa);
                    zza(sb, 1, "health_monitor", zzcllVar.zzboY);
                    if (zzcllVar.zzbwg.longValue() != 0) {
                        zza(sb, 1, "android_id", zzcllVar.zzbwg);
                    }
                    zza(sb, 1, zzcllVar.zzbvJ);
                    zza(sb, 1, zzcllVar.zzbwb);
                    zza(sb, 1, zzcllVar.zzbvI);
                    zza(sb, 1);
                    sb.append("}\n");
                }
            }
        }
        sb.append("}\n");
        return sb.toString();
    }

    @Nullable
    protected final String zzb(zzcgl zzcglVar) {
        if (zzcglVar == null) {
            return null;
        }
        if (!zzyu()) {
            return zzcglVar.toString();
        }
        return "origin=" + zzcglVar.zzbpg + ",name=" + zzdX(zzcglVar.name) + ",params=" + zzb(zzcglVar.zzbpQ);
    }

    @Nullable
    protected final String zzdX(String str) {
        if (str == null) {
            return null;
        }
        return zzyu() ? zza(str, AppMeasurement.Event.zzbok, AppMeasurement.Event.zzboj, zzbqM) : str;
    }

    @Nullable
    protected final String zzdY(String str) {
        if (str == null) {
            return null;
        }
        return zzyu() ? zza(str, AppMeasurement.Param.zzbom, AppMeasurement.Param.zzbol, zzbqN) : str;
    }

    @Nullable
    protected final String zzdZ(String str) {
        if (str == null) {
            return null;
        }
        if (!zzyu()) {
            return str;
        }
        if (!str.startsWith("_exp_")) {
            return zza(str, AppMeasurement.UserProperty.zzbor, AppMeasurement.UserProperty.zzboq, zzbqO);
        }
        return "experiment_id(" + str + ")";
    }

    @Override // com.google.android.gms.internal.zzciu
    public final /* bridge */ /* synthetic */ void zzjB() {
        super.zzjB();
    }

    @Override // com.google.android.gms.internal.zzciv
    protected final void zzjC() {
    }

    @Override // com.google.android.gms.internal.zzciu
    public final /* bridge */ /* synthetic */ com.google.android.gms.common.util.zzf zzkp() {
        return super.zzkp();
    }

    @Override // com.google.android.gms.internal.zzciu
    public final /* bridge */ /* synthetic */ zzckx zzwA() {
        return super.zzwA();
    }

    @Override // com.google.android.gms.internal.zzciu
    public final /* bridge */ /* synthetic */ zzchr zzwB() {
        return super.zzwB();
    }

    @Override // com.google.android.gms.internal.zzciu
    public final /* bridge */ /* synthetic */ zzckm zzwC() {
        return super.zzwC();
    }

    @Override // com.google.android.gms.internal.zzciu
    public final /* bridge */ /* synthetic */ zzchs zzwD() {
        return super.zzwD();
    }

    @Override // com.google.android.gms.internal.zzciu
    public final /* bridge */ /* synthetic */ zzcgx zzwE() {
        return super.zzwE();
    }

    @Override // com.google.android.gms.internal.zzciu
    public final /* bridge */ /* synthetic */ zzchi zzwF() {
        return super.zzwF();
    }

    @Override // com.google.android.gms.internal.zzciu
    public final /* bridge */ /* synthetic */ zzcfy zzwG() {
        return super.zzwG();
    }

    @Override // com.google.android.gms.internal.zzciu
    public final /* bridge */ /* synthetic */ void zzwn() {
        super.zzwn();
    }

    @Override // com.google.android.gms.internal.zzciu
    public final /* bridge */ /* synthetic */ void zzwo() {
        super.zzwo();
    }

    @Override // com.google.android.gms.internal.zzciu
    public final /* bridge */ /* synthetic */ void zzwp() {
        super.zzwp();
    }

    @Override // com.google.android.gms.internal.zzciu
    public final /* bridge */ /* synthetic */ zzcfo zzwq() {
        return super.zzwq();
    }

    @Override // com.google.android.gms.internal.zzciu
    public final /* bridge */ /* synthetic */ zzcfv zzwr() {
        return super.zzwr();
    }

    @Override // com.google.android.gms.internal.zzciu
    public final /* bridge */ /* synthetic */ zzcix zzws() {
        return super.zzws();
    }

    @Override // com.google.android.gms.internal.zzciu
    public final /* bridge */ /* synthetic */ zzcgs zzwt() {
        return super.zzwt();
    }

    @Override // com.google.android.gms.internal.zzciu
    public final /* bridge */ /* synthetic */ zzcgf zzwu() {
        return super.zzwu();
    }

    @Override // com.google.android.gms.internal.zzciu
    public final /* bridge */ /* synthetic */ zzcjp zzwv() {
        return super.zzwv();
    }

    @Override // com.google.android.gms.internal.zzciu
    public final /* bridge */ /* synthetic */ zzcjl zzww() {
        return super.zzww();
    }

    @Override // com.google.android.gms.internal.zzciu
    public final /* bridge */ /* synthetic */ zzcgt zzwx() {
        return super.zzwx();
    }

    @Override // com.google.android.gms.internal.zzciu
    public final /* bridge */ /* synthetic */ zzcfz zzwy() {
        return super.zzwy();
    }

    @Override // com.google.android.gms.internal.zzciu
    public final /* bridge */ /* synthetic */ zzcgv zzwz() {
        return super.zzwz();
    }
}
