package goveed20.LiteraryAssociationApplication.listeners;

import goveed20.LiteraryAssociationApplication.model.BaseUser;
import goveed20.LiteraryAssociationApplication.model.enums.UserRole;
import goveed20.LiteraryAssociationApplication.repositories.BaseUserRepository;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Service
public class PlagiarismStartListener implements ExecutionListener {

    @Autowired
    private BaseUserRepository baseUserRepository;

    @Override
    public void notify(DelegateExecution delegateExecution) {
        BaseUser writer = (BaseUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        delegateExecution.setVariable("writer", writer.getUsername());
        delegateExecution.setVariable("bpmnFile", "plagiarism");

        Map<String, String> boardMembers = new HashMap<>();
        baseUserRepository.findAllByRole(UserRole.BOARD_MEMBER).forEach(member -> boardMembers.put(member.getUsername(), ""));
        delegateExecution.setVariable("board_members", boardMembers);
        delegateExecution.setVariable("board_members_list", new ArrayList<>(boardMembers.keySet()));
    }
}
