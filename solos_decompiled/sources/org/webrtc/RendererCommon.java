package org.webrtc;

import android.graphics.Point;
import android.opengl.GLES20;
import android.opengl.Matrix;
import android.view.View;
import java.nio.ByteBuffer;

/* JADX INFO: loaded from: classes57.dex */
public class RendererCommon {
    private static float BALANCED_VISIBLE_FRACTION = 0.5625f;

    public interface GlDrawer {
        void drawOes(int i, float[] fArr, int i2, int i3, int i4, int i5, int i6, int i7);

        void drawRgb(int i, float[] fArr, int i2, int i3, int i4, int i5, int i6, int i7);

        void drawYuv(int[] iArr, float[] fArr, int i, int i2, int i3, int i4, int i5, int i6);

        void release();
    }

    public interface RendererEvents {
        void onFirstFrameRendered();

        void onFrameResolutionChanged(int i, int i2, int i3);
    }

    public enum ScalingType {
        SCALE_ASPECT_FIT,
        SCALE_ASPECT_FILL,
        SCALE_ASPECT_BALANCED
    }

    public static class YuvUploader {
        private ByteBuffer copyBuffer;

        public void uploadYuvData(int[] outputYuvTextures, int width, int height, int[] strides, ByteBuffer[] planes) {
            ByteBuffer packedByteBuffer;
            int[] planeWidths = {width, width / 2, width / 2};
            int[] planeHeights = {height, height / 2, height / 2};
            int copyCapacityNeeded = 0;
            for (int i = 0; i < 3; i++) {
                if (strides[i] > planeWidths[i]) {
                    copyCapacityNeeded = Math.max(copyCapacityNeeded, planeWidths[i] * planeHeights[i]);
                }
            }
            if (copyCapacityNeeded > 0 && (this.copyBuffer == null || this.copyBuffer.capacity() < copyCapacityNeeded)) {
                this.copyBuffer = ByteBuffer.allocateDirect(copyCapacityNeeded);
            }
            for (int i2 = 0; i2 < 3; i2++) {
                GLES20.glActiveTexture(33984 + i2);
                GLES20.glBindTexture(3553, outputYuvTextures[i2]);
                if (strides[i2] == planeWidths[i2]) {
                    packedByteBuffer = planes[i2];
                } else {
                    VideoRenderer.nativeCopyPlane(planes[i2], planeWidths[i2], planeHeights[i2], strides[i2], this.copyBuffer, planeWidths[i2]);
                    packedByteBuffer = this.copyBuffer;
                }
                GLES20.glTexImage2D(3553, 0, 6409, planeWidths[i2], planeHeights[i2], 0, 6409, 5121, packedByteBuffer);
            }
        }
    }

    public static class VideoLayoutMeasure {
        private ScalingType scalingTypeMatchOrientation = ScalingType.SCALE_ASPECT_BALANCED;
        private ScalingType scalingTypeMismatchOrientation = ScalingType.SCALE_ASPECT_BALANCED;

        public void setScalingType(ScalingType scalingType) {
            this.scalingTypeMatchOrientation = scalingType;
            this.scalingTypeMismatchOrientation = scalingType;
        }

        public void setScalingType(ScalingType scalingTypeMatchOrientation, ScalingType scalingTypeMismatchOrientation) {
            this.scalingTypeMatchOrientation = scalingTypeMatchOrientation;
            this.scalingTypeMismatchOrientation = scalingTypeMismatchOrientation;
        }

        public Point measure(int widthSpec, int heightSpec, int frameWidth, int frameHeight) {
            int maxWidth = View.getDefaultSize(Integer.MAX_VALUE, widthSpec);
            int maxHeight = View.getDefaultSize(Integer.MAX_VALUE, heightSpec);
            if (frameWidth == 0 || frameHeight == 0 || maxWidth == 0 || maxHeight == 0) {
                return new Point(maxWidth, maxHeight);
            }
            float frameAspect = frameWidth / frameHeight;
            float displayAspect = maxWidth / maxHeight;
            ScalingType scalingType = ((frameAspect > 1.0f ? 1 : (frameAspect == 1.0f ? 0 : -1)) > 0) == ((displayAspect > 1.0f ? 1 : (displayAspect == 1.0f ? 0 : -1)) > 0) ? this.scalingTypeMatchOrientation : this.scalingTypeMismatchOrientation;
            Point layoutSize = RendererCommon.getDisplaySize(scalingType, frameAspect, maxWidth, maxHeight);
            if (View.MeasureSpec.getMode(widthSpec) == 1073741824) {
                layoutSize.x = maxWidth;
            }
            if (View.MeasureSpec.getMode(heightSpec) == 1073741824) {
                layoutSize.y = maxHeight;
                return layoutSize;
            }
            return layoutSize;
        }
    }

    public static final float[] identityMatrix() {
        return new float[]{1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f};
    }

    public static final float[] verticalFlipMatrix() {
        return new float[]{1.0f, 0.0f, 0.0f, 0.0f, 0.0f, -1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 1.0f};
    }

    public static final float[] horizontalFlipMatrix() {
        return new float[]{-1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f};
    }

    public static float[] rotateTextureMatrix(float[] textureMatrix, float rotationDegree) {
        float[] rotationMatrix = new float[16];
        Matrix.setRotateM(rotationMatrix, 0, rotationDegree, 0.0f, 0.0f, 1.0f);
        adjustOrigin(rotationMatrix);
        return multiplyMatrices(textureMatrix, rotationMatrix);
    }

    public static float[] multiplyMatrices(float[] a, float[] b) {
        float[] resultMatrix = new float[16];
        Matrix.multiplyMM(resultMatrix, 0, a, 0, b, 0);
        return resultMatrix;
    }

    public static float[] getLayoutMatrix(boolean mirror, float videoAspectRatio, float displayAspectRatio) {
        float scaleX = 1.0f;
        float scaleY = 1.0f;
        if (displayAspectRatio > videoAspectRatio) {
            scaleY = videoAspectRatio / displayAspectRatio;
        } else {
            scaleX = displayAspectRatio / videoAspectRatio;
        }
        if (mirror) {
            scaleX *= -1.0f;
        }
        float[] matrix = new float[16];
        Matrix.setIdentityM(matrix, 0);
        Matrix.scaleM(matrix, 0, scaleX, scaleY, 1.0f);
        adjustOrigin(matrix);
        return matrix;
    }

    public static Point getDisplaySize(ScalingType scalingType, float videoAspectRatio, int maxDisplayWidth, int maxDisplayHeight) {
        return getDisplaySize(convertScalingTypeToVisibleFraction(scalingType), videoAspectRatio, maxDisplayWidth, maxDisplayHeight);
    }

    private static void adjustOrigin(float[] matrix) {
        matrix[12] = matrix[12] - ((matrix[0] + matrix[4]) * 0.5f);
        matrix[13] = matrix[13] - ((matrix[1] + matrix[5]) * 0.5f);
        matrix[12] = matrix[12] + 0.5f;
        matrix[13] = matrix[13] + 0.5f;
    }

    private static float convertScalingTypeToVisibleFraction(ScalingType scalingType) {
        switch (scalingType) {
            case SCALE_ASPECT_FIT:
                return 1.0f;
            case SCALE_ASPECT_FILL:
                return 0.0f;
            case SCALE_ASPECT_BALANCED:
                return BALANCED_VISIBLE_FRACTION;
            default:
                throw new IllegalArgumentException();
        }
    }

    private static Point getDisplaySize(float minVisibleFraction, float videoAspectRatio, int maxDisplayWidth, int maxDisplayHeight) {
        if (minVisibleFraction == 0.0f || videoAspectRatio == 0.0f) {
            return new Point(maxDisplayWidth, maxDisplayHeight);
        }
        int width = Math.min(maxDisplayWidth, Math.round((maxDisplayHeight / minVisibleFraction) * videoAspectRatio));
        int height = Math.min(maxDisplayHeight, Math.round((maxDisplayWidth / minVisibleFraction) / videoAspectRatio));
        return new Point(width, height);
    }
}
