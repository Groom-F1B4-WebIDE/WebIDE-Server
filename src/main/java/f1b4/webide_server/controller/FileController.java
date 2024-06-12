package f1b4.webide_server.controller;

import f1b4.webide_server.domain.FileEntity;
import f1b4.webide_server.dto.CreateFileReq;
import f1b4.webide_server.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/file")
public class FileController {

    @Autowired
    private FileService fileService;

    /**
     * 1. 파일 생성
     * Post Mapping
     * req : memberID, filename, fileType
     * resp : status OK, new file 객체
     * */
    @PostMapping("/create")
    public ResponseEntity<FileEntity> createFile(@RequestBody CreateFileReq createFileReq) {
        try {
            //Member member = memberRepository.findById(memberId)
            FileEntity newFile = fileService.creatFile(createFileReq.getFileName(), createFileReq.getFileType());
            return ResponseEntity.ok(newFile);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body((FileEntity) Map.of("error", "Invalid file type"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body((FileEntity) Map.of("error", "An error occurred while creating the file"));
        }
    }

    /**
     * 1. 파일 읽기 및 불러오기
     * Get Mapping
     * req : memberID, filename, fileType
     * resp : status OK, new file 객체
     * */
    @GetMapping("/read")
    public ResponseEntity<String> readFile(@RequestBody String fileName) {
        Optional<FileEntity> optionalFile = fileService.readFile(fileName);
        if (optionalFile.isPresent()) {
            FileEntity file = optionalFile.get();
            String content = file.getContent();
            return ResponseEntity.ok(content);
        } else {
            // 파일이 존재하지 않을 때의 처리
            //404에러코드와 함게 error : none이라는 json을 같이 body에 보내줌
            Map<String, String> response = new HashMap<>();
            response.put("error", "None");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response.toString());
        }
    }

    /**
     * 3. 파일 list 불러오기
     * Get Mapping
     * req : none
     * resp : status OK, file lists 객체
     * */
    @GetMapping("/readlists")
    public ResponseEntity<List<FileEntity>> readFileList() {
        List<FileEntity> filelist = fileService.readFileList();
        return ResponseEntity.ok(filelist);
    }

    /**
     * 4. 파일 수정 및 변경사항 저장
     * Put Mapping
     * req : filename, content, memberID
     * resp : status OK, file lists 객체
     * */
    @PutMapping("/update")
    public ResponseEntity<FileEntity> updateFile(@RequestBody String fileName,
                                           @RequestParam String content){
        FileEntity updatedfile = fileService.updateFile();
        return ResponseEntity.ok(updatedfile);
        //repository로 file update 로직
        //return ok??
    }

    @DeleteMapping("/delete")
    public ResponseEntity<FileEntity> deleteFile(@RequestBody Long Id) {
        fileService.deleteFile(Id);
        System.out.println("pass delete");
        return ResponseEntity.ok().build();
    }

}
