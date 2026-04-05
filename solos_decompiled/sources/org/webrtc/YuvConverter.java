package org.webrtc;

import android.opengl.GLES20;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import org.webrtc.ThreadUtils;

/* JADX INFO: loaded from: classes57.dex */
class YuvConverter {
    private static final String FRAGMENT_SHADER = "#extension GL_OES_EGL_image_external : require\nprecision mediump float;\nvarying vec2 interp_tc;\n\nuniform samplerExternalOES oesTex;\nuniform vec2 xUnit;\nuniform vec4 coeffs;\n\nvoid main() {\n  gl_FragColor.r = coeffs.a + dot(coeffs.rgb,\n      texture2D(oesTex, interp_tc - 1.5 * xUnit).rgb);\n  gl_FragColor.g = coeffs.a + dot(coeffs.rgb,\n      texture2D(oesTex, interp_tc - 0.5 * xUnit).rgb);\n  gl_FragColor.b = coeffs.a + dot(coeffs.rgb,\n      texture2D(oesTex, interp_tc + 0.5 * xUnit).rgb);\n  gl_FragColor.a = coeffs.a + dot(coeffs.rgb,\n      texture2D(oesTex, interp_tc + 1.5 * xUnit).rgb);\n}\n";
    private static final String VERTEX_SHADER = "varying vec2 interp_tc;\nattribute vec4 in_pos;\nattribute vec4 in_tc;\n\nuniform mat4 texMatrix;\n\nvoid main() {\n    gl_Position = in_pos;\n    interp_tc = (texMatrix * in_tc).xy;\n}\n";
    private final int coeffsLoc;
    private int frameBufferHeight;
    private final int frameBufferId;
    private int frameBufferWidth;
    private final int frameTextureId;
    private final GlShader shader;
    private final int texMatrixLoc;
    private final int xUnitLoc;
    private static final FloatBuffer DEVICE_RECTANGLE = GlUtil.createFloatBuffer(new float[]{-1.0f, -1.0f, 1.0f, -1.0f, -1.0f, 1.0f, 1.0f, 1.0f});
    private static final FloatBuffer TEXTURE_RECTANGLE = GlUtil.createFloatBuffer(new float[]{0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f});
    private final ThreadUtils.ThreadChecker threadChecker = new ThreadUtils.ThreadChecker();
    private boolean released = false;

    public YuvConverter() {
        this.threadChecker.checkIsOnValidThread();
        this.frameTextureId = GlUtil.generateTexture(3553);
        this.frameBufferWidth = 0;
        this.frameBufferHeight = 0;
        int[] frameBuffers = new int[1];
        GLES20.glGenFramebuffers(1, frameBuffers, 0);
        this.frameBufferId = frameBuffers[0];
        GLES20.glBindFramebuffer(36160, this.frameBufferId);
        GlUtil.checkNoGLES2Error("Generate framebuffer");
        GLES20.glFramebufferTexture2D(36160, 36064, 3553, this.frameTextureId, 0);
        GlUtil.checkNoGLES2Error("Attach texture to framebuffer");
        GLES20.glBindFramebuffer(36160, 0);
        this.shader = new GlShader(VERTEX_SHADER, FRAGMENT_SHADER);
        this.shader.useProgram();
        this.texMatrixLoc = this.shader.getUniformLocation("texMatrix");
        this.xUnitLoc = this.shader.getUniformLocation("xUnit");
        this.coeffsLoc = this.shader.getUniformLocation("coeffs");
        GLES20.glUniform1i(this.shader.getUniformLocation("oesTex"), 0);
        GlUtil.checkNoGLES2Error("Initialize fragment shader uniform values.");
        this.shader.setVertexAttribArray("in_pos", 2, DEVICE_RECTANGLE);
        this.shader.setVertexAttribArray("in_tc", 2, TEXTURE_RECTANGLE);
    }

    public void convert(ByteBuffer buf, int width, int height, int stride, int srcTextureId, float[] transformMatrix) {
        this.threadChecker.checkIsOnValidThread();
        if (this.released) {
            throw new IllegalStateException("YuvConverter.convert called on released object");
        }
        if (stride % 8 != 0) {
            throw new IllegalArgumentException("Invalid stride, must be a multiple of 8");
        }
        if (stride < width) {
            throw new IllegalArgumentException("Invalid stride, must >= width");
        }
        int y_width = (width + 3) / 4;
        int uv_width = (width + 7) / 8;
        int uv_height = (height + 1) / 2;
        int total_height = height + uv_height;
        int size = stride * total_height;
        if (buf.capacity() < size) {
            throw new IllegalArgumentException("YuvConverter.convert called with too small buffer");
        }
        float[] transformMatrix2 = RendererCommon.multiplyMatrices(transformMatrix, RendererCommon.verticalFlipMatrix());
        GLES20.glBindFramebuffer(36160, this.frameBufferId);
        GlUtil.checkNoGLES2Error("glBindFramebuffer");
        if (this.frameBufferWidth != stride / 4 || this.frameBufferHeight != total_height) {
            this.frameBufferWidth = stride / 4;
            this.frameBufferHeight = total_height;
            GLES20.glActiveTexture(33984);
            GLES20.glBindTexture(3553, this.frameTextureId);
            GLES20.glTexImage2D(3553, 0, 6408, this.frameBufferWidth, this.frameBufferHeight, 0, 6408, 5121, null);
            int status = GLES20.glCheckFramebufferStatus(36160);
            if (status != 36053) {
                throw new IllegalStateException("Framebuffer not complete, status: " + status);
            }
        }
        GLES20.glActiveTexture(33984);
        GLES20.glBindTexture(36197, srcTextureId);
        GLES20.glUniformMatrix4fv(this.texMatrixLoc, 1, false, transformMatrix2, 0);
        GLES20.glViewport(0, 0, y_width, height);
        GLES20.glUniform2f(this.xUnitLoc, transformMatrix2[0] / width, transformMatrix2[1] / width);
        GLES20.glUniform4f(this.coeffsLoc, 0.299f, 0.587f, 0.114f, 0.0f);
        GLES20.glDrawArrays(5, 0, 4);
        GLES20.glViewport(0, height, uv_width, uv_height);
        GLES20.glUniform2f(this.xUnitLoc, (2.0f * transformMatrix2[0]) / width, (2.0f * transformMatrix2[1]) / width);
        GLES20.glUniform4f(this.coeffsLoc, -0.169f, -0.331f, 0.499f, 0.5f);
        GLES20.glDrawArrays(5, 0, 4);
        GLES20.glViewport(stride / 8, height, uv_width, uv_height);
        GLES20.glUniform4f(this.coeffsLoc, 0.499f, -0.418f, -0.0813f, 0.5f);
        GLES20.glDrawArrays(5, 0, 4);
        GLES20.glReadPixels(0, 0, this.frameBufferWidth, this.frameBufferHeight, 6408, 5121, buf);
        GlUtil.checkNoGLES2Error("YuvConverter.convert");
        GLES20.glBindFramebuffer(36160, 0);
        GLES20.glBindTexture(3553, 0);
        GLES20.glBindTexture(36197, 0);
    }

    public void release() {
        this.threadChecker.checkIsOnValidThread();
        this.released = true;
        this.shader.release();
        GLES20.glDeleteTextures(1, new int[]{this.frameTextureId}, 0);
        GLES20.glDeleteFramebuffers(1, new int[]{this.frameBufferId}, 0);
        this.frameBufferWidth = 0;
        this.frameBufferHeight = 0;
    }
}
