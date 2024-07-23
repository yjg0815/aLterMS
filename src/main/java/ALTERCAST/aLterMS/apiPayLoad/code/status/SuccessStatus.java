package ALTERCAST.aLterMS.apiPayLoad.code.status;

import ALTERCAST.aLterMS.apiPayLoad.code.BaseCode;
import ALTERCAST.aLterMS.apiPayLoad.code.ReasonDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum SuccessStatus implements BaseCode {
    // 인증, 인가 관련 응답
    _OK(HttpStatus.OK, "AUTH200", "로그인이 성공하였습니다."),
    _CREATED(HttpStatus.CREATED, "AUTH201", "회원가입이 성공하였습니다."),

    // 게시물 관련 응답
//    POST_CREATED(HttpStatus.CREATED, "POST201", "게시글 작성이 성공하였습니다."),
//    POST_UPDATED(HttpStatus.OK, "POST200", "게시글 수정이 성공하였습니다."),
//    POST_DELETED(HttpStatus.OK, "POST200", "게시글 삭제가 성공하였습니다."),
//    POST_GET_LIST(HttpStatus.OK, "POST200", "게시글 목록 조회가 성공하였습니다."),
//    POST_GET_DETAIL(HttpStatus.OK, "POST200", "단일 게시글 조회가 성공하였습니다."),

    // 학생 요청 관련 응답
    GET_USER_INFO(HttpStatus.OK, "STU2001", "학생 정보 조회 완료"),
    UPDATE_USER_INFO(HttpStatus.OK, "STU2002", "학생 정보 수정 완료"),
    DELETE_USER_INFO(HttpStatus.OK, "STU2003", "학생 탈퇴 완료")

    ;


    private final HttpStatus httpStatus;

    private final String code;

    private final String message;

    @Override
    public ReasonDTO getReason() {
        return ReasonDTO.builder()
                .message(this.message)
                .code(this.code)
                .isSuccess(true)
                .build();
    }

    @Override
    public ReasonDTO getReasonHttpStatus() {
        return ReasonDTO.builder()
                .message(this.message)
                .code(this.code)
                .isSuccess(true)
                .httpStatus(this.httpStatus)
                .build();
    }
}
