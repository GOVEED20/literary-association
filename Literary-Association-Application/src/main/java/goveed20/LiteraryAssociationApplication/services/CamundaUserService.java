package goveed20.LiteraryAssociationApplication.services;

import goveed20.LiteraryAssociationApplication.model.BaseUser;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.identity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CamundaUserService {
    @Autowired
    private IdentityService identityService;

    public void createCamundaUser(BaseUser user) {
        if (identityService.createUserQuery().userId(user.getUsername()).singleResult() == null) {
            User camundaUser = identityService.newUser(user.getUsername());
            camundaUser.setEmail(user.getEmail());
            camundaUser.setPassword(user.getPassword());
            camundaUser.setFirstName(user.getName());
            camundaUser.setLastName(user.getSurname());

            identityService.saveUser(camundaUser);
        }
    }
}
