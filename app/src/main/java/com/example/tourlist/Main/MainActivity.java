package com.example.tourlist.Main;

import android.content.res.ColorStateList;
import android.os.Bundle;

import com.example.tourlist.Main.ViewPager.Frag3_New;
import com.example.tourlist.XMLParser;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

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

    private boolean doubleBackToExitPressedOnce = false;
    private Handler mHandler = new Handler();

    private BottomNavigationView bottomNavigationView;
    private FragmentManager fm;
    private FragmentTransaction ft;
    private Frag5_Login frag5_login;
    private Frag_Profile frag_profile;

    private Frag5_Register frag5_register;
    private Frag1_NaverMap frag1_NaverMap;
    private Frag4_Gpt frag4_Gpt;
    private XMLParser.Old_Frag3_Tourist_Search frag3_TouristSearch;
    private Slide1_Course_List slide1_course_list;
    private Frag3_Place_List frag3_place_list;
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
        setWindowInsets();

        initializeFragments();
        initializeFirebaseAuth();

        setupBottomNavigationView();

        if (savedInstanceState == null) {
            setInitialFragment();
        }

        setupDrawer();
        setupButtons();
    }

    private void setWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void initializeFragments() {
        frag_profile= new Frag_Profile();
        frag5_login = new Frag5_Login();
        frag5_register = new Frag5_Register();
        frag1_NaverMap = new Frag1_NaverMap();
        frag4_Gpt = new Frag4_Gpt();
        frag3_TouristSearch = new XMLParser.Old_Frag3_Tourist_Search();
        resizableFragment = new ResizableFragment();
        slide1_course_list = new Slide1_Course_List();
        frag3_place_list = new Frag3_Place_List();
        slide2_favoriteList = new Slide2_FavoriteList();

        fm = getSupportFragmentManager();
    }

    private void initializeFirebaseAuth() {
        mAuth = FirebaseAuth.getInstance();
    }

    private void setupBottomNavigationView() {
        bottomNavigationView = findViewById(R.id.bottomNavi);
        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                isUserInteraction = true;

                int nextTabId = menuItem.getItemId();
                boolean forward = nextTabId > currentTabId;

                ImageButton openbutton = findViewById(R.id.openbutton);

                if (nextTabId == R.id.action_map) {
                    Log.d("u", "a");
                    setFrag(frag1_NaverMap, "NaverMap");
                    addNewResizableFragment(Slide1_Course_List.class);
                    openbutton.setVisibility(View.VISIBLE);
                } else if (nextTabId == R.id.action_memory) {
                    setFrag(frag1_NaverMap, "NaverMap");
                    addNewResizableFragment(Slide2_FavoriteList.class);
                    openbutton.setVisibility(View.VISIBLE);

                } else if (nextTabId == R.id.action_tourist_search) {
                    setFrag(new Frag3_New(), "TouristSearch");
                    openbutton.setVisibility(View.VISIBLE);
                    removeResizableFragment();

                } else if (nextTabId == R.id.action_gpt) {
                    setFrag(frag4_Gpt, "Gpt");
                    openbutton.setVisibility(View.VISIBLE);
                    removeResizableFragment();

                } else if (nextTabId == R.id.action_account) {
                    handleAccountTab();
                    openbutton.setVisibility(View.GONE); // or View.INVISIBLE
                    removeResizableFragment();

                }

                updateTabColors(nextTabId);
                currentTabId = nextTabId;
                return true;
            }
        });
    }


    private void handleAccountTab() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            setFrag(frag_profile, "Frag_Profile");
        } else {
            setFrag(frag5_login, "Frag5_Login");
        }
    }

    private void updateTabColors(int nextTabId) {
        MenuItem selectedItem = bottomNavigationView.getMenu().findItem(nextTabId);
        selectedItem.setIconTintList(ColorStateList.valueOf(getResources().getColor(R.color.selected_color)));

        MenuItem previousItem = bottomNavigationView.getMenu().findItem(currentTabId);
        previousItem.setIconTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));
    }

    private void setInitialFragment() {
        setFrag(frag1_NaverMap, "NaverMap");
        addNewResizableFragment(Slide1_Course_List.class);
    }

    private void setupDrawer() {
        drawerLayout = findViewById(R.id.drawer_layout);
        drawerView = findViewById(R.id.drawer);

        drawerLayout.addDrawerListener(listener);
        drawerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawer(drawerView);
            }
        });
    }



    private void setupButtons() {
        ImageButton openbutton = findViewById(R.id.openbutton);
        openbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(drawerView);
            }
        });

        LinearLayout button = findViewById(R.id.login_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                FirebaseUser currentUser = mAuth.getCurrentUser();

                if (currentUser != null && currentUser.isEmailVerified()) {
                    // 이미 로그인된 상태
                    Toast.makeText(getApplicationContext(), "이미 로그인되어 있습니다.", Toast.LENGTH_SHORT).show();
                } else {
                    // 로그인되지 않은 상태
                    setFrag(new Frag5_Login(), "Frag5_Login");
                    drawerLayout.closeDrawer(drawerView);
                }
            }
        });

        LinearLayout logoutButton = findViewById(R.id.btn_logout);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser currentUser = mAuth.getCurrentUser();
                if (currentUser != null) {
                    mAuth.signOut();




                    Toast.makeText(MainActivity.this, "로그아웃되었습니다.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "로그인된 사용자가 없습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void setFrag(Fragment fragment, String tag) {
        ft = fm.beginTransaction();
        ft.replace(R.id.main_frame, fragment, tag);
        ft.commit();
    }


    private void addNewResizableFragment(Class<? extends Fragment> fragmentClass) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        String tag = fragmentClass.getName();

        // 새로운 프래그먼트를 생성하여 교체합니다.
        ResizableFragment newResizableFragment = new ResizableFragment();
        Bundle bundle = new Bundle();
        bundle.putString("child_fragment_class", fragmentClass.getName());
        newResizableFragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.overlay_frame, newResizableFragment, tag);

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

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "한 번 더 누르면 앱이 종료됩니다.", Toast.LENGTH_SHORT).show();

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000); // 2초 동안 뒤로가기를 두 번 누르지 않으면 다시 false로 변경
    }
}
