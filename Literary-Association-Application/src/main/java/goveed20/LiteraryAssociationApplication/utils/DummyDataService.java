package goveed20.LiteraryAssociationApplication.utils;

import goveed20.LiteraryAssociationApplication.model.BaseUser;
import goveed20.LiteraryAssociationApplication.model.Location;
import goveed20.LiteraryAssociationApplication.model.Reader;
import goveed20.LiteraryAssociationApplication.model.enums.UserRole;
import goveed20.LiteraryAssociationApplication.repositories.BaseUserRepository;
import goveed20.LiteraryAssociationApplication.services.CamundaUserService;
import goveed20.LiteraryAssociationApplication.services.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;

@Service
public class DummyDataService {
    @Autowired
    private BaseUserRepository baseUserRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CamundaUserService camundaUserService;

    @Autowired
    private LocationService locationService;

    @EventListener(ApplicationReadyEvent.class)
    public void addDummyBoardMembers() {
        if (baseUserRepository.findAllByRole(UserRole.BOARD_MEMBER).isEmpty()) {
            BaseUser boardMember1 = BaseUser.builder()
                    .name("board_member_1_name")
                    .surname("board_member_1_surname")
                    .email("board_member_1@test.com")
                    .username("boardMember1")
                    .password(passwordEncoder.encode("board_member_1"))
                    .verified(true)
                    .role(UserRole.BOARD_MEMBER)
                    .location(Location.builder().longitude(0.0).latitude(0.0).city("test1").country("test1").build())
                    .build();

            BaseUser boardMember2 = BaseUser.builder()
                    .name("board_member_2_name")
                    .surname("board_member_2_surname")
                    .email("board_member_2@test.com")
                    .username("boardMember2")
                    .verified(true)
                    .role(UserRole.BOARD_MEMBER)
                    .password(passwordEncoder.encode("board_member_2"))
                    .location(Location.builder().longitude(0.0).latitude(0.0).city("test2").country("test2").build())
                    .build();

            baseUserRepository.save(boardMember1);
            baseUserRepository.save(boardMember2);

            camundaUserService.createCamundaUser(boardMember1);
            camundaUserService.createCamundaUser(boardMember2);

            System.out.println("Created dummy board members!");
        }

        if (baseUserRepository.findAllByRole(UserRole.READER).isEmpty()) {
            Reader reader = Reader.readerBuilder()
                    .role(UserRole.READER)
                    .username("reader1")
                    .password(passwordEncoder.encode("password1"))
                    .name("reader1")
                    .surname("reader1")
                    .email("reader@reader.com")
                    .comments(new HashSet<>())
                    .transactions(new HashSet<>())
                    .genres(new HashSet<>())
                    .betaReader(false)
                    .location(locationService.createLocation("dummyland", "dummytown"))
                    .verified(true)
                    .build();

            baseUserRepository.save(reader);
            System.out.println("Create dummy reader 'reader1' with password 'password1'!");
        }
    }
}
