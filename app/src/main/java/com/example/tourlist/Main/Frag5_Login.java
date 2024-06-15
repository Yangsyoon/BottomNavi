package com.example.tourlist.Main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.tourlist.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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

    private FirebaseAuth mAuth; // 파이어베이스 인증.
    private DatabaseReference mDatabaseReference; // 실시간 데이터베이스. 서버연동.
    private EditText mEtEmail, mEtPwd; // 로그인 입력 필드

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag5_login, container, false);

        mAuth = FirebaseAuth.getInstance();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("hongdroid");

        mEtEmail = view.findViewById(R.id.et_email);
        mEtPwd = view.findViewById(R.id.et_pwd);

        Button btn_login = view.findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEtEmail.getText().toString();
                String pwd = mEtPwd.getText().toString();

                mAuth.signInWithEmailAndPassword(email, pwd).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null && user.isEmailVerified()) {
                                Toast.makeText(getContext(), "로그인 성공", Toast.LENGTH_SHORT).show();
                                navigateToProfile();
                            } else {
                                Toast.makeText(getContext(), "이메일 인증을 완료해주세요.", Toast.LENGTH_SHORT).show();
                                mAuth.signOut();
                            }
                        } else {
                            Toast.makeText(getContext(), "로그인 실패", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        Button btn_register = view.findViewById(R.id.btn_register);
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                Frag5_Register frag5_register = new Frag5_Register();
                transaction.replace(R.id.main_frame, frag5_register);
                transaction.commit();
            }
        });

        Button btn_guest_login = view.findViewById(R.id.btn_guest_login);
        btn_guest_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser user = mAuth.getCurrentUser();
                if (user == null) {
                    mAuth.signInAnonymously().addOnCompleteListener(getActivity(), task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(getContext(), "게스트 로그인 성공", Toast.LENGTH_SHORT).show();
                            navigateToProfile();
                        } else {
                            Toast.makeText(getContext(), "게스트 로그인 실패", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(getContext(), "로그아웃이 필요합니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button btn_signout = view.findViewById(R.id.btn_signout);
        btn_signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Toast.makeText(getContext(), "로그아웃", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    private void navigateToProfile() {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        Frag1_NaverMap frag1_NaverMap = new Frag1_NaverMap();
        transaction.replace(R.id.main_frame, frag1_NaverMap);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
