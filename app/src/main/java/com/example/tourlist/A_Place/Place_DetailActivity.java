package com.example.tourlist.A_Place;

import static android.content.ContentValues.TAG;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
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
    public PlaceViewModel viewModel;

    private ImageView heartIcon;
    private TextView likeCountTextView;
    private TextView descriptionTextView;
    private TextView moreTextView;

    // 조회 수를 표시할 TextView
    private TextView viewCountTextView;
    private ImageView bookmarkIcon;
    private ImageView mapIcon;
    private ImageView shareIcon;

    private DatabaseReference likeRef;

    public TouristPlaceRepository_call_parser repository;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_detail);

        place = PlaceDataHolder.getInstance().getPlace();

        // place 객체가 null인지 확인
        if (place == null) {
            Log.e("PlaceDetailActivity", "place 객체가 null입니다.");
            // place 객체가 null인 경우 예외 처리 또는 기본값 설정
            Toast.makeText(this, "Place 정보가 없습니다.", Toast.LENGTH_LONG).show();
            // 필요에 따라 액티비티 종료
            finish();
            return;
        }

        Log.d("PlaceDetailActivity", "ch " + place.getTitle());
        updateUI(place);

        // ViewModel 설정
        viewModel = new ViewModelProvider(this).get(PlaceViewModel.class);
        viewModel.getBlogSearchResult().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String result) {
                if (result != null) {
                    Toast.makeText(Place_DetailActivity.this, result, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(Place_DetailActivity.this, "검색 실패", Toast.LENGTH_LONG).show();
                }
            }
        });

        // Blog 검색 시작
        String query = place.getTitle(); // place.getTitle()을 사용하여 장소명을 검색어로 사용
        viewModel.searchBlog(query);

        // 뷰 찾기
        heartIcon = findViewById(R.id.heartIcon);
        descriptionTextView = findViewById(R.id.descriptionTextView);
        moreTextView = findViewById(R.id.moreTextView);
        likeCountTextView = findViewById(R.id.likeCountTextView); // 좋아요 수를 표시할 TextView
        /*viewCountTextView = findViewById(R.id.viewCountTextView);*/ // 조회 수를
        bookmarkIcon = findViewById(R.id.bookmarkIcon); // 즐겨찾기 아이콘
        mapIcon = findViewById(R.id.mapIcon); // 지도 아이콘
        /*shareIcon = findViewById(R.id.shareIcon);*/ // 공유 아이콘

        try {
            double mapX = getIntent().getDoubleExtra("mapx", 0.0);
            double mapY = getIntent().getDoubleExtra("mapy", 0.0);
            Log.d("PlaceDetailActivity", mapX + " " + mapY);
        } catch (Exception e) {
            Log.d("PlaceDetailActivity", e.toString());
        }

        // 기타 초기 설정
        mAuth = FirebaseAuth.getInstance();

        mapView = findViewById(R.id.naver_map_view2);
        mapView.getMapAsync(this);

        commentsRecyclerView = findViewById(R.id.commentsRecyclerView);

        // 댓글 리사이클러뷰 설정
        commentList = new ArrayList<>();
        commentsAdapter = new CommentsAdapter(commentList, this, this);
        commentsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        commentsRecyclerView.setAdapter(commentsAdapter);

        // 댓글 작성 버튼 클릭 리스너 설정
        commentEditText = findViewById(R.id.commentEditText);
        postCommentButton = findViewById(R.id.postCommentButton);
        postCommentButton.setOnClickListener(v -> postComment());
        Log.d("PlaceDetailActivity", "Place name:2 " + place.getTitle());

        setViewCountListener(); // 조회 수 리스너 설정
        incrementViewCount(); // 액티비티 실행 시 조회 수 증가
        setLikeCountListener(); // 좋아요 수 리스너 설정
        setLike_ClickListener();
        setDescriptionTextViewRunnable();
        setMoreTextViewClickListener();
        setBookmark_ClickListener();// 즐겨찾기 버튼 클릭 리스너 설정
        setMapIcon_ClickListener(); // 지도 아이콘 클릭 리스너 설정
        setShareIcon_ClickListener(); // 공유 아이콘 클릭 리스너 설정
        checkFavoriteStatus(); // 액티비티 실행 시 즐겨찾기 상태 확인
    }

    public void updateUI(Place place) {
        this.place = place;

        TextView placeNameTextView = findViewById(R.id.placeNameTextView2);
        TextView addressTextView = findViewById(R.id.addressTextView2);
        TextView descriptionTextView = findViewById(R.id.descriptionTextView);
        TextView phoneTextView = findViewById(R.id.phoneTextView);
        TextView homepageTextView = findViewById(R.id.homepageTextView);
        TextView zipcodeTextView = findViewById(R.id.zipcodeTextView);
        ImageView imageView = findViewById(R.id.imageView);

        if (place.getTitle() != null) {
            placeNameTextView.setText(place.getTitle());
        } else {
            placeNameTextView.setText("-");
        }

        if (place.getAddr1() != null) {
            addressTextView.setText(place.getAddr1());
        } else {
            addressTextView.setText("-");
        }

        if (place.getOverview() != null) {
            descriptionTextView.setText(place.getOverview());
        } else {
            descriptionTextView.setText("-");
        }

        if (place.getTel() != null) {
            phoneTextView.setText(place.getTel());
        } else {
            phoneTextView.setText("-");
        }

        if (place.getZipcode() != null) {
            zipcodeTextView.setText(place.getZipcode());
        } else {
            zipcodeTextView.setText("-");
        }

        // HTML 데이터에서 URL 추출
        String homepageHtml = place.getHomepage();
        String homepageUrl = extractUrlFromHtml(homepageHtml);
        homepageTextView.setText(homepageUrl);

        Log.d("PlaceDetailActivity2", "Place name: " + place.getContentid());

        String imageUrl = place.getFirstimage();
        if (imageUrl != null && !imageUrl.isEmpty()) {
            Uri uri = Uri.parse(imageUrl);
            Glide.with(this).load(uri).into(imageView);
        } else {
            Log.d("2", "Image URL is null or empty");
        }

        placeLocation = new LatLng(place.getMapx(), place.getMapy());
        loadComments();
    }

    private String extractUrlFromHtml(String html) {
        Document document = Jsoup.parse(html);
        Element link = document.select("a").first();
        return link != null ? link.attr("href") : "";
    }

    private void setShareIcon_ClickListener() {
        shareIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharePlaceURL();
            }
        });
    }

    private void setMapIcon_ClickListener() {
        mapIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 다이얼로그 표시
                showMapDialog();
            }
        });
    }

    private void showMapDialog() {
        new AlertDialog.Builder(this)
                .setTitle("네이버 지도 전환")
                .setMessage("네이버 지도 앱으로 전환하시겠습니까?")
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // 네이버 지도 앱으로 전환
                        LatLng destinationLatLng = placeLocation; // 목적지 좌표
                        String destinationName = place.getTitle(); // 목적지 이름

                        String url = generatePlaceURL(destinationLatLng, destinationName);
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        Log.d("PlaceDetailActivity", "d" + url);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("취소", null)
                .setIcon(android.R.drawable.ic_dialog_info)
                .show();
    }

    private String generatePlaceURL(LatLng latLng, String placeName) {
        String url = "nmap://place?";
        try {
            url += "lat=" + latLng.latitude;
            url += "&lng=" + latLng.longitude;
            url += "&name=" + URLEncoder.encode(placeName, "UTF-8"); // 이름에 공백 등이 있을 수 있으므로 인코딩
            url += "&appname=com.example.tourlist"; // 앱 이름 추가
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return url;
    }

    // 장소 정보를 웹 페이지 URL로 변환하는 메서드
    private String generateWebMapURL(LatLng latLng, String placeName) {
        String url = "https://map.naver.com/v5/search/";
        try {
            url += URLEncoder.encode(placeName, "UTF-8");
            url += "/place/";
            url += "?c=14128520.3803532,4508712.2741748,15,0,0,0,dh&placeId=";
            url += "&placeType=SITE";
            url += "&lat=" + latLng.latitude;
            url += "&lng=" + latLng.longitude;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return url;
    }

    private String generateTransitRouteURL(LatLng sourceLatLng, String sourceName, LatLng destinationLatLng, String destinationName) {
        String url = "nmap://route/public?";
        try {
            url += "slat=" + sourceLatLng.latitude;
            url += "&slng=" + sourceLatLng.longitude;
            url += "&sname=" + URLEncoder.encode(sourceName, "UTF-8"); // 이름에 공백 등이 있을 수 있으므로 인코딩
            url += "&dlat=" + destinationLatLng.latitude;
            url += "&dlng=" + destinationLatLng.longitude;
            url += "&dname=" + URLEncoder.encode(destinationName, "UTF-8"); // 이름에 공백 등이 있을 수 있으므로 인코딩
            url += "&appname=com.example.tourlist"; // 앱 이름 추가
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return url;
    }

    // 공유할 URL 생성 및 인텐트 생성
    private void sharePlaceURL() {
        LatLng destinationLatLng = placeLocation; // 목적지 좌표
        String destinationName = place.getTitle(); // 목적지 이름

        String url = generateWebMapURL(destinationLatLng, destinationName);

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "공유할 장소");
        shareIntent.putExtra(Intent.EXTRA_TEXT, url);

        startActivity(Intent.createChooser(shareIntent, "공유"));
    }

    private void setViewCountListener() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        String placeTitle = place.getTitle();  // 장소명을 얻기 위한 메서드
        DatabaseReference viewRef = database.getReference("Places").child(placeTitle).child("View").child("count");

        viewRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Long currentCount = dataSnapshot.getValue(Long.class);
                if (currentCount == null) {
                    currentCount = 0L;
                }
                viewCountTextView.setText(String.valueOf(currentCount));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // 실패 처리
            }
        });
    }

    private void setBookmark_ClickListener() {
        bookmarkIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    String userId = user.getUid();
                    DatabaseReference favoriteRef = FirebaseDatabase.getInstance().getReference("users")
                            .child(userId)
                            .child("favorites")
                            .child(place.getTitle());

                    favoriteRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                // 즐겨찾기에서 삭제할지 묻는 다이얼로그 표시
                                showRemoveFavoriteDialog(favoriteRef);
                            } else {
                                // 즐겨찾기에 추가할지 묻는 다이얼로그 표시
                                showAddFavoriteDialog(favoriteRef);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            // 실패 처리
                        }
                    });
                } else {
                    Toast.makeText(Place_DetailActivity.this, "로그인이 필요합니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void checkFavoriteStatus() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            DatabaseReference favoriteRef = FirebaseDatabase.getInstance().getReference("users")
                    .child(userId)
                    .child("favorites")
                    .child(place.getTitle());

            favoriteRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        // 즐겨찾기에 추가된 상태이면 채워진 별 이미지로 변경
                        bookmarkIcon.setImageResource(R.drawable.star_24_yellow);
                    } else {
                        // 즐겨찾기에 없는 상태이면 빈 별 이미지로 변경
                        bookmarkIcon.setImageResource(R.drawable.star_24);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // 실패 처리
                }
            });
        } else {
            bookmarkIcon.setImageResource(R.drawable.star_24); // 로그인되지 않은 경우 빈 별 이미지로 설정
        }
    }

    private void showAddFavoriteDialog(DatabaseReference favoriteRef) {
        new AlertDialog.Builder(this)
                .setTitle("즐겨찾기 추가")
                .setMessage("즐겨찾기에 추가하시겠습니까?")
                .setPositiveButton("추가", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // 즐겨찾기에 추가
                        FavoriteLocation location = new FavoriteLocation(place.getTitle(), place.getMapx(), place.getMapy());
                        favoriteRef.setValue(location).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(Place_DetailActivity.this, "즐겨찾기에 추가되었습니다.", Toast.LENGTH_SHORT).show();
                                    bookmarkIcon.setImageResource(R.drawable.star_24_yellow); // 채워진 별 이미지로 변경
                                } else {
                                    Toast.makeText(Place_DetailActivity.this, "즐겨찾기 추가에 실패했습니다.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                })
                .setNegativeButton("취소", null)
                .setIcon(android.R.drawable.ic_dialog_info)
                .show();
    }

    private void showRemoveFavoriteDialog(DatabaseReference favoriteRef) {
        new AlertDialog.Builder(this)
                .setTitle("즐겨찾기 삭제")
                .setMessage("즐겨찾기에서 삭제하시겠습니까?")
                .setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // 즐겨찾기에서 삭제
                        favoriteRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(Place_DetailActivity.this, "즐겨찾기에서 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                                    bookmarkIcon.setImageResource(R.drawable.star_24); // 빈 별 이미지로 변경
                                } else {
                                    Toast.makeText(Place_DetailActivity.this, "즐겨찾기 삭제에 실패했습니다.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                })
                .setNegativeButton("취소", null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void incrementViewCount() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        String placeTitle = place.getTitle();  // 장소명을 얻기 위한 메서드
        DatabaseReference viewRef = database.getReference("Places").child(placeTitle).child("View").child("count");

        viewRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Long currentCount = dataSnapshot.getValue(Long.class);
                if (currentCount == null) {
                    currentCount = 0L;
                }
                viewRef.setValue(currentCount + 1);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // 실패 처리
            }
        });
    }

    private void setLikeCountListener() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        String placeTitle = place.getTitle();  // 장소명을 얻기 위한 메서드
        likeRef = database.getReference("Places").child(placeTitle).child("Like").child("count");

        likeRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Long currentCount = dataSnapshot.getValue(Long.class);
                if (currentCount == null) {
                    currentCount = 0L;
                }
                likeCountTextView.setText(String.valueOf(currentCount));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // 실패 처리
            }
        });
    }

    private void setLike_ClickListener() {
        heartIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                String placeTitle = place.getTitle();  // 장소명을 얻기 위한 메서드
                Log.d("PlaceDetailActivity", "qqq" + placeTitle);
                likeRef = database.getReference("Places").child(placeTitle).child("Like").child("count");

                if (user != null) {
                    likeRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Long currentCount = dataSnapshot.getValue(Long.class);
                            if (currentCount == null) {
                                currentCount = 0L;
                            }
                            likeRef.setValue(currentCount + 1);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            // 실패 처리
                        }
                    });
                } else {
                    Toast.makeText(Place_DetailActivity.this, "로그인이 필요합니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setDescriptionTextViewRunnable() {
        descriptionTextView.post(new Runnable() {
            @Override
            public void run() {
                if (descriptionTextView.getLineCount() > 4) {
                    moreTextView.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void setMoreTextViewClickListener() {
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
        String placeId = place.getTitle();
        mDatabase = FirebaseDatabase.getInstance().getReference("Places").child(placeId).child("comments");
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
                Log.d("PlaceDetailActivity", "pli" + placeId);
                mDatabase = FirebaseDatabase.getInstance().getReference("Places").child(placeId).child("comments");
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
