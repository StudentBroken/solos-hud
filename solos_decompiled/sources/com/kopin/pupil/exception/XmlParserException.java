package com.kopin.pupil.exception;

import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: loaded from: classes25.dex */
public class XmlParserException extends CallbackException implements Parcelable {
    public static final Parcelable.Creator<XmlParserException> CREATOR = new Parcelable.Creator<XmlParserException>() { // from class: com.kopin.pupil.exception.XmlParserException.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public XmlParserException[] newArray(int size) {
            return new XmlParserException[size];
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public XmlParserException createFromParcel(Parcel source) {
            return new XmlParserException(source);
        }
    };
    private String mPageId;
    private int mSrcLine;

    public XmlParserException(Parcel in) {
        super(in.readString());
        this.mSrcLine = -1;
        this.mSrcLine = in.readInt();
        this.mPageId = in.readString();
    }

    public XmlParserException(String message, ParserExtraInfo info) {
        super(message);
        this.mSrcLine = -1;
        this.mSrcLine = info.mLineNr;
    }

    @Override // com.kopin.pupil.exception.CallbackException, android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // com.kopin.pupil.exception.CallbackException, android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(getMessage());
        dest.writeInt(this.mSrcLine);
        dest.writeString(this.mPageId);
    }

    public void setSrcLine(int srcLine) {
        this.mSrcLine = srcLine;
    }

    public void setPageId(String pageId) {
        this.mPageId = pageId;
    }

    public String getPageId() {
        return this.mPageId;
    }

    @Override // java.lang.Throwable
    public String getLocalizedMessage() {
        return this.mSrcLine != -1 ? "Parse exception at line " + this.mSrcLine + " caused by: " + getMessage() : super.getLocalizedMessage();
    }

    public static class ParserExtraInfo implements Parcelable {
        public static final Parcelable.Creator CREATOR = new Parcelable.Creator() { // from class: com.kopin.pupil.exception.XmlParserException.ParserExtraInfo.1
            @Override // android.os.Parcelable.Creator
            public ParserExtraInfo createFromParcel(Parcel source) {
                return new ParserExtraInfo(source);
            }

            @Override // android.os.Parcelable.Creator
            public ParserExtraInfo[] newArray(int size) {
                return new ParserExtraInfo[size];
            }
        };
        private int mLineNr;

        public ParserExtraInfo(Parcel in) {
            this.mLineNr = in.readInt();
        }

        public ParserExtraInfo(int lineNr) {
            this.mLineNr = lineNr;
        }

        public int getLineNr() {
            return this.mLineNr;
        }

        @Override // android.os.Parcelable
        public int describeContents() {
            return 1;
        }

        @Override // android.os.Parcelable
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.mLineNr);
        }
    }
}
