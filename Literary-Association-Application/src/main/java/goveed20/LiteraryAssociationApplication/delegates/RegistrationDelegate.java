package goveed20.LiteraryAssociationApplication.delegates;

import goveed20.LiteraryAssociationApplication.dtos.FormSubmissionFieldDTO;
import goveed20.LiteraryAssociationApplication.model.*;
import goveed20.LiteraryAssociationApplication.model.enums.UserRole;
import goveed20.LiteraryAssociationApplication.repositories.GenreRepository;
import goveed20.LiteraryAssociationApplication.repositories.ReaderRepository;
import goveed20.LiteraryAssociationApplication.repositories.VerificationTokenRepository;
import goveed20.LiteraryAssociationApplication.repositories.WriterRepository;
import goveed20.LiteraryAssociationApplication.services.CamundaUserService;
import goveed20.LiteraryAssociationApplication.utils.UtilService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.UUID;

@Service
public class RegistrationDelegate implements JavaDelegate {

    @Autowired
    private ReaderRepository readerRepository;

    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private WriterRepository writerRepository;

    @Autowired
    private CamundaUserService camundaUserService;

    @SuppressWarnings("unchecked")
    @Override
    public void execute(DelegateExecution delegateExecution) {
        List<FormSubmissionFieldDTO> registration = (List<FormSubmissionFieldDTO>) delegateExecution
                .getVariable("registration");
        String userRole = (String) delegateExecution.getVariable("userRole");

        BaseUser user = null;
        if (userRole.equals("reader")) {
            Reader reader = createReader(registration);
            user = reader;
            readerRepository.save(reader);
        } else {
//            Writer writer = createWriter(registration);
//            user = writer;
//            writerRepository.save(writer);
//            delegateExecution.setVariable("user", writer.getUsername());
        }

        camundaUserService.createCamundaUser(user);
        VerificationToken vt = VerificationToken.builder().user(user)
                .disposableHash(String.valueOf(UUID.randomUUID().toString().hashCode())).build();
        verificationTokenRepository.save(vt);
    }

    private Reader createReader(List<FormSubmissionFieldDTO> registration) {
        String country = registration.stream().filter((field) -> field.getFieldId().equals("country")).findAny().orElseThrow().getFieldValue();
        String city = registration.stream().filter((field) -> field.getFieldId().equals("city")).findAny().orElseThrow().getFieldValue();

        Reader reader = Reader.readerBuilder()
                .role(UserRole.READER)
                .comments(new HashSet<>())
                .transactions(new HashSet<>())
                .genres(new HashSet<>())
                .betaReader(registration.stream().anyMatch((field) -> field.getFieldId().equals("beta_reader")))
                //.location(createLocation(country, city))
                .verified(false)
                .build();

        registration.forEach((field) -> {
            switch (field.getFieldId()) {
                case "username":
                    reader.setUsername(field.getFieldValue());
                    break;
                case "password":
                    reader.setPassword(passwordEncoder.encode(field.getFieldValue()));
                    break;
                case "name":
                    reader.setName(field.getFieldValue());
                    break;
                case "surname":
                    reader.setSurname(field.getFieldValue());
                    break;
                case "email":
                    reader.setEmail(field.getFieldValue());
                    break;
                case "genres":
                    UtilService.parseGenres(field.getFieldValue()).forEach(g -> reader.getGenres()
                            .add(genreRepository.findByGenre(g.getGenre())));
                    break;
                case "beta_genres":
                    if (reader.getBetaReader()) {
                        BetaReaderStatus brs = BetaReaderStatus.builder().betaGenres(new HashSet<>())
                                .betaReaderPapers(new HashSet<>()).penaltyPoints(0).reader(reader).build();
                        UtilService.parseGenres(field.getFieldValue()).forEach(g -> brs.getBetaGenres()
                                .add(genreRepository.findByGenre(g.getGenre())));
                        reader.setBetaReaderStatus(brs);
                    }
                    break;
            }
        });

        return reader;
    }
}
