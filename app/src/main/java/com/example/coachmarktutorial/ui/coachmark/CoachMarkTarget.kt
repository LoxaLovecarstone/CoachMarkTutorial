package com.example.coachmarktutorial.ui.coachmark

// 가장 먼저 코치마크를 달아줄 시나리오를 정의해준다
enum class CoachMarkTarget {
    REFRESH,
    FAB, // [초반] 글쓰기 진입용

    // [쓰기 시나리오]
    POST_BUTTON,
    POST_CATEGORY,
    POST_CATEGORY_OPTION,
    POST_TITLE,
    POST_CONTENT,     // [작성용] 내용 입력
    POST_SUBMIT,      // 1. 등록 버튼 클릭

    // [작성 확인 및 검색 진입]
    HOME_POST_CHECK,  // 2. 홈에서 작성된 글 확인
    FAB_SEARCH_OPEN,  // 3. FAB 열기
    FAB_SEARCH,       // 4. 검색 버튼 클릭

    // [검색 및 수정 진입]
    SEARCH_INPUT,       // 5. 검색어 입력
    SEARCH_RESULT_ITEM, // 6. 검색 결과 롱프레스
    CONTEXT_EDIT,       // 7. 수정하기 메뉴 클릭

    // [수정 시나리오] (일직선 배치!)
    POST_EDIT_CONTENT,  // 8. [New] [수정용] 내용 입력 (여기서 텍스트 추가 연출)
    POST_EDIT_SUBMIT,   // 9. [수정용] 수정 완료 버튼

    // [수정 완료 확인 및 삭제 시나리오]
    // (수정 완료 후 다시 검색 화면으로 돌아왔을 때)
    SEARCH_RESULT_ITEM_DELETE, // 10. [New] 삭제를 위해 다시 롱프레스 (기존 SEARCH_RESULT_ITEM과 분리)
    CONTEXT_DELETE,             // 11. 삭제하기 (마지막)

    SEARCH_DELETE_CONFIRM,     // 1. (검색화면) 삭제되었습니다! 확인 메시지
    TUTORIAL_COMPLETED         // 2. (홈화면) 축하합니다! 완료 메시지 (중앙)
}

// CoachMarkTarget에 제목과 내용을 매핑
val CoachMarkTarget.texts: Pair<String, String>
    get() = when (this) {
        CoachMarkTarget.REFRESH -> "새로고침" to "데이터를 최신으로 업데이트하려면\n이 버튼을 누르세요."
        CoachMarkTarget.FAB -> "메뉴 열기" to "새로운 글을 쓰기 위해\n메뉴를 열어보세요."
        CoachMarkTarget.POST_BUTTON -> "글쓰기" to "새로운 글을 작성하려면\n이 버튼을 선택하세요."
        CoachMarkTarget.POST_CATEGORY -> "카테고리" to "먼저 글의 카테고리를 설정하세요."
        CoachMarkTarget.POST_CATEGORY_OPTION -> "항목 선택" to "목록에서 '질문'을 선택해주세요."
        CoachMarkTarget.POST_TITLE -> "제목 입력" to "여기에 게시글의 제목을 입력하세요."
        CoachMarkTarget.POST_CONTENT -> "내용 입력" to "게시글의 내용을 자유롭게 작성하세요."
        CoachMarkTarget.POST_SUBMIT -> "등록하기" to "모두 작성했다면 등록 버튼을 누르세요."

        CoachMarkTarget.HOME_POST_CHECK -> "작성 완료" to "방금 작성한 글이\n홈 화면에 등록되었네요!"
        CoachMarkTarget.FAB_SEARCH_OPEN -> "메뉴 열기" to "이제 작성한 글을 찾기 위해\n다시 메뉴를 열어보세요."
        CoachMarkTarget.FAB_SEARCH -> "글 검색" to "작성된 글을 찾으러 가봅시다."
        CoachMarkTarget.SEARCH_INPUT -> "검색어 입력" to "방금 쓴 글을 검색해 볼게요."
        CoachMarkTarget.SEARCH_RESULT_ITEM -> "옵션 보기" to "아이템을 꾹~ 길게 눌러서\n옵션 메뉴를 열어보세요."
        CoachMarkTarget.CONTEXT_EDIT -> "수정하기" to "내용을 수정하려면\n이 버튼을 누르세요."

        // [New] 수정 전용 문구 추가
        CoachMarkTarget.POST_EDIT_CONTENT -> "내용 수정" to "기존 내용을 수정하거나\n새로운 내용을 추가해보세요."
        CoachMarkTarget.POST_EDIT_SUBMIT -> "수정 완료" to "내용 수정이 끝났다면\n수정 버튼을 누르세요."

        // [New] 삭제 전용 롱프레스 문구 (기존과 동일하지만 단계 분리됨)
        CoachMarkTarget.SEARCH_RESULT_ITEM_DELETE -> "옵션 보기" to "삭제를 위해 아이템을\n다시 길게 눌러보세요."
        CoachMarkTarget.CONTEXT_DELETE -> "삭제하기" to "데이터 정리를 위해\n삭제 버튼을 눌러보세요."
        CoachMarkTarget.SEARCH_DELETE_CONFIRM -> "삭제 완료" to "글이 정상적으로 삭제되었습니다.\n화면을 눌러 홈으로 이동하세요."
        CoachMarkTarget.TUTORIAL_COMPLETED -> "튜토리얼 완료!" to "축하합니다! 모든 기능을 익히셨습니다.\n이제 앱을 자유롭게 사용해보세요."
    }