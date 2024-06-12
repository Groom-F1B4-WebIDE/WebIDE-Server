package f1b4.webide_server.service;

import f1b4.webide_server.domain.FileEntity;
import f1b4.webide_server.repository.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
        Optional<FileEntity> file = fileRepository.findByFileName(fileName);
        if (file.isPresent()) {
            FileEntity updatefile = file.get();
            updatefile.setContent(newcontent);
            return fileRepository.save(updatefile);
        } else {
            // 파일이 존재하지 않을 때의 처리
            throw new RuntimeException("File not found with name: " + fileName);
        }

    }

    public void deleteFile(Long id) {
        fileRepository.deleteById(id);
    }

}
