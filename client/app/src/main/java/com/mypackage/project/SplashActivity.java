package com.mypackage.project;

import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.Timer;
import java.util.TimerTask;


public class SplashActivity extends AppCompatActivity {
int start = 0;
    TimerTask timerTask;
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Window window = getWindow();
        window.setFormat(PixelFormat.RGBA_8888);
    }

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        StartAnimations();
    }

    private void StartAnimations() {
        Display display = getWindowManager().getDefaultDisplay();
        int w = (display.getWidth());
        int h = (display.getHeight());
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.alpha);
        anim.reset();
        LinearLayout l = (LinearLayout) findViewById(R.id.lin_lay);
        l.clearAnimation();
        l.startAnimation(anim);
        anim = AnimationUtils.loadAnimation(this, R.anim.translate);
        anim.reset();
        ImageView iv = (ImageView) findViewById(R.id.logo);
        iv.getLayoutParams().height = h * 27 / 100;
        iv.getLayoutParams().width = w * 60 / 100;
        iv.clearAnimation();
        iv.startAnimation(anim);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                final Handler handler = new Handler();
                Timer t = new Timer();
                timerTask = new TimerTask() {
                    public void run() {
                        handler.post(new Runnable() {
                            public void run() {
                                if (start == 1) {
                                    timerTask.cancel();
                                    timerTask = null;
                                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                                start = 1;
                            }
                        });
                    }
                };

                // public void schedule (TimerTask task, long delay, long period)
                t.schedule(timerTask, 0, 1000);  //
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}