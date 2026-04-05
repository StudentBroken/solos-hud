package com.kopin.pupil.aria.debug;

import android.content.Context;
import com.kopin.accessory.utility.CallHelper;
import com.kopin.solos.common.CommonFileUtil;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

/* JADX INFO: loaded from: classes43.dex */
public class WaveFileRecorder {
    private static final byte[] WAVE_HEADER = {82, 73, 70, 70, 0, 0, 0, 0, 87, 65, 86, 69, 102, 109, 116, CallHelper.CallState.FLAG_SCO_STATE, CallHelper.CallState.FLAG_HFP_STATE, 0, 0, 0, 1, 0, 0, 0, CallHelper.CallState.FLAG_CALL_ACTIVE, 62, 0, 0, 0, 0, 0, 0, 4, 0, CallHelper.CallState.FLAG_HFP_STATE, 0, 100, 97, 116, 97, 0, 0, 0, 0};
    private static boolean isStereo;
    private static File mWavFile;
    private static boolean noHeader;

    public static void rawOrWav(boolean raw) {
        noHeader = raw;
    }

    public static String record(boolean stereo, Context context) {
        Calendar now = Calendar.getInstance();
        String name = String.format("audiorec_%d_%d-%d_%d.raw", Integer.valueOf(now.get(5)), Integer.valueOf(now.get(2)), Integer.valueOf(now.get(11)), Integer.valueOf(now.get(12)));
        mWavFile = new File(CommonFileUtil.getExternalPath(context), name);
        try {
            mWavFile.createNewFile();
            isStereo = stereo;
            createFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return name;
    }

    public static String stop() {
        String name;
        if (noHeader) {
            name = mWavFile.getName();
        } else {
            name = copyAndWriteHeader();
            mWavFile.delete();
        }
        mWavFile = null;
        return name;
    }

    public static boolean isActive() {
        return mWavFile != null;
    }

    private static void createFile() {
        try {
            FileOutputStream out = new FileOutputStream(mWavFile);
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
    }

    private static void setInt(byte[] data, int at, int val) {
        int at2 = at + 1;
        data[at] = (byte) (val & 255);
        int at3 = at2 + 1;
        data[at2] = (byte) ((val >> 8) & 255);
        data[at3] = (byte) ((val >> 16) & 255);
        data[at3 + 1] = (byte) ((val >> 24) & 255);
    }

    private static byte[] createHeader(long len, boolean stereo) {
        int byteRate = (stereo ? 4 : 2) * 16000;
        byte[] ret = (byte[]) WAVE_HEADER.clone();
        setInt(ret, 4, (int) (36 + len));
        ret[22] = (byte) (stereo ? 2 : 1);
        setInt(ret, 28, byteRate);
        ret[32] = (byte) (((stereo ? 2 : 1) * 16) / 8);
        setInt(ret, 40, (int) len);
        return ret;
    }

    private static String copyAndWriteHeader() {
        FileInputStream in;
        FileOutputStream out;
        byte[] buffer;
        File wavFile = new File(mWavFile.getAbsolutePath().replace(".raw", ".wav"));
        try {
            in = new FileInputStream(mWavFile);
            long len = in.getChannel().size();
            byte[] header = createHeader(len, isStereo);
            out = new FileOutputStream(wavFile);
            out.write(header);
            buffer = new byte[1024];
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
        while (true) {
            int read = in.read(buffer);
            if (read == -1) {
                break;
            }
            out.write(buffer, 0, read);
            return wavFile.getName();
        }
        out.close();
        in.close();
        return wavFile.getName();
    }

    public static void writeData(byte[] data) {
        if (mWavFile != null) {
            try {
                FileOutputStream out = new FileOutputStream(mWavFile, true);
                out.write(data);
                out.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e2) {
                e2.printStackTrace();
            }
        }
    }
}
