package goveed20.LiteraryAssociationApplication.services;

import goveed20.LiteraryAssociationApplication.model.BaseUser;
import goveed20.LiteraryAssociationApplication.model.Genre;
import goveed20.LiteraryAssociationApplication.model.Writer;
import goveed20.LiteraryAssociationApplication.model.enums.GenreEnum;
import goveed20.LiteraryAssociationApplication.model.enums.UserRole;
import goveed20.LiteraryAssociationApplication.repositories.BaseUserRepository;
import goveed20.LiteraryAssociationApplication.repositories.GenreRepository;
import goveed20.LiteraryAssociationApplication.repositories.WriterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;

@Service
public class StartUpService {

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private BaseUserRepository baseUserRepository;

    @Autowired
    private WriterRepository writerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private LocationService locationService;

    @Autowired
    private CamundaUserService camundaUserService;

    @EventListener(ApplicationReadyEvent.class)
    public void createDataOnStartUp() {
        if (genreRepository.findAll().isEmpty()) {
            Arrays.stream(GenreEnum.values()).forEach(e -> genreRepository.save(new Genre(null, e)));
        }

        Writer writer = Writer.writerBuilder()
                .role(UserRole.WRITER)
                .genres(new HashSet<>())
                .location(locationService.createLocation("Serbia", "Novi Sad"))
                .comments(new HashSet<>())
                .transactions(new HashSet<>())
                .verified(true)
                .membershipApproved(true)
                .workingPapers(new HashSet<>())
                .books(new HashSet<>())
                .username("perata")
                .password(passwordEncoder.encode("Pera1997!"))
                .name("Pero")
                .surname("Peric")
                .email("perata@maildrop.cc")
                .genres(new HashSet<>())
                .build();

        writerRepository.save(writer);

        BaseUser editor = BaseUser.builder()
                .role(UserRole.EDITOR)
                .username("editor")
                .password(passwordEncoder.encode("Editor1997!"))
                .email("editor@maildrop.cc")
                .name("Editor")
                .surname("Editorovic")
                .verified(true)
                .location(locationService.createLocation("Serbia", "Novi Sad"))
                .build();

        baseUserRepository.save(editor);

        BaseUser lector = BaseUser.builder()
                .role(UserRole.LECTOR)
                .username("lector")
                .password(passwordEncoder.encode("Lector1997!"))
                .email("lector@maildrop.cc")
                .name("Lector")
                .surname("Lectorovic")
                .verified(true)
                .location(locationService.createLocation("Serbia", "Novi Sad"))
                .build();

        baseUserRepository.save(lector);

        camundaUserService.createCamundaUser(writer);
        camundaUserService.createCamundaUser(editor);
        camundaUserService.createCamundaUser(lector);
    }
}
