package com.example.coachmarktutorial.ui.coachmark


// 가장 먼저 코치마크를 달아줄 시나리오를 정의해준다
enum class CoachMarkTarget {
    REFRESH,
    FAB,

    POST_BUTTON,  // FAB 메뉴 중 글 쓰기 버튼
    POST_CATEGORY,
    POST_CATEGORY_OPTION,
    POST_TITLE,
    POST_CONTENT,
    POST_SUBMIT,

    // [Search Scenario] - 나중에 사용
    SEARCH_INPUT
}

// CoachMarkTarget에 제목과 내용을 매핑
// 확장 프로퍼티
// 클래스 밖에서 마치 그 클래스의 멤버 변수인 것처럼 새로운 속성을 추가
val CoachMarkTarget.texts: Pair<String, String>
    get() = when (this) {
        CoachMarkTarget.REFRESH -> "새로고침" to "데이터를 최신으로 업데이트하려면\n이 버튼을 누르세요."
        CoachMarkTarget.FAB -> "글쓰기" to "새로운 게시글을 작성하고 싶다면\n이 버튼을 눌러보세요."
        CoachMarkTarget.POST_BUTTON -> "글쓰기" to "새로운 글을 작성하려면\n이 버튼을 선택하세요."
        CoachMarkTarget.POST_CATEGORY -> "카테고리" to "여러 카테고리 중 질문을 해봅시다"
        CoachMarkTarget.POST_CATEGORY_OPTION -> "항목 선택" to "목록에서 '질문'을 선택해주세요."
        CoachMarkTarget.POST_TITLE -> "제목 입력" to "여기에 게시글의 제목을 입력하세요."
        CoachMarkTarget.POST_CONTENT -> "내용 입력" to "게시글의 내용을 자유롭게 작성하세요."
        CoachMarkTarget.POST_SUBMIT -> "등록하기" to "모두 작성했다면 등록 버튼을 누르세요."
        CoachMarkTarget.SEARCH_INPUT -> "검색" to "이제 작성한 글을 검색해 볼까요?"
    }

/*
확장 프로퍼티는 실제로 메모리에 값을 저장할 공간(Backing Field)이 없음

따라서 "이 변수를 호출할 때마다 매번 계산해서 돌려줘라"는 의미로 Custom Getter (get()) 를 반드시 작성해야 함.

여기서 this는 현재 이 속성을 호출하고 있는 Enum의 특정 항목(예: REFRESH, FAB 등)을 가리킴
 */