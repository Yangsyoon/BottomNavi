package com.example.tourlist.Main.ViewPager;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.tourlist.Main.Slide1_Place_List;

public class ViewPagerAdapter extends FragmentStateAdapter {

    public ViewPagerAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        // 페이지 포지션에 따라 프래그먼트를 반환
        switch (position) {
            case 0:
                return new Slide1_Place_List(); // 장소 검색 프래그먼트
            case 1:
                return new BoardFragment(); // 게시판 프래그먼트
            default:
                return new Slide1_Place_List();
        }
    }

    @Override
    public int getItemCount() {
        return 2; // 페이지 수
    }
}
