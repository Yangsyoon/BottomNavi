package com.example.tourlist.Main;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.tourlist.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private FragmentManager fm;
    private FragmentTransaction ft;
    private Frag1_Login frag1_login;
    private Frag1_Register frag1_register;
    private Frag2_FavoriteList frag2_favoriteList;
    private Frag3_NaverMap frag3_NaverMap;
    private Frag4_Gpt frag4_Empty;
    private Frag5_TouristFragment frag5_TouristFragment;

    private FirebaseAuth mAuth;
    private boolean isUserInteraction = false;
    private int currentTabId = R.id.action_account; // 현재 탭을 추적

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

        // 최하단 네비게이션 바. 색상
//        Window window = getWindow();
//        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//        window.setNavigationBarColor(ContextCompat.getColor(this, R.color.darkblue));

        frag1_login = new Frag1_Login();
        frag1_register = new Frag1_Register();
        frag2_favoriteList = new Frag2_FavoriteList();
        frag3_NaverMap = new Frag3_NaverMap();
        frag4_Empty = new Frag4_Gpt();
        frag5_TouristFragment = new Frag5_TouristFragment();

        bottomNavigationView = findViewById(R.id.bottomNavi);
        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                isUserInteraction = true;

                int nextTabId = menuItem.getItemId();
                boolean forward = nextTabId > currentTabId;

                if(nextTabId == R.id.action_account) {
                    setFrag(0, forward);
                } else if(nextTabId == R.id.action_memory) {
                    setFrag(1, forward);
                } else if(nextTabId == R.id.action_map) {
                    setFrag(2, forward);
                } else if(nextTabId == R.id.action_empty) {
                    setFrag(3, forward);
                } else if(nextTabId == R.id.action_tourist) {
                    setFrag(4, forward);
                }

                currentTabId = nextTabId;
                return true;
            }
        });

        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                if (isUserInteraction) {
                    isUserInteraction = false;
                    return;
                }

                String tag = null;
                Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.main_frame);

                if (currentFragment != null) {
                    if (currentFragment instanceof Frag1_Login) {
                        tag = "Login";
                    } else if (currentFragment instanceof Frag2_FavoriteList) {
                        tag = "Favorite";
                    } else if (currentFragment instanceof Frag3_NaverMap) {
                        tag = "NaverMap";
                    } else if (currentFragment instanceof Frag4_Gpt) {
                        tag = "Empty";
                    } else if (currentFragment instanceof Frag5_TouristFragment) {
                        tag = "Tourist";
                    }

                    if (tag != null) {
                        switch (tag) {
                            case "Login":
                                bottomNavigationView.setSelectedItemId(R.id.action_account);
                                break;
                            case "Favorite":
                                bottomNavigationView.setSelectedItemId(R.id.action_memory);
                                break;
                            case "NaverMap":
                                bottomNavigationView.setSelectedItemId(R.id.action_map);
                                break;
                            case "Empty":
                                bottomNavigationView.setSelectedItemId(R.id.action_empty);
                                break;
                            case "Tourist":
                                bottomNavigationView.setSelectedItemId(R.id.action_tourist);
                                break;
                        }
                        int backStackEntryCount = getSupportFragmentManager().getBackStackEntryCount();
                        Log.d("BackStack", "Current Back Stack Entry Count: " + backStackEntryCount);
                    }
                }
            }
        });

        if (savedInstanceState == null) {
            setFrag(0, true); //첫 프래그먼트 화면을 무엇으로 지정해줄 것인지 선택
        }
    }

    private void setFrag(int n, boolean forward) {
        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();
        if (forward) {
            ft.setCustomAnimations(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
        } else {
            ft.setCustomAnimations(R.anim.slide_in_from_left, R.anim.slide_out_to_right);
        }
        switch(n) {
            case 0:
                ft.replace(R.id.main_frame, frag1_login);
                break;
            case 1:
                ft.replace(R.id.main_frame, frag2_favoriteList);
                ft.addToBackStack(null);
                break;
            case 2:
                ft.replace(R.id.main_frame, frag3_NaverMap);
                ft.addToBackStack(null);
                break;
            case 3:
                ft.replace(R.id.main_frame, frag4_Empty);
                ft.addToBackStack(null);
                break;
            case 4:
                ft.replace(R.id.main_frame, frag5_TouristFragment);
                ft.addToBackStack(null);
                break;
        }
        ft.commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null && !user.isAnonymous()) {
            mAuth.signOut(); // 익명 계정이 아닌 경우에만 로그아웃
        }
    }
}