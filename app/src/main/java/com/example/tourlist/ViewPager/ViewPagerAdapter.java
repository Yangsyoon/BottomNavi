package com.example.tourlist.ViewPager;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.tourlist.Main.Frag3_Place_List;
import com.example.tourlist.ViewPager.Blank.BlankFragment;
import com.example.tourlist.ViewPager.Board.BoardFragment;
import com.example.tourlist.ViewPager.Calendar.CalendarFragment;


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
                return new BlankFragment(); // 장소 검색 프래그먼트
//                return new Frag3_Place_List(); // 장소 검색 프래그먼트
            case 1:
                return new BlankFragment(); // 빈 프래그먼트
            case 2:
                return new BoardFragment(); // 게시판 프래그먼트
            case 3:
                return new CalendarFragment(); // 캘린더 프래그먼트
            default:
                return new Frag3_Place_List();
        }
    }


    @Override
    public int getItemCount() {
        return 3; // 페이지 수
    }
}
