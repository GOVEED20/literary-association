package goveed20.LiteraryAssociationApplication.services;

import goveed20.LiteraryAssociationApplication.dtos.BookDTO;
import goveed20.LiteraryAssociationApplication.dtos.BookListItemDTO;
import goveed20.LiteraryAssociationApplication.exceptions.BusinessProcessException;
import goveed20.LiteraryAssociationApplication.exceptions.NotFoundException;
import goveed20.LiteraryAssociationApplication.model.Book;
import goveed20.LiteraryAssociationApplication.model.Retailer;
import goveed20.LiteraryAssociationApplication.model.WorkingPaper;
import goveed20.LiteraryAssociationApplication.repositories.BookRepository;
import goveed20.LiteraryAssociationApplication.repositories.RetailerRepository;
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
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookService {

    private static final String booksFolder = "Literary-Association-Application/src/main/resources/workingPapers/";

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private WorkingPaperRepository workingPaperRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private RetailerRepository retailerRepository;

    public List<BookListItemDTO> getBooks() {
        return bookRepository.findAll()
                .stream()
                .map(b -> new BookListItemDTO(b.getId(), b.getTitle(), b.getPublisher(), b.getISBN(), b.getPublicationYear()))
                .collect(Collectors.toList());
    }

    public BookDTO getBook(Long id) {
        Optional<Book> bookOptional = bookRepository.findById(id);

        if (bookOptional.isEmpty()) {
            throw new NotFoundException(String.format("Book with id '%d' not found", id));
        }

        Book book = bookOptional.get();

        return BookDTO.builder()
                .genreEnum(book.getGenre().getGenre())
                .ISBN(book.getISBN())
                .place(book.getPublicationPlace())
                .publisher(book.getPublisher())
                .synopsis(book.getSynopsis())
                .title(book.getTitle())
                .price(book.getPrice())
                .year(book.getPublicationYear())
                .build();
    }


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

    public List<String> getRetailersForBook(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Book with id '%d' not found", id)));

        return retailerRepository.findAllByBooksContaining(book)
                .stream()
                .map(Retailer::getName)
                .collect(Collectors.toList());
    }
}
