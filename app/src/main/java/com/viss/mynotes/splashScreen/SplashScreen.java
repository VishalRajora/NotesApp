package com.viss.mynotes.splashScreen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.viss.mynotes.R;
import com.viss.mynotes.auth.LoginClass;

public class SplashScreen extends AppCompatActivity {

    ImageView logo, mainTitle, subtitle;
    Animation topAni, bottonAni;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_splash_screen );


        getWindow ().setFlags ( WindowManager.LayoutParams.FLAG_FULLSCREEN , WindowManager.LayoutParams.FLAG_FULLSCREEN );//for full Screen

        //view cast
        logo = findViewById ( R.id.logoSplash );
        mainTitle = findViewById ( R.id.mainTitile );
        subtitle = findViewById ( R.id.subtitile );

        //animation casting
        topAni = AnimationUtils.loadAnimation ( SplashScreen.this , R.anim.top_animation_splash );
        bottonAni = AnimationUtils.loadAnimation ( SplashScreen.this , R.anim.bottom_animation_splash );


        logo.setAnimation ( topAni );
        mainTitle.setAnimation ( bottonAni );
        subtitle.setAnimation ( bottonAni );

        Handler handler = new Handler ();
        handler.postDelayed ( new Runnable () {
            @Override
            public void run() {

                startActivity ( new Intent ( SplashScreen.this , LoginClass.class ) );

                finish ();
            }
        } , 2500 );
    }
}
