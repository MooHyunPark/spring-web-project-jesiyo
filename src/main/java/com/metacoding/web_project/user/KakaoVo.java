package com.metacoding.web_project.user;

public class KakaoVo {
  String url = "https://kauth.kakao.com/oauth/authorize?client_id=8892b459656e74ff4f8db62e1582534a&redirect_uri=http://localhost:8080/login/kakao&response_type=code&scopeprofile_nickname";
  String oauthurl="https://kauth.kakao.com/oauth/token?grant_type=authorization_code" +
      "client_id=8892b459656e74ff4f8db62e1582534a&redirect_uri=http://localhost:8080/login/kakao&code=";
}
