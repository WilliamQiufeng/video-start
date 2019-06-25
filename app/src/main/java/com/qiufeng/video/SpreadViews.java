package com.qiufeng.video;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SpreadViews extends View {
    List<SpreadBlock> blocks;
    boolean initialized = false;
    long lastTime = 0;
    long totalTime = 0;

    public SpreadViews(Context context) {
        super(context);
        blocks = new ArrayList<>();
    }

    /**
     * *    adjacent (certain)
     * * ____________ certain degree here (variable deg)
     * *|_|      (  /
     * o|         `/
     * p|         /
     * p|        /
     * o|       /                         opposite
     * s|      / hypotenuse   tan (deg) = --------
     * i|     /                    |      adjacent
     * t|    /                    \_/
     * e|   /                 opposite  = tan (deg) * adjacent
     * *|  /
     * *|-/
     * *|/ The point to be calculated for the slope
     * <p>
     * *              90 degrees
     * *               |
     * *   II(reversed)|    I (not reversed)
     * *               |
     * *               |
     * *               |
     * *0 ----------------------------- 180 degrees
     * *               |
     * *               |
     * * III(reversed) |   IV (not reversed)
     * *               |
     * *               |
     * *             270 degrees
     * **
     *
     * @param amount
     */
    public void generateRandomBlocks(int amount) {
        float range = 360; //stands for 360 degrees
        float interval = range / amount;
        for (float f = 0; f < range; f += interval) {
            boolean reverse = f < 90 || f > 270;
            float x = (f % 90f)/* * 0.1f*/;
            Log.i("com.qiufeng.video", "Generated block with " + f + " deg and x = " + x);
            float dist = (float) Math.tan(f) * x;
            Random rand = new Random();
            int size_x = rand.nextInt(200) + 100;
            int size_y = rand.nextInt(200) + 100;
            SpreadBlock spreadBlock = new SpreadBlock((reverse ? -1 : 1) * x, dist, size_x, size_y, reverse);
            spreadBlock.setSpeed(rand.nextInt(150));
            Log.d("SpreadBlockSpeed", String.valueOf(spreadBlock.speed));
            this.blocks.add(spreadBlock);
        }
        initialized = false;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        initialized = false;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //Background
        canvas.drawColor(Color.BLACK);
        //Time counting
        if (lastTime == 0) {
            lastTime = System.currentTimeMillis();
        } else {
            long now = System.currentTimeMillis();
            long howLong = now - lastTime;
            totalTime += howLong;
            lastTime = now;
        }
        if (!initialized) {
            for (SpreadBlock spreadBlock : blocks) {
                spreadBlock.calculateSlope(getWidth(), getHeight());
            }
            initialized = true;
        }
        for (SpreadBlock spreadBlock : blocks) {
            spreadBlock.draw(canvas);
            if (spreadBlock.getSpeed() == 0f) continue;
            spreadBlock.update();
            Log.i("com.qiufeng.video", spreadBlock.toString());

        }
    }
}
