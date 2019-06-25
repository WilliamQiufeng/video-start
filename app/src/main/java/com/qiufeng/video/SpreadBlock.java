package com.qiufeng.video;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

import java.util.Random;

public class SpreadBlock {
    float x, y, size_x, size_y;
    float speed;
    long lastTime = System.currentTimeMillis();
    private Paint paint = new Paint();
    private int alpha = new Random().nextInt(150);
    private double slope;
    /**
     * Suppose there is a graph:
     * *    | <- (-5, 5)
     * \   |
     * \  |
     * \ |
     * _____\O_______
     * |\
     * | \
     * |  \
     * |   \
     * |    \ <- (5, -5)
     * <p>
     * Here we've calculated the slope that is 1.
     * When spreading animations are acting, it wouldn't know whether its x-axis should increase or
     * decrease without a reverse sign showing whether the x value should decrease.
     */
    private boolean reverse;

    public SpreadBlock(float x, float y, float size_x, float size_y, boolean reverse) {
        this.x = x;
        this.y = y;
        this.size_x = size_x;
        this.size_y = size_y;
        this.reverse = reverse;
        paint.setColor(0xE91E63);
        paint.setAlpha(alpha);
    }

    /**
     * (y2 - y1)
     * Slope(m) = ---------
     * (x2 - x1)
     * And, of course, we have to calculate the center x and center y.
     *
     * @param canvas_w
     * @param canvas_h
     */
    public void calculateSlope(int canvas_w, int canvas_h) {
        /*int center_x = canvas_w / 2;
        int center_y = canvas_h / 2;
        slope = (y - center_y) / (x - center_x);*/
        slope = y / x;
    }

    public void draw(Canvas canvas) {
        int canvas_w = canvas.getWidth() / 2;
        int canvas_h = canvas.getHeight() / 2;
        float abs_x = canvas_w + x;
        float abs_y = canvas_h + y;
        //Draw the rectangle relatively in center.
        float center_from_x = abs_x - size_x / 2;
        float center_from_y = abs_y - size_y / 2;
        float center_to_x = abs_x + size_x / 2;
        float center_to_y = abs_y + size_y / 2;
        canvas.drawRect(center_from_x, center_from_y, center_to_x, center_to_y, paint);
    }

    public void update() {
        long now = System.currentTimeMillis();
        float last = (now - lastTime) / 1000f;
        Log.i("TimeLast", String.valueOf(last));
        //The speed decays as it approaches the edge
        speed = speed * 0.8f;
        x += speed * (reverse ? -1 : 1);
        y = (float) slope * x * 0.5f;
        // Simulates z-axis's increase in order to let it looks like it is closer when spread out
        size_x += speed * .4;
        size_y += speed * .4;
        lastTime = now;
    }

    @Override
    public String toString() {
        return "SpreadBlock{" +
                "alpha=" + alpha +
                ", slope=" + slope +
                ", reverse=" + reverse +
                ", x=" + x +
                ", y=" + y +
                ", size_x=" + size_x +
                ", size_y=" + size_y +
                ", speed=" + speed +
                ", lastTime=" + lastTime +
                '}';
    }

/*
        Getters and Setters below
     */

    public Paint getPaint() {
        return paint;
    }

    public void setPaint(Paint paint) {
        this.paint = paint;
    }

    public int getAlpha() {
        return alpha;
    }

    public void setAlpha(int alpha) {
        this.alpha = alpha;
    }

    public double getSlope() {
        return slope;
    }

    public void setSlope(double slope) {
        this.slope = slope;
    }

    public boolean isReverse() {
        return reverse;
    }

    public void setReverse(boolean reverse) {
        this.reverse = reverse;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getSize_x() {
        return size_x;
    }

    public void setSize_x(float size_x) {
        this.size_x = size_x;
    }

    public float getSize_y() {
        return size_y;
    }

    public void setSize_y(float size_y) {
        this.size_y = size_y;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
        Log.i("ChangeInSpeed", String.valueOf(this.speed));
    }

    public long getLastTime() {
        return lastTime;
    }

    public void setLastTime(long lastTime) {
        this.lastTime = lastTime;
    }
}
