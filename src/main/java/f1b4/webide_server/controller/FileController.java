package f1b4.webide_server.controller;

import f1b4.webide_server.dto.ReadFileReq;
import f1b4.webide_server.entity.FileEntity;
import f1b4.webide_server.dto.CreateFileReq;
import f1b4.webide_server.dto.DeleteFileReq;
import f1b4.webide_server.dto.UpdateFileReq;
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
            System.out.println();
            return ResponseEntity.ok(newFile);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body((FileEntity) Map.of("error", "Invalid file type"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body((FileEntity) Map.of("error", "An error occurred while creating the file"));
        }
    }

    /**
     * 2. 파일 읽기 및 불러오기
     * Get Mapping
     * req : filename, fileType (current)
     * resp : status OK, content return
     * file list 불러올때 memberID req값으로 넣어줘야함
     * */
    @GetMapping("/read")
    public ResponseEntity<String> readFile(@RequestParam(value = "fileName") String fileName) {
        Optional<FileEntity> optionalFile = fileService.readFile(fileName);
        if (optionalFile.isPresent()) {
            FileEntity file = optionalFile.get();
            String content = file.getContent();
            //content가 null이면 공백 반환
            if (content == null) {
                return ResponseEntity.ok("");
            }
            return ResponseEntity.ok(content);
        } else {
            // 파일이 존재하지 않을 때의 처리
            //404에러 발생
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * 3. 파일 list 불러오기
     * Get Mapping
     * req : none (current)
     * resp : status OK, file lists 객체
     * file list 불러올때 memberID req값으로 넣어줘야함
     * */
    @GetMapping("/readlists")
    public ResponseEntity<Map<String, String>> readFileList() {
        List<FileEntity> filelist = fileService.readFileList();
        Map<String, String> resFileList = new HashMap<>();
        for (FileEntity file : filelist) {
            String name = file.getFileName();
            String type = file.getFileType();
            resFileList.put(name, type);
        }
        return ResponseEntity.ok(resFileList);
    }

    /**
     * 4. 파일 수정 및 변경사항 저장
     * Put Mapping
     * req : filename, content (current)
     * resp : status OK, updated된 file 객체
     * memberID req에 나중에 추가해줘야함
     * */
    @PutMapping("/update")
    public ResponseEntity<FileEntity> updateFile(@RequestBody UpdateFileReq updateFileReq){
        FileEntity updatedfile = fileService.updateFile(updateFileReq.getFileName(), updateFileReq.getContent());
        return ResponseEntity.ok(updatedfile);
    }

    /**
     * 4. 파일 삭제
     * Delete Mapping
     * req : fileName(current)
     * resp : status OK, 삭제된 후 파일 리스트목록 return / status 404
     * memberID req에 나중에 추가해줘야함
     * */
    @DeleteMapping("/delete")
    public ResponseEntity<Map<String, String>> deleteFile(@RequestBody DeleteFileReq deleteFileReq) {
        boolean isDeleted = fileService.deleteFile(deleteFileReq.getFileName());
        if (isDeleted) {
            //삭제 성공
            List<FileEntity> currentfilelist = fileService.readFileList();
            Map<String, String> rescurrentfilelist = new HashMap<>();
            for (FileEntity file : currentfilelist) {
                String name = file.getFileName();
                String type = file.getFileType();
                rescurrentfilelist.put(name, type);
            }
            return ResponseEntity.ok(rescurrentfilelist);
        } else {
            //파일 없는데 시도한 경우, file notFound 404 Error 발생
            return ResponseEntity.notFound().build();
        }
    }
}