package goveed20.LiteraryAssociationApplication.delegates;

import goveed20.LiteraryAssociationApplication.dtos.FormSubmissionFieldDTO;
import goveed20.LiteraryAssociationApplication.model.*;
import goveed20.LiteraryAssociationApplication.model.enums.UserRole;
import goveed20.LiteraryAssociationApplication.repositories.*;
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

    @SuppressWarnings("unchecked")
    @Override
    public void execute(DelegateExecution delegateExecution) {
        List<FormSubmissionFieldDTO> registration = (List<FormSubmissionFieldDTO>) delegateExecution
                .getVariable("registration");
        String userRole = (String) delegateExecution.getVariable("userRole");

        BaseUser user;
        if (userRole.equals("reader")) {
            Reader reader = createReader(registration);
            user = reader;
            readerRepository.save(reader);
        } else {
            Writer writer = createWriter(registration);
            user = writer;
            writerRepository.save(writer);
        }

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
                .location(createLocation(country, city))
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

    private Writer createWriter(List<FormSubmissionFieldDTO> registration) {
        String country = registration.stream().filter((field) -> field.getFieldId().equals("country")).findAny().orElseThrow().getFieldValue();
        String city = registration.stream().filter((field) -> field.getFieldId().equals("city")).findAny().orElseThrow().getFieldValue();

        Writer writer = Writer.writerBuilder()
                .role(UserRole.WRITER)
                .genres(new HashSet<>())
                .location(createLocation(country, city))
                .comments(new HashSet<>())
                .transactions(new HashSet<>())
                .verified(false)
                .membershipApproved(false)
                .workingPapers(new HashSet<>())
                .books(new HashSet<>())
                .build();

        registration.forEach((field) -> {
            switch (field.getFieldId()) {
                case "username":
                    writer.setUsername(field.getFieldValue());
                    break;
                case "password":
                    writer.setPassword(passwordEncoder.encode(field.getFieldValue()));
                    break;
                case "name":
                    writer.setName(field.getFieldValue());
                    break;
                case "surname":
                    writer.setSurname(field.getFieldValue());
                    break;
                case "email":
                    writer.setEmail(field.getFieldValue());
                    break;
                case "genres":
                    UtilService.parseGenres(field.getFieldValue()).forEach(g -> writer.getGenres()
                            .add(genreRepository.findByGenre(g.getGenre())));
                    break;
            }
        });

        return writer;
    }

    private Location createLocation(String country, String city) {
        return Location.builder()
                .country(country)
                .city(city)
                .latitude(45.0)
                .longitude(45.0)
                .build();
    }
}
