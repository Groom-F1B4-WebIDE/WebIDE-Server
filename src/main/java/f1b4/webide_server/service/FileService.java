package f1b4.webide_server.service;

import f1b4.webide_server.entity.FileEntity;
import f1b4.webide_server.repository.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FileService {

    @Autowired
    private FileRepository fileRepository;

    public FileEntity creatFile(String fileName, String fileType) {
        FileEntity newfile = new FileEntity(fileName, fileType);
        try {
            fileRepository.save(newfile);
        } catch (Exception e) {
            System.out.println("Error while saving file: " + e.getMessage());
        }
        return newfile;
    }

    public Optional<FileEntity> readFile(String fileName) {
        return fileRepository.findByFileName(fileName);

    }

    public List<FileEntity> readFileList() {
        return fileRepository.findAll();
    }

    public FileEntity updateFile(String fileName, String newcontent) {
        try {
            Optional<FileEntity> file = fileRepository.findByFileName(fileName);
            if (file.isPresent()) {
                FileEntity updatefile = file.get();
                updatefile.setContent(newcontent);
                fileRepository.save(updatefile);
                return updatefile;
            } else {
                // 파일이 존재하지 않을 때의 처리
                throw new RuntimeException("File not found with name: " + fileName);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error updating file: " + e.getMessage());

        }
    }

    public boolean deleteFile(Long id) {
        try {
            fileRepository.deleteById(id);
            return true; // 삭제 성공
        } catch (EmptyResultDataAccessException e) {
            // 파일이 존재하지 않는 경우 처리
            return false; // 삭제 실패
        }
    }


}
