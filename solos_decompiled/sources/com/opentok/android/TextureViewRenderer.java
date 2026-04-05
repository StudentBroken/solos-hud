package com.opentok.android;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.opengl.GLES20;
import android.opengl.Matrix;
import android.view.TextureView;
import android.view.View;
import com.opentok.android.BaseVideoRenderer;
import com.opentok.android.grafika.gles.EglCore;
import com.opentok.android.grafika.gles.WindowSurface;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.concurrent.locks.ReentrantLock;

/* JADX INFO: loaded from: classes15.dex */
public class TextureViewRenderer extends BaseVideoRenderer {
    Context ctx;
    Renderer renderer = new Renderer();
    boolean videoLastStatus;
    TextureView view;

    public TextureViewRenderer(Context context) {
        this.ctx = context;
        this.view = new TextureView(context);
        this.view.setSurfaceTextureListener(this.renderer);
        this.renderer.start();
    }

    @Override // com.opentok.android.BaseVideoRenderer
    public void onFrame(BaseVideoRenderer.Frame frame) {
        this.renderer.displayFrame(frame);
    }

    @Override // com.opentok.android.BaseVideoRenderer
    public void setStyle(String key, String value) {
        if (BaseVideoRenderer.STYLE_VIDEO_SCALE.equals(key)) {
            if (!BaseVideoRenderer.STYLE_VIDEO_FIT.equals(value)) {
                if (!BaseVideoRenderer.STYLE_VIDEO_FILL.equals(value)) {
                    return;
                }
                this.renderer.enableVideoFit(false);
                return;
            }
            this.renderer.enableVideoFit(true);
        }
    }

    @Override // com.opentok.android.BaseVideoRenderer
    public void onVideoPropertiesChanged(boolean videoEnabled) {
        this.renderer.setEnableVideo(videoEnabled);
    }

    @Override // com.opentok.android.BaseVideoRenderer
    public View getView() {
        return this.view;
    }

    @Override // com.opentok.android.BaseVideoRenderer
    public void onPause() {
        this.videoLastStatus = this.renderer.isEnableVideo();
        this.renderer.setEnableVideo(false);
    }

    @Override // com.opentok.android.BaseVideoRenderer
    public void onResume() {
        this.renderer.setEnableVideo(this.videoLastStatus);
    }

    private static class Renderer extends Thread implements TextureView.SurfaceTextureListener {
        static final int COORDS_PER_VERTEX = 3;
        static final int TEXTURECOORDS_PER_VERTEX = 2;
        BaseVideoRenderer.Frame currentFrame;
        private ShortBuffer drawListBuffer;
        EglCore eglCore;
        private final String fragmentShaderCode;
        ReentrantLock frameLock;
        int glProgram;
        Object lock;
        float[] scaleMatrix;
        SurfaceTexture surfaceTexture;
        private FloatBuffer textureBuffer;
        private int textureHeight;
        int[] textureIds;
        private int textureWidth;
        private FloatBuffer vertexBuffer;
        private short[] vertexIndex;
        private final String vertexShaderCode;
        private boolean videoEnabled;
        private boolean videoFitEnabled;
        private int viewportHeight;
        private int viewportWidth;
        static float[] xyzCoords = {-1.0f, 1.0f, 0.0f, -1.0f, -1.0f, 0.0f, 1.0f, -1.0f, 0.0f, 1.0f, 1.0f, 0.0f};
        static float[] uvCoords = {0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f, 1.0f, 0.0f};

        private Renderer() {
            this.vertexShaderCode = "uniform mat4 uMVPMatrix;attribute vec4 aPosition;\nattribute vec2 aTextureCoord;\nvarying vec2 vTextureCoord;\nvoid main() {\n  gl_Position = uMVPMatrix * aPosition;\n  vTextureCoord = aTextureCoord;\n}\n";
            this.fragmentShaderCode = "precision mediump float;\nuniform sampler2D Ytex;\nuniform sampler2D Utex,Vtex;\nvarying vec2 vTextureCoord;\nvoid main(void) {\n  float nx,ny,r,g,b,y,u,v;\n  mediump vec4 txl,ux,vx;  nx=vTextureCoord[0];\n  ny=vTextureCoord[1];\n  y=texture2D(Ytex,vec2(nx,ny)).r;\n  u=texture2D(Utex,vec2(nx,ny)).r;\n  v=texture2D(Vtex,vec2(nx,ny)).r;\n  y=1.1643*(y-0.0625);\n  u=u-0.5;\n  v=v-0.5;\n  r=y+1.5958*v;\n  g=y-0.39173*u-0.81290*v;\n  b=y+2.017*u;\n  gl_FragColor=vec4(r,g,b,1.0);\n}\n";
            this.lock = new Object();
            this.videoEnabled = true;
            this.videoFitEnabled = false;
            this.textureIds = new int[3];
            this.scaleMatrix = new float[16];
            this.vertexIndex = new short[]{0, 1, 2, 0, 2, 3};
            this.frameLock = new ReentrantLock();
            ByteBuffer bb = ByteBuffer.allocateDirect(xyzCoords.length * 4);
            bb.order(ByteOrder.nativeOrder());
            this.vertexBuffer = bb.asFloatBuffer();
            this.vertexBuffer.put(xyzCoords);
            this.vertexBuffer.position(0);
            ByteBuffer tb = ByteBuffer.allocateDirect(uvCoords.length * 4);
            tb.order(ByteOrder.nativeOrder());
            this.textureBuffer = tb.asFloatBuffer();
            this.textureBuffer.put(uvCoords);
            this.textureBuffer.position(0);
            ByteBuffer dlb = ByteBuffer.allocateDirect(this.vertexIndex.length * 2);
            dlb.order(ByteOrder.nativeOrder());
            this.drawListBuffer = dlb.asShortBuffer();
            this.drawListBuffer.put(this.vertexIndex);
            this.drawListBuffer.position(0);
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            waitUntilSurfaceIsReady();
            this.eglCore = new EglCore(null, 2);
            WindowSurface windowSurface = new WindowSurface(this.eglCore, this.surfaceTexture);
            windowSurface.makeCurrent();
            setupgl();
            renderFrameLoop(windowSurface);
            windowSurface.release();
            this.eglCore.release();
        }

        @Override // android.view.TextureView.SurfaceTextureListener
        public void onSurfaceTextureAvailable(SurfaceTexture st, int width, int height) {
            synchronized (this.lock) {
                this.surfaceTexture = st;
                this.viewportWidth = width;
                this.viewportHeight = height;
                this.lock.notify();
            }
        }

        @Override // android.view.TextureView.SurfaceTextureListener
        public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int width, int height) {
            GLES20.glViewport(0, 0, width, height);
            this.viewportWidth = width;
            this.viewportHeight = height;
        }

        @Override // android.view.TextureView.SurfaceTextureListener
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
            synchronized (this.lock) {
                this.surfaceTexture = null;
            }
            return true;
        }

        @Override // android.view.TextureView.SurfaceTextureListener
        public void onSurfaceTextureUpdated(SurfaceTexture surface) {
            synchronized (this.lock) {
                this.surfaceTexture = surface;
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void enableVideoFit(boolean videoFit) {
            this.videoFitEnabled = videoFit;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void setEnableVideo(boolean video) {
            this.videoEnabled = video;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean isEnableVideo() {
            return this.videoEnabled;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void displayFrame(BaseVideoRenderer.Frame frame) {
            this.frameLock.lock();
            if (this.currentFrame != null) {
                this.currentFrame.recycle();
            }
            this.currentFrame = frame;
            this.frameLock.unlock();
        }

        private void waitUntilSurfaceIsReady() {
            synchronized (this.lock) {
                while (this.surfaceTexture == null) {
                    try {
                        this.lock.wait();
                    } catch (InterruptedException e) {
                        OtLog.d("Waiting for surface ready was interrupted", new Object[0]);
                    }
                }
            }
        }

        private void initializeTexture(int name, int id, int width, int height) {
            GLES20.glActiveTexture(name);
            GLES20.glBindTexture(3553, id);
            GLES20.glTexParameterf(3553, 10241, 9728.0f);
            GLES20.glTexParameterf(3553, 10240, 9729.0f);
            GLES20.glTexParameterf(3553, 10242, 33071.0f);
            GLES20.glTexParameterf(3553, 10243, 33071.0f);
            GLES20.glTexImage2D(3553, 0, 6409, width, height, 0, 6409, 5121, null);
        }

        private void setupTextures(BaseVideoRenderer.Frame frame) {
            if (this.textureIds[0] != 0) {
                GLES20.glDeleteTextures(3, this.textureIds, 0);
            }
            GLES20.glGenTextures(3, this.textureIds, 0);
            int width = frame.getWidth();
            int height = frame.getHeight();
            int paddedWidth = (width + 1) >> 1;
            int paddedHeight = (height + 1) >> 1;
            initializeTexture(33984, this.textureIds[0], width, height);
            initializeTexture(33985, this.textureIds[1], paddedWidth, paddedHeight);
            initializeTexture(33986, this.textureIds[2], paddedWidth, paddedHeight);
            this.textureWidth = frame.getWidth();
            this.textureHeight = frame.getHeight();
        }

        private void updateTextures(BaseVideoRenderer.Frame frame) {
            int width = frame.getWidth();
            int height = frame.getHeight();
            int halfWidth = (width + 1) >> 1;
            int halfHeight = (height + 1) >> 1;
            int ySize = width * height;
            int uvSize = halfWidth * halfHeight;
            ByteBuffer bb = frame.getBuffer();
            bb.clear();
            if (bb.remaining() == (uvSize * 2) + ySize) {
                bb.position(0);
                GLES20.glPixelStorei(3317, 1);
                GLES20.glPixelStorei(3333, 1);
                GLES20.glActiveTexture(33984);
                GLES20.glBindTexture(3553, this.textureIds[0]);
                GLES20.glTexSubImage2D(3553, 0, 0, 0, width, height, 6409, 5121, bb);
                bb.position(ySize);
                GLES20.glActiveTexture(33985);
                GLES20.glBindTexture(3553, this.textureIds[1]);
                GLES20.glTexSubImage2D(3553, 0, 0, 0, halfWidth, halfHeight, 6409, 5121, bb);
                bb.position(ySize + uvSize);
                GLES20.glActiveTexture(33986);
                GLES20.glBindTexture(3553, this.textureIds[2]);
                GLES20.glTexSubImage2D(3553, 0, 0, 0, halfWidth, halfHeight, 6409, 5121, bb);
                return;
            }
            this.textureWidth = 0;
            this.textureHeight = 0;
        }

        private void renderFrameLoop(WindowSurface surface) {
            while (true) {
                synchronized (this.lock) {
                    if (this.surfaceTexture == null) {
                        return;
                    }
                }
                this.frameLock.lock();
                if (this.currentFrame != null && this.videoEnabled) {
                    GLES20.glUseProgram(this.glProgram);
                    if (this.textureWidth != this.currentFrame.getWidth() || this.textureHeight != this.currentFrame.getHeight()) {
                        setupTextures(this.currentFrame);
                    }
                    updateTextures(this.currentFrame);
                    Matrix.setIdentityM(this.scaleMatrix, 0);
                    float scalex = 1.0f;
                    float scaley = 1.0f;
                    float ratio = this.currentFrame.getWidth() / this.currentFrame.getHeight();
                    float vratio = this.viewportWidth / this.viewportHeight;
                    if (this.videoFitEnabled) {
                        if (ratio > vratio) {
                            scaley = vratio / ratio;
                        } else {
                            scalex = ratio / vratio;
                        }
                    } else if (ratio < vratio) {
                        scaley = vratio / ratio;
                    } else {
                        scalex = ratio / vratio;
                    }
                    Matrix.scaleM(this.scaleMatrix, 0, (this.currentFrame.isMirroredX() ? -1.0f : 1.0f) * scalex, scaley, 1.0f);
                    int mvpMatrix = GLES20.glGetUniformLocation(this.glProgram, "uMVPMatrix");
                    GLES20.glUniformMatrix4fv(mvpMatrix, 1, false, this.scaleMatrix, 0);
                    GLES20.glDrawElements(4, this.vertexIndex.length, 5123, this.drawListBuffer);
                } else {
                    GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
                    GLES20.glClear(16384);
                }
                this.frameLock.unlock();
                surface.swapBuffers();
            }
        }

        private int loadShader(int type, String shaderCode) {
            int shader = GLES20.glCreateShader(type);
            GLES20.glShaderSource(shader, shaderCode);
            GLES20.glCompileShader(shader);
            return shader;
        }

        private void setupgl() {
            GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
            GLES20.glClear(16384);
            int vertexShader = loadShader(35633, "uniform mat4 uMVPMatrix;attribute vec4 aPosition;\nattribute vec2 aTextureCoord;\nvarying vec2 vTextureCoord;\nvoid main() {\n  gl_Position = uMVPMatrix * aPosition;\n  vTextureCoord = aTextureCoord;\n}\n");
            int fragmentShader = loadShader(35632, "precision mediump float;\nuniform sampler2D Ytex;\nuniform sampler2D Utex,Vtex;\nvarying vec2 vTextureCoord;\nvoid main(void) {\n  float nx,ny,r,g,b,y,u,v;\n  mediump vec4 txl,ux,vx;  nx=vTextureCoord[0];\n  ny=vTextureCoord[1];\n  y=texture2D(Ytex,vec2(nx,ny)).r;\n  u=texture2D(Utex,vec2(nx,ny)).r;\n  v=texture2D(Vtex,vec2(nx,ny)).r;\n  y=1.1643*(y-0.0625);\n  u=u-0.5;\n  v=v-0.5;\n  r=y+1.5958*v;\n  g=y-0.39173*u-0.81290*v;\n  b=y+2.017*u;\n  gl_FragColor=vec4(r,g,b,1.0);\n}\n");
            this.glProgram = GLES20.glCreateProgram();
            GLES20.glAttachShader(this.glProgram, vertexShader);
            GLES20.glAttachShader(this.glProgram, fragmentShader);
            GLES20.glLinkProgram(this.glProgram);
            int positionHandle = GLES20.glGetAttribLocation(this.glProgram, "aPosition");
            int textureHandle = GLES20.glGetAttribLocation(this.glProgram, "aTextureCoord");
            GLES20.glVertexAttribPointer(positionHandle, 3, 5126, false, 12, (Buffer) this.vertexBuffer);
            GLES20.glEnableVertexAttribArray(positionHandle);
            GLES20.glVertexAttribPointer(textureHandle, 2, 5126, false, 8, (Buffer) this.textureBuffer);
            GLES20.glEnableVertexAttribArray(textureHandle);
            GLES20.glUseProgram(this.glProgram);
            int identifier = GLES20.glGetUniformLocation(this.glProgram, "Ytex");
            GLES20.glUniform1i(identifier, 0);
            int identifier2 = GLES20.glGetUniformLocation(this.glProgram, "Utex");
            GLES20.glUniform1i(identifier2, 1);
            int identifier3 = GLES20.glGetUniformLocation(this.glProgram, "Vtex");
            GLES20.glUniform1i(identifier3, 2);
            this.textureWidth = 0;
            this.textureHeight = 0;
        }
    }
}
