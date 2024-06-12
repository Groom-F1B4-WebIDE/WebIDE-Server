package f1b4.webide_server.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateFileReq {
        private String fileName;
        //private Long memberId;
        private String content;
}
