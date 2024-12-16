

function kakakoLogin() {
    const kauthUrl = 'https://kauth.kakao.com/oauth/authorize?client_id=8892b459656e74ff4f8db62e1582534a&redirect_uri=http://localhost:8080/login/kakao&response_type=code&scope=profile_nickname'
    location.href = kauthUrl;
}
