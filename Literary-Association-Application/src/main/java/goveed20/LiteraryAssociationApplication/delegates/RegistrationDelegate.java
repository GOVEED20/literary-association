package goveed20.LiteraryAssociationApplication.delegates;

import goveed20.LiteraryAssociationApplication.dtos.FormSubmissionFieldDTO;
import goveed20.LiteraryAssociationApplication.model.*;
import goveed20.LiteraryAssociationApplication.model.enums.UserRole;
import goveed20.LiteraryAssociationApplication.repositories.BetaReaderStatusRepository;
import goveed20.LiteraryAssociationApplication.repositories.ReaderRepository;
import goveed20.LiteraryAssociationApplication.repositories.VerificationTokenRepository;
import goveed20.LiteraryAssociationApplication.utils.UtilService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class RegistrationDelegate implements JavaDelegate {

    @Autowired
    ReaderRepository readerRepository;

    @Autowired
    VerificationTokenRepository verificationTokenRepository;

    @Autowired
    BetaReaderStatusRepository betaReaderStatusRepository;

    @SuppressWarnings("unchecked")
    @Override
    public void execute(DelegateExecution delegateExecution) {
        List<FormSubmissionFieldDTO> registration = (List<FormSubmissionFieldDTO>)delegateExecution
                .getVariable("registration");
        Reader reader = Reader.readerBuilder().role(UserRole.READER)
                .comments(new HashSet<>()).transactions(new HashSet<>()).build();

        /* DEFAULT LOCATION */
        Location l = Location.builder().city("default").country("default").latitude(45.0).longitude(45.0).build();
        reader.setLocation(l);

        Optional<FormSubmissionFieldDTO> betaReader = registration.stream()
                .filter(f -> f.getFieldId().equals("beta_reader")).findFirst();
        betaReader.ifPresent(formSubmissionFieldDTO -> reader
                .setBetaReader(Boolean.parseBoolean(formSubmissionFieldDTO.getFieldValue())));

        BetaReaderStatus brs = null;
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
                case "email":
                    reader.setEmail(formField.getFieldValue());
                    break;
                case "genres":
                    reader.setGenres(UtilService.parseGenres(formField.getFieldValue()));
                    break;
                case "beta_genres":
                    if (reader.getBetaReader()) {
                        brs = BetaReaderStatus.builder().betaGenres(UtilService
                                .parseGenres(formField.getFieldValue()))
                                .betaReaderPapers(new HashSet<>()).penaltyPoints(0).reader(reader).build();
                    }
                    reader.setBetaReaderStatus(brs);
                    break;
            }
        }
        reader.setVerified(false);

        VerificationToken vt = VerificationToken.builder().user(reader)
                .disposableHash(String.valueOf(UUID.randomUUID().toString().hashCode())).build();
        readerRepository.save(reader);
        verificationTokenRepository.save(vt);

        delegateExecution.setVariable("userRole", "reader");
    }
}
