package com.kopin.accessory.utility;

/* JADX INFO: loaded from: classes14.dex */
public class WhisperConfig {
    public boolean isAVADEnabled;
    public boolean isOutputEqualized;
    public boolean isPersisted;
    public int sensitivityVAD;
    public byte vadMode;
    public final int[] micGain = new int[4];
    public final int[] AVADThreshold = new int[3];
    public final int[] AVADValues = new int[4];
    public final int[] outputConfig = new int[3];

    public String toString() {
        StringBuilder sb = new StringBuilder("Whisper Config: ");
        sb.append(this.isPersisted ? "PERSISTED" : "RUNTIME");
        sb.append(", Output Equalizer: ").append(this.isOutputEqualized ? "ON" : "OFF");
        sb.append(", AVAD: ").append(this.isAVADEnabled ? "ENABLED" : "DISABLED");
        sb.append(", VAD Mode: ").append(Byte.toString(this.vadMode)).append("\n");
        sb.append(String.format("Mic Gains: %08x %08x %08x %08x\n", Integer.valueOf(this.micGain[0]), Integer.valueOf(this.micGain[1]), Integer.valueOf(this.micGain[2]), Integer.valueOf(this.micGain[3])));
        sb.append(String.format("VAD Sensitivity: %08x\n", Integer.valueOf(this.sensitivityVAD)));
        sb.append(String.format("AVAD Thresholds: %08x %08x %08x\n", Integer.valueOf(this.AVADThreshold[0]), Integer.valueOf(this.AVADThreshold[1]), Integer.valueOf(this.AVADThreshold[2])));
        sb.append(String.format("AVAD Values: %08x %08x %08x %08x\n", Integer.valueOf(this.AVADValues[0]), Integer.valueOf(this.AVADValues[1]), Integer.valueOf(this.AVADValues[2]), Integer.valueOf(this.AVADValues[3])));
        sb.append(String.format("Output Config: %08x %08x %08x\n", Integer.valueOf(this.outputConfig[0]), Integer.valueOf(this.outputConfig[1]), Integer.valueOf(this.outputConfig[2])));
        return sb.toString();
    }
}
