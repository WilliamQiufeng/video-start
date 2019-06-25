/*
 * This file is created by Qiufeng54321.
 * Copyright (c) 2019.
 * All rights reserved.
 * This file is under GNU General Public License v3.0.
 */

package com.qiufeng.video;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.pm.ActivityInfo;
import android.content.res.AssetFileDescriptor;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    boolean doAddText = true;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        immerse();
        MediaPlayer player = playBGM();

        final FrameLayout frameLayout = new FrameLayout(this);
        frameLayout.setBackgroundColor(Color.BLACK);


        final SpreadViews sv = new SpreadViews(this);
        sv.generateRandomBlocks(40);


        frameLayout.addView(sv, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, Gravity.CENTER));

        final TextView textView = new TextView(MainActivity.this);
        textView.setText(R.string.author);
        textView.setTextSize(25);
        textView.setTextColor(Color.WHITE);
        textView.setTypeface(Typeface.MONOSPACE);
        frameLayout.addView(textView, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER));
        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
        ScaleAnimation scaleAnimation = new ScaleAnimation(1f, 1.2f, 1f, 1.2f/*, textView.getWidth() / 2f, textView.getHeight() / 2f*/);
        scaleAnimation.setFillAfter(true);
        AnimationSet animationSet = new AnimationSet(true);
        animationSet.setDuration(4000);
        animationSet.setFillAfter(true);
        animationSet.setInterpolator(new DecelerateInterpolator());
        animationSet.addAnimation(alphaAnimation);
        animationSet.addAnimation(scaleAnimation);
        alphaAnimation.setDuration(1000);
        scaleAnimation.setDuration(4000);
        animationSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                AnimatorSet animatorSet = new AnimatorSet();
                ObjectAnimator disappear1 = ObjectAnimator.ofFloat(textView, "alpha", 1, 0);
                ObjectAnimator appear1 = ObjectAnimator.ofFloat(textView, "alpha", 0, 1);
                ObjectAnimator disappear2 = ObjectAnimator.ofFloat(textView, "alpha", 1, 0);
                ObjectAnimator appear2 = ObjectAnimator.ofFloat(textView, "alpha", 0, 1);
                ObjectAnimator disappear3 = ObjectAnimator.ofFloat(textView, "alpha", 1, 0);
                ObjectAnimator disappear4 = ObjectAnimator.ofFloat(sv, "alpha", 1, 0);
                disappear1.setDuration(0);
                disappear2.setDuration(0);
                appear1.setDuration(0);
                disappear3.setDuration(0);
                appear2.setDuration(0);
                disappear4.setDuration(0);
                appear1.setStartDelay(150);
                disappear2.setStartDelay(150);
                appear2.setStartDelay(100);
                disappear3.setStartDelay(100);
                animatorSet.playSequentially(disappear1, appear1, disappear2, appear2, disappear3, disappear4);
                animatorSet.start();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        //textView.setAnimation(animationSet);


        setContentView(frameLayout);
        textView.startAnimation(animationSet);
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                sv.invalidate();
                handler.postDelayed(this, 100);
            }
        });
        player.start();
        //setContentView(R.layout.activity_main);
    }

    protected void immerse() {
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            //设置让应用主题内容占据状态栏和导航栏
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            //设置状态栏和导航栏颜色为透明
            getWindow().setStatusBarColor(Color.TRANSPARENT);
            getWindow().setNavigationBarColor(Color.TRANSPARENT);
        }
        this.getSupportActionBar().hide();
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        // Following code from https://blog.csdn.net/do168/article/details/51587935
        Window window = getWindow();
        //隐藏状态栏
        //定义全屏参数
        int flag = WindowManager.LayoutParams.FLAG_FULLSCREEN;
        //设置当前窗体为全屏显示
        window.setFlags(flag, flag);
        //End following code from https://blog.csdn.net/do168/article/details/51587935

        setImmersive(true);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    protected MediaPlayer playBGM() {
        AssetFileDescriptor is = getResources().openRawResourceFd(R.raw.beginning);
        MediaPlayer mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(is);
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mediaPlayer;
    }
}
