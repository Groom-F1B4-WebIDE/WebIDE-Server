package f1b4.webide_server.dto;

import lombok.Data;

@Data
public class CreateFileReq {
    private String fileName;
    private String fileType;
    //private Long memeberId;
}
