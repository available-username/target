package se.thirdbase.target.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;

import se.thirdbase.target.model.BulletHole;

/**
 * Created by alexp on 2/19/16.
 */
public class PrecisionTargetView extends TargetView {

    private static final String TAG = PrecisionTargetView.class.getSimpleName();

    private static final int MAX_NBR_BULLETS = 5;
    private static final float VIRTUAL_WIDTH = 60.0f; //cm
    private static float VIRTUAL_HEIGHT;

    public PrecisionTargetView(Context context) {
        super(context, null);
    }

    public PrecisionTargetView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public int getMaxNbrBullets() {
        return MAX_NBR_BULLETS;
    }

    @Override
    public int getBulletScore(int bulletIdx) {
        BulletHole hole = mBulletHoles.get(bulletIdx);
        float diameter = hole.getCaliber().getDiameter();
        float radius = Math.abs(hole.getRadius() - diameter / 2);

        Log.d(TAG, "Radius: " + radius);
        return (int)Math.ceil(10 - radius / 2.5f);
    }

    @Override
    public int getTotalScore() {
        int total = 0;
        int size = mBulletHoles.size();

        for (int i = 0; i < size; i++) {
            total += getBulletScore(i);
        }

        return total;
    }

    private boolean touches(float bulletRadius, float bulletDiameter, float radius) {
        float upperBound = bulletRadius + bulletDiameter / 2;
        float lowerBound = bulletRadius - bulletDiameter / 2;

        return lowerBound <= radius && radius <= upperBound;
    }

    @Override
    protected void drawTarget(Canvas canvas) {
        float zoomLevel = getZoomLevel();
        float pixelsPerCm = zoomLevel * getPixelsPerCm();

        float radiusIncrement = 2.5f; // cm
        float textSize = pixelsPerCm * radiusIncrement * 0.75f;
        float textHeightOffset = textSize / 2;
        float textWidthOffset = (pixelsPerCm * radiusIncrement) / 2;

        PointF center = getCenterPixelCoordinate();

        Paint paint = new Paint();
        paint.setColor(Color.GREEN);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        canvas.drawColor(Color.WHITE);

        paint.setAntiAlias(true);
        paint.setStrokeWidth(2);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.BLACK);

        float bulletRadius = Float.MAX_VALUE;
        float bulletDiameter = 0f;
        int ring;

        BulletHole bulletHole = getActiveBulletHole();
        if (bulletHole != null) {
            bulletRadius = bulletHole.getRadius();
            bulletDiameter = bulletHole.getCaliber().getDiameter();
        }

        for (ring = 10; ring > 4; ring--) {
            float radius = ring * radiusIncrement;

            if (touches(bulletRadius, bulletDiameter, radius)) {
                paint.setColor(Color.RED);
            } else {
                paint.setColor(Color.BLACK);
            }

            canvas.drawCircle(center.x, center.y, radius * pixelsPerCm, paint);
        }

        paint.setColor(Color.BLACK);

        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        canvas.drawCircle(center.x, center.y, ring * radiusIncrement * pixelsPerCm, paint);

        paint.setStyle(Paint.Style.STROKE);

        for (; ring > 0; ring--) {
            float radius = ring * radiusIncrement;

            if (touches(bulletRadius, bulletDiameter, radius)) {
                paint.setColor(Color.RED);
            } else {
                if (ring == 4) {
                    paint.setColor(Color.BLACK);
                } else {
                    paint.setColor(Color.WHITE);
                }
            }

            canvas.drawCircle(center.x, center.y, radius * pixelsPerCm, paint);
        }

        paint.setColor(Color.WHITE);

        // finally, the inner ring
        canvas.drawCircle(center.x, center.y, 1.25f * pixelsPerCm, paint);

        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setStrokeWidth(1);
        paint.setFakeBoldText(true);
        paint.setTextSize(textSize);
        paint.setTextAlign(Paint.Align.CENTER);

        for (int i = 0; i < 4; i++) {
            double angle = i * Math.PI / 2;

            int num;
            for (num = 1; num < 10; num++) {
                int color;

                if (num < 4) {
                    color = Color.WHITE;
                } else {
                    color = Color.BLACK;
                }

                paint.setColor(color);

                float r = pixelsPerCm * radiusIncrement * num;
                float x = center.x + (float) (r * Math.cos(angle));
                float y = center.y + (float) (r * Math.sin(angle));

                switch (i) {
                    case 0:
                        paint.setTextAlign(Paint.Align.CENTER);
                        x += textWidthOffset;
                        y += textHeightOffset / 2;
                        break;
                    case 1:
                        paint.setTextAlign(Paint.Align.CENTER);
                        y += textHeightOffset * 2;
                        break;
                    case 2:
                        paint.setTextAlign(Paint.Align.CENTER);
                        x -= textWidthOffset;
                        y += textHeightOffset / 2;
                        break;
                    case 3:
                        paint.setTextAlign(Paint.Align.CENTER);
                        y -= textHeightOffset;
                        break;
                }

                canvas.drawText("" + (10 - num), x, y, paint);
            }
        }
    }
}
