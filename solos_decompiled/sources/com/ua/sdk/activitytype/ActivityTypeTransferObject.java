package com.ua.sdk.activitytype;

import com.google.gson.annotations.SerializedName;
import com.ua.sdk.datapoint.BaseDataTypes;
import com.ua.sdk.internal.ApiTransferObject;
import com.ua.sdk.internal.Link;
import java.util.List;

/* JADX INFO: loaded from: classes65.dex */
class ActivityTypeTransferObject extends ApiTransferObject {
    protected static final String ICON_URL = "icon_url";
    protected static final String LINK_PARENT = "parent";
    protected static final String LINK_ROOT = "root";

    @SerializedName("has_children")
    Boolean hasChildren;

    @SerializedName("mets")
    Double mets;

    @SerializedName("mets_speed")
    List<MetsSpeedTransferObject> metsSpeed;

    @SerializedName("name")
    String name;

    @SerializedName("short_name")
    String shortName;

    class MetsSpeedTransferObject {

        @SerializedName("mets")
        String mets;

        @SerializedName(BaseDataTypes.ID_SPEED)
        Double speed;

        MetsSpeedTransferObject() {
        }
    }

    public static ActivityTypeImpl toImpl(ActivityTypeTransferObject to) {
        if (to == null) {
            return null;
        }
        ActivityTypeImpl at = new ActivityTypeImpl(to.getActivityId(), to.getParentActivityId(), to.name, to.shortName, to.mets, to.getMetsSpeedString(), to.hasChildren, null);
        at.setLinkMap(to.getLinkMap());
        return at;
    }

    private String getLinkActivityId(String relation) {
        Link link = getLink(relation);
        if (link == null) {
            return null;
        }
        return link.getId();
    }

    public String getActivityId() {
        return getLinkActivityId("self");
    }

    public String getParentActivityId() {
        return getLinkActivityId(LINK_PARENT);
    }

    public String getMetsSpeedString() {
        if (this.metsSpeed == null || this.metsSpeed.isEmpty()) {
            return "{}";
        }
        int size = this.metsSpeed.size();
        StringBuilder builder = new StringBuilder();
        builder.append('{');
        for (int i = 0; i < size; i++) {
            if (i > 0) {
                builder.append(',');
            }
            MetsSpeedTransferObject msto = this.metsSpeed.get(i);
            builder.append("\"").append(msto.mets).append("\"").append(":").append(msto.speed);
        }
        builder.append('}');
        return builder.toString();
    }
}
