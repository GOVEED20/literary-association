package goveed20.LiteraryAssociationApplication.delegates.writerRegistration;

import goveed20.LiteraryAssociationApplication.model.BaseUser;
import goveed20.LiteraryAssociationApplication.model.enums.UserRole;
import goveed20.LiteraryAssociationApplication.repositories.BaseUserRepository;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReviewDelegate implements JavaDelegate {
    @Autowired
    private BaseUserRepository baseUserRepository;

    @Override
    public void execute(DelegateExecution delegateExecution) {
        List<String> boardMembers = baseUserRepository.findAllByRole(UserRole.BOARD_MEMBER).stream()
                .map(BaseUser::getUsername)
                .collect(Collectors.toList());

        delegateExecution.setVariable("board_members", boardMembers);
    }
}
