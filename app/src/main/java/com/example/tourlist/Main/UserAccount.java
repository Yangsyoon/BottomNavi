package com.example.tourlist.Main;

public class UserAccount {
    // 클래스의 유일한 인스턴스를 저장하기 위한 정적 변수
    private static UserAccount instance;

    // 사용자 정보 필드
    private String idToken; // Firebase Uid(고유 토큰정보)
    private String emailId; // 이메일 아이디
    private String password; // 비밀번호
    private String nickname; // 닉네임
    private String gender; // 성별

    // private 생성자로 외부에서의 인스턴스 생성을 막음
    private UserAccount() {
    }

    // 인스턴스를 얻기 위한 정적 메서드
    public static synchronized UserAccount getInstance() {
        if (instance == null) {
            instance = new UserAccount();
        }
        return instance;
    }

    // Getter 및 Setter 메서드
    public String getIdToken() {
        return idToken;
    }

    public void setIdToken(String idToken) {
        this.idToken = idToken;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
