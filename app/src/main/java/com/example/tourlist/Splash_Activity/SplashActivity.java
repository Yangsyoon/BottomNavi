package com.example.tourlist.Splash_Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.ViewTreeObserver;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.FrameLayout;

import com.example.tourlist.Main.MainActivity;
import com.example.tourlist.R;

public class SplashActivity extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 3000; // 스플래시 화면 표시 시간 (3초)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        final ImageView splashImage = findViewById(R.id.splashImage);

        final FrameLayout parentLayout = (FrameLayout) splashImage.getParent();

        // 뷰 레이아웃 초기화 완료 후 실행
        splashImage.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                splashImage.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                // 부모 뷰의 초기 크기 가져오기
                int parentWidth = parentLayout.getWidth();
                int parentHeight = parentLayout.getHeight();

                // 자식 뷰의 크기를 부모 뷰보다 더 크게 설정
                FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(parentWidth * 2, parentHeight);
                splashImage.setLayoutParams(layoutParams);

                // 초기 위치 조정
                splashImage.setTranslationX(-parentWidth / 2);

                // 애니메이션 설정 (이미지가 중심에서 시작하여 좌측으로 이동)
                TranslateAnimation animation = new TranslateAnimation(0, parentWidth / 4, 0, 0);
                animation.setDuration(SPLASH_TIME_OUT); // 애니메이션 지속 시간
                animation.setFillAfter(true); // 애니메이션 종료 후 위치 고정
                splashImage.startAnimation(animation);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent i = new Intent(SplashActivity.this, MainActivity.class);
                        startActivity(i);
                        finish();
                    }
                }, SPLASH_TIME_OUT);
            }
        });
    }
}
