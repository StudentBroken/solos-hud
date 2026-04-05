package com.twitter.sdk.android.tweetui;

import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.CharacterStyle;
import android.text.style.ClickableSpan;
import android.view.View;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/* JADX INFO: loaded from: classes9.dex */
final class TweetTextLinkifier {
    private static final String PHOTO_TYPE = "photo";

    private TweetTextLinkifier() {
    }

    static CharSequence linkifyUrls(FormattedTweetText tweetText, LinkClickListener listener, boolean stripLastPhotoEntity, int linkColor) {
        FormattedMediaEntity lastPhoto;
        if (tweetText == null) {
            return null;
        }
        if (TextUtils.isEmpty(tweetText.text)) {
            return tweetText.text;
        }
        SpannableStringBuilder spannable = new SpannableStringBuilder(tweetText.text);
        List<FormattedUrlEntity> urls = tweetText.urlEntities;
        List<FormattedMediaEntity> media = tweetText.mediaEntities;
        if (stripLastPhotoEntity) {
            lastPhoto = getLastPhotoEntity(tweetText);
        } else {
            lastPhoto = null;
        }
        List<FormattedUrlEntity> combined = mergeAndSortEntities(urls, media);
        addUrlEntities(spannable, combined, lastPhoto, listener, linkColor);
        return spannable;
    }

    static List<FormattedUrlEntity> mergeAndSortEntities(List<FormattedUrlEntity> urls, List<FormattedMediaEntity> media) {
        if (media != null) {
            ArrayList<FormattedUrlEntity> combined = new ArrayList<>(urls);
            combined.addAll(media);
            Collections.sort(combined, new Comparator<FormattedUrlEntity>() { // from class: com.twitter.sdk.android.tweetui.TweetTextLinkifier.1
                @Override // java.util.Comparator
                public int compare(FormattedUrlEntity lhs, FormattedUrlEntity rhs) {
                    if (lhs == null && rhs != null) {
                        return -1;
                    }
                    if (lhs != null && rhs == null) {
                        return 1;
                    }
                    if (lhs == null && rhs == null) {
                        return 0;
                    }
                    if (lhs.start >= rhs.start) {
                        return lhs.start > rhs.start ? 1 : 0;
                    }
                    return -1;
                }
            });
            return combined;
        }
        return urls;
    }

    private static void addUrlEntities(SpannableStringBuilder spannable, List<FormattedUrlEntity> entities, FormattedMediaEntity lastPhoto, final LinkClickListener listener, final int linkColor) {
        if (entities != null && !entities.isEmpty()) {
            int offset = 0;
            for (final FormattedUrlEntity url : entities) {
                int start = url.start - offset;
                int end = url.end - offset;
                if (start >= 0 && end <= spannable.length()) {
                    if (lastPhoto != null && lastPhoto.start == url.start) {
                        spannable.replace(start, end, "");
                        int len = end - start;
                        int i = end - len;
                        offset += len;
                    } else if (!TextUtils.isEmpty(url.displayUrl)) {
                        spannable.replace(start, end, (CharSequence) url.displayUrl);
                        int len2 = end - (url.displayUrl.length() + start);
                        offset += len2;
                        CharacterStyle span = new ClickableSpan() { // from class: com.twitter.sdk.android.tweetui.TweetTextLinkifier.2
                            @Override // android.text.style.ClickableSpan
                            public void onClick(View widget) {
                                if (listener != null) {
                                    listener.onUrlClicked(url.url);
                                }
                            }

                            @Override // android.text.style.ClickableSpan, android.text.style.CharacterStyle
                            public void updateDrawState(TextPaint ds) {
                                ds.setColor(linkColor);
                                ds.setUnderlineText(false);
                            }
                        };
                        spannable.setSpan(span, start, end - len2, 33);
                    }
                }
            }
        }
    }

    private static FormattedMediaEntity getLastPhotoEntity(FormattedTweetText formattedTweetText) {
        if (formattedTweetText == null) {
            return null;
        }
        List<FormattedMediaEntity> mediaEntityList = formattedTweetText.mediaEntities;
        if (mediaEntityList.isEmpty()) {
            return null;
        }
        for (int i = mediaEntityList.size() - 1; i >= 0; i--) {
            FormattedMediaEntity entity = mediaEntityList.get(i);
            if ("photo".equals(entity.type)) {
                return entity;
            }
        }
        return null;
    }
}
