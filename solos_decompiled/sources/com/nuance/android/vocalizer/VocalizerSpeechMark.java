package com.nuance.android.vocalizer;

/* JADX INFO: loaded from: classes16.dex */
public class VocalizerSpeechMark implements Cloneable {
    protected static final int VMOBILE_MRK_AUDIODATA = 2048;
    public static final int VMOBILE_MRK_BOOKMARK = 8;
    public static final int VMOBILE_MRK_PHONEME = 4;
    public static final int VMOBILE_MRK_TEXTUNIT = 1;
    public static final int VMOBILE_MRK_WORD = 2;
    private int mScheduledAtFrame = 0;
    private int mMarkType = 0;
    private int mSrcPos = 0;
    private int mSrcTextLen = 0;
    private int mDestPos = 0;
    private int mDestLen = 0;
    private int mPhonemeId = 0;
    private int mMarkId = 0;
    private short[] mAudioData = null;
    private int mJawOpen = 0;
    private int mTeethUpVisible = 0;
    private int mTeethLoVisible = 0;
    private int mMouthHeight = 0;
    private int mMouthWidth = 0;
    private int mMouthUpturn = 0;
    private int mTonguePos = 0;
    private int mLipTension = 0;
    private String mLHPhoneme = "";

    public int getType() {
        return this.mMarkType;
    }

    public int getSrcPos() {
        return this.mSrcPos / 2;
    }

    public int getSrcTextLen() {
        return this.mSrcTextLen / 2;
    }

    public int getDestPos() {
        return this.mDestPos;
    }

    public int getDestLen() {
        return this.mDestLen;
    }

    public int getPhonemeId() {
        return this.mPhonemeId;
    }

    public int getMarkId() {
        return this.mMarkId;
    }

    public int getJawOpen() {
        return this.mJawOpen;
    }

    public int getTeethUpVisible() {
        return this.mTeethUpVisible;
    }

    public int getTeethLoVisible() {
        return this.mTeethLoVisible;
    }

    public int getMouthHeight() {
        return this.mMouthHeight;
    }

    public int getMouthWidth() {
        return this.mMouthWidth;
    }

    public int getMouthUpturn() {
        return this.mMouthUpturn;
    }

    public int getTonguePos() {
        return this.mTonguePos;
    }

    public int getLipTension() {
        return this.mLipTension;
    }

    public String getLHPhoneme() {
        return this.mLHPhoneme;
    }

    protected void scheduleAtFrame(int i) {
        this.mScheduledAtFrame = i;
    }

    protected int getScheduleAtFrame() {
        return this.mScheduledAtFrame;
    }

    protected short[] getAudioData() {
        return this.mAudioData;
    }

    protected void setAudioData(short[] sArr) {
        this.mAudioData = sArr;
    }

    protected void setType(int i) {
        this.mMarkType = i;
    }
}
