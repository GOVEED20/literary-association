package goveed20.LiteraryAssociationApplication.delegates;

import goveed20.LiteraryAssociationApplication.utils.ReviewResult;
import goveed20.LiteraryAssociationApplication.model.BaseUser;
import goveed20.LiteraryAssociationApplication.model.enums.UserRole;
import goveed20.LiteraryAssociationApplication.repositories.BaseUserRepository;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ReviewDelegate implements JavaDelegate {
    @Autowired
    private BaseUserRepository baseUserRepository;

    @Autowired
    private RuntimeService runtimeService;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        List<String> boardMembers = baseUserRepository.findAllByRole(UserRole.BOARD_MEMBER).stream()
                .map(BaseUser::getUsername)
                .collect(Collectors.toList());

        runtimeService.setVariable(delegateExecution.getProcessInstanceId(), "board_members", boardMembers);

        Map<String, ReviewResult> reviewResults = new HashMap<>();
        boardMembers.forEach(bm -> reviewResults.put(bm, new ReviewResult()));

        runtimeService.setVariable(delegateExecution.getProcessInstanceId(), "review_result", reviewResults);
    }
}
