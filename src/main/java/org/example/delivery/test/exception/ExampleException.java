package org.example.delivery.test.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.web.ErrorResponseException;

public class ExampleException extends ErrorResponseException {

//    ErrorResponse 필드 목록 (ErrorResponseException 구현체 기준)
//
//      private final HttpStatusCode status;            : HttpStatus
//      private final HttpHeaders headers;              : header 별도로 설정 시 정의
//      private final ProblemDetail body;               : response body. 아래 ProblemDetail 참고
//      private final String messageDetailCode;         : i18n 사용 시 정의
//      private final Object[] messageDetailArguments;  : String messageDetailCode 값 치환
//
//    ErrorResponse 로 구현하면 표준적이고, Spring의 지원을 받을 수 있음.
//    초기 학습 곡선이 꽤나 가파르지만
//    적응하면 표준적인 구성을 놓치지 않고 활용 가능함.
//
//    @ExceptionHandler() 를 작성하지 않아도 throw 시 자동으로 response.
//
//
//    ProblemDetail 필드 목록 (RFC 9457)
//
//      private URI type;                       : 해당 문제를 설명하는 링크 (default=about:blank)
//      private String title;                   : 사람이 인지하기 위한 문제의 제목 (간략한 설명, 이름, HTTPStatus, ...)
//      private int status;                     : HttpStatus
//      private String detail;                  : 문제의 세부 설명
//      private URI instance;                   : 오류가 발생한 URI
//      private Map<String, Object> properties; : Custom 용도
//
//    ProblemDetail에 정의된 값이, throw 시 HTTP Response의 Body로 전달됨.

//    자세한 get, set 등 메서드는 해당 구현체 참고


//    아래와 같이 생성자를 정의하고, Controller, Service 등에서
//    throw new ExampleException( {custom} )
//    까지만 하면 HTTP Response 까지 자동 처리
    public ExampleException(HttpStatusCode status, String title){
        super(status);

        getBody().setTitle(title);
    }


    public ExampleException(HttpStatusCode status, String title, String detail){
        super(status);

        ProblemDetail body = this.getBody();

        body.setTitle(title);
        body.setDetail(detail);
    }

    public ExampleException(int status, String title, String detail){
        super(HttpStatusCode.valueOf(status));

        ProblemDetail body = this.getBody();

        body.setTitle(title);
        body.setDetail(detail);
    }

    public ExampleException(String title, String detail){
        super(HttpStatus.OK);

        ProblemDetail body = this.getBody();

        body.setTitle(title);
        body.setDetail(detail);
    }

    public ExampleException(){
        super(HttpStatus.OK);

        ProblemDetail body = this.getBody();

        body.setTitle("Requested Empty Title");
        body.setDetail("ExampleException's default constructor");
    }


//    이 예시 생성자의 내부 구현
//        this.headers = new HttpHeaders();
//        this.status = status;
//        this.body = ProblemDetail.forStatus(status);
//        this.messageDetailCode = this.initMessageDetailCode((String) null);
//        this.messageDetailArguments = (Object[]) null;
    public ExampleException(HttpStatusCode status) {
        super(status);
    }

}








