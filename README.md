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
    ├── database/           # AppDatabase, dao, DataSource, converters
    ├── local/entity/
    ├── remote/             # dto, firebase
    ├── repo/
    └── mapper/


  ```

## 기술적 도전과 해결

### 1. 보안 및 외부 API 관리  
API 키 노출 방지를 위한 Firebase Functions 사용 및 서버 사이드 호출 구조로 변경  

- **문제:** 앱 코드에 OpenAI API 키가 들어 있으면, 디컴파일 시 키가 노출될 수 있어 보안에 취약함.
- **원인:** 프로젝트 내에서 API를 직접 호출하면서 키를 앱에 포함할 수밖에 없는 구조.
- **해결:** Firebase Functions에 레시피 생성용 함수를 두고, OpenAI 호출은 그 서버 함수에서만 수행하도록 변경. API 키는 Google Cloud Secret Manager로 관리.

### 2. 리스트 갱신 최적화  
검색 기록 리스트 깜빡임 해결을 위한 DiffUtil 고유 ID 기반 아이템 비교 로직 개선  

- **문제:** 최근 검색어 삭제·갱신 시 RecyclerView 전체가 다시 그려지며 깜빡이는 현상 발생.
- **원인:** DiffUtil이 “같은 항목인지” 판단할 때 객체 전체를 비교해, 조금만 바뀌어도 다른 항목으로 인식함.
- **해결:** areItemsTheSame을 DB Primary Key(id)만 비교하도록 수정. setHasFixedSize 적용으로 불필요한 레이아웃 계산을 줄여 갱신 시 깜빡임 완화.

### 3. 화면 전환·이벤트 중복 실행 방지  
중복 화면 전환·토스트 방지를 위한 StateFlow / SharedFlow 분리 및 일회성 이벤트 보장  

- **문제:** 레시피 수신 후 화면 전환·토스트가 재구독 시점에 다시 소비되어, 같은 이벤트가 두 번 실행됨.
- **원인:** “지금 화면 상태”와 “한 번만 실행해야 할 이벤트”를 같은 방식(Flow)으로 다루어, 이벤트가 재구독 시 다시 소비됨.
- **해결:** UI 상태는 StateFlow로, 화면 전환·토스트 등 일회성 이벤트는 SharedFlow로 분리해, 소비 후에는 다시 쓰이지 않도록 구성.

### 4. 데이터 정합성 및 쇼핑 리스트 표시  
GPT 응답 파싱·쇼핑 리스트 그룹화 정리를 통한 데이터 정합성 및 중복 표시 해결  

- **문제:** GPT 응답 형식이 들쭉날쭉해 쇼핑 리스트 파싱 오류·누락 발생. 같은 레시피 헤더·항목이 중복되거나 비정상적으로 늘어나는 현상.
- **원인:** 재료·수량 파싱이 GPT가 주는 형식과 맞지 않음. 쇼핑 리스트 그룹화 시 여러 번 순회하거나 잘못 묶어서 중복·비정상 증가 발생.
- **해결:** 재료명은 trim 등 전처리만 적용하고, 수량 부재 시 기본값 매핑으로 파싱 안정화. 레시피별로 한 번만 묶고 한 번만 순회해 리스트를 만들도록 그룹화·출력 로직 단일화.
