package com.example.tourlist.Main.ViewPager.Board;

import static android.app.Activity.RESULT_OK;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.widget.ViewPager2;

import com.example.tourlist.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class ResizableFragment2_Post extends Fragment {

    private static final String TAG = "ResizableFragment2";
    private int minHeight;
    private int maxHeight;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference dbReference;
    private ViewPager2 viewPager;

    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri imageUri;
    private ImageView imageView;

    private FirebaseStorage storage;
    private StorageReference storageReference;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: called");
        View view = inflater.inflate(R.layout.resizable_fragment2_post, container, false);

        mAuth = FirebaseAuth.getInstance();
        dbReference= FirebaseDatabase.getInstance().getReference("Board");
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference("BoardImages");

        final FrameLayout resizableView = view.findViewById(R.id.resizable_view);
        ImageButton dragButton = view.findViewById(R.id.drag_button);
        Button registerButton = view.findViewById(R.id.register_button);
//        EditText editText = view.findViewById(R.id.editText);
        imageView = view.findViewById(R.id.imageView);
        ImageButton closeButton = view.findViewById(R.id.close_button);

        // onCreateView 내에서
        EditText titleEditText = view.findViewById(R.id.titleEditText);
        EditText contentEditText = view.findViewById(R.id.editText);

        viewPager = getActivity().findViewById(R.id.viewPager); // 뷰페이저 초기화


        minHeight = (int) (200 * getResources().getDisplayMetrics().density); // 최소 높이를 200dp로 설정
        maxHeight = (int) (680 * getResources().getDisplayMetrics().density); // 최대 높이를 680dp로 설정

        dragButton.setOnTouchListener(createSimpleDragTouchListener(resizableView));
        resizableView.setOnTouchListener(createDisableSwipeTouchListener()); // 터치 리스너 설정
        setCloseButtonClickListener(closeButton);
        setImageViewClickListener(imageView);


        setRegisterButtonClickListener(registerButton, titleEditText, contentEditText, imageView);


        return view;
    }


    private void setImageViewClickListener(ImageView imageView) {
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            imageView.setImageURI(imageUri);
        }
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    private void setRegisterButtonClickListener(Button registerButton, EditText titleEditText, EditText contentEditText, ImageView imageView) {
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = titleEditText.getText().toString().trim();
                String content = contentEditText.getText().toString().trim();
                FirebaseUser user = mAuth.getCurrentUser();
Log.d("d","asdf");
                if (user != null) {
                    String userId = user.getUid();
                    long timestamp = System.currentTimeMillis();

                    if (imageUri != null) {
                        StorageReference fileReference = storageReference.child(title + "." + getFileExtension(imageUri));
                        fileReference.putFile(imageUri)
                                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                String imageUrl = uri.toString();
                                                savePostToDatabase(title, userId, timestamp, content, imageUrl);
                                            }
                                        });
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getActivity(), "이미지 업로드 실패: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                    } else {
                        savePostToDatabase(title, userId, timestamp, content, null);
                    }
                } else {
                    Toast.makeText(getActivity(), "로그인이 필요합니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getActivity().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }
    private void setCloseButtonClickListener(ImageButton closeButton) {
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.slide_out_down, R.anim.fade_out);

                transaction.remove(ResizableFragment2_Post.this);
                transaction.commit();
            }
        });
    }

    private void savePostToDatabase(String title, String userId, long timestamp, String content, @Nullable String imageUrl) {
        DatabaseReference postRef = dbReference.child(title);
        postRef.child("title").setValue(title); // 글 제목 저장
        postRef.child("userId").setValue(userId);
        postRef.child("timestamp").setValue(timestamp);
        postRef.child("content").setValue(content);
        if (imageUrl != null) {
            postRef.child("imageUrl").setValue(imageUrl);
        }

        // 등록 완료 후 토스트 메시지 띄우기
        Toast.makeText(getActivity(), "게시글이 등록되었습니다.", Toast.LENGTH_SHORT).show();

        // BoardFragment 새로고침
        Fragment parentFragment = getParentFragment();
        if (parentFragment instanceof BoardFragment) {
            ((BoardFragment) parentFragment).fetchBoardItems();
        }

        // 프래그먼트 제거
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.fade_in, R.anim.slide_out_down);
        transaction.remove(ResizableFragment2_Post.this);
        transaction.commit();
    }
    // 단순한 드래그 기능을 위한 별도 함수
    private View.OnTouchListener createSimpleDragTouchListener(final FrameLayout resizableView) {
        return new View.OnTouchListener() {
            float initialY;
            float lastY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        initialY = event.getRawY();
                        lastY = initialY;
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        float currentY = event.getRawY();
                        float deltaY = lastY - currentY;
                        int newHeight = (int) (resizableView.getHeight() + deltaY);

                        // 최소 높이와 최대 높이 사이로 제한
                        if (newHeight < minHeight) {
                            newHeight = minHeight;
                        } else if (newHeight > maxHeight) {
                            newHeight = maxHeight;
                        }

                        ViewGroup.LayoutParams layoutParams = resizableView.getLayoutParams();
                        layoutParams.height = newHeight;
                        resizableView.setLayoutParams(layoutParams);

                        lastY = currentY;
                        return true;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        return true;
                }
                return false;
            }
        };
    }

    // 프래그먼트를 터치할 때 ViewPager2 스와이프 비활성화
    private View.OnTouchListener createDisableSwipeTouchListener() {
        return new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (viewPager != null) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            viewPager.setUserInputEnabled(false); // 스와이프 비활성화
                            break;
                        case MotionEvent.ACTION_UP:
                        case MotionEvent.ACTION_CANCEL:
                            viewPager.setUserInputEnabled(true); // 스와이프 활성화
                            break;
                    }
                }
                return false;
            }
        };
    }

    // 드래그 기능을 위한 별도 함수
    private View.OnTouchListener createDragTouchListener(final FrameLayout resizableView) {
        return new View.OnTouchListener() {
            float initialY;
            float lastY;
            boolean isDragging = false;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        initialY = event.getRawY();
                        lastY = initialY;
                        isDragging = true;
                        Log.d(TAG, "ACTION_DOWN: initialY = " + initialY);
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        if (isDragging) {
                            float currentY = event.getRawY();
                            float deltaY = lastY - currentY;
                            int newHeight = (int) (resizableView.getHeight() + deltaY);

                            // 최소 높이와 최대 높이 사이로 제한
                            if (newHeight < minHeight) {
                                newHeight = minHeight;
                            } else if (newHeight > maxHeight) {
                                newHeight = maxHeight;
                            }

                            ViewGroup.LayoutParams layoutParams = resizableView.getLayoutParams();
                            layoutParams.height = newHeight;
                            resizableView.setLayoutParams(layoutParams);

                            lastY = currentY;
                            Log.d(TAG, "ACTION_MOVE: newHeight = " + newHeight);
                        }
                        return true;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        isDragging = false;
                        Log.d(TAG, "ACTION_UP or ACTION_CANCEL");
                        return true;
                }
                return false;
            }
        };
    }
}