package f1b4.webide_server.entity.code;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CodeRequest {
    private String code;
    private String language;
    private String inputValues;
}
