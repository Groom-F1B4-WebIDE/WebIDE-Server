package f1b4.webide_server.compile;

import lombok.Data;

@Data
public class CodeRequest {
    private String code;
    private String language;
    private String inputValues;
}
