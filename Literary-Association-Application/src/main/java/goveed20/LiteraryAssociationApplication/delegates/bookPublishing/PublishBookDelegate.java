package goveed20.LiteraryAssociationApplication.delegates.bookPublishing;

import goveed20.LiteraryAssociationApplication.model.Book;
import goveed20.LiteraryAssociationApplication.model.WorkingPaper;
import goveed20.LiteraryAssociationApplication.model.enums.WorkingPaperStatus;
import goveed20.LiteraryAssociationApplication.repositories.BookRepository;
import goveed20.LiteraryAssociationApplication.repositories.WorkingPaperRepository;
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

    @SuppressWarnings("unchecked")
    @Override
    public void execute(DelegateExecution delegateExecution) {
        Map<String, Object> data = (Map<String, Object>) delegateExecution.getVariable("data");

        WorkingPaper workingPaper = workingPaperRepository.findByTitle(
                (String) delegateExecution.getVariable("working_paper"));

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

        bookRepository.save(book);
    }
}
