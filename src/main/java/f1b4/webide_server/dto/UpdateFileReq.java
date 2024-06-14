package f1b4.webide_server.dto;

import lombok.Data;

@Data
public class UpdateFileReq {
        private String fileName;
        //private Long memberId;
        private String content;
}
