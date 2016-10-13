package com.orangemoo.com.beta.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.orangemoo.com.beta.R;
import com.orangemoo.com.beta.activity.MainActivity;
import com.orangemoo.com.beta.activity.MaterialTutorialActivity;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * @author Rebecca Franks
 * @since 2015/07/16 3:16 PM
 */
public class SplashActivity extends AppCompatActivity implements SplashContract.View {
    private static final int SPLASH_SCREEN_REQUEST_CODE = 1;
    private SplashContract.UserActionsListener splashPresenter;

    @Bind(R.id.ic_launch_title)
    ImageView imageView;

    @Bind(R.id.tx_launch_title)
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ButterKnife.bind(this);

        Animation operatingAnim = AnimationUtils.loadAnimation(this, R.anim.luancher_img_repeat);
        imageView.setAnimation(operatingAnim);
        operatingAnim.start();

        /*TranslateAnimation alphaAnimation2 = new TranslateAnimation(0, 0, 0,
                -70);
        alphaAnimation2.setDuration(800);
        alphaAnimation2.setRepeatCount(3);
        alphaAnimation2.setRepeatMode(Animation.REVERSE);
        textView.setAnimation(alphaAnimation2);
        alphaAnimation2.start();*/

        splashPresenter = new SplashPresenter(this);
        showSplashAfterDelay();
    }

    private void showSplashAfterDelay() {
        (new Handler()).postDelayed(new Runnable() {
            @Override
            public void run() {
                splashPresenter.loadSplash();
            }
        }, 3500);

    }

    @Override
    public void loadTutorial() {
        Intent mainAct = new Intent(SplashActivity.this, MaterialTutorialActivity.class);
        mainAct.putParcelableArrayListExtra(MaterialTutorialActivity.MATERIAL_TUTORIAL_ARG_TUTORIAL_ITEMS, splashPresenter.getTutorialItems(this));
        startActivityForResult(mainAct, SPLASH_SCREEN_REQUEST_CODE);
    }


    @Override
    public void loadMainScreen() {
        Intent mainAct = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(mainAct);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //    super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == SPLASH_SCREEN_REQUEST_CODE) {
            splashPresenter.finishedTutorial();

        }
    }
}
