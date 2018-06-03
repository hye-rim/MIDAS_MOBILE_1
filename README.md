# MIDAS_MOBILE_1
MIDAS Challenge 2018 우수상

`카페 솔루션 만들기 MOBILE 1조`

#### concept

- 마이다스 사원
  - midas Email을 통한 손쉬운 가입
  - 직관적 UI를 통해 팀/개인 단위로 쉽게 예약
- 관리자
  - 당일 예약만 가능하도록하여 손쉽게 주문
  - 손님 예약시 푸시알람이 오게하여 반응성 향상

#### 기능
- 관리자
  - 로그인 (관리자/사용자)
  - 회원 조회/수정/삭제/등록
  - 메뉴 조회/수정/삭제/등록
  - 예약 현황 조회
  - 예약 처리
  - 월별 수입내역 조회

- 사용자
  - 회원가입
  - 로그인
  - 메뉴 조회
  - 예약하기 (음료 종류/사이즈 선택)
  - 장바구니 기능
  - 월별 구매내역 조회
  - 회원 정보 상세 조회

#### 설계

<img align="center" width="80%" height="80%" style="margin:0px 0px 0px 0px" src="/design.png">

- api 목록

<img align="center" width="80%" height="80%" style="margin:0px 0px 0px 0px" src="/apiList.png">

#### 팀 구성

  - [이하늘](https://gitlab.com/softsquared-hanul)
    - UI/UX
    - Server & DB
    - Android 20%
  - [박혜림](https://github.com/hye-rim)
    - 관리자 기능 Android 40%
  - [김헌진](https://github.com/KimHunJin)
    - 사용자 기능 Android 40%

<br/>

#### OpenSource

- [butterknife](http://jakewharton.github.io/butterknife/) 8.8.1
- [Glide](https://github.com/bumptech/glide) 4.7.1
- [retrofit2](http://square.github.io/retrofit/) 2.3.0
- [okhttp3](http://square.github.io/okhttp/) 3.8.1
- [rxjava2](https://github.com/ReactiveX/RxJava) 2.1.2
- [firebase fcm](https://firebase.google.com/?hl=ko) 11.8.0
- [MPAndroidChart](https://github.com/PhilJay/MPAndroidChart) 3.0.3
- [navigationtabbar](https://github.com/Devlight/NavigationTabBar) 1.2.5
   - bottom navigation tab bar Library
