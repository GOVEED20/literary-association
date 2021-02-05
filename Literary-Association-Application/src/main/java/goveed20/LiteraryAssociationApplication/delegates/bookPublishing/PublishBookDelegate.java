package goveed20.LiteraryAssociationApplication.delegates.bookPublishing;

import goveed20.LiteraryAssociationApplication.model.Book;
import goveed20.LiteraryAssociationApplication.model.WorkingPaper;
import goveed20.LiteraryAssociationApplication.model.Writer;
import goveed20.LiteraryAssociationApplication.model.enums.WorkingPaperStatus;
import goveed20.LiteraryAssociationApplication.repositories.BookRepository;
import goveed20.LiteraryAssociationApplication.repositories.WorkingPaperRepository;
import goveed20.LiteraryAssociationApplication.repositories.WriterRepository;
import goveed20.LiteraryAssociationApplication.utils.NotificationService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class PublishBookDelegate implements JavaDelegate {

    @Autowired
    private WorkingPaperRepository workingPaperRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private WriterRepository writerRepository;

    @Autowired
    private NotificationService notificationService;

    @SuppressWarnings("unchecked")
    @Override
    public void execute(DelegateExecution delegateExecution) {
        Map<String, Object> data = (Map<String, Object>) delegateExecution.getVariable("data");

        WorkingPaper workingPaper = workingPaperRepository.findByTitle(
                (String) delegateExecution.getVariable("working_paper"));

        Writer writer = writerRepository.findByUsername((String) delegateExecution.getVariable("writer")).get();

        if (bookRepository.findByISBN((String) data.get("isbn")).isPresent()) {
            throw notificationService.sendErrorNotification("Book with given ISBN already exists");
        }

        Book book = Book.bookBuilder()
                .title(workingPaper.getTitle())
                .synopsis(workingPaper.getSynopsis())
                .genre(workingPaper.getGenre())
                .file(workingPaper.getFile())
                .ISBN((String) data.get("isbn"))
                .keywords((String) data.get("keywords"))
                .publisher((String) data.get("publisher"))
                .publicationYear((Integer.parseInt((String) data.get("publication_year"))))
                .publicationPlace((String) data.get("publication_place"))
                .pages((Integer.parseInt((String) data.get("pages"))))
                .price(Double.parseDouble((String) data.get("price")))
                .status(WorkingPaperStatus.APPROVED)
                .build();

        book.setWriter(writer);
        bookRepository.save(book);

        notificationService.sendSuccessNotification("Book successfully published");
    }
}
