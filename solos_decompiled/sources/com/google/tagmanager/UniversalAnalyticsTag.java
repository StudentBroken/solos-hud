package com.google.tagmanager;

import android.content.Context;
import com.google.analytics.containertag.common.FunctionType;
import com.google.analytics.containertag.common.Key;
import com.google.analytics.midtier.proto.containertag.TypeSystem;
import com.google.analytics.tracking.android.Fields;
import com.google.analytics.tracking.android.HitTypes;
import com.google.analytics.tracking.android.Tracker;
import com.google.android.gms.common.util.VisibleForTesting;
import com.google.firebase.analytics.FirebaseAnalytics;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/* JADX INFO: loaded from: classes49.dex */
class UniversalAnalyticsTag extends TrackingTag {
    private static final String DEFAULT_TRACKING_ID = "_GTM_DEFAULT_TRACKER_";
    private static Map<String, String> defaultItemMap;
    private static Map<String, String> defaultTransactionMap;
    private final DataLayer mDataLayer;
    private final TrackerProvider mTrackerProvider;
    private final Set<String> mTurnOffAnonymizeIpValues;
    private static final String ID = FunctionType.UNIVERSAL_ANALYTICS.toString();
    private static final String ACCOUNT = Key.ACCOUNT.toString();
    private static final String ANALYTICS_PASS_THROUGH = Key.ANALYTICS_PASS_THROUGH.toString();
    private static final String ANALYTICS_FIELDS = Key.ANALYTICS_FIELDS.toString();
    private static final String TRACK_TRANSACTION = Key.TRACK_TRANSACTION.toString();
    private static final String TRANSACTION_DATALAYER_MAP = Key.TRANSACTION_DATALAYER_MAP.toString();
    private static final String TRANSACTION_ITEM_DATALAYER_MAP = Key.TRANSACTION_ITEM_DATALAYER_MAP.toString();

    public static String getFunctionId() {
        return ID;
    }

    public UniversalAnalyticsTag(Context context, DataLayer dataLayer) {
        this(context, dataLayer, new TrackerProvider(context));
    }

    @VisibleForTesting
    UniversalAnalyticsTag(Context context, DataLayer dataLayer, TrackerProvider trackerProvider) {
        super(ID, new String[0]);
        this.mDataLayer = dataLayer;
        this.mTrackerProvider = trackerProvider;
        this.mTurnOffAnonymizeIpValues = new HashSet();
        this.mTurnOffAnonymizeIpValues.add("");
        this.mTurnOffAnonymizeIpValues.add("0");
        this.mTurnOffAnonymizeIpValues.add("false");
    }

    private boolean checkBooleanProperty(Map<String, TypeSystem.Value> tag, String key) {
        TypeSystem.Value value = tag.get(key);
        if (value == null) {
            return false;
        }
        return value.getBoolean();
    }

    @Override // com.google.tagmanager.TrackingTag
    public void evaluateTrackingTag(Map<String, TypeSystem.Value> tag) {
        Tracker tracker = this.mTrackerProvider.getTracker(DEFAULT_TRACKING_ID);
        if (checkBooleanProperty(tag, ANALYTICS_PASS_THROUGH)) {
            tracker.send(convertToGaFields(tag.get(ANALYTICS_FIELDS)));
        } else if (checkBooleanProperty(tag, TRACK_TRANSACTION)) {
            sendTransaction(tracker, tag);
        } else {
            Log.w("Ignoring unknown tag.");
        }
        this.mTrackerProvider.close(tracker);
    }

    private String getDataLayerString(String field) {
        Object data = this.mDataLayer.get(field);
        if (data == null) {
            return null;
        }
        return data.toString();
    }

    private List<Map<String, String>> getTransactionItems() {
        Object data = this.mDataLayer.get("transactionProducts");
        if (data == null) {
            return null;
        }
        if (!(data instanceof List)) {
            throw new IllegalArgumentException("transactionProducts should be of type List.");
        }
        List<Object> list = (List) data;
        for (Object obj : list) {
            if (!(obj instanceof Map)) {
                throw new IllegalArgumentException("Each element of transactionProducts should be of type Map.");
            }
        }
        return (List) data;
    }

    private void addParam(Map<String, String> itemParams, String gaKey, String value) {
        if (value != null) {
            itemParams.put(gaKey, value);
        }
    }

    private void sendTransaction(Tracker tracker, Map<String, TypeSystem.Value> tag) {
        tag.get(ACCOUNT).getString();
        String transactionId = getDataLayerString("transactionId");
        if (transactionId == null) {
            Log.e("Cannot find transactionId in data layer.");
            return;
        }
        List<Map<String, String>> sendQueue = new LinkedList<>();
        try {
            Map<String, String> params = convertToGaFields(tag.get(ANALYTICS_FIELDS));
            params.put(Fields.HIT_TYPE, HitTypes.TRANSACTION);
            Map<String, String> transactionFields = getTransactionFields(tag);
            for (Map.Entry<String, String> entry : transactionFields.entrySet()) {
                addParam(params, entry.getValue(), getDataLayerString(entry.getKey()));
            }
            sendQueue.add(params);
            List<Map<String, String>> items = getTransactionItems();
            if (items != null) {
                for (Map<String, String> item : items) {
                    if (item.get("name") == null) {
                        Log.e("Unable to send transaction item hit due to missing 'name' field.");
                        return;
                    }
                    Map<String, String> itemParams = convertToGaFields(tag.get(ANALYTICS_FIELDS));
                    itemParams.put(Fields.HIT_TYPE, HitTypes.ITEM);
                    itemParams.put(Fields.TRANSACTION_ID, transactionId);
                    Map<String, String> itemFields = getTransactionItemFields(tag);
                    for (Map.Entry<String, String> entry2 : itemFields.entrySet()) {
                        addParam(itemParams, entry2.getValue(), item.get(entry2.getKey()));
                    }
                    sendQueue.add(itemParams);
                }
            }
            for (Map<String, String> gaParam : sendQueue) {
                tracker.send(gaParam);
            }
        } catch (IllegalArgumentException e) {
            Log.e("Unable to send transaction", e);
        }
    }

    private Map<String, String> valueToMap(TypeSystem.Value map) {
        if (map.getType() != TypeSystem.Value.Type.MAP) {
            return null;
        }
        Map<String, String> params = new HashMap<>(map.getMapKeyCount());
        for (int i = 0; i < map.getMapKeyCount(); i++) {
            String key = Types.valueToString(map.getMapKey(i));
            String value = Types.valueToString(map.getMapValue(i));
            params.put(key, value);
        }
        return params;
    }

    private Map<String, String> convertToGaFields(TypeSystem.Value analyticsFields) {
        if (analyticsFields == null || analyticsFields.getType() != TypeSystem.Value.Type.MAP) {
            return new HashMap();
        }
        Map<String, String> params = valueToMap(analyticsFields);
        String aip = params.get(Fields.ANONYMIZE_IP);
        if (aip != null && this.mTurnOffAnonymizeIpValues.contains(aip.toLowerCase())) {
            params.remove(Fields.ANONYMIZE_IP);
            return params;
        }
        return params;
    }

    private Map<String, String> getTransactionFields(Map<String, TypeSystem.Value> tag) {
        TypeSystem.Value map = tag.get(TRANSACTION_DATALAYER_MAP);
        if (map != null) {
            return valueToMap(map);
        }
        if (defaultTransactionMap == null) {
            HashMap<String, String> defaultMap = new HashMap<>();
            defaultMap.put("transactionId", Fields.TRANSACTION_ID);
            defaultMap.put("transactionAffiliation", Fields.TRANSACTION_AFFILIATION);
            defaultMap.put("transactionTax", Fields.TRANSACTION_TAX);
            defaultMap.put("transactionShipping", Fields.TRANSACTION_SHIPPING);
            defaultMap.put("transactionTotal", Fields.TRANSACTION_REVENUE);
            defaultMap.put("transactionCurrency", Fields.CURRENCY_CODE);
            defaultTransactionMap = defaultMap;
        }
        return defaultTransactionMap;
    }

    private Map<String, String> getTransactionItemFields(Map<String, TypeSystem.Value> tag) {
        TypeSystem.Value map = tag.get(TRANSACTION_ITEM_DATALAYER_MAP);
        if (map != null) {
            return valueToMap(map);
        }
        if (defaultItemMap == null) {
            HashMap<String, String> defaultMap = new HashMap<>();
            defaultMap.put("name", Fields.ITEM_NAME);
            defaultMap.put("sku", Fields.ITEM_SKU);
            defaultMap.put("category", Fields.ITEM_CATEGORY);
            defaultMap.put(FirebaseAnalytics.Param.PRICE, Fields.ITEM_PRICE);
            defaultMap.put(FirebaseAnalytics.Param.QUANTITY, Fields.ITEM_QUANTITY);
            defaultMap.put(FirebaseAnalytics.Param.CURRENCY, Fields.CURRENCY_CODE);
            defaultItemMap = defaultMap;
        }
        return defaultItemMap;
    }
}
