package com.example.tourlist.Course;

import static android.content.ContentValues.TAG;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.bumptech.glide.Glide;
import com.example.tourlist.R;

public class CourseDetail_Activity extends AppCompatActivity {

    private LinearLayout placesContainer;
    private TouristCourseRepository repository;
    private MutableLiveData<TouristCourse> touristCourse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frag_course_detail);

        placesContainer = findViewById(R.id.placesContainer);

        repository = TouristCourseRepository.getInstance(getApplication());

        touristCourse = new MutableLiveData<>();

        String contentId = getIntent().getStringExtra("CONTENT_ID");

        repository.loadTouristCourseDetails(contentId, touristCourse);

        touristCourse.observe(this, new Observer<TouristCourse>() {
            @Override
            public void onChanged(TouristCourse course) {
                if (course != null) {
                    for (TouristCoursePlace place : course.getPlaces()) {
                        addPlaceCard(place);
                    }
                }
            }
        });
    }

    private void addPlaceCard(TouristCoursePlace place) {
        CardView cardView = new CardView(this);
        LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        cardParams.setMargins(16, 16, 16, 16); // 상, 하, 좌, 우 마진 설정
        cardView.setLayoutParams(cardParams);
        cardView.setCardElevation(8);
        cardView.setRadius(16); // 모서리를 더 둥글게 설정

        LinearLayout cardContent = new LinearLayout(this);
        cardContent.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        cardContent.setOrientation(LinearLayout.VERTICAL);
        cardContent.setPadding(24, 24, 24, 24); // 카드뷰 내부에 더 많은 패딩 설정

        TextView nameTextView = new TextView(this);
        nameTextView.setText(place.getSubname());
        nameTextView.setTextSize(22); // 텍스트 크기 키움
        nameTextView.setLineSpacing(1.5f, 1.5f); // 줄 간격 설정

        ImageView imageView = new ImageView(this);
        LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                300); // 이미지뷰 높이 증가
        imageParams.setMargins(0, 16, 0, 16); // 이미지뷰 상하 마진 설정
        imageView.setLayoutParams(imageParams);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        // 공통정보조회 API로부터 firstimage를 가져와서 설정
        String imageUrl = place.getFirstimage();
//        String imageUrl = "http://tong.visitkorea.or.kr/cms/resource/87/2733187_image2_1.jpg";

        if (imageUrl!=null&&!imageUrl.isEmpty()) {
            Uri uri = Uri.parse(imageUrl);
            // 이미지를 비동기적으로 로드하는 라이브러리 사용 (예: Glide, Picasso)
            Glide.with(getApplication()).load(uri).into(imageView);
        } else {
//            imageView.setImageResource(R.drawable.placeholder_image); // 기본 이미지 설정
            Log.d(TAG, " null");
        }


//        Bitmap bitmap = base64ToBitmap(imageUrl);
//        if (bitmap != null) {
//            imageView.setImageBitmap(bitmap);
//        } else {
//            Log.d(TAG, "Decoded bitmap is null");
//        }

        TextView overviewTextView = new TextView(this);
        overviewTextView.setText(place.getSubdetailoverview());
        overviewTextView.setTextSize(18); // 텍스트 크기 키움
        overviewTextView.setLineSpacing(1.5f, 1.5f); // 줄 간격 설정

        cardContent.addView(nameTextView);
        cardContent.addView(imageView); // 이미지뷰 추가
        cardContent.addView(overviewTextView);

        cardView.addView(cardContent);

        placesContainer.addView(cardView);
    }

//    private Bitmap base64ToBitmap(String base64String) {
//        try {
//            byte[] decodedString = Base64.decode(base64String, Base64.DEFAULT);
//            return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
//        } catch (Exception e) {
//            Log.e(TAG, "Error decoding Base64 string", e);
//            return null;
//        }
//    }
}
