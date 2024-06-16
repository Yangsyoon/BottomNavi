package com.example.tourlist.Main;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.tourlist.R;

public class ResizableFragment extends Fragment {

    private static final String TAG = "ResizableFragment";
    private int minHeight; // px 단위의 최소 높이
    private int maxHeight; // 화면 높이에 대한 최대 높이
    private int initialHeight; // px 단위의 초기 높이

    private Slide1_Course_List slide1_course_list;

    private Slide1_Place_List slide1_place_list;
    private Fragment currentFragment;
    private int currentFragidx=0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: called");
        View view = inflater.inflate(R.layout.resizable_fragment, container, false);

        final FrameLayout resizableView = view.findViewById(R.id.resizable_view);
        ImageButton dragButton = view.findViewById(R.id.drag_button);
//        Button change_button=view.findViewById(R.id.change_button);



        slide1_course_list =new Slide1_Course_List();
        slide1_place_list=new Slide1_Place_List();

//        // Frag2_FavoriteList를 추가합니다.
//        Frag2_FavoriteList frag2_favoriteList = new Frag2_FavoriteList();
//        getChildFragmentManager().beginTransaction()
//                .add(R.id.frameLayout_drag_button_below, frag2_favoriteList).commit();

        // 최소 높이를 200dp로 설정
        minHeight = (int) (37 * getResources().getDisplayMetrics().density);

        // 레이아웃이 그려진 후에 maxHeight를 설정
        view.post(new Runnable() {
            @Override
            public void run() {
                maxHeight = view.getHeight();
                Log.d(TAG, "Max height set to " + maxHeight);
            }
        });

        //오류 많아서 뻄.
/*        change_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeChildFragment();

            }
        });*/

        dragButton.setOnTouchListener(new OnTouchListener() {
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
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "6");

        // Arguments로 전달된 자식 Fragment를 추가합니다.
        Bundle args = getArguments();
        if (args != null) {
            String fragmentClassName = args.getString("child_fragment_class");
            Fragment childFragment = getChildFragment(fragmentClassName);
            currentFragment = getChildFragment(fragmentClassName);
            Log.d(TAG, "7 "+fragmentClassName);
            if (childFragment != null) {
                addChildFragment(childFragment,false);
            }
        }
    }

    private Fragment getChildFragment(String fragmentClassName) {
        // 전달된 클래스 이름에 따라 자식 Fragment를 생성합니다.
        try {
            if (fragmentClassName.equals(Slide2_FavoriteList.class.getName())) {
                Log.d(TAG, "1");

                return new Slide2_FavoriteList();

            } else if (fragmentClassName.equals(Slide1_Course_List.class.getName())) {
                Log.d(TAG, "2");

                return new Slide1_Course_List();

            }
             else if (fragmentClassName.equals(Slide1_Place_List.class.getName())) {
                Log.d(TAG, "2");

                return new Slide1_Place_List();
            }
            else if (fragmentClassName.equals(Frag3_Tourist_Search.class.getName())) {
                Log.d(TAG, "2");

                return new Frag3_Tourist_Search();
            } else if (fragmentClassName.equals(Frag4_Gpt.class.getName())) {
                Log.d(TAG, "3");

                return new Frag4_Gpt();
            } // 추가적인 Fragment 클래스도 여기에 추가할 수 있습니다.
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void addChildFragment(Fragment fragment,boolean addToBackStack) {
        Log.d("k", "4");

        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.frameLayout_drag_button_below, fragment);
        transaction.addToBackStack(null);

//        if (addToBackStack) {
//            transaction.addToBackStack(null);
//        }

        transaction.commit();
    }

    private void changeChildFragment() {

//        if (currentFragment != null) {
//            Fragment newFragment;
//            if (currentFragment instanceof Slide1_Place_List) {
//                newFragment = slide1_course_list;
//            } else {
//                newFragment = slide1_place_list;
//            }
//            addChildFragment(newFragment, false); // 프래그먼트 교체 시 백스택에 추가하지 않음
//            currentFragment = newFragment;

//        }



        if(currentFragidx==0){
            addChildFragment(slide1_course_list,false);
            currentFragidx=1;

        }
        else{
            addChildFragment(slide1_place_list,false);
            currentFragidx=0;


            Log.d("k","5");
        }
    }

}
