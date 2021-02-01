package goveed20.LiteraryAssociationApplication.delegates.writerRegistration;

import goveed20.LiteraryAssociationApplication.model.BaseUser;
import goveed20.LiteraryAssociationApplication.repositories.BaseUserRepository;
import goveed20.LiteraryAssociationApplication.services.EmailService;
import goveed20.LiteraryAssociationApplication.utils.ReviewResult;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class NotificationDelegate implements JavaDelegate {
    @Autowired
    private EmailService emailService;

    @Autowired
    private BaseUserRepository baseUserRepository;

    @SuppressWarnings("unchecked")
    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        boolean suitable = (boolean) delegateExecution.getVariable("suitable");
        boolean furtherReview = (boolean) delegateExecution.getVariable("further_review");
        int i = (int) delegateExecution.getVariable("i");
        Boolean membershipNotPaid = (Boolean) delegateExecution.getVariable("membership_not_paid");
        Boolean newWritingsNotSubmitted = (Boolean) delegateExecution.getVariable("new_writings_not_submitted");
        List<ReviewResult> reviewResults = (List<ReviewResult>) delegateExecution.getVariable("review_results");

        String email = (String) delegateExecution.getVariable("email");

        Optional<BaseUser> userOptional = baseUserRepository.findByEmail(email);

        if (userOptional.isPresent()) {
            BaseUser user = userOptional.get();

            String text;
            if (membershipNotPaid != null && membershipNotPaid) {
                text = String.format("Dear %s %s,\nYou have failed to pay your first membership fee in time, therefore your membership application is rejected.\nBest regards", user.getName(), user.getSurname());
            } else if (newWritingsNotSubmitted != null && newWritingsNotSubmitted) {
                text = String.format("Dear %s %s,\nYou have failed to submit required writings in time, therefore your membership application is rejected.\nBest regards", user.getName(), user.getSurname());
            } else if (suitable) {
                text = String.format("Dear %s %s,\nOur board members have found you suitable for membership in our literary association. In order to complete your registration, you are required to pay your first membership fee in the next 2 weeks.\nBest regards", user.getName(), user.getSurname());
            } else if (furtherReview && i > 3) {
                text = String.format("Dear %s %s,\nYou have reached review cycle's limit, therefore your membership application is rejected.\nBest regards", user.getName(), user.getSurname());
            } else if (furtherReview) {
                text = String.format("Dear %s %s,\nOur board members have requested more writings for review. In order to complete your registration, you are required to send additional writings in the next 2 weeks.\n%s\nBest regards", user.getName(), user.getSurname(), reviewResults.stream().map(ReviewResult::getComment).collect(Collectors.joining("\n")));
            } else {
                text = String.format("Dear %s %s,\nOur board members have found you unsuitable for membership in our literary association, therefore your membership application is rejected.\n%s" +
                        "\nBest regards", user.getName(), user.getSurname(), reviewResults.stream().map(ReviewResult::getComment).collect(Collectors.joining("\n")));
            }
            emailService.sendEmail(email, "Literary association registration status", text);
        }
    }
}
