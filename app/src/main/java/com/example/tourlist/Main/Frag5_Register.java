package com.example.tourlist.Main;

import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
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

public class Frag5_Register extends Fragment {

    private View view;


    private FirebaseAuth mFirebaseAuth; //파이어베이스 인증.
    //이것만으로도 회원가입은 구현 가능. 근데 데이터베이스로 관리해야...

    private DatabaseReference mDatabaseReference; //실시간 데이터베이스. 서버연동.
    private EditText mEtEmail, mEtPwd, mEtnickname, mEtBirthdate;
    private RadioGroup mRgGender;
    private Button mBtnRegister; //회원가입 버튼


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag5_register2, container, false);


        mFirebaseAuth = FirebaseAuth.getInstance();//google
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("hongdroid");//실시간 데이터베이스.=Fireb
        //앱이름 보통 넣어준다. 근데 길어서 hongdroid로 넣어준다..

        mEtEmail = view.findViewById(R.id.et_email);
        mEtPwd = view.findViewById(R.id.et_pwd);
        mEtnickname = view.findViewById(R.id.et_nickname);
        mEtBirthdate = view.findViewById(R.id.et_birthdate);
        mRgGender = view.findViewById(R.id.rg_gender);
        mBtnRegister = view.findViewById(R.id.btn_register);

        mEtPwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // 입력 전 처리할 로직
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // 입력 중 처리할 로직
            }

            @Override
            public void afterTextChanged(Editable s) {
                // 입력 후 처리할 로직
                // 여기서 입력한 내용이 보이도록 할 필요는 없습니다.
                // 기본적으로 입력한 내용이 보입니다.
            }
        });

        ImageView buttonToggle = view.findViewById(R.id.iv_toggle_pwd);

        buttonToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mEtPwd.getInputType() == (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)) {
                    mEtPwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
//                    buttonToggle.setText("Hide");
                } else {
                    mEtPwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
//                    buttonToggle.setText("Show");
                }
                // 커서 위치를 끝으로 이동
                mEtPwd.setSelection(mEtPwd.getText().length());
            }
        });

        mBtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //회원가입 처리 시작
                String email = mEtEmail.getText().toString(); //사용자가 입력한 값을 가져온다. 문자열로 변환
                String pwd = mEtPwd.getText().toString();
                String nickname = mEtnickname.getText().toString();

                //Firebase Auth 진행. 회원가입.
                // Firebase Auth 진행. 회원가입.
                mFirebaseAuth.createUserWithEmailAndPassword(email, pwd).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mFirebaseAuth.getCurrentUser(); // 로그인 성공 시 현재의 유저로 가져온다.

                            // UserAccount 싱글톤 인스턴스를 가져옴
                            UserAccount account = UserAccount.getInstance();

                            account.setIdToken(user.getUid()); // 고유값 설정.
                            account.setEmailId(user.getEmail()); // 로그인 성공했으니 이메일 설정.
                            account.setPassword(pwd);
                            account.setNickname(nickname);

                            // Database에 값 삽입.
                            mDatabaseReference.child("UserAccount").child(user.getUid()).setValue(account);

                            Toast.makeText(getContext(), "회원가입에 성공하셨습니다.", Toast.LENGTH_SHORT).show();

                            // 이메일 인증 보내기
                            sendEmailVerification(user);

                            // 로그인 화면으로 전환
                            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                            FragmentTransaction transaction = fragmentManager.beginTransaction();

                            Frag5_Login frag5_login = (Frag5_Login) fragmentManager.findFragmentByTag("Login");
                            if (frag5_login != null) {
                                // 기존에 생성된 프래그먼트가 있으면 보여줍니다.
                                transaction.show(frag5_login);
                            } else {
                                // 기존에 생성된 프래그먼트가 없으면 추가합니다.
                                frag5_login = new Frag5_Login();
                                transaction.add(R.id.main_frame, frag5_login, "Login");
                            }

                            // 현재 프래그먼트를 숨깁니다.
                            Fragment currentFragment = fragmentManager.findFragmentByTag("Register");
                            if (currentFragment != null) {
                                transaction.hide(currentFragment);
                            }

                            transaction.commit();

                            // BottomNavigationView 탭 선택 상태 업데이트
//            BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.bottomNavi);
//            bottomNavigationView.setSelectedItemId(R.id.action_account);
                        } else {
                            Toast.makeText(getContext(), "회원가입에 실패하셨습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


            }
        });


        TextView btnLoginSwitch = view.findViewById(R.id.tv_login_switch);
        btnLoginSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();

                // 모든 프래그먼트를 숨김
                for (Fragment fragment : fragmentManager.getFragments()) {
                    transaction.hide(fragment);
                }

                // 프래그먼트가 이미 추가되어 있는지 확인
                Frag5_Login existingFragment = (Frag5_Login) fragmentManager.findFragmentByTag(Frag5_Login.class.getSimpleName());
                if (existingFragment == null) {
                    // 프래그먼트가 없으면 추가
                    Frag5_Login frag5Login = new Frag5_Login();
                    transaction.add(R.id.main_frame, frag5Login, Frag5_Login.class.getSimpleName());
                    transaction.addToBackStack(null);  // 백스택에 추가하여 뒤로 가기 기능을 제공
                } else {
                    // 프래그먼트가 이미 존재하면 보여줌
                    transaction.show(existingFragment);
                }
                transaction.commit();
            }
        });


        // 텍스트 변경 감지기 추가
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                checkFieldsForEmptyValues();
                String email = mEtEmail.getText().toString().trim();
                String pwd = mEtPwd.getText().toString().trim();
                String nickname = mEtnickname.getText().toString().trim();
                String birthdate = mEtBirthdate.getText().toString().trim();
                boolean genderSelected = mRgGender.getCheckedRadioButtonId() != -1;

                boolean allFieldsFilled = !email.isEmpty() && !pwd.isEmpty() && !nickname.isEmpty() && !birthdate.isEmpty();
//                 && genderSelected;

                mBtnRegister.setEnabled(allFieldsFilled);

                if (allFieldsFilled) {
                    mBtnRegister.setBackgroundTintList(ContextCompat.getColorStateList(getContext(), R.color.login_blue));
                } else {
                    mBtnRegister.setBackgroundTintList(ContextCompat.getColorStateList(getContext(), R.color.login_gray));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        };

        mEtEmail.addTextChangedListener(textWatcher);
        mEtPwd.addTextChangedListener(textWatcher);
        mEtnickname.addTextChangedListener(textWatcher);
        mEtBirthdate.addTextChangedListener(textWatcher);

        mRgGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                checkFieldsForEmptyValues();
            }
        });


        return view;
    }

    private void sendEmailVerification(FirebaseUser user) {
        if (user != null) {
            user.sendEmailVerification()
                    .addOnCompleteListener(getActivity(), task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(getContext(),
                                    user.getEmail() + "로 인증메일 전송됨",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Log.e("TAG", "sendEmailVerification", task.getException());
                            Toast.makeText(getContext(),
                                    "인증 메일 전송 실패",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
        }


    }

    private void checkFieldsForEmptyValues() {


    }
}
