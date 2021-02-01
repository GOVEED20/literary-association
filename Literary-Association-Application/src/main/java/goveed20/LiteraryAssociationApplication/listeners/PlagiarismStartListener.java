package goveed20.LiteraryAssociationApplication.listeners;

import goveed20.LiteraryAssociationApplication.model.BaseUser;
import goveed20.LiteraryAssociationApplication.model.enums.UserRole;
import goveed20.LiteraryAssociationApplication.repositories.BaseUserRepository;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class PlagiarismStartListener implements ExecutionListener {

    @Autowired
    private BaseUserRepository baseUserRepository;

    @Override
    public void notify(DelegateExecution delegateExecution) throws Exception {
        BaseUser writer = (BaseUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        delegateExecution.setVariable("writer", writer.getUsername());
        delegateExecution.setVariable("bpmnFile", "src/main/resources/plagiarism.bpmn");
        Map<String, String> boardMembers = new HashMap<>();
        baseUserRepository.findAllByRole(UserRole.BOARD_MEMBER).forEach(member -> {
            boardMembers.put(member.getUsername(), "");
        });
        delegateExecution.setVariable("board_members", boardMembers);
    }
}
