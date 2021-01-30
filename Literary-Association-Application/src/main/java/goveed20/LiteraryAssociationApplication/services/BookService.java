package goveed20.LiteraryAssociationApplication.services;

import goveed20.LiteraryAssociationApplication.exceptions.BusinessProcessException;
import goveed20.LiteraryAssociationApplication.model.WorkingPaper;
import goveed20.LiteraryAssociationApplication.repositories.WorkingPaperRepository;
import org.apache.commons.io.FileUtils;
import org.camunda.bpm.engine.RuntimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Base64;

@Service
public class BookService {

    private static final String booksFolder = "Literary-Association-Application/src/main/resources/workingPapers/";

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private WorkingPaperRepository workingPaperRepository;

    public WorkingPaper submitPaper(String processID, String base64File) throws IOException {
        byte[] pdfDecoded = Base64.getDecoder().decode(base64File);
        if (pdfDecoded[0] != 0x25 || pdfDecoded[1] != 0x50 || pdfDecoded[2] != 0x44 || pdfDecoded[3] != 0x46) {
            throw new BusinessProcessException("Invalid file type. It should be a PDF file");
        }

        String workingPaperTitle = (String) runtimeService.getVariable(processID, "working_paper");
        WorkingPaper paper = workingPaperRepository.findByTitle(workingPaperTitle);
        if (paper == null) {
            throw new EntityNotFoundException("Working paper is not found");
        }

        File filePaper = new File(booksFolder + workingPaperTitle + ".pdf");
        OutputStream os = new FileOutputStream(filePaper);
        os.write(pdfDecoded);
        os.flush();
        os.close();

        paper.setFile(filePaper.getPath());
        return paper;
    }

    public String downloadBook(String bookTitle) throws Exception {
        WorkingPaper paper = workingPaperRepository.findByTitle(bookTitle);
        if (paper == null) {
            throw new EntityNotFoundException("Book with given title does not exist");
        }

        File file = new File(paper.getFile());
        FileOutputStream outputStream;

        try {
            outputStream = new FileOutputStream(paper.getTitle() + ".pdf");
            outputStream.write(FileUtils.readFileToByteArray(file));
            outputStream.close();
        } catch (IOException e) {
            throw new Exception("Book file does not exist");
        }

        return "Successful download";
    }
}
