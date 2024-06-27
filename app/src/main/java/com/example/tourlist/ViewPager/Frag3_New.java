package com.example.tourlist.ViewPager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.tourlist.R;

public class Frag3_New extends Fragment {

    private ViewPager2 viewPager;
    private Button btnSearchPlace, btnSearchPlace2, btnBoard, btnCalendar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new, container, false);

        btnSearchPlace = view.findViewById(R.id.btnSearchPlace);
        btnSearchPlace2 = view.findViewById(R.id.btnSearchPlace2);
        btnBoard = view.findViewById(R.id.btnBoard);
        btnCalendar = view.findViewById(R.id.btnCalendar);
        viewPager = view.findViewById(R.id.viewPager);

        // ViewPager2 어댑터 설정
        viewPager.setAdapter(new ViewPagerAdapter(this));

        setButtonClickListeners();

        return view;
    }

    private void setButtonClickListeners() {
        btnSearchPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(0);
            }
        });

        btnSearchPlace2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(1);
            }
        });

        btnBoard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(2);
            }
        });

        btnCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(3);
            }
        });
    }
}
