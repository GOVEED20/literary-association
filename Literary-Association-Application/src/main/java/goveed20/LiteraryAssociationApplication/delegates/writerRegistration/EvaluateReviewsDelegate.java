package goveed20.LiteraryAssociationApplication.delegates.writerRegistration;

import goveed20.LiteraryAssociationApplication.utils.ReviewResult;
import goveed20.LiteraryAssociationApplication.utils.ReviewStatus;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EvaluateReviewsDelegate implements JavaDelegate {
    @SuppressWarnings("unchecked")
    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        List<ReviewResult> reviewResults = (List<ReviewResult>) delegateExecution.getVariable("review_results");

        long total = reviewResults.size();
        long suitable = countByStatus(reviewResults, ReviewStatus.SUITABLE);
        long unsuitable = countByStatus(reviewResults, ReviewStatus.UNSUITABLE);

        if (unsuitable >= total / 2) {
            delegateExecution.setVariable("suitable", false);
            delegateExecution.setVariable("further_review", false);
        } else if (suitable == total) {
            delegateExecution.setVariable("suitable", true);
            delegateExecution.setVariable("further_review", false);
        } else {
            delegateExecution.setVariable("suitable", false);
            delegateExecution.setVariable("further_review", true);
        }
    }

    private long countByStatus(List<ReviewResult> results, ReviewStatus status) {
        return results.stream().filter(result -> result.getStatus().equals(status)).count();
    }
}
