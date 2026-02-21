# 🍳 GptRecipeApp
검색어나 재료를 입력하면 OpenAI(GPT)로 레시피와 재료 목록을 생성하고, 즐겨찾기·최근 검색·쇼핑 리스트를 로컬에 저장하는 Android 레시피 앱입니다. 
Kotlin + Clean Architecture + MVVM 기반으로, Firebase Functions를 통해 OpenAI API를 호출해 API 키를 앱에 노출하지 않고, Room으로 오프라인 데이터를 관리합니다.

## 주요 기능

- **키워드/재료 검색** — 음식 이름으로 검색하거나, 카테고리별 재료를 선택해 GPT에게 재료 목록·레시피 요청
- **레시피 생성** — 선택한 재료로 요리 가능한 메뉴와 레시피, 웰빙 팁을 생성
- **즐겨찾기** — 마음에 드는 레시피를 저장해 목록에서 바로 조회
- **최근 검색** — 검색어 기록을 저장·삭제하고, 탭하여 다시 검색
- **쇼핑 리스트** — 레시피 기준으로 쇼핑 목록 자동 생성, 카테고리별 그룹·체크·삭제 지원

## 기술 스택

- **Language:** Kotlin
- **UI:** DataBinding
- **Architecture:** Clean Architecture + MVVM
- **Async:** Kotlin Coroutines + Flow
- **DI:** Hilt (KSP)
- **Jetpack:** ViewModel, Navigation Component, Lifecycle
- **Local DB:** Room (Entity, DAO, TypeConverter)
- **Backend:** Firebase Functions → OpenAI API (gpt-3.5-turbo), API 키는 Secret Manager로 관리
- **Image:** Lottie, Flexbox
- **Build:** Gradle Kotlin DSL, Version Catalog (libs.versions.toml)
  
## 프로젝트 구조
 ```
GptRecipeApp/
├── app/                    # 진입점
│   └── Application.kt
│
├── presentation/           # UI 레이어
│   ├── MainActivity.kt
│   ├── ui/                  # splash, search, recipe, favorite, shoppinglist 등
│   ├── model/
│   └── mapper/
│
├── domain/                 # 비즈니스 로직
│   ├── model/
│   ├── repo/
│   └── usecase/
│
├── data/                   # 데이터 레이어
│   ├── database/           # AppDatabase, dao, DataSource, converters
│   ├── local/entity/
│   ├── remote/             # dto, firebase
│   ├── repo/
│   └── mapper/
│
└── functions/              # Firebase Cloud Functions
    └── src/index.ts        # OpenAI 연동
  ```
