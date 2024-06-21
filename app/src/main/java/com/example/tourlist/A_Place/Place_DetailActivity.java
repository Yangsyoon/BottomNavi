package com.example.tourlist.A_Place;

import static android.content.ContentValues.TAG;

import android.net.Uri;
import android.os.Bundle;
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
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tourlist.Main.FavoriteLocation;
import com.example.tourlist.Main.Frag5_Register;
import com.example.tourlist.R;
import com.example.tourlist.Tourist_Detail_Activity.Comment.Comment;
import com.example.tourlist.Tourist_Detail_Activity.Comment.CommentsAdapter;
import com.example.tourlist.Tourist_Detail_Activity.Detail_files.TouristPlaceDataHolder;
import com.example.tourlist.Tourist_Detail_Activity.TouristPlaceDetailActivity;
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

public class Place_DetailActivity extends AppCompatActivity implements OnMapReadyCallback {
    public FirebaseAuth mAuth;
    public MapView mapView;
    public NaverMap mMap;
    public DatabaseReference mDatabase;
    public Place place;
    public String userName = "3"; // 초기값 설정
    public LatLng placeLocation;
    public RecyclerView commentsRecyclerView;
    public CommentsAdapter commentsAdapter;
    public List<Comment> commentList;
    public EditText commentEditText;
    public Button postCommentButton;
    public Place_ViewModel viewModel;

    public TouristPlaceRepository_call_parser repository;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_detail);

        place=new Place();


        try {
            double mapX = getIntent().getDoubleExtra("mapx", 0.0);
            double mapY = getIntent().getDoubleExtra("mapy", 0.0);
            place.setMapx(mapX);
            place.setMapy(mapY);
            Log.d("PlaceDetailActivity",mapX+" "+mapY) ;


        }catch (Exception e){
            Log.d("PlaceDetailActivity", e.toString());
        }
        // ViewModel 설정
        String contentId = getIntent().getStringExtra("CONTENT_ID");
        String contenttypeid = getIntent().getStringExtra("CONTENT_TYPE_ID");
        viewModel = new ViewModelProvider(this).get(Place_ViewModel.class);
        Log.d("PlaceDetailActivity",contentId);
        Log.d("urlf","detail"+contenttypeid);

        // 뷰 찾기
        Button favoriteButton = findViewById(R.id.addfavoriteButton);

        TextView descriptionTextView = findViewById(R.id.descriptionTextView);
        TextView moreTextView = findViewById(R.id.moreTextView);
        descriptionTextView.post(new Runnable() {
            @Override
            public void run() {
                if (descriptionTextView.getLineCount() > 4) {
                    moreTextView.setVisibility(View.VISIBLE);
                }
            }
        });

        moreTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (moreTextView.getText().equals("더보기")) {
                    descriptionTextView.setMaxLines(Integer.MAX_VALUE);
                    descriptionTextView.setEllipsize(null);
                    moreTextView.setText("간략히");
                } else {
                    descriptionTextView.setMaxLines(4);
                    descriptionTextView.setEllipsize(android.text.TextUtils.TruncateAt.END);
                    moreTextView.setText("더보기");
                }
            }
        });



//        // 기타 초기 설정
        mAuth = FirebaseAuth.getInstance();
//        mapView.onCreate(savedInstanceState); // MapView 생명주기 메서드 호출

        // ViewModel에서 데이터 가져오기
        if (contentId != null) {
            viewModel.getPlacedetail(contentId,contenttypeid).observe(this, new Observer<Place>() {
                @Override
                public void onChanged(Place place) {
                    if (place != null) {

                        updateUI(place);
//                        placeNameTextView.setText(place.getTitle());
                        Log.d("PlaceDetailActivity3", "Place name: " + place.getAddr1());

                    }
                }
            });
        }

        // 즐겨찾기 버튼 클릭 리스너 설정
        favoriteButton.setOnClickListener(v -> {
            FirebaseUser user = mAuth.getCurrentUser();
            if (user == null) {
                Toast.makeText(Place_DetailActivity.this, "즐겨찾기 기능은 로그인이 필요합니다.", Toast.LENGTH_SHORT).show();
            } else {
                addFavoriteLocation(place.getTitle(), place.getMapx(), place.getMapy());
            }
        });

        mapView = findViewById(R.id.naver_map_view2);
        mapView.getMapAsync(this);

        commentsRecyclerView = findViewById(R.id.commentsRecyclerView);

        // 댓글 리사이클러뷰 설정
        commentList = new ArrayList<>();
        commentsAdapter = new CommentsAdapter(commentList, this, this);
        commentsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        commentsRecyclerView.setAdapter(commentsAdapter);


        commentEditText = findViewById(R.id.commentEditText);
        postCommentButton = findViewById(R.id.postCommentButton);
        postCommentButton.setOnClickListener(v -> postComment());

//
        // 댓글 작성 버튼 클릭 리스너 설정


    }

    public void updateUI(Place place) {
        this.place = place;

        TextView placeNameTextView = findViewById(R.id.placeNameTextView2);
//        TextView locationTextView = findViewById(R.id.locationTextView);
        TextView addressTextView = findViewById(R.id.addressTextView2);
        TextView descriptionTextView = findViewById(R.id.descriptionTextView);
        TextView phoneTextView = findViewById(R.id.phoneTextView);
        ImageView imageView=findViewById(R.id.imageView);
//
        placeNameTextView.setText(place.getTitle());
//        locationTextView.setText("Latitude: " + place.getMapx() + ", Longitude: " + place.getMapy());
        addressTextView.setText(place.getAddr1());
        descriptionTextView.setText(place.getOverview());
        phoneTextView.setText(place.getTel());
        Log.d("PlaceDetailActivity2", "Place name: " + place.getContentid());


        String imageUrl = place.getFirstimage();
        if (imageUrl != null && !imageUrl.isEmpty()) {
            Uri uri = Uri.parse(imageUrl);
            Glide.with(this).load(uri).into(imageView);
        } else {
            Log.d("2", "Image URL is null or empty");
        }




//
        placeLocation = new LatLng(place.getMapx(), place.getMapy());
        loadComments();
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
                        Toast.makeText(Place_DetailActivity.this, "Nickname loaded: " + userName, Toast.LENGTH_SHORT).show();
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

    private void loadComments() {
        mDatabase = FirebaseDatabase.getInstance().getReference("comments").child(place.getTitle());
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
//                Log.e(TAG, "Failed to load comments", error.toException());
            }
        });
    }

    private void postComment() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            String content = commentEditText.getText().toString().trim();

            if (!content.isEmpty()) {
                String placeId = place.getTitle();
                mDatabase = FirebaseDatabase.getInstance().getReference("comments").child(placeId);
                String key = mDatabase.push().getKey();

                Comment comment = new Comment(key, userId, userName, content, System.currentTimeMillis(), placeId);
                mDatabase.child(key).setValue(comment).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        commentEditText.setText("");
                        Toast.makeText(Place_DetailActivity.this, "댓글이 작성되었습니다.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(Place_DetailActivity.this, "댓글 작성에 실패했습니다.", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(this, "댓글을 입력하세요.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "로그인이 필요합니다.", Toast.LENGTH_SHORT).show();
        }
    }

    private void addFavoriteLocation(String placeName, double latitude, double longitude) {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            mDatabase = FirebaseDatabase.getInstance().getReference("users").child(userId).child("favorites");
            String key = mDatabase.push().getKey();
            FavoriteLocation favoriteLocation = new FavoriteLocation(placeName, latitude, longitude);
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
        if (place == null) {
            Log.d("PlaceDetailActivity", "Place is null");
            return;
        }

        placeLocation = new LatLng(place.getMapy(), place.getMapx());

        Log.d("PlaceDetailActivity1", "Place location: " + placeLocation);

        ////////////////////////////////////////
//       ` // 임의의 위도와 경도를 설정
//        double newLatitude = 35.87434;
//        double newLongitude = 128.6394; // 경도를 임의로 설정
//
//        // 새로운 LatLng 객체 생성
//        placeLocation = new LatLng(newLatitude, newLongitude);

        // 마커 설정
        Marker marker = new Marker();
        marker.setPosition(placeLocation);
        marker.setMap(naverMap);
        marker.setCaptionText(place.getTitle());

        // 카메라 업데이트
        CameraUpdate cameraUpdate = CameraUpdate.scrollTo(placeLocation);
        naverMap.moveCamera(cameraUpdate);
    }
    @Override
    protected void onStart() {
        super.onStart();
        loadUserNickname(); // 사용자 닉네임을 onStart에서 불러오기
        mapView.onStart();
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
