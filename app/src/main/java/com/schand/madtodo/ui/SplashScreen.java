package com.schand.madtodo.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.schand.madtodo.R;

public class SplashScreen extends AppCompatActivity {

    Animation topAnimation,bottomAnimation;

    ImageView logoImage;
    TextView devText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        //Animations

        topAnimation = AnimationUtils.loadAnimation(this,R.anim.topanim);
        bottomAnimation = AnimationUtils.loadAnimation(this,R.anim.bottomanim);

        logoImage = findViewById(R.id.imageView2);
        devText = findViewById(R.id.textView6);


        logoImage.setAnimation(topAnimation);
        devText.setAnimation(bottomAnimation);



        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences preferences = getApplicationContext().getSharedPreferences("madtodo", MODE_PRIVATE);

                int userId = preferences.getInt("user_id",0);
                
                if (userId > 0) {
                    Intent i = new Intent(SplashScreen.this, MainActivity.class);
                    i.putExtra("userid", userId);
                    startActivity(i);
                } else {

                    Intent i = new Intent(SplashScreen.this, LoginActivity.class);
                    startActivity(i);
                }
                finish();
            }
        }, 3000);

    }
}
