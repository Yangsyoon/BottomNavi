package com.example.tourlist.Main.ViewPager.Board;

import com.example.tourlist.R;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class BoardFragment extends Fragment {

    private ResizableFragment2_Post resizableFragment2Post;
    private boolean isResizableFragmentVisible = false;
    private RecyclerView recyclerView;
    private BoardAdapter boardAdapter;
    private List<BoardItem> boardItemList;
    private DatabaseReference dbReference;
    private FirebaseAuth mAuth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_board, container, false);

        FloatingActionButton fab = view.findViewById(R.id.fab);
        setFabClickListener(fab);
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        boardItemList = new ArrayList<>();
        boardAdapter = new BoardAdapter(boardItemList);
        recyclerView.setAdapter(boardAdapter);

        mAuth = FirebaseAuth.getInstance();
        dbReference = FirebaseDatabase.getInstance().getReference("Board");
        Button refreshButton = view.findViewById(R.id.refresh_button);
        setRefreshButtonClickListener(refreshButton);



        fetchBoardItems(); // 게시판에 처음 들어왔을 때 데이터 가져오기


        return view;
    }

    public void fetchBoardItems() {
        dbReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boardItemList.clear();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    BoardItem boardItem = postSnapshot.getValue(BoardItem.class);
                    boardItemList.add(boardItem);
                    Log.d("BoardFragment", "Data: " + boardItem.getTitle());
                }
                boardAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle possible errors.
            }
        });
    }

    private void setRefreshButtonClickListener(Button refreshButton) {
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchBoardItems();
            }
        });
    }
    private void setFabClickListener(FloatingActionButton fab) {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPostFragment();
            }
        });
    }

    private void showPostFragment() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            toggleResizableFragment(); // 프래그먼트 보여주는 메서드 호출
        } else {
            Toast.makeText(getActivity(), "로그인이 필요합니다.", Toast.LENGTH_SHORT).show();
        }
    }

    private void toggleResizableFragment() {
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        if (resizableFragment2Post == null) {
            resizableFragment2Post = new ResizableFragment2_Post();
            transaction.setCustomAnimations(R.anim.slide_in_up, R.anim.fade_out); // 애니메이션 추가
            transaction.add(R.id.overlay_frame, resizableFragment2Post, "ResizableFragment2");
        } else if (isResizableFragmentVisible) {
            transaction.setCustomAnimations(R.anim.fade_in, R.anim.slide_out_down);
            transaction.remove(resizableFragment2Post);
            resizableFragment2Post = null; // 프래그먼트를 null로 설정하여 다시 생성하도록 함
        } else {
            resizableFragment2Post = new ResizableFragment2_Post();
            transaction.setCustomAnimations(R.anim.slide_in_up, R.anim.fade_out);
            transaction.add(R.id.overlay_frame, resizableFragment2Post, "ResizableFragment2");
        }

        transaction.commit();
        isResizableFragmentVisible = !isResizableFragmentVisible;
    }


}
