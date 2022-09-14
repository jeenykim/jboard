package jeeny.jboard.question;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter
@Setter
public class QuestionForm {
    @NotEmpty(message="제목은 필수항목입니다.")
    @Size(max=200)
    private String subject;

    @NotEmpty(message="내용은 필수항목입니다.")
    private String content;
}

//    subject 속성에는 @NotEmpty와 @Size 애너테이션이 적용되었다.
//    @NotEmpty는 해당 값이 Null 또는 빈 문자열("")을 허용하지 않음을 의미한다.
//    그리고 여기에 사용된 message 속성은 검증이 실패할 경우 화면에 표시할 오류 메시지이다. @Size(max=200)은 최대 길이가 200 바이트를 넘으면 안된다는 의미이다.
//    이와 같이 설정하면 길이가 200 byte 보다 큰 제목이 입력되면 오류가 발생할 것이다.
//    content 속성 역시 @NotEmpty로 빈 값을 허용하지 않도록 했다.