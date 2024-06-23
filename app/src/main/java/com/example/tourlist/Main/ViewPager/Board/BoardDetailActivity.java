package com.example.tourlist.Main.ViewPager.Board;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tourlist.Main.Frag5_Register;
import com.example.tourlist.Main.ViewPager.Board.Comment.Comment;
import com.example.tourlist.Main.ViewPager.Board.Comment.CommentAdapter;
import com.example.tourlist.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class BoardDetailActivity extends AppCompatActivity {

    private String userName = "3"; // 초기값 설정
    private DatabaseReference dbReference;
    private TextView titleTextView, contentTextView;
    private ImageView imageView, optionsIcon;
    private RecyclerView commentRecyclerView;
    private CommentAdapter commentAdapter;
    private List<Comment> commentList;
    private FirebaseAuth mAuth;
    private EditText commentEditText;
    private Button postCommentButton;
    private DatabaseReference commentsRef;
    private ImageView heartIcon;
    private TextView likeCountTextView;

    private boolean isAuthor = true;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_detail);



        titleTextView = findViewById(R.id.titleTextView);
        contentTextView = findViewById(R.id.contentTextView);
        imageView = findViewById(R.id.imageView);
        optionsIcon = findViewById(R.id.optionsIcon);
        heartIcon = findViewById(R.id.heartIcon); // 좋아요 아이콘 추가
        likeCountTextView = findViewById(R.id.likeCountTextView); // 좋아요 수를 표시할 TextView

        commentEditText = findViewById(R.id.commentEditText);
        commentRecyclerView = findViewById(R.id.commentRecyclerView);
        postCommentButton = findViewById(R.id.postCommentButton);
        mAuth = FirebaseAuth.getInstance();

        String postTitle = getIntent().getStringExtra("postTitle");
        Log.d("BoardDetailActivity", "Post Title: " + postTitle);
        commentsRef = FirebaseDatabase.getInstance().getReference("Board").child(postTitle).child("comments");
        dbReference = FirebaseDatabase.getInstance().getReference("Board").child(postTitle);

        loadUserNickname();

        // 어댑터 설정
        commentList = new ArrayList<>();
        commentAdapter = new CommentAdapter(commentList);
        commentRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        commentRecyclerView.setAdapter(commentAdapter);

        postCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postComment();
            }
        });

        loadPostDetails();
        loadComments();
        setLikeClickListener(); // 좋아요 클릭 리스너 설정
        setOptionsIconClickListener();
    }

    private void setLikeClickListener() {
        heartIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                if (user != null) {
                    String userId = user.getUid();
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    String placeTitle = titleTextView.getText().toString(); // 게시물 제목을 가져옴

                    DatabaseReference likeRef = database.getReference("Board")
                            .child(placeTitle)
                            .child("Like");

                    DatabaseReference userLikeRef = likeRef.child("users").child(userId);

                    userLikeRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            boolean isLiked = dataSnapshot.exists();

                            if (isLiked) {
                                // 이미 좋아요를 누른 경우, 좋아요 취소
                                likeRef.child("count").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot countSnapshot) {
                                        Long currentCount = countSnapshot.getValue(Long.class);
                                        if (currentCount != null && currentCount > 0) {
                                            likeRef.child("count").setValue(currentCount - 1);
                                        }
                                        userLikeRef.removeValue();  // 사용자 좋아요 상태 제거
                                        heartIcon.setImageResource(R.drawable.heart_empty);  // 하트 아이콘을 빈 하트로 변경
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                        // 실패 처리
                                    }
                                });
                            } else {
                                // 좋아요를 누르지 않은 경우, 좋아요 추가
                                likeRef.child("count").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot countSnapshot) {
                                        Long currentCount = countSnapshot.getValue(Long.class);
                                        if (currentCount == null) {
                                            currentCount = 0L;
                                        }
                                        likeRef.child("count").setValue(currentCount + 1);
                                        userLikeRef.setValue(true);  // 사용자 좋아요 상태 추가
                                        heartIcon.setImageResource(R.drawable.heart_fill);  // 하트 아이콘을 채워진 하트로 변경
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                        // 실패 처리
                                    }
                                });
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            // 실패 처리
                        }
                    });
                } else {
                    Toast.makeText(BoardDetailActivity.this, "로그인이 필요합니다.", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(BoardDetailActivity.this, "Nickname loaded: " + userName, Toast.LENGTH_SHORT).show();
                    } else {
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }
    }

    private void postComment() {
        String commentText = commentEditText.getText().toString().trim();
        if (!commentText.isEmpty()) {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                String userId = user.getUid();
                // 사용자 닉네임 가져오기
                long timestamp = System.currentTimeMillis();
                // 날짜 형식 변환
                String formattedDate = getFormattedDate(timestamp);

                Comment comment = new Comment(userId, userName, commentText,formattedDate);

                // Push the comment to the database
                commentsRef.push().setValue(comment).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(BoardDetailActivity.this, "댓글이 등록되었습니다.", Toast.LENGTH_SHORT).show();
                            commentEditText.setText("");
                        } else {
                            Toast.makeText(BoardDetailActivity.this, "댓글 등록에 실패했습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            } else {
                Toast.makeText(this, "로그인이 필요합니다.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "댓글을 입력하세요.", Toast.LENGTH_SHORT).show();
        }
    }

    private String getFormattedDate(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date resultDate = new Date(timestamp);
        return sdf.format(resultDate);
    }

    private void loadPostDetails() {
        dbReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String title = dataSnapshot.child("title").getValue(String.class);
                    String content = dataSnapshot.child("content").getValue(String.class);
                    String imageUrl = dataSnapshot.child("imageUrl").getValue(String.class);

                    titleTextView.setText(title);
                    contentTextView.setText(content);
                    if (imageUrl != null) {
                        Glide.with(BoardDetailActivity.this).load(imageUrl).into(imageView);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle possible errors.
            }
        });
    }

    private void loadComments() {
        commentsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                commentList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Comment comment = dataSnapshot.getValue(Comment.class);
                    if (comment != null) {
                        commentList.add(comment);
                    }
                }
                commentAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Log or handle database error
            }
        });
    }

    private void setOptionsIconClickListener() {

        Log.d("a","t");
        optionsIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(v);
            }
        });
    }

    private void showPopupMenu(View v) {
        PopupMenu popupMenu = new PopupMenu(this, v);
        popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int itemId = item.getItemId();

                if (itemId == R.id.menu_edit) {
                    if (isAuthor) {
                        openPostFragmentForEditing();
                    } else {
                        Toast.makeText(BoardDetailActivity.this, "작성자만 수정할 수 있습니다.", Toast.LENGTH_SHORT).show();
                    }
                    return true;
                } else if (itemId == R.id.menu_delete) {
                    if (isAuthor) {
                        deletePost();
                    } else {
                        Toast.makeText(BoardDetailActivity.this, "작성자만 삭제할 수 있습니다.", Toast.LENGTH_SHORT).show();
                    }
                    return true;
                } else {
                    return false;
                }
            }
        });
    }

    private void openPostFragmentForEditing() {
        ResizableFragment2_Post resizableFragment2Post = new ResizableFragment2_Post();
        Bundle args = new Bundle();
        args.putString("postTitle", titleTextView.getText().toString());
        resizableFragment2Post.setArguments(args);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.overlay_frame2, resizableFragment2Post);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void deletePost() {
        dbReference.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(BoardDetailActivity.this, "게시글이 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                    finish(); // Close the activity after deletion
                } else {
                    Toast.makeText(BoardDetailActivity.this, "게시글 삭제에 실패했습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
