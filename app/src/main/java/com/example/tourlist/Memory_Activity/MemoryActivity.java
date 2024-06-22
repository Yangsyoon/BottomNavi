package com.example.tourlist.Memory_Activity;

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

import com.bumptech.glide.Glide;
import com.example.tourlist.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class MemoryActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1; // 상수 정의

    private EditText memory_EditText;
    private ImageView memory_ImageView;
    private TextView memory_TextView;
    private Button saveButton;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    private StorageReference mStorage;

    private Uri imageUri; // 선택된 이미지의 URI를 저장하는 변수
    private Uri testUri; // 선택된 이미지의 URI를 저장하는 변수




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memory);

        memory_EditText = findViewById(R.id.memory_text);
        memory_ImageView = findViewById(R.id.memory_image);
        memory_TextView = findViewById(R.id.memory_display);
        saveButton = findViewById(R.id.save_button);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mStorage = FirebaseStorage.getInstance().getReference(); // mStorage 초기화


        Intent intent = getIntent();
        String locationKey = intent.getStringExtra("location_key");
        String locationName = intent.getStringExtra("location_name");

        // 전달받은 데이터를 사용
        TextView locationNameView = findViewById(R.id.location_name);
        locationNameView.setText(locationName);

        // Firebase에서 저장된 텍스트 불러오기
//        loadMemoryText(locationKey);

        // Firebase에서 저장된 텍스트와 이미지 불러오기
        loadMemoryText(locationName);
//        loadMemoryImage(locationKey);
        //이미지
        getFireBaseProfileImage(locationName);


        //하단 글 텍스트뷰
        memory_TextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                memory_TextView.setVisibility(View.GONE);
                memory_EditText.setVisibility(View.VISIBLE);
                memory_EditText.setText(memory_TextView.getText());
            }
        });

        //글,image 저장 버튼
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                save_Text(locationKey);
                save_Text(locationName);

                uploadImage(imageUri);
            }
        });

    }


private void save_Text(String locationName) {
    String text = memory_EditText.getText().toString();
    FirebaseUser user = mAuth.getCurrentUser();


    if (user != null && !text.isEmpty()) {
        String userId = user.getUid();
        Log.d("MemoryActivity3", "Memory ...");

        mDatabase = FirebaseDatabase.getInstance().getReference("memories");
        Log.d("MemoryActivity3", "Memory ...database");


        mDatabase.child(user.getUid()).child(locationName).child("text").setValue(text)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d("MemoryActivity", "Memory saved");
                        memory_TextView.setText(text);
                        memory_EditText.setVisibility(View.GONE);
                        memory_TextView.setVisibility(View.VISIBLE);
                        Toast.makeText(MemoryActivity.this, "Memory saved", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.d("MemoryActivity", "Memory not saved");

                        Toast.makeText(MemoryActivity.this, "Failed to save memory", Toast.LENGTH_SHORT).show();
                    }
                });

    }




}

//
private void loadMemoryText(String locationKey) {
    FirebaseUser user = mAuth.getCurrentUser();

    if (user != null) {
        String userId = user.getUid();

        DatabaseReference memoryRef = mDatabase.child("memories").child(userId).child(locationKey).child("text");




        memoryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String text = dataSnapshot.getValue(String.class);
                    memory_TextView.setText(text);


                } else {
                    String s=getIntent().getStringExtra("location_name");
                    memory_TextView.setText("Click to add a memory"+s);
//                    memory_TextView.setText("Click to add a memory");
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MemoryActivity.this, "Failed to load memory", Toast.LENGTH_SHORT).show();
            }
        });
    }
}


/*

private void loadMemoryImage(String locationKey) {
    FirebaseUser user = mAuth.getCurrentUser();

    if (user != null) {
        String userId = user.getUid();
        Log.d("MemoryActivity4", "Memory image exists");

        StorageReference imageRef=FirebaseStorage.getInstance().getReference();

        imageRef = mStorage.child("memories").child(user.getUid()).child(locationKey).child("1.jpg");
        mStorage.child("memories").child(user.getUid()).child(locationKey).child("1.jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Got the download URL for 'users/me/profile.png'
                uri=Uri.parse(uri.toString());
                memory_ImageView.setImageURI(uri);
                Log.d("MemoryActivity10", "Memory image exists");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });



//        imageRef.(new ValueEventListener() {
//
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                if (dataSnapshot.exists()) {
//                    Log.d("MemoryActivity5", "Memory image exists");
//                    String imageUrl = dataSnapshot.getValue(String.class);
//                    if (imageUrl != null && !imageUrl.isEmpty()) {
//                        Uri uri = Uri.parse(imageUrl);
//                        memory_ImageView.setImageURI(uri);
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                Toast.makeText(MemoryActivity.this, "Failed to load image", Toast.LENGTH_SHORT).show();
//            }
//        });
    }
}

*/
private void getFireBaseProfileImage(String locationKey) {
    //우선 디렉토리 파일 하나만든다.
//    File file = this.getExternalFilesDir(Environment.DIRECTORY_PICTURES + "/profile_img"); //이미지를 저장할 수 있는 디렉토리
//    //구분할 수 있게 /toolbar_images폴더에 넣어준다.
//    //이 파일안에 저 디렉토리가 있는지 확인
//    if (!file.isDirectory()) { //디렉토리가 없으면,
//        file.mkdir(); //디렉토리를 만든다.
//    }
//    downloadImg(locationKey); //이미지 다운로드해서 가져오기 메서드


//    load=(ImageView)findViewById(R.id.loadimg);
    Log.d("check1", "locationKey"+locationKey);


    FirebaseUser user = mAuth.getCurrentUser();
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageReference = storage.getReference();
    StorageReference pathReference = storageReference.child("memories").child(user.getUid()).child(locationKey);//.child("1.jpg");
    if (pathReference == null) {
        Toast.makeText(MemoryActivity.this, "저장소에 사진이 없습니다." ,Toast.LENGTH_SHORT).show();
        Log.d("check1", "저장소사진없."+locationKey);

    } else {
        Log.d("check1", "저장소 사진 있");

        StorageReference submitProfile = pathReference.child("1.jpg");
        submitProfile.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(MemoryActivity.this).load(uri).into(memory_ImageView);
                Log.d("check1", " OnSuccess"+locationKey);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("check1", " Onfail"+e);


            }
        });
    }
}

/**이미지 다운로드해서 가져오기 메서드 */
private void downloadImg(String locationKey) {
    Log.d("오냐오냐", "locationKey"+locationKey);

    FirebaseUser user = mAuth.getCurrentUser();
//    FirebaseStorage storage = FirebaseStorage.getInstance(); //스토리지 인스턴스를 만들고, //다운로드는 주소를 넣는다.
//    StorageReference storageRef = storage.getReference();//스토리지를 참조한다
//    imageRef = mStorage.child("memories").child(user.getUid()).child(locationKey).child("1.jpg");


    // Reference to an image file in Cloud Storage
//    StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("memories").child(user.getUid()).child(locationKey).child("1.jpg");

// ImageView in your Activity
//    ImageView imageView = findViewById(R.id.imageView);

// Download directly from StorageReference using Glide
// (See MyAppGlideModule for Loader registration)
//    Glide.with(this)
//            .load(storageReference)
//            .into(memory_ImageView);
    Log.d("오냐오냐", "sibal");

/*
    StorageReference listRef = FirebaseStorage.getInstance().getReference().child("memories").child(user.getUid()).child(locationKey).child("1.jpg");
    listRef.listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
        @Override
        public void onSuccess(ListResult listResult) {
            for(StorageReference file:listResult.getItems()){
                file.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
//                        memory_ImageView.setImageURI(uri);
//                        memory_ImageView.setImageURI(testUri);


//                        ImageDatas.add(uri.toString());
                        Log.e("Itemvalue3",testUri.toString());
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }


                });
            }
        }
    });*/



//    storageRef.child("memories").child(user.getUid()).child(locationKey).child("1.jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//    storageRef.child("memories").child(user.getUid()).child(locationKey).child("1.jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//        @Override  storageRef.child("profile_img/" + "profile" + num + ".jpg").
//        public void onSuccess(Uri uri) {
//            Log.d("오냐오냐", "성공:"+String.valueOf(uri));
////            Glide.with().load(uri).into(memory_ImageView);
//            memory_ImageView.setImageURI(uri);
////            dialogwithUri = uri; //첫 다이얼로그 프로필 보여주기
//        }
//    }).addOnFailureListener(new OnFailureListener() {
//        @Override
//        public void onFailure(@NonNull Exception exception) {
//            Log.d("오냐오냐", "fail");
//
//
//        }
//    });
}


public void selectImage(View view) {
            Intent intent = new Intent(Intent.ACTION_PICK);
    intent.setType("image/*");
    startActivityForResult(intent, PICK_IMAGE_REQUEST);

}

@Override
protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
        imageUri = data.getData();
        memory_ImageView.setImageURI(imageUri);

        Log.e("Itemvalue10",imageUri.toString());

////////////////////////////////test
        testUri=imageUri;

    }
}

private void uploadImage(Uri imageUri) {
    FirebaseUser user = mAuth.getCurrentUser();
    if (user != null) {
        mStorage = FirebaseStorage.getInstance().getReference();
        String locationName = getIntent().getStringExtra("location_name");
//        String locationKey = getIntent().getStringExtra("location_key");

        StorageReference fileReference = mStorage.child("memories").child(user.getUid()).child(locationName).child("1.jpg");
//        StorageReference fileReference = mStorage.child("memories").child(user.getUid()).child(locationKey).child("imageUrl").child(System.currentTimeMillis() + ".jpg");
        fileReference.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> fileReference.getDownloadUrl().addOnSuccessListener(uri -> {
                    String imageUrl = uri.toString();
                    saveMemoryWithImage(imageUrl);
                    Toast.makeText(this, "Image uploaded successfully", Toast.LENGTH_SHORT).show();
                }))
                .addOnFailureListener(e -> Toast.makeText(MemoryActivity.this, "Failed to upload image", Toast.LENGTH_SHORT).show());
    }
}

//데이타 베이스에 텍스트랑 이미지 저장.
private void saveMemoryWithImage(String imageUrl) {
    String text = memory_EditText.getText().toString();
    FirebaseUser user = mAuth.getCurrentUser();

//    if (user != null && !text.isEmpty()) {
//        String userId = user.getUid();
////        String locationKey = getIntent().getStringExtra("location_key");
//        String locationKey = getIntent().getStringExtra("location_name");
////        String key = mDatabase.child("memories").child(userId).child(locationKey).push().getKey();
//        String key = mDatabase.child("memories").child(userId).child(locationKey).push().getKey();
//
//        Memory memory = new Memory(text, imageUrl);
//
//        mDatabase.child("memories").child(userId).child(locationKey).child(key).setValue(memory)
//                .addOnCompleteListener(task -> {
//                    if (task.isSuccessful()) {
//                        Toast.makeText(MemoryActivity.this, "Memory saved", Toast.LENGTH_SHORT).show();
//                    } else {
//                        Toast.makeText(MemoryActivity.this, "Failed to save memory", Toast.LENGTH_SHORT).show();
//                    }
//                });
//    }
}


}

class Memory {
    private String text;
    private String imageUrl;

    public Memory() {
    }

    public Memory(String text, String imageUrl) {
        this.text = text;
        this.imageUrl = imageUrl;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
