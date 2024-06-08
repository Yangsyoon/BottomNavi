package com.example.tourlist.Tourist_Detail_Activity;

import static android.content.ContentValues.TAG;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tourlist.Main.FavoriteLocation;
import com.example.tourlist.Main.Frag5_Register;
import com.example.tourlist.R;
import com.example.tourlist.Tourist_Detail_Activity.Comment.Comment;
import com.example.tourlist.Tourist_Detail_Activity.Comment.CommentsAdapter;
import com.example.tourlist.Tourist_Detail_Activity.Detail_files.TouristPlace;
import com.example.tourlist.Tourist_Detail_Activity.Detail_files.TouristPlaceDataHolder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.MapView;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.overlay.Marker;

import java.util.ArrayList;
import java.util.List;

public class TouristPlaceDetailActivity extends AppCompatActivity implements OnMapReadyCallback {
    private FirebaseAuth mAuth;
    private MapView mapView;
    private NaverMap mMap;
    private DatabaseReference mDatabase;
    public TouristPlace place;
    private String userName = "3"; // 초기값 설정
    LatLng placeLocation;
    private RecyclerView commentsRecyclerView;
    private CommentsAdapter commentsAdapter;
    private List<Comment> commentList;
    private EditText commentEditText;
    private Button postCommentButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mAuth = FirebaseAuth.getInstance();
        place = TouristPlaceDataHolder.getInstance().getPlace();

        Button favoriteButton = findViewById(R.id.addfavoriteButton);
        favoriteButton.setOnClickListener(v -> {
            FirebaseUser user = mAuth.getCurrentUser();
            if (user == null) {
                Toast.makeText(TouristPlaceDetailActivity.this, "즐겨찾기 기능은 로그인이 필요합니다.", Toast.LENGTH_SHORT).show();
            } else {
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
        mapView.getMapAsync(this);

        // 댓글 RecyclerView 설정
        commentsRecyclerView = findViewById(R.id.commentsRecyclerView);


        commentList = new ArrayList<>();
        commentsAdapter = new CommentsAdapter(commentList, this, this);
        commentsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        commentsRecyclerView.setAdapter(commentsAdapter);

        // 댓글 작성 기능 설정
        commentEditText = findViewById(R.id.commentEditText);
        postCommentButton = findViewById(R.id.postCommentButton);
        postCommentButton.setOnClickListener(v -> postComment());

        loadComments();
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadUserNickname(); // 사용자 닉네임을 onStart에서 불러오기
        mapView.onStart();
    }

    private void loadUserNickname() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("hongdroid").child("UserAccount").child(user.getUid());
            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Frag5_Register.UserAccount account = snapshot.getValue(Frag5_Register.UserAccount.class);
                    if (account != null) {
                        userName = account.getNickname();
                        Toast.makeText(TouristPlaceDetailActivity.this, "Nickname loaded: " + userName, Toast.LENGTH_SHORT).show();
                    } else {
                        Log.d(TAG, "User data not found");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e(TAG, "Failed to load user data", error.toException());
                }
            });
        }
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

    private void loadComments() {
        mDatabase = FirebaseDatabase.getInstance().getReference("comments").child(place.getPlaceName());
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                commentList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Comment comment = dataSnapshot.getValue(Comment.class);
                    if (comment != null) {
                        commentList.add(comment);
                    }
                }
                commentsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "Failed to load comments", error.toException());
            }
        });
    }

    private void postComment() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            String content = commentEditText.getText().toString().trim();

            if (!content.isEmpty()) {
                String placeId = place.getPlaceName();
                mDatabase = FirebaseDatabase.getInstance().getReference("comments").child(placeId);
                String key = mDatabase.push().getKey();

                Comment comment = new Comment(key, userId, userName, content, System.currentTimeMillis(), placeId);
                mDatabase.child(key).setValue(comment).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        commentEditText.setText("");
                        Toast.makeText(TouristPlaceDetailActivity.this, "댓글이 작성되었습니다.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(TouristPlaceDetailActivity.this, "댓글 작성에 실패했습니다.", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(this, "댓글을 입력하세요.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "로그인이 필요합니다.", Toast.LENGTH_SHORT).show();
        }
    }

    private void addFavoriteLocation(String place_name, double latitude, double longitude) {
        Log.d(TAG, "addFavoriteLocation called");

        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            mDatabase = FirebaseDatabase.getInstance().getReference("users").child(userId).child("favorites");
            String key = mDatabase.push().getKey();
            FavoriteLocation favoriteLocation = new FavoriteLocation(place_name, latitude, longitude);
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
        mMap = naverMap;
        placeLocation = new LatLng(place.getLatitude(), place.getLongitude());

        // 마커 설정
        Marker marker = new Marker();
        marker.setPosition(placeLocation);
        marker.setMap(naverMap);
        marker.setCaptionText(place.getPlaceName());

        // 카메라 업데이트
        CameraUpdate cameraUpdate = CameraUpdate.scrollTo(placeLocation);
        naverMap.moveCamera(cameraUpdate);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}
