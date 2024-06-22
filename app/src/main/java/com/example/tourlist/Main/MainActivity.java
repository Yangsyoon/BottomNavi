package com.example.tourlist.Main;

import android.content.res.ColorStateList;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.tourlist.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private FragmentManager fm;
    private FragmentTransaction ft;
    private Frag5_Login frag5_login;
    private Frag5_Register frag5_register;
    private Frag1_NaverMap frag1_NaverMap;
    private Frag4_Gpt frag4_Gpt;
    private Frag3_Tourist_Search frag3_TouristSearch;
    private Slide1_Course_List slide1_course_list;
    private Slide1_Place_List slide1_place_list;
    private Slide2_FavoriteList slide2_favoriteList;
    private ResizableFragment resizableFragment;

    private FirebaseAuth mAuth;
    private boolean isUserInteraction = false;
    private int currentTabId = R.id.action_account; // 현재 탭을 추적

    private DrawerLayout drawerLayout;
    private View drawerView;

    private FloatingActionButton fab_main, fab_sub1, fab_sub2;
    private Animation fab_open, fab_close;
    private boolean isFabOpen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        frag5_login = new Frag5_Login();
        frag5_register = new Frag5_Register();
        frag1_NaverMap = new Frag1_NaverMap();
        frag4_Gpt = new Frag4_Gpt();
        frag3_TouristSearch = new Frag3_Tourist_Search();
        resizableFragment = new ResizableFragment();
        slide1_course_list = new Slide1_Course_List();
        slide1_place_list = new Slide1_Place_List();
        slide2_favoriteList = new Slide2_FavoriteList();

        fm = getSupportFragmentManager();

        bottomNavigationView = findViewById(R.id.bottomNavi);
        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                isUserInteraction = true;

                int nextTabId = menuItem.getItemId();
                boolean forward = nextTabId > currentTabId;

                if (nextTabId == R.id.action_map) {
                    setFrag(frag1_NaverMap, "NaverMap");
                    addNewResizableFragment(Slide1_Course_List.class);
                }else if (nextTabId == R.id.action_memory) {
                    setFrag(frag1_NaverMap, "NaverMap");
                    addNewResizableFragment(Slide2_FavoriteList.class);
                }
                else if (nextTabId == R.id.action_tourist_search) {
                    setFrag(slide1_place_list, "TouristSearch");
                }
                 else if (nextTabId == R.id.action_gpt) {
                    setFrag(frag4_Gpt, "Gpt");
                }
                else if (nextTabId == R.id.action_account) {
                    setFrag(frag5_login, "Login");
                }


                // 선택된 메뉴 아이템의 색상 변경
                MenuItem selectedItem = bottomNavigationView.getMenu().findItem(nextTabId);
                selectedItem.setIconTintList(ColorStateList.valueOf(getResources().getColor(R.color.selected_color)));

                // 이전에 선택된 메뉴 아이템의 색상 원래대로 변경
                MenuItem previousItem = bottomNavigationView.getMenu().findItem(currentTabId);
                previousItem.setIconTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));

                currentTabId = nextTabId;
                return true;
            }
        });

        //초기.
        if (savedInstanceState == null) {
            setFrag(frag1_NaverMap, "NaverMap");
            addNewResizableFragment(Slide1_Course_List.class);
//            addNewResizableFragment(Slide1_Place_List.class);
        }

        drawerLayout = findViewById(R.id.drawer_layout);
        drawerView = findViewById(R.id.drawer);

        ImageButton openbutton = findViewById(R.id.openbutton);
        openbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(drawerView);
            }
        });

        drawerLayout.addDrawerListener(listener);
        drawerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawer(drawerView);
            }
        });

        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFrag(frag5_login, "Login");
                drawerLayout.closeDrawer(drawerView);
            }
        });
    }

    private void setFrag(Fragment fragment, String tag) {
        ft = fm.beginTransaction();

        Fragment existingFragment = fm.findFragmentByTag(tag);
        if (existingFragment != null) {
            ft.show(existingFragment);
        } else {
            ft.add(R.id.main_frame, fragment, tag);
        }

        for (Fragment f : fm.getFragments()) {
            if (f != null && !f.equals(existingFragment) && f.isVisible()) {
                ft.hide(f);
            }
        }

        ft.commit();
    }

    private void addNewResizableFragment(Class<? extends Fragment> fragmentClass) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        String tag = fragmentClass.getName();

        // 기존 프래그먼트를 찾습니다.
        ResizableFragment existingFragment = (ResizableFragment) getSupportFragmentManager().findFragmentByTag(tag);

        if (existingFragment != null) {
            // 이미 추가된 프래그먼트라면 보여줍니다.
            fragmentTransaction.show(existingFragment);
        } else {
            // 새로운 프래그먼트라면 생성하고 추가합니다.
            ResizableFragment newResizableFragment = new ResizableFragment();
            Bundle bundle = new Bundle();
            bundle.putString("child_fragment_class", fragmentClass.getName());
            newResizableFragment.setArguments(bundle);
            fragmentTransaction.add(R.id.overlay_frame, newResizableFragment, tag);
        }

        // 다른 프래그먼트를 숨깁니다.
        for (Fragment f : getSupportFragmentManager().getFragments()) {
            if (f != null && f instanceof ResizableFragment && !f.equals(existingFragment) && f.isVisible()) {
                fragmentTransaction.hide(f);
            }
        }

        fragmentTransaction.commit();
    }


    DrawerLayout.DrawerListener listener = new DrawerLayout.DrawerListener() {
        @Override
        public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
            // 슬라이드 했을 때
        }

        @Override
        public void onDrawerOpened(@NonNull View drawerView) {
            // 드로어가 오픈됐을 때
        }

        @Override
        public void onDrawerClosed(@NonNull View drawerView) {
            // 드로어가 닫혔을 때
        }

        @Override
        public void onDrawerStateChanged(int newState) {
            // 드로어 상태가 바뀌었을 때
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            mAuth.signOut(); // 유저 있으면 로그아웃
        }
    }



    private void removeResizableFragment() {
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.overlay_frame);
        if (currentFragment != null) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.remove(currentFragment);
            fragmentTransaction.commit();
        }
    }

}
