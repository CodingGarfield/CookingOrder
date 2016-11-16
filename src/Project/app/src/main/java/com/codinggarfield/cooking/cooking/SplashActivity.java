package com.codinggarfield.cooking.cooking;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SplashActivity extends AppCompatActivity {
    private RelativeLayout rootLayout;
    private TextView versionText;
    private ImageView bgiv;

    private static final int sleepTime = 3000;


    @Override
    protected void onCreate(Bundle arg0) {
        setContentView(R.layout.splash);
        super.onCreate(arg0);
//        getActionBar().hide();
        intologin=new Intent(this,LoginActivity.class);
        rootLayout = (RelativeLayout) findViewById(R.id.splash_root);
        versionText = (TextView) findViewById(R.id.tv_version);
        bgiv=(ImageView)findViewById(R.id.bgcolor);
        int colorA = Color.parseColor("#ffffff"),colorB = Color.parseColor("#3F51B5");
        ObjectAnimator objectAnimator = ObjectAnimator.ofInt(bgiv,"backgroundColor",colorA,colorB);
        objectAnimator.setDuration(3000);
        objectAnimator.setEvaluator(new ArgbEvaluator());
        objectAnimator.start();

        versionText.setText(getVersion());
        AlphaAnimation animation = new AlphaAnimation(0.3f, 1.0f);
        animation.setDuration(1000);
        rootLayout.startAnimation(animation);
    }

    Intent intologin;
    @Override
    protected void onStart() {
        super.onStart();

        final ConnectivityManager manager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        // 检查网络连接，如果无网络可用，就不需要进行连网操作等
        final NetworkInfo info = manager.getActiveNetworkInfo();
        new Thread(new Runnable() {
            public void run() {
                if(info == null || !manager.getBackgroundDataSetting()) {
                    Toast.makeText(getApplicationContext(),"无网络，请打开wifi或数据连接",Toast.LENGTH_SHORT).show();
                }
                else
                {
//                    if(1)
//                    {
//                        intoMain();
//                    }
//                    else
//                    {
                        try {
                            Thread.sleep(sleepTime);
                        } catch (InterruptedException e) {
                        }
                        startActivity(intologin);
                        finish();
//                    }
                }

            }
        }).start();
    }
    private void intoMain() {
        finish();
        Intent intomain = new Intent(this,MainActivity.class);
        startActivity(intomain);
    }

    /**
     * get sdk version
     */
    private String getVersion() {
        return "1.0.0";
    }
}
