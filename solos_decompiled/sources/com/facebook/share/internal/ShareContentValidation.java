package com.facebook.share.internal;

import android.graphics.Bitmap;
import android.net.Uri;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.internal.Utility;
import com.facebook.internal.Validate;
import com.facebook.share.model.ShareContent;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.ShareMedia;
import com.facebook.share.model.ShareMediaContent;
import com.facebook.share.model.ShareOpenGraphAction;
import com.facebook.share.model.ShareOpenGraphContent;
import com.facebook.share.model.ShareOpenGraphObject;
import com.facebook.share.model.ShareOpenGraphValueContainer;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.model.ShareVideo;
import com.facebook.share.model.ShareVideoContent;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/* JADX INFO: loaded from: classes41.dex */
public class ShareContentValidation {
    private static Validator ApiValidator;
    private static Validator DefaultValidator;
    private static Validator WebShareValidator;

    public static void validateForMessage(ShareContent content) {
        validate(content, getDefaultValidator());
    }

    public static void validateForNativeShare(ShareContent content) {
        validate(content, getDefaultValidator());
    }

    public static void validateForWebShare(ShareContent content) {
        validate(content, getWebShareValidator());
    }

    public static void validateForApiShare(ShareContent content) {
        validate(content, getApiValidator());
    }

    private static Validator getDefaultValidator() {
        if (DefaultValidator == null) {
            DefaultValidator = new Validator();
        }
        return DefaultValidator;
    }

    private static Validator getApiValidator() {
        if (ApiValidator == null) {
            ApiValidator = new ApiValidator();
        }
        return ApiValidator;
    }

    private static Validator getWebShareValidator() {
        if (WebShareValidator == null) {
            WebShareValidator = new WebShareValidator();
        }
        return WebShareValidator;
    }

    private static void validate(ShareContent content, Validator validator) throws FacebookException {
        if (content == null) {
            throw new FacebookException("Must provide non-null content to share");
        }
        if (content instanceof ShareLinkContent) {
            validator.validate((ShareLinkContent) content);
            return;
        }
        if (content instanceof SharePhotoContent) {
            validator.validate((SharePhotoContent) content);
            return;
        }
        if (content instanceof ShareVideoContent) {
            validator.validate((ShareVideoContent) content);
        } else if (content instanceof ShareOpenGraphContent) {
            validator.validate((ShareOpenGraphContent) content);
        } else if (content instanceof ShareMediaContent) {
            validator.validate((ShareMediaContent) content);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void validateLinkContent(ShareLinkContent linkContent, Validator validator) {
        Uri imageUrl = linkContent.getImageUrl();
        if (imageUrl != null && !Utility.isWebUri(imageUrl)) {
            throw new FacebookException("Image Url must be an http:// or https:// url");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void validatePhotoContent(SharePhotoContent photoContent, Validator validator) {
        List<SharePhoto> photos = photoContent.getPhotos();
        if (photos == null || photos.isEmpty()) {
            throw new FacebookException("Must specify at least one Photo in SharePhotoContent.");
        }
        if (photos.size() > 6) {
            throw new FacebookException(String.format(Locale.ROOT, "Cannot add more than %d photos.", 6));
        }
        for (SharePhoto photo : photos) {
            validator.validate(photo);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void validatePhotoForApi(SharePhoto photo, Validator validator) {
        if (photo == null) {
            throw new FacebookException("Cannot share a null SharePhoto");
        }
        Bitmap photoBitmap = photo.getBitmap();
        Uri photoUri = photo.getImageUrl();
        if (photoBitmap == null) {
            if (photoUri == null) {
                throw new FacebookException("SharePhoto does not have a Bitmap or ImageUrl specified");
            }
            if (Utility.isWebUri(photoUri) && !validator.isOpenGraphContent()) {
                throw new FacebookException("Cannot set the ImageUrl of a SharePhoto to the Uri of an image on the web when sharing SharePhotoContent");
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void validatePhotoForNativeDialog(SharePhoto photo, Validator validator) {
        validatePhotoForApi(photo, validator);
        if (photo.getBitmap() != null || !Utility.isWebUri(photo.getImageUrl())) {
            Validate.hasContentProvider(FacebookSdk.getApplicationContext());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void validatePhotoForWebDialog(SharePhoto photo, Validator validator) {
        if (photo == null) {
            throw new FacebookException("Cannot share a null SharePhoto");
        }
        Uri imageUri = photo.getImageUrl();
        if (imageUri == null || !Utility.isWebUri(imageUri)) {
            throw new FacebookException("SharePhoto must have a non-null imageUrl set to the Uri of an image on the web");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void validateVideoContent(ShareVideoContent videoContent, Validator validator) {
        validator.validate(videoContent.getVideo());
        SharePhoto previewPhoto = videoContent.getPreviewPhoto();
        if (previewPhoto != null) {
            validator.validate(previewPhoto);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void validateVideo(ShareVideo video, Validator validator) {
        if (video == null) {
            throw new FacebookException("Cannot share a null ShareVideo");
        }
        Uri localUri = video.getLocalUrl();
        if (localUri == null) {
            throw new FacebookException("ShareVideo does not have a LocalUrl specified");
        }
        if (!Utility.isContentUri(localUri) && !Utility.isFileUri(localUri)) {
            throw new FacebookException("ShareVideo must reference a video that is on the device");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void validateMediaContent(ShareMediaContent mediaContent, Validator validator) {
        List<ShareMedia> media = mediaContent.getMedia();
        if (media == null || media.isEmpty()) {
            throw new FacebookException("Must specify at least one medium in ShareMediaContent.");
        }
        if (media.size() > 6) {
            throw new FacebookException(String.format(Locale.ROOT, "Cannot add more than %d media.", 6));
        }
        for (ShareMedia medium : media) {
            validator.validate(medium);
        }
    }

    public static void validateMedium(ShareMedia medium, Validator validator) {
        if (medium instanceof SharePhoto) {
            validator.validate((SharePhoto) medium);
        } else {
            if (medium instanceof ShareVideo) {
                validator.validate((ShareVideo) medium);
                return;
            }
            throw new FacebookException(String.format(Locale.ROOT, "Invalid media type: %s", medium.getClass().getSimpleName()));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void validateOpenGraphContent(ShareOpenGraphContent openGraphContent, Validator validator) {
        validator.validate(openGraphContent.getAction());
        String previewPropertyName = openGraphContent.getPreviewPropertyName();
        if (Utility.isNullOrEmpty(previewPropertyName)) {
            throw new FacebookException("Must specify a previewPropertyName.");
        }
        if (openGraphContent.getAction().get(previewPropertyName) == null) {
            throw new FacebookException("Property \"" + previewPropertyName + "\" was not found on the action. The name of the preview property must match the name of an action property.");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void validateOpenGraphAction(ShareOpenGraphAction openGraphAction, Validator validator) {
        if (openGraphAction == null) {
            throw new FacebookException("Must specify a non-null ShareOpenGraphAction");
        }
        if (Utility.isNullOrEmpty(openGraphAction.getActionType())) {
            throw new FacebookException("ShareOpenGraphAction must have a non-empty actionType");
        }
        validator.validate(openGraphAction, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void validateOpenGraphObject(ShareOpenGraphObject openGraphObject, Validator validator) {
        if (openGraphObject == null) {
            throw new FacebookException("Cannot share a null ShareOpenGraphObject");
        }
        validator.validate(openGraphObject, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void validateOpenGraphValueContainer(ShareOpenGraphValueContainer valueContainer, Validator validator, boolean requireNamespace) {
        Set<String> keySet = valueContainer.keySet();
        for (String key : keySet) {
            validateOpenGraphKey(key, requireNamespace);
            Object o = valueContainer.get(key);
            if (o instanceof List) {
                List list = (List) o;
                for (Object objectInList : list) {
                    if (objectInList == null) {
                        throw new FacebookException("Cannot put null objects in Lists in ShareOpenGraphObjects and ShareOpenGraphActions");
                    }
                    validateOpenGraphValueContainerObject(objectInList, validator);
                }
            } else {
                validateOpenGraphValueContainerObject(o, validator);
            }
        }
    }

    private static void validateOpenGraphKey(String key, boolean requireNamespace) {
        if (requireNamespace) {
            String[] components = key.split(":");
            if (components.length < 2) {
                throw new FacebookException("Open Graph keys must be namespaced: %s", key);
            }
            for (String component : components) {
                if (component.isEmpty()) {
                    throw new FacebookException("Invalid key found in Open Graph dictionary: %s", key);
                }
            }
        }
    }

    private static void validateOpenGraphValueContainerObject(Object o, Validator validator) {
        if (o instanceof ShareOpenGraphObject) {
            validator.validate((ShareOpenGraphObject) o);
        } else if (o instanceof SharePhoto) {
            validator.validate((SharePhoto) o);
        }
    }

    private static class WebShareValidator extends Validator {
        private WebShareValidator() {
            super();
        }

        @Override // com.facebook.share.internal.ShareContentValidation.Validator
        public void validate(SharePhotoContent photoContent) {
            throw new FacebookException("Cannot share SharePhotoContent via web sharing dialogs");
        }

        @Override // com.facebook.share.internal.ShareContentValidation.Validator
        public void validate(ShareVideoContent videoContent) {
            throw new FacebookException("Cannot share ShareVideoContent via web sharing dialogs");
        }

        @Override // com.facebook.share.internal.ShareContentValidation.Validator
        public void validate(ShareMediaContent mediaContent) {
            throw new FacebookException("Cannot share ShareMediaContent via web sharing dialogs");
        }

        @Override // com.facebook.share.internal.ShareContentValidation.Validator
        public void validate(SharePhoto photo) {
            ShareContentValidation.validatePhotoForWebDialog(photo, this);
        }
    }

    private static class ApiValidator extends Validator {
        private ApiValidator() {
            super();
        }

        @Override // com.facebook.share.internal.ShareContentValidation.Validator
        public void validate(SharePhoto photo) {
            ShareContentValidation.validatePhotoForApi(photo, this);
        }

        @Override // com.facebook.share.internal.ShareContentValidation.Validator
        public void validate(ShareVideoContent videoContent) {
            if (!Utility.isNullOrEmpty(videoContent.getPlaceId())) {
                throw new FacebookException("Cannot share video content with place IDs using the share api");
            }
            if (!Utility.isNullOrEmpty(videoContent.getPeopleIds())) {
                throw new FacebookException("Cannot share video content with people IDs using the share api");
            }
            if (!Utility.isNullOrEmpty(videoContent.getRef())) {
                throw new FacebookException("Cannot share video content with referrer URL using the share api");
            }
        }

        @Override // com.facebook.share.internal.ShareContentValidation.Validator
        public void validate(ShareMediaContent mediaContent) {
            throw new FacebookException("Cannot share ShareMediaContent using the share api");
        }

        @Override // com.facebook.share.internal.ShareContentValidation.Validator
        public void validate(ShareLinkContent linkContent) {
            if (!Utility.isNullOrEmpty(linkContent.getQuote())) {
                throw new FacebookException("Cannot share link content with quote using the share api");
            }
        }
    }

    private static class Validator {
        private boolean isOpenGraphContent;

        private Validator() {
            this.isOpenGraphContent = false;
        }

        public void validate(ShareLinkContent linkContent) {
            ShareContentValidation.validateLinkContent(linkContent, this);
        }

        public void validate(SharePhotoContent photoContent) {
            ShareContentValidation.validatePhotoContent(photoContent, this);
        }

        public void validate(ShareVideoContent videoContent) {
            ShareContentValidation.validateVideoContent(videoContent, this);
        }

        public void validate(ShareMediaContent mediaContent) {
            ShareContentValidation.validateMediaContent(mediaContent, this);
        }

        public void validate(ShareOpenGraphContent openGraphContent) {
            this.isOpenGraphContent = true;
            ShareContentValidation.validateOpenGraphContent(openGraphContent, this);
        }

        public void validate(ShareOpenGraphAction openGraphAction) {
            ShareContentValidation.validateOpenGraphAction(openGraphAction, this);
        }

        public void validate(ShareOpenGraphObject openGraphObject) {
            ShareContentValidation.validateOpenGraphObject(openGraphObject, this);
        }

        public void validate(ShareOpenGraphValueContainer openGraphValueContainer, boolean requireNamespace) {
            ShareContentValidation.validateOpenGraphValueContainer(openGraphValueContainer, this, requireNamespace);
        }

        public void validate(SharePhoto photo) {
            ShareContentValidation.validatePhotoForNativeDialog(photo, this);
        }

        public void validate(ShareVideo video) {
            ShareContentValidation.validateVideo(video, this);
        }

        public void validate(ShareMedia medium) {
            ShareContentValidation.validateMedium(medium, this);
        }

        public boolean isOpenGraphContent() {
            return this.isOpenGraphContent;
        }
    }
}
