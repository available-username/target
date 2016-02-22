package se.thirdbase.target.util;

import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.util.Log;

/**
 * Created by alexp on 2/22/16.
 */
public class ViewMath {

    private static final String TAG = ViewMath.class.getSimpleName();

    private float MIN_ZOOM_FACTOR = 1.0f;

    private float mMaxZoomFactor;
    private float mPixelsPerCm;
    private float mZoomLevel = MIN_ZOOM_FACTOR;

    private float mRealWidth;
    private float mRealHeight;

    public Rect mSrcRect = new Rect();
    public Rect mDstRect = new Rect();
    public Rect mScaledRect = new Rect();

    private static int clamp(int val, int min, int max) {
        return Math.max(min, Math.min(max, val));
    }

    private static float clamp(float val, float min, float max) {
        return Math.max(min, Math.min(max, val));
    }

    public ViewMath(int viewWidth, int viewHeight, float realWidth, float realHeight, float maxZoomFactor) {
        mMaxZoomFactor = maxZoomFactor;
        mRealWidth = realWidth;
        mRealHeight = realHeight;

        int zoomWidth = (int) (viewWidth * maxZoomFactor);
        int zoomHeight = (int) (viewHeight * maxZoomFactor);

        mSrcRect = new Rect(0, 0, zoomWidth, zoomHeight);
        mScaledRect = new Rect(0, 0, viewWidth, viewHeight);
        mDstRect = new Rect(0, 0, viewWidth, viewHeight);

        mPixelsPerCm = ((float)viewWidth) / realWidth;
    }

    public void zoomIn() {
        zoomIn(mDstRect.width() / 2, mDstRect.height() / 2);
    }

    public void zoomIn(float pixelX, float pixelY) {
        int centerX = (int)((pixelX / mDstRect.right) * mSrcRect.right);
        int centerY = (int)((pixelY / mDstRect.bottom) * mSrcRect.bottom);

        int width = (int) ((mSrcRect.right - mSrcRect.left) / mMaxZoomFactor);
        int height = (int) ((mSrcRect.bottom - mSrcRect.top) / mMaxZoomFactor);

        mScaledRect.left = clamp(centerX - width / 2, 0, mSrcRect.right - width);
        mScaledRect.top = clamp(centerY - height / 2, 0, mSrcRect.bottom - height);
        mScaledRect.right = mScaledRect.left + mDstRect.width();
        mScaledRect.bottom = mScaledRect.top + mDstRect.height();

        mZoomLevel = mMaxZoomFactor;
    }

    public void zoomOut() {
        mScaledRect.top = mDstRect.top;
        mScaledRect.left = mDstRect.left;
        mScaledRect.bottom = mDstRect.bottom;
        mScaledRect.right = mDstRect.right;

        mZoomLevel = MIN_ZOOM_FACTOR;
    }

    /**
     * Translate from screen coordinates to source coordinates.
     *
     * @param pixelX screen x coordinate
     * @param pixelY screen y coordnate
     * @return a PointF representing the translation
     */
    public PointF translateCoordinate(float pixelX, float pixelY) {
        // Find out where we are in the source rectangle
        pixelX = mScaledRect.left + mScaledRect.width() * pixelX / mDstRect.width();
        pixelY = mScaledRect.top + mScaledRect.height() * pixelY / mDstRect.height();

        Log.d(TAG, "mScaledRect: " + mScaledRect.toShortString());

        return new PointF(pixelX, pixelY);
    }

    public PointF getCenterPixelCoordinate() {
        PointF center = new PointF();

        center.x = mScaledRect.width() * mZoomLevel / 2 - mScaledRect.left;
        center.y = mScaledRect.height() * mZoomLevel / 2 - mScaledRect.top;

        return center;
    }

    public void translate(float distanceX, float distanceY) {
        int width = mDstRect.width();
        int height = mDstRect.height();
        mScaledRect.left = (int) clamp(mScaledRect.left + distanceX, 0, mSrcRect.right - width);
        mScaledRect.top = (int) clamp(mScaledRect.top + distanceY, 0, mSrcRect.bottom - height);
        mScaledRect.right = mScaledRect.left + width;
        mScaledRect.bottom = mScaledRect.top + height;
    }

    public Rect getSrcRect() {
        return mSrcRect;
    }

    public Rect getScaledRect() {
        return mScaledRect;
    }

    public Rect getDstRect() {
        return mDstRect;
    }

    public float getPixelsPerCm() {
        return mPixelsPerCm *  mZoomLevel;
    }

    public float getZoomLevel() {
        return mZoomLevel;
    }
}
