package goveed20.LiteraryAssociationApplication.delegates.plagiarism;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class CheckVotesDelegate implements JavaDelegate {

    @SuppressWarnings("unchecked")
    @Override
    public void execute(DelegateExecution delegateExecution) {
        Map<String, String> boardMembers = (Map<String, String>) delegateExecution.getVariable("board_members");
        delegateExecution.setVariable("unanimous_decision", boardMembers.values().stream().distinct().count() <= 1);
    }
}
