package goveed20.LiteraryAssociationApplication.services;

import goveed20.LiteraryAssociationApplication.exceptions.BusinessProcessException;
import goveed20.LiteraryAssociationApplication.model.WorkingPaper;
import goveed20.LiteraryAssociationApplication.repositories.WorkingPaperRepository;
import org.apache.commons.io.FileUtils;
import org.camunda.bpm.engine.RuntimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Service
public class BookService {

    private static final String booksFolder = "Literary-Association-Application/src/main/resources/writings/";

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private WorkingPaperRepository workingPaperRepository;

    public WorkingPaper submitPaper(String processID, String path) throws IOException {
        File writingsFile = new File(path);
        String writingsString = FileUtils.readFileToString(writingsFile);
        FileUtils.forceDelete(writingsFile);
        if (!writingsString.contains("data:application/pdf;base64,")) {
            throw new BusinessProcessException("Invalid file type. It should be a PDF file");
        }
        writingsString = writingsString.replace("data:application/pdf;base64,", "");
        byte[] decoded = Base64.getMimeDecoder().decode(writingsString.getBytes(StandardCharsets.UTF_8));

        String workingPaperTitle = (String) runtimeService.getVariable(processID, "working_paper");
        WorkingPaper paper = workingPaperRepository.findByTitle(workingPaperTitle);
        if (paper == null) {
            throw new EntityNotFoundException("Working paper is not found");
        }

        File filePaper = new File(booksFolder + workingPaperTitle + ".pdf");
        filePaper.createNewFile();
        OutputStream os = new FileOutputStream(filePaper);
        os.write(decoded);
        os.flush();
        os.close();

        paper.setFile(filePaper.getPath());
        return paper;
    }

    public ResponseEntity downloadBook(String bookTitle) throws Exception {
        WorkingPaper paper = workingPaperRepository.findByTitle(bookTitle);
        if (paper == null) {
            throw new EntityNotFoundException("Book with given title does not exist");
        }

        File file = new File(paper.getFile());
        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName() + "\"")
                .contentLength(file.length())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }
}
