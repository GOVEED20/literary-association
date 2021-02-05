package goveed20.LiteraryAssociationApplication.delegates.readerRegistration;

import goveed20.LiteraryAssociationApplication.model.BetaReaderStatus;
import goveed20.LiteraryAssociationApplication.model.Reader;
import goveed20.LiteraryAssociationApplication.model.VerificationToken;
import goveed20.LiteraryAssociationApplication.model.enums.UserRole;
import goveed20.LiteraryAssociationApplication.repositories.GenreRepository;
import goveed20.LiteraryAssociationApplication.repositories.ReaderRepository;
import goveed20.LiteraryAssociationApplication.repositories.VerificationTokenRepository;
import goveed20.LiteraryAssociationApplication.services.CamundaUserService;
import goveed20.LiteraryAssociationApplication.services.LocationService;
import goveed20.LiteraryAssociationApplication.utils.NotificationService;
import goveed20.LiteraryAssociationApplication.utils.UtilService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Map;
import java.util.UUID;

@Service
public class CreateReaderDelegate implements JavaDelegate {
    @Autowired
    private ReaderRepository readerRepository;

    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CamundaUserService camundaUserService;

    @Autowired
    private LocationService locationService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private GenreRepository genreRepository;

    @SuppressWarnings("unchecked")
    @Override
    public void execute(DelegateExecution delegateExecution) {
        Map<String, Object> data = (Map<String, Object>) delegateExecution.getVariable("data");

        if (readerRepository.findByUsername(String.valueOf(data.get("username"))) == null) {
            throw notificationService.sendErrorNotification("User with given username already exists");
        }
        if (readerRepository.findByEmail(String.valueOf(data.get("email"))) != null) {
            throw notificationService.sendErrorNotification("User with given email address already exists");
        }

        Reader reader = createReader(data);
        readerRepository.save(reader);
        delegateExecution.setVariable("user", reader.getUsername());

        camundaUserService.createCamundaUser(reader);
        VerificationToken vt = VerificationToken.builder().user(reader)
                .disposableHash(String.valueOf(UUID.randomUUID().toString().hashCode())).build();
        verificationTokenRepository.save(vt);

        notificationService.sendSuccessNotification("Check email for verification");
    }

    private Reader createReader(Map<String, Object> data) {
        Reader reader = Reader.readerBuilder()
                .role(UserRole.READER)
                .username((String) data.get("username"))
                .password(passwordEncoder.encode((String) data.get("password")))
                .name((String) data.get("name"))
                .surname((String) data.get("surname"))
                .email((String) data.get("email"))
                .comments(new HashSet<>())
                .transactions(new HashSet<>())
                .genres(new HashSet<>())
                .betaReader((Boolean) data.get("beta_reader"))
                .location(locationService.createLocation((String) data.get("country"), (String) data.get("city")))
                .verified(false)
                .build();
        UtilService.parseGenres((String) data.get("genres")).forEach(g -> reader.getGenres()
                .add(genreRepository.findByGenre(g.getGenre())));

        if (reader.getBetaReader()) {
            BetaReaderStatus brs = BetaReaderStatus.builder().betaGenres(new HashSet<>())
                    .betaReaderPapers(new HashSet<>()).penaltyPoints(0).reader(reader).build();
            UtilService.parseGenres((String) data.get("beta_genres")).forEach(g -> brs.getBetaGenres()
                    .add(genreRepository.findByGenre(g.getGenre())));
            reader.setBetaReaderStatus(brs);
        }

        return reader;
    }
}
