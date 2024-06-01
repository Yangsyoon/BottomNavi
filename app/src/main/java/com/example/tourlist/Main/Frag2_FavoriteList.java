package com.example.tourlist.Main;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tourlist.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Frag2_FavoriteList extends Fragment {

    private String fragmentTag = "Favorite";

    public void setFragmentTag(String tag) {
        this.fragmentTag = tag;
    }

    public String getFragmentTag() {
        return fragmentTag;
    }

    private View view;

    private RecyclerView favoriteRecyclerView;
    private FavoriteAdapter adapter;
    private List<FavoriteLocation> favoriteLocations;

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag2_favorite_list, container, false);

        favoriteRecyclerView = view.findViewById(R.id.favorite_list);
        favoriteRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        favoriteLocations = new ArrayList<>();
        adapter = new FavoriteAdapter(getContext(), favoriteLocations);
        favoriteRecyclerView.setAdapter(adapter);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            String userId = user.getUid();
            mDatabase = FirebaseDatabase.getInstance().getReference("users").child(userId).child("favorites");
            loadFavoriteLocations();
        } else {
            Toast.makeText(getContext(), "로그인이 필요합니다.", Toast.LENGTH_SHORT).show();
        }

        adapter.setOnItemLongClickListener(this::showEditDialog);

        return view;
    }

    private void loadFavoriteLocations() {
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                favoriteLocations.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    FavoriteLocation location = snapshot.getValue(FavoriteLocation.class);
                    if (location != null) {
                        location.setKey(snapshot.getKey());
                        favoriteLocations.add(location);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "데이터를 불러오는 중 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showEditDialog(FavoriteLocation location) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("장소 이름 수정");

        final EditText input = new EditText(getContext());
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setText(location.getName());
        builder.setView(input);

        builder.setPositiveButton("저장", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name = input.getText().toString();
                location.setName(name);
                updateFavoriteLocation(location);
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void updateFavoriteLocation(FavoriteLocation location) {
        if (location.getKey() != null) {
            mDatabase.child(location.getKey()).setValue(location).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(getContext(), "장소 이름이 수정되었습니다.", Toast.LENGTH_SHORT).show();
                    loadFavoriteLocations();
                } else {
                    Toast.makeText(getContext(), "수정 실패", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
