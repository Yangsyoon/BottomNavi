package com.example.tourlist.Main;

import android.content.Context;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private BottomNavigationView bottomNavigationView;
    private FragmentManager fm;
    private FragmentTransaction ft;
    private Frag5_Login frag5_login;
    private Frag5_Register frag5_register;
    private Frag2_FavoriteList frag2_favoriteList;
    private Frag1_NaverMap frag1_NaverMap;
    private Frag4_Gpt frag4_Gpt;
    private Frag3_Tourist_Search frag3_TouristSearch;
    private ResizableFragment resizableFragment;
    private FirebaseAuth mAuth;
    private boolean isUserInteraction = false;
    private int currentTabId = R.id.action_account; // 현재 탭을 추적

    private DrawerLayout drawerLayout;
    private View drawerView;

    private Context mContext;
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

        mContext = getApplicationContext();
        fab_open = AnimationUtils.loadAnimation(mContext, R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(mContext, R.anim.fab_close);

        fab_main = (FloatingActionButton)findViewById(R.id.fab_main);
        fab_sub1 = (FloatingActionButton)findViewById(R.id.fab_sub1);
        fab_sub2 = (FloatingActionButton)findViewById(R.id.fab_sub2);

        fab_main.setOnClickListener(this);
        fab_sub1.setOnClickListener(this);
        fab_sub2.setOnClickListener(this);

        frag5_login = new Frag5_Login();
        frag5_register = new Frag5_Register();
        frag2_favoriteList = new Frag2_FavoriteList();
        frag1_NaverMap = new Frag1_NaverMap();
        frag4_Gpt = new Frag4_Gpt();
        frag3_TouristSearch = new Frag3_Tourist_Search();
        resizableFragment = new ResizableFragment();

        bottomNavigationView = findViewById(R.id.bottomNavi);
        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                isUserInteraction = true;

                int nextTabId = menuItem.getItemId();
                boolean forward = nextTabId > currentTabId;

                if (nextTabId == R.id.action_account) {
                    setFrag(4, forward);
                } else if (nextTabId == R.id.action_memory) {
                    setFrag(1, forward);
                } else if (nextTabId == R.id.action_map) {
                    setFrag(0, forward);
                } else if (nextTabId == R.id.action_gpt) {
                    setFrag(3, forward);
                } else if (nextTabId == R.id.action_tourist_search) {
                    setFrag(2, forward);
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
                    if (currentFragment instanceof Frag5_Login) {
                        tag = "Login";
                    } else if (currentFragment instanceof Frag2_FavoriteList) {
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
                            case "Tourist_Search":
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
            setFrag(0, true); // 첫 프래그먼트 화면을 무엇으로 지정해줄 것인지 선택
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

        drawerLayout.setDrawerListener(listener);
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

    private void setFrag(int n, boolean forward) {
        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();
        if (forward) {
            // ft.setCustomAnimations(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
        } else {
            // ft.setCustomAnimations(R.anim.slide_in_from_left, R.anim.slide_out_to_right);
        }
        switch (n) {
            case 4:
                ft.replace(R.id.main_frame, frag5_login);
                removeResizableFragment(); // ResizableFragment 제거
                break;
            case 1:
                ft.replace(R.id.main_frame, frag2_favoriteList);
                ft.addToBackStack(null);
                removeResizableFragment(); // ResizableFragment 제거
                break;
            case 0:
                ft.replace(R.id.main_frame, frag1_NaverMap);
                ft.addToBackStack(null);
                addResizableFragment(); // ResizableFragment 추가
                break;
            case 3:
                ft.replace(R.id.main_frame, frag4_Gpt);
                ft.addToBackStack(null);
                removeResizableFragment(); // ResizableFragment 제거
                break;
            case 2:
                ft.replace(R.id.main_frame, frag3_TouristSearch);
                ft.addToBackStack(null);
                removeResizableFragment(); // ResizableFragment 제거
                break;
        }
        ft.commit();
    }

    private void addResizableFragment() {
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.overlay_frame);
        if (currentFragment == null) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.overlay_frame, resizableFragment);
            fragmentTransaction.commit();
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
    protected void onDestroy() {
        super.onDestroy();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            mAuth.signOut(); // 유저 있으면 로그아웃
        }
    }

    @Override
    public void onClick(View v) {
        /*switch (v.getId()) {
            case R.id.fab_main:
                toggleFab();
                break;
            case R.id.fab_sub1:
                toggleFab();
                Toast.makeText(this, "Camera Open-!", Toast.LENGTH_SHORT).show();
                break;
            case R.id.fab_sub2:
                toggleFab();
                Toast.makeText(this, "Map Open-!", Toast.LENGTH_SHORT).show();
                break;
        }*/
        int id = v.getId();
        if(id == R.id.fab_main)
        {
            toggleFab();
        }
        else if(id == R.id.fab_sub1)
        {
            toggleFab();
            Toast.makeText(this, "Camera Open-!", Toast.LENGTH_SHORT).show();
        }
        else if(id == R.id.fab_sub2)
        {
            toggleFab();
            Toast.makeText(this, "Map Open-!", Toast.LENGTH_SHORT).show();
        }
    }

    private void toggleFab() {
        if (isFabOpen) {
            fab_main.setImageResource(R.drawable.ic_add);
            fab_sub1.startAnimation(fab_close);
            fab_sub2.startAnimation(fab_close);
            fab_sub1.setClickable(false);
            fab_sub2.setClickable(false);
            isFabOpen = false;
        } else {
            fab_main.setImageResource(R.drawable.ic_close);
            fab_sub1.startAnimation(fab_open);
            fab_sub2.startAnimation(fab_open);
            fab_sub1.setClickable(true);
            fab_sub2.setClickable(true);
            isFabOpen = true;
        }
    }
}
