package com.example.tourlist.Main;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.tourlist.Course.Frag_Course_List;
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
    private Slide2_FavoriteList slide2_favoriteList;
    private Frag1_NaverMap frag1_NaverMap;
    private Frag4_Gpt frag4_Gpt;
    private Frag3_Tourist_Search frag3_TouristSearch;
    private Frag_Course_List frag_course_list;
    private ResizableFragment resizableFragment;
    private Slide1 slide1;
    private Frag_Profile fragProfile;
    private FirebaseAuth mAuth;
    private boolean isUserInteraction = false;
    private int currentTabId = R.id.action_account;

    private DrawerLayout drawerLayout;
    private View drawerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            v.setPadding(insets.getInsets(WindowInsetsCompat.Type.systemBars()).left,
                    insets.getInsets(WindowInsetsCompat.Type.systemBars()).top,
                    insets.getInsets(WindowInsetsCompat.Type.systemBars()).right,
                    insets.getInsets(WindowInsetsCompat.Type.systemBars()).bottom);
            return insets;
        });

        mAuth = FirebaseAuth.getInstance();

        frag5_login = new Frag5_Login();
        frag5_register = new Frag5_Register();
        slide2_favoriteList = new Slide2_FavoriteList();
        frag1_NaverMap = new Frag1_NaverMap();
        frag4_Gpt = new Frag4_Gpt();
        frag3_TouristSearch = new Frag3_Tourist_Search();
        resizableFragment = new ResizableFragment();
        slide1 = new Slide1();
        frag_course_list = new Frag_Course_List();
        fragProfile = new Frag_Profile();

        bottomNavigationView = findViewById(R.id.bottomNavi);
        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                isUserInteraction = true;

                int nextTabId = menuItem.getItemId();
                boolean forward = nextTabId > currentTabId;

                if (nextTabId == R.id.action_account) {
                    FirebaseUser currentUser = mAuth.getCurrentUser();
                    if (currentUser != null) {
                        setFrag(5, forward);
                    } else {
                        setFrag(4, forward);
                    }
                } else if (nextTabId == R.id.action_memory) {
                    setFrag(1, forward);
                } else if (nextTabId == R.id.action_map) {
                    setFrag(0, forward);
                } else if (nextTabId == R.id.action_gpt) {
                    setFrag(3, forward);
                } else if (nextTabId == R.id.action_tourist_search) {
                    setFrag(2, forward);
                }

                MenuItem selectedItem = bottomNavigationView.getMenu().findItem(nextTabId);
                selectedItem.setIconTintList(ColorStateList.valueOf(getResources().getColor(R.color.selected_color)));

                MenuItem previousItem = bottomNavigationView.getMenu().findItem(currentTabId);
                previousItem.setIconTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));

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
                    if (currentFragment instanceof Frag5_Login) {
                        tag = "Login";
                    } else if (currentFragment instanceof Slide2_FavoriteList) {
                        tag = "Favorite";
                    } else if (currentFragment instanceof Frag1_NaverMap) {
                        tag = "NaverMap";
                    } else if (currentFragment instanceof Frag4_Gpt) {
                        tag = "Gpt";
                    } else if (currentFragment instanceof Frag3_Tourist_Search) {
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
                            case "Gpt":
                                bottomNavigationView.setSelectedItemId(R.id.action_gpt);
                                break;
                            case "Tourist":
                                bottomNavigationView.setSelectedItemId(R.id.action_tourist_search);
                                break;
                        }
                        int backStackEntryCount = getSupportFragmentManager().getBackStackEntryCount();
                        Log.d("BackStack", "Current Back Stack Entry Count: " + backStackEntryCount);
                    }
                }
            }
        });

        if (savedInstanceState == null) {
            setFrag(4, true); // 앱 시작 시 무조건 로그인 프래그먼트를 표시
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
                Frag5_Login loginFragment = new Frag5_Login();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.main_frame, loginFragment);
                transaction.commit();

                drawerLayout.closeDrawer(drawerView);
            }
        });
    }

    DrawerLayout.DrawerListener listener = new DrawerLayout.DrawerListener() {
        @Override
        public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
        }

        @Override
        public void onDrawerOpened(@NonNull View drawerView) {
        }

        @Override
        public void onDrawerClosed(@NonNull View drawerView) {
        }

        @Override
        public void onDrawerStateChanged(int newState) {
        }
    };

    private void setFrag(int n, boolean forward) {
        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();
        if (forward) {
            // ft.setCustomAnimations(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
        } else {
            // ft.setCustomAnimations(R.anim.slide_in_from_left, R.anim.slide_out_to_right);
        }
        switch (n) {
            case 0:
                ft.replace(R.id.main_frame, frag1_NaverMap);
                ft.addToBackStack(null);
                addNewResizableFragment(Frag3_Tourist_Search.class); // ResizableFragment 추가
                break;
            case 1:
                ft.replace(R.id.main_frame, frag1_NaverMap);
                ft.addToBackStack(null);
                addNewResizableFragment(Slide2_FavoriteList.class);
                break;
            case 2:
                ft.replace(R.id.main_frame, frag3_TouristSearch);
                ft.addToBackStack(null);
                removeResizableFragment(); // ResizableFragment 제거
                break;
            case 3:
                ft.replace(R.id.main_frame, frag4_Gpt);
                ft.addToBackStack(null);
                removeResizableFragment(); // ResizableFragment 제거
                break;
            case 4:
                ft.replace(R.id.main_frame, frag5_login);
                removeResizableFragment(); // ResizableFragment 제거
                break;
            case 5:
                ft.replace(R.id.main_frame, fragProfile);
                removeResizableFragment(); // ResizableFragment 제거
                break;
        }
        ft.commit();
    }

    private void addNewResizableFragment(Class<? extends Fragment> fragmentClass) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        ResizableFragment newResizableFragment = new ResizableFragment();
        Bundle bundle = new Bundle();
        bundle.putString("child_fragment_class", fragmentClass.getName());
        newResizableFragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.overlay_frame, newResizableFragment);
        fragmentTransaction.commit();
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
    protected void onDestroy() {
        super.onDestroy();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            mAuth.signOut(); // 유저 있으면 로그아웃
        }
    }
}
