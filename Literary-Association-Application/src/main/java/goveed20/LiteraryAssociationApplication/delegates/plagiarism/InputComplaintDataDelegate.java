package goveed20.LiteraryAssociationApplication.delegates.plagiarism;

import goveed20.LiteraryAssociationApplication.model.BaseUser;
import goveed20.LiteraryAssociationApplication.model.Book;
import goveed20.LiteraryAssociationApplication.model.Writer;
import goveed20.LiteraryAssociationApplication.model.enums.UserRole;
import goveed20.LiteraryAssociationApplication.repositories.BaseUserRepository;
import goveed20.LiteraryAssociationApplication.repositories.BookRepository;
import goveed20.LiteraryAssociationApplication.services.EmailService;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Map;

@Service
public class InputComplaintDataDelegate implements JavaDelegate {

    @Autowired
    private EmailService emailService;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private BaseUserRepository baseUserRepository;

    @SuppressWarnings("unchecked")
    @Override
    public void execute(DelegateExecution delegateExecution) {
        Writer writer = (Writer) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Map<String, Object> data = (Map<String, Object>) delegateExecution.getVariable("data");

        if (writer.getBooks().stream().noneMatch(book -> book.getTitle().equals(data.get("my_book")))) {
            throw new BpmnError("Your chosen book does not exist.");
        }

        Book plagiarismBook = bookRepository.findByTitle((String) data.get("plagiarism_book"))
                .orElseThrow(() -> new BpmnError("Chosen plagiarism book does not exist"));

        if (plagiarismBook.getWriter().getName().equals(data.get("writer_name")) &&
                plagiarismBook.getWriter().getName().equals(data.get("writer_surname"))) {
            throw new BpmnError("Chosen plagiarism book and writer do not match.");
        }

        ArrayList<BaseUser> editors = (ArrayList<BaseUser>) baseUserRepository.findAllByRole(UserRole.EDITOR);
        BaseUser editor = editors.get((int) (Math.random() * editors.size()));

        delegateExecution.setVariable("editor", editor.getUsername());

        String text = String.format("Dear %s %s,\nPlagiarism complaint request has been received from %s, for " +
                        "book with title %s", editor.getName(),
                editor.getSurname(), delegateExecution.getVariable("writer"), plagiarismBook.getTitle());
        emailService.sendEmail(editor.getEmail(), "Plagiarism complaint", text);

    }
}
