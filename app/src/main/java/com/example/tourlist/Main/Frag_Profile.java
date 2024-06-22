package com.example.tourlist.Main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.tourlist.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Frag_Profile extends Fragment {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseReference;
    private ImageView profileImage;
    private TextView username, followersCount, followingCount;
    private GridView postsGrid;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_profile, container, false);
//
//        mAuth = FirebaseAuth.getInstance();
//        mDatabaseReference = FirebaseDatabase.getInstance().getReference("hongdroid");
//
//        profileImage = view.findViewById(R.id.profile_image);
//        username = view.findViewById(R.id.username);
//        followersCount = view.findViewById(R.id.followers_count);
//        followingCount = view.findViewById(R.id.following_count);
//        postsGrid = view.findViewById(R.id.posts_grid);
//
//        FirebaseUser user = mAuth.getCurrentUser();
//        if (user != null) {
////            loadUserProfile(user.getUid());
//        }
        return view;
    }

    private void loadUserProfile(String uid) {
        mDatabaseReference.child("Users").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String name = snapshot.child("username").getValue(String.class);
                    String followers = snapshot.child("followersCount").getValue(String.class);
                    String following = snapshot.child("followingCount").getValue(String.class);

                    username.setText(name);
                    followersCount.setText("팔로워 " + followers);
                    followingCount.setText("팔로잉 " + following);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        });
    }
}
