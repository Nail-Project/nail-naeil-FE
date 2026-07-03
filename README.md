# 💅 NailNaeil

> 나만의 네일 스타일을 찾고, 예약하고, 공유하는 네일 서비스 앱

<br>

## 👥 팀원 소개

| 이름  | 역할             | GitHub                               |
|-----|----------------|--------------------------------------|
| 이원영 | Android-Leader | [@sjieys](https://github.com/sjieys) |
| 성가윤 | Android        | [@yunviia](https://github.com/yunviia)   |
| 배재경 | Android        | [@Jaekyung](https://github.com/Jaekyung)          |
| 유예은 | Android        | [@Yeeun102](https://github.com/Yeeun102)            |

<br>

## 🛠 기술 스택

| 구분 | 내용 |
|------|------|
| 언어 | Kotlin |
| 아키텍처 | MVVM |
| UI | XML View, Material Design 3 |
| Async | Coroutines + Flow |
| Network | Retrofit2 + OkHttp3 |
| Image | Glide |
| DI | Hilt |
| 최소 SDK | API 24 (Android 7.0) |
| 타겟 SDK | API 36 |

<br>

## 📁 프로젝트 폴더 구조

```
com.example.nailnail/
│
├── ui/                         # UI 레이어
│   ├── login/
│   │   ├── LoginActivity.kt
│   │   └── LoginViewModel.kt
│   ├── home/
│   │   ├── HomeActivity.kt
│   │   └── HomeViewModel.kt
│   └── common/                 # 공통 UI 컴포넌트
│
├── data/                       # 데이터 레이어
│   ├── repository/
│   │   └── UserRepository.kt
│   ├── remote/
│   │   ├── ApiService.kt
│   │   └── dto/
│   └── local/                  # 추후 추가
│
├── domain/                     # 비즈니스 규칙 (필요 시 추가)
│   └── model/
│
└── util/                       # 공통 유틸
    ├── Extensions.kt
    └── Constants.kt
```

<br>

## 📐 컨벤션

### 브랜치 전략

```
main         ← 배포 브랜치
develop      ← 통합 브랜치
feat/#이슈번호-기능명   ← 기능 개발
fix/#이슈번호-버그명    ← 버그 수정
```

### 커밋 메시지

```
feat: 새로운 기능 추가 (#Issue 번호)
fix: 버그 수정 (#Issue 번호)
refactor: 코드 리팩토링 (#Issue 번호)
style: 코드 포맷팅, 세미콜론 누락 등 (#Issue 번호)
chore: 빌드 설정, 패키지 매니저 수정 (#Issue 번호)
docs: 문서 수정 (#Issue 번호)
test: 테스트 코드 (#Issue 번호)
```

예시: `feat: 로그인 화면 네이버 소셜 로그인 추가 (#2)`

### 코드 컨벤션

- **파일명**: PascalCase (`LoginActivity.kt`)
- **변수/함수**: camelCase (`userName`, `fetchUserInfo()`)
- **상수**: UPPER_SNAKE_CASE (`BASE_URL`)
- **리소스 ID**: snake_case (`btn_login`, `tv_title`)

### PR 규칙

- PR 제목: `[feat] 로그인 화면 구현`
- 최소 1명 이상 코드 리뷰 후 merge
- PR 템플릿 필수 작성

<br>

## ⚙️ 빌드 및 실행 방법

### 요구사항

- Android Studio Meerkat 이상
- JDK 11 이상
- Android SDK 36

### 실행 순서

```bash
# 1. 레포지토리 클론
git clone https://github.com/Nail-Project/nail-naeil-FE/

# 2. Android Studio에서 프로젝트 열기

# 3. local.properties에 API 키 설정 (필요 시)
NAVER_CLIENT_ID=your_client_id
NAVER_CLIENT_SECRET=your_client_secret

# 4. Run 'app' (Shift + F10)
```

<br>

## 📱 화면 목록 & 플로우

```
[앱 시작]
    │
    ▼
1. 회원가입 / 로그인  ──────회원가입(최초가입) 시─────▶ 온보딩 화면
    │
    ▼
2. 메인 화면
    ├──▶ 2.1 디자인 올리고 견적 받기 ───────────▶ [3. 견적받기로 이동]
    ├──▶ 2.2 주소 설정
    └──▶ 2.3 디자인 매거진

[3. 견적받기] (2.1에서 클릭 시 이동)
    │
    ▼
  1단계 사진 업로드
    │
    ▼
  2단계 견적 정보 입력
    │
    ▼
  3단계 탐색 범위 지정
    │
    ▼
  4단계 확인
    ├── 1. 설정 확인
    ├── 2. 요청사항 더보기
    ├── 3. 요청사항 변경
    ├── 4. 일정 변경
    ├── 5. 제거 변경
    ├── 6. 시술 부위 변경
    ├── 7. 샵 범위 변경
    └── 8. 확인 메시지 ───▶ [4. 견적함]

4. 견적함
    ├──▶ 4.1 응답 상태 확인 목록 페이지
    │        ├── 4.1.1 전체 탭
    │        └── 4.1.2 진행중 탭
    │              │ 카드 클릭
    │              ▼
    └──▶ 4.2 하나의 견적에 대한 응답 상태 확인
             ├── 4.2.1 응답 안 한 네일샵 카드 (평균 응답시간 / 샵 이름 / 평점 / 응답 대기중)
             └── 4.2.2 응답한 네일샵 카드 (샵 이름 / 평점 / 내 위치로부터 거리 / 가격 / 코멘트 / 예약 가능일 / 상세보기 / 예약하기)
                      │
                      ▼ 4.2.3 상세보기 버튼 클릭
              네일샵 상세 페이지

5. 예약
    ├──▶ 5.1 확정된 예약 탭 (자세히 보기 / 예약 변경·취소 버튼)
    │        │
    │        ▼ 5.1.1 자세히 보기 페이지
    │        ├── 5.1.1.1 예약 취소 버튼 ───▶ 바텀시트로 예약 취소 관련 정보 적기
    │        └── 5.1.1.2 예약 변경 버튼 ───▶ 5.1.2 예약 변경 페이지 (예약 변경 안내 모달)
    │
    └──▶ 5.2 지난 예약 탭
             ├── 시술 완료된 견적
             └── 취소된 견적

6. 마이페이지 (미완)
```

| 화면 이름              | 스크린 ID                | 진입 경로                        | 담당자   |
|--------------------|-----------------------|------------------------------|-------|
| 로그인/회원가입 화면        | AuthScreen            | 앱 최초 진입                      | 🔲 A   |
| 홈 화면               | HomeScreen            | 로그인 완료 후                     | 🔲 A   |
| 주소 설정 화면           | AddressScreen         | 홈 화면에서 진입                    | 🔲 A   |
| 디자인 매거진 화면         | MagazineScreen        | 홈 화면에서 진입                    | 🔲 A   |
| 견적받기 - 사진 업로드      | EstimateStep1Screen   | 홈 화면(디자인 올리기)에서 진입          | 🔲 B   |
| 견적받기 - 견적 정보 입력    | EstimateStep2Screen   | 사진 업로드 다음 단계                 | 🔲 B   |
| 견적받기 - 탐색 범위 지정    | EstimateStep3Screen   | 견적 정보 입력 다음 단계              | 🔲 B   |
| 견적받기 - 확인/수정       | EstimateStep4Screen   | 탐색 범위 지정 다음 단계              | 🔲 B   |
| 견적함 - 응답 목록        | EstimateListScreen    | 홈/탭 내비게이션에서 진입              | 🔲 C   |
| 견적함 - 응답 상세        | EstimateDetailScreen  | 견적함 목록 카드 클릭 시              | 🔲 C   |
| 네일샵 상세 화면          | ShopDetailScreen      | 응답 상세의 상세보기 버튼 클릭 시        | 🔲 C   |
| 예약 - 확정된 예약 탭      | ReservationScreen     | 견적/홈 화면에서 진입                | 🔲 D   |
| 예약 - 자세히 보기        | ReservationDetailScreen | 확정된 예약 카드의 자세히 보기 클릭 시    | 🔲 D   |
| 예약 - 예약 변경         | ReservationEditScreen | 자세히 보기의 예약 변경 버튼 클릭 시      | 🔲 D   |
| 예약 - 지난 예약 탭       | PastReservationScreen | 예약 화면 탭 전환                   | 🔲 D   |
| 마이페이지 화면 (미완)      | MyPageScreen          | 홈 화면에서 진입                    | 🔲 D  |
