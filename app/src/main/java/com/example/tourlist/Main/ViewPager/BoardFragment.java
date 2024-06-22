package com.example.tourlist.Main.ViewPager;

import com.example.tourlist.R;



import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class BoardFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_board, container, false);

        FloatingActionButton fab = view.findViewById(R.id.fab);
        setFabClickListener(fab);

        return view;
    }

    private void setFabClickListener(FloatingActionButton fab) {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 플로팅 버튼 클릭 시 동작 구현
                // 예: 새로운 게시글 작성 화면으로 전환
            }
        });
    }
}
