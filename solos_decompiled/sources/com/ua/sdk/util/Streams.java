package com.ua.sdk.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

/* JADX INFO: loaded from: classes65.dex */
public class Streams {
    public static void writeFully(CharSequence str, OutputStream out) throws IOException {
        try {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
            writer.write(str.toString());
            writer.flush();
            writer.close();
        } finally {
            out.close();
        }
    }

    public static String readFully(InputStream in) throws IOException {
        if (in == null) {
            return "";
        }
        StringBuffer response = new StringBuffer();
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        try {
            char[] buffer = new char[1024];
            for (int length = reader.read(buffer); length > 0; length = reader.read(buffer)) {
                response.append(buffer, 0, length);
            }
            return response.toString();
        } finally {
            try {
                in.close();
            } catch (IOException e) {
            }
        }
    }
}
