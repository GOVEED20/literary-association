package goveed20.LiteraryAssociationApplication.delegates.writerRegistration;

import goveed20.LiteraryAssociationApplication.model.VerificationToken;
import goveed20.LiteraryAssociationApplication.model.Writer;
import goveed20.LiteraryAssociationApplication.model.enums.UserRole;
import goveed20.LiteraryAssociationApplication.repositories.GenreRepository;
import goveed20.LiteraryAssociationApplication.repositories.VerificationTokenRepository;
import goveed20.LiteraryAssociationApplication.repositories.WriterRepository;
import goveed20.LiteraryAssociationApplication.services.CamundaUserService;
import goveed20.LiteraryAssociationApplication.services.LocationService;
import goveed20.LiteraryAssociationApplication.utils.UtilService;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Map;
import java.util.UUID;

@Service
public class CreateWriterDelegate implements JavaDelegate {
    @Autowired
    private WriterRepository writerRepository;

    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CamundaUserService camundaUserService;

    @Autowired
    private LocationService locationService;

    @Autowired
    private GenreRepository genreRepository;

    @SuppressWarnings("unchecked")
    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        Map<String, Object> data = (Map<String, Object>) delegateExecution.getVariable("data");

        if (writerRepository.findByUsername(String.valueOf(data.get("username"))).isPresent()) {
            throw new BpmnError("User with given username already exists");
        }
        if (writerRepository.findByEmail(String.valueOf(data.get("email"))) != null) {
            throw new BpmnError("User with given email address already exists");
        }

        Writer writer = createWriter(data);
        writerRepository.save(writer);
        delegateExecution.setVariable("user", writer.getUsername());

        camundaUserService.createCamundaUser(writer);
        VerificationToken vt = VerificationToken.builder().user(writer)
                .disposableHash(String.valueOf(UUID.randomUUID().toString().hashCode())).build();
        verificationTokenRepository.save(vt);
    }

    private Writer createWriter(Map<String, Object> data) {
        Writer writer = Writer.writerBuilder()
                .role(UserRole.WRITER)
                .genres(new HashSet<>())
                .location(locationService.createLocation(String.valueOf(data.get("country")), String.valueOf(data.get("city"))))
                .comments(new HashSet<>())
                .transactions(new HashSet<>())
                .verified(false)
                .membershipApproved(false)
                .workingPapers(new HashSet<>())
                .books(new HashSet<>())
                .username(String.valueOf(data.get("username")))
                .password(passwordEncoder.encode(String.valueOf(data.get("password"))))
                .name(String.valueOf(data.get("name")))
                .surname(String.valueOf(data.get("surname")))
                .email(String.valueOf(data.get("email")))
                .genres(new HashSet<>())
                .build();

        UtilService.parseGenres(String.valueOf(data.get("genres")))
                .forEach(g -> writer.getGenres().add(genreRepository.findByGenre(g.getGenre())));

        return writer;
    }
}
