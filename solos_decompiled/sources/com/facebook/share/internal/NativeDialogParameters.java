package com.facebook.share.internal;

import android.os.Bundle;
import com.facebook.FacebookException;
import com.facebook.internal.Utility;
import com.facebook.internal.Validate;
import com.facebook.share.model.ShareContent;
import com.facebook.share.model.ShareHashtag;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.ShareMediaContent;
import com.facebook.share.model.ShareOpenGraphContent;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.model.ShareVideoContent;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes41.dex */
public class NativeDialogParameters {
    public static Bundle create(UUID callId, ShareContent shareContent, boolean shouldFailOnDataError) {
        Validate.notNull(shareContent, "shareContent");
        Validate.notNull(callId, "callId");
        if (shareContent instanceof ShareLinkContent) {
            ShareLinkContent linkContent = (ShareLinkContent) shareContent;
            Bundle nativeParams = create(linkContent, shouldFailOnDataError);
            return nativeParams;
        }
        if (shareContent instanceof SharePhotoContent) {
            SharePhotoContent photoContent = (SharePhotoContent) shareContent;
            List<String> photoUrls = ShareInternalUtility.getPhotoUrls(photoContent, callId);
            Bundle nativeParams2 = create(photoContent, photoUrls, shouldFailOnDataError);
            return nativeParams2;
        }
        if (shareContent instanceof ShareVideoContent) {
            ShareVideoContent videoContent = (ShareVideoContent) shareContent;
            String videoUrl = ShareInternalUtility.getVideoUrl(videoContent, callId);
            Bundle nativeParams3 = create(videoContent, videoUrl, shouldFailOnDataError);
            return nativeParams3;
        }
        if (shareContent instanceof ShareOpenGraphContent) {
            ShareOpenGraphContent openGraphContent = (ShareOpenGraphContent) shareContent;
            try {
                JSONObject openGraphActionJSON = ShareInternalUtility.toJSONObjectForCall(callId, openGraphContent);
                Bundle nativeParams4 = create(openGraphContent, ShareInternalUtility.removeNamespacesFromOGJsonObject(openGraphActionJSON, false), shouldFailOnDataError);
                return nativeParams4;
            } catch (JSONException e) {
                throw new FacebookException("Unable to create a JSON Object from the provided ShareOpenGraphContent: " + e.getMessage());
            }
        }
        if (!(shareContent instanceof ShareMediaContent)) {
            return null;
        }
        ShareMediaContent mediaContent = (ShareMediaContent) shareContent;
        List<Bundle> mediaInfos = ShareInternalUtility.getMediaInfos(mediaContent, callId);
        Bundle nativeParams5 = create(mediaContent, mediaInfos, shouldFailOnDataError);
        return nativeParams5;
    }

    private static Bundle create(ShareLinkContent linkContent, boolean dataErrorsFatal) {
        Bundle params = createBaseParameters(linkContent, dataErrorsFatal);
        Utility.putNonEmptyString(params, ShareConstants.TITLE, linkContent.getContentTitle());
        Utility.putNonEmptyString(params, ShareConstants.DESCRIPTION, linkContent.getContentDescription());
        Utility.putUri(params, ShareConstants.IMAGE_URL, linkContent.getImageUrl());
        Utility.putNonEmptyString(params, ShareConstants.QUOTE, linkContent.getQuote());
        return params;
    }

    private static Bundle create(SharePhotoContent photoContent, List<String> imageUrls, boolean dataErrorsFatal) {
        Bundle params = createBaseParameters(photoContent, dataErrorsFatal);
        params.putStringArrayList(ShareConstants.PHOTOS, new ArrayList<>(imageUrls));
        return params;
    }

    private static Bundle create(ShareVideoContent videoContent, String videoUrl, boolean dataErrorsFatal) {
        Bundle params = createBaseParameters(videoContent, dataErrorsFatal);
        Utility.putNonEmptyString(params, ShareConstants.TITLE, videoContent.getContentTitle());
        Utility.putNonEmptyString(params, ShareConstants.DESCRIPTION, videoContent.getContentDescription());
        Utility.putNonEmptyString(params, "VIDEO", videoUrl);
        return params;
    }

    private static Bundle create(ShareMediaContent mediaContent, List<Bundle> mediaInfos, boolean dataErrorsFatal) {
        Bundle params = createBaseParameters(mediaContent, dataErrorsFatal);
        params.putParcelableArrayList(ShareConstants.MEDIA, new ArrayList<>(mediaInfos));
        return params;
    }

    private static Bundle create(ShareOpenGraphContent openGraphContent, JSONObject openGraphActionJSON, boolean dataErrorsFatal) {
        Bundle params = createBaseParameters(openGraphContent, dataErrorsFatal);
        String previewProperty = (String) ShareInternalUtility.getFieldNameAndNamespaceFromFullName(openGraphContent.getPreviewPropertyName()).second;
        Utility.putNonEmptyString(params, ShareConstants.PREVIEW_PROPERTY_NAME, previewProperty);
        Utility.putNonEmptyString(params, ShareConstants.ACTION_TYPE, openGraphContent.getAction().getActionType());
        Utility.putNonEmptyString(params, ShareConstants.ACTION, openGraphActionJSON.toString());
        return params;
    }

    private static Bundle createBaseParameters(ShareContent content, boolean dataErrorsFatal) {
        Bundle params = new Bundle();
        Utility.putUri(params, ShareConstants.CONTENT_URL, content.getContentUrl());
        Utility.putNonEmptyString(params, ShareConstants.PLACE_ID, content.getPlaceId());
        Utility.putNonEmptyString(params, ShareConstants.REF, content.getRef());
        params.putBoolean(ShareConstants.DATA_FAILURES_FATAL, dataErrorsFatal);
        List<String> peopleIds = content.getPeopleIds();
        if (!Utility.isNullOrEmpty(peopleIds)) {
            params.putStringArrayList(ShareConstants.PEOPLE_IDS, new ArrayList<>(peopleIds));
        }
        ShareHashtag shareHashtag = content.getShareHashtag();
        if (shareHashtag != null) {
            Utility.putNonEmptyString(params, ShareConstants.HASHTAG, shareHashtag.getHashtag());
        }
        return params;
    }
}
