package com.example.tourlist.Main.ViewPager;

import com.example.tourlist.Main.ResizableFragment2;
import com.example.tourlist.R;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class BoardFragment extends Fragment {

    private ResizableFragment2 resizableFragment2;
    private boolean isResizableFragmentVisible = false;

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
                toggleResizableFragment();
            }
        });
    }

    private void toggleResizableFragment() {
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        if (resizableFragment2 == null) {
            resizableFragment2 = new ResizableFragment2();
            transaction.setCustomAnimations(R.anim.slide_in_up, R.anim.fade_out); // 애니메이션 추가
            transaction.add(R.id.overlay_frame, resizableFragment2, "ResizableFragment2");
        } else if (isResizableFragmentVisible) {
            transaction.setCustomAnimations(R.anim.fade_in, R.anim.slide_out_down);
            transaction.hide(resizableFragment2);
        } else {
            transaction.setCustomAnimations(R.anim.slide_in_up, R.anim.fade_out);
            transaction.show(resizableFragment2);
        }

        transaction.commit();
        isResizableFragmentVisible = !isResizableFragmentVisible;
    }


}
