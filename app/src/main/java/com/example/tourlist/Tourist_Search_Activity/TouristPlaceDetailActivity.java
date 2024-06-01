package com.example.tourlist.Tourist_Search_Activity;

import static android.content.ContentValues.TAG;

import static java.security.AccessController.getContext;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tourlist.Main.FavoriteLocation;
import com.example.tourlist.Main.Frag2_FavoriteList;
import com.example.tourlist.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.MapView;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.overlay.Marker;

public class TouristPlaceDetailActivity extends AppCompatActivity implements OnMapReadyCallback {
    private FirebaseAuth mAuth;
    private MapView mapView;

    private DatabaseReference mDatabase;

    public TouristPlace place ;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tourist_place_detail);

        mAuth = FirebaseAuth.getInstance();


        place = TouristPlaceDataHolder.getInstance().getPlace();




        Button favoriteButton = findViewById(R.id.addfavoriteButton);
        favoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    // 데이터베이스에 위도 경도 추가 함수...
//                Toast.makeText(TouristPlaceDetailActivity.this, "zclikclick.", Toast.LENGTH_SHORT).show();
                    addFavoriteLocation(place.getPlaceName(), place.getLatitude(), place.getLongitude());
            }
        });

        if (place != null) {
            TextView placeNameTextView = findViewById(R.id.placeNameTextView);
            TextView locationTextView = findViewById(R.id.locationTextView);
            TextView addressTextView = findViewById(R.id.addressTextView);
            TextView descriptionTextView = findViewById(R.id.descriptionTextView);
            TextView phoneTextView = findViewById(R.id.phoneTextView);
            ImageView imageView = findViewById(R.id.imageView);


            placeNameTextView.setText(place.getPlaceName());
            locationTextView.setText("Latitude: " + place.getLatitude() + ", Longitude: " + place.getLongitude());
            addressTextView.setText("Address: " + place.getAddress());
            descriptionTextView.setText("Description: " + place.getDescription());
            phoneTextView.setText("Phone: " + place.getPhone());

            String photoUrl = place.getPhotoUrl();
            if (photoUrl != null && !photoUrl.isEmpty()) {
                Log.d(TAG, "Decoding Base64 photo URL: " + photoUrl);
                Bitmap bitmap = base64ToBitmap(photoUrl);
                if (bitmap != null) {
                    imageView.setImageBitmap(bitmap);
                } else {
                    Log.d(TAG, "Decoded bitmap is null");
                }
            } else {
                Log.d(TAG, "Photo URL is null or empty");
            }
        } else {
            Log.e(TAG, "TouristPlace data is null");
        }

        mapView = findViewById(R.id.naver_map_view);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

    }

    private Bitmap base64ToBitmap(String base64String) {
        try {
            byte[] decodedString = Base64.decode(base64String, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        } catch (Exception e) {
            Log.e(TAG, "Error decoding Base64 string", e);
            return null;
        }
    }

    private void addFavoriteLocation(String place_name, double latitude, double longitude) {

        Log.d(TAG, "addFavoriteLocation called");

        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {

            // 해당 유저 계정에 해당하는 데이터베이스 받아옴.

            String userId = user.getUid();
            mDatabase = FirebaseDatabase.getInstance().getReference("users").child(userId).child("favorites");


            // 받아온 데이터 베이스에서 키 받고,  그 키를 통해  favoriteLocation(위도 경도) 등록.
            String key = mDatabase.push().getKey();
//            FavoriteLocation favoriteLocation = new FavoriteLocation(location.place_name, location.latitude, location.longitude);
            FavoriteLocation favoriteLocation = new FavoriteLocation(place_name,latitude, longitude);
            mDatabase.child(key).setValue(favoriteLocation).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(this, "즐겨찾기에 추가되었습니다.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "즐겨찾기 추가 실패", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
// 마커 설정
        Marker marker = new Marker();
        LatLng placeLocation = new LatLng(place.getLatitude(), place.getLongitude());
        marker.setPosition(placeLocation);
        marker.setMap(naverMap);
        marker.setCaptionText(place.getPlaceName());

        //

//        placeLocation = new LatLng(37.5665, 126.9780);

        // 카메라 업데이트
        CameraUpdate cameraUpdate = CameraUpdate.scrollTo(placeLocation);
        naverMap.moveCamera(cameraUpdate);
    }
}