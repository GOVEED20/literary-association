package goveed20.LiteraryAssociationApplication.delegates;

import goveed20.LiteraryAssociationApplication.dtos.FormSubmissionFieldDTO;
import goveed20.LiteraryAssociationApplication.model.User;
import goveed20.LiteraryAssociationApplication.model.VerificationToken;
import goveed20.LiteraryAssociationApplication.repositories.UserRepository;
import goveed20.LiteraryAssociationApplication.repositories.VerificationTokenRepository;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
public class RegistrationDelegate implements JavaDelegate {

    @Autowired
    UserRepository userRepository;

    @Autowired
    VerificationTokenRepository verificationTokenRepository;

    @SuppressWarnings("unchecked")
    @Override
    public void execute(DelegateExecution delegateExecution) {
        List<FormSubmissionFieldDTO> registration = (List<FormSubmissionFieldDTO>)delegateExecution.getVariable("registration");
        User reader = new User();
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        for (FormSubmissionFieldDTO formField : registration) {
            switch(formField.getFieldId()) {
                case "username":
                    reader.setUsername(formField.getFieldValue());
                    break;
                case "password":
                    reader.setPassword(passwordEncoder.encode(formField.getFieldValue()));
                    break;
                case "name":
                    reader.setName(formField.getFieldValue());
                    break;
                case "surname":
                    reader.setSurname(formField.getFieldValue());
                    break;
                case "country":
                    reader.setCountry(formField.getFieldValue());
                    break;
                case "city":
                    reader.setCity(formField.getFieldValue());
                    break;
                case "email":
                    reader.setEmail(formField.getFieldValue());
                    break;
                case "beta_reader":
                    reader.setBetaReader(Boolean.parseBoolean(formField.getFieldValue()));
                    break;
                default:
                    reader.setGenres(new HashSet<>());
                    reader.setBetaGenres(new HashSet<>());
                    reader.setVerified(false);
            }
        }

        VerificationToken vt = new VerificationToken(reader);
        userRepository.save(reader);
        verificationTokenRepository.save(vt);
    }
}
