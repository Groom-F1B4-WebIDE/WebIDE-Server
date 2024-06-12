package f1b4.webide_server.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateFileReq {
    private String fileName;
    private String fileType;
    //private Long memeberId;
}
