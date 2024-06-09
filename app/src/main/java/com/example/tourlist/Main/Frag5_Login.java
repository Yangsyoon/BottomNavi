package com.example.tourlist.Main;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
                btnLogin.setBackgroundTintList(getResources().getColorStateList(isEnabled ? R.color.login_blue: R.color.login_gray));
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

                mAuth.signInWithEmailAndPassword(email, pwd).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // 로그인 성공
                            FirebaseUser user = mAuth.getCurrentUser();

                            if (user != null && user.isEmailVerified()) {
                                // 이메일 인증 완료
                                Toast.makeText(getContext(), "로그인 성공", Toast.LENGTH_SHORT).show();

                                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                                Frag1_NaverMap frag1_NaverMap = new Frag1_NaverMap();
                                transaction.replace(R.id.main_frame, frag1_NaverMap);
                                transaction.addToBackStack(null);
                                transaction.commit();
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

        TextView btn_register = view.findViewById(R.id.btn_register);
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                Frag5_Register frag5_register = new Frag5_Register();
                transaction.replace(R.id.main_frame, frag5_register);
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
