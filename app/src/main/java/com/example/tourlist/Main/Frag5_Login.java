package com.example.tourlist.Main;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.tourlist.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Frag5_Login extends Fragment {
    private String fragmentTag = "Login";

    public void setFragmentTag(String tag) {
        this.fragmentTag = tag;
    }

    public String getFragmentTag() {
        return fragmentTag;
    }

    private View view;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseReference;
    private EditText mEtEmail, mEtPwd;
    private Button btnLogin;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag5_login, container, false);

        mAuth = FirebaseAuth.getInstance();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("hongdroid");

        mEtEmail = view.findViewById(R.id.et_email);
        mEtPwd = view.findViewById(R.id.et_pwd);
        btnLogin = view.findViewById(R.id.btn_login);

        // 텍스트 변경 감지기 추가
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String emailInput = mEtEmail.getText().toString().trim();
                String passwordInput = mEtPwd.getText().toString().trim();

                boolean isEnabled = !emailInput.isEmpty() && !passwordInput.isEmpty();
                btnLogin.setEnabled(isEnabled);
                btnLogin.setBackgroundTintList(getResources().getColorStateList(isEnabled ? R.color.login_blue : R.color.login_gray));
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        };

        mEtEmail.addTextChangedListener(textWatcher);
        mEtPwd.addTextChangedListener(textWatcher);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 로그인 요청.
                String email = mEtEmail.getText().toString();
                String pwd = mEtPwd.getText().toString();

                Log.d("Login", "email: " + email + ", pwd: " + pwd);
                mAuth.signInWithEmailAndPassword(email, pwd).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // 로그인 성공
                            FirebaseUser user = mAuth.getCurrentUser();

                            if (user != null && user.isEmailVerified()) {
                                // 이메일 인증 완료
                                Toast.makeText(getContext(), "로그인 성공", Toast.LENGTH_SHORT).show();

                                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                FragmentTransaction transaction = fragmentManager.beginTransaction();

                                Frag1_NaverMap frag1_NaverMap = (Frag1_NaverMap) fragmentManager.findFragmentByTag("NaverMap");
                                if (frag1_NaverMap != null) {
                                    // 기존에 생성된 프래그먼트가 있으면 보여줍니다.
                                    transaction.replace(R.id.main_frame, frag1_NaverMap, "NaverMap");
                                } else {
                                    // 기존에 생성된 프래그먼트가 없으면 추가합니다.
                                    frag1_NaverMap = new Frag1_NaverMap();
                                    transaction.replace(R.id.main_frame, frag1_NaverMap, "NaverMap");
                                }

                                // 로그인 프래그먼트를 교체합니다.
                                transaction.replace(R.id.main_frame, frag1_NaverMap, "NaverMap");

                                transaction.commit();

                                // BottomNavigationView 탭 선택 상태 업데이트
                                BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.bottomNavi);
                                bottomNavigationView.setSelectedItemId(R.id.action_map);
                            } else {
                                // 이메일 인증 미완료
                                Toast.makeText(getContext(), "Please verify your email address", Toast.LENGTH_SHORT).show();
                                mAuth.signOut();
                            }
                        } else {
                            // 로그인 실패
                            Toast.makeText(getContext(), "로그인 실패", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        TextView btnRegister = view.findViewById(R.id.btn_register);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();

                // 모든 프래그먼트를 숨김
                for (Fragment fragment : fragmentManager.getFragments()) {
                    transaction.hide(fragment);
                }

                // 프래그먼트가 이미 추가되어 있는지 확인
                Frag5_Register existingFragment = (Frag5_Register) fragmentManager.findFragmentByTag(Frag5_Register.class.getSimpleName());
                if (existingFragment == null) {
                    // 프래그먼트가 없으면 추가
                    Frag5_Register frag5Register = new Frag5_Register();
                    transaction.replace(R.id.main_frame, frag5Register, Frag5_Register.class.getSimpleName());
                    transaction.addToBackStack(null);  // 백스택에 추가하여 뒤로 가기 기능을 제공
                } else {
                    // 프래그먼트가 이미 존재하면 보여줌
                    transaction.replace(R.id.main_frame, existingFragment);
                }
                transaction.commit();
            }
        });

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
