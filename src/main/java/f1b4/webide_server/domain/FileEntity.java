package f1b4.webide_server.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "File")
public class FileEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fileID;

    @Column(nullable = false)
    private String fileName;

    @Column(nullable = false)
    private String fileType;

    @Column(nullable = true)
    private String content;

//    @ManyToOne
//    @JoinColumn(name = "memberId", nullable = false)
//    private Member member;

    //추가 생성자 2개만 받는
    public FileEntity(String fileName, String fileType) {
        this.fileName = fileName;
        this.fileType = fileType;
        this.content = null;
    }
}
