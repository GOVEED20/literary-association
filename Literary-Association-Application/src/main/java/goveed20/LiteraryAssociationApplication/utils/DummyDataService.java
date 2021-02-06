package goveed20.LiteraryAssociationApplication.utils;

import goveed20.LiteraryAssociationApplication.model.*;
import goveed20.LiteraryAssociationApplication.model.enums.GenreEnum;
import goveed20.LiteraryAssociationApplication.model.enums.UserRole;
import goveed20.LiteraryAssociationApplication.repositories.BaseUserRepository;
import goveed20.LiteraryAssociationApplication.repositories.BookRepository;
import goveed20.LiteraryAssociationApplication.repositories.GenreRepository;
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
    private GenreRepository genreRepository;

    @Autowired
    private BookRepository bookRepository;

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
                    .username("reader2")
                    .password(passwordEncoder.encode("password2"))
                    .name("reader2")
                    .surname("reader2")
                    .email("reader2@maildrop.cc")
                    .comments(new HashSet<>())
                    .transactions(new HashSet<>())
                    .genres(new HashSet<>())
                    .betaReader(false)
                    .location(locationService.createLocation("dummyland", "dummytown"))
                    .verified(true)
                    .build();

            baseUserRepository.save(reader);
            System.out.println("Create dummy reader 'reader2' with password 'password2'!");
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

        Book book = Book.bookBuilder()
                .ISBN("123412341234")
                .publisher("Carobna kljiga")
                .title("Kljiga")
                .publicationYear(1998)
                .keywords("kljucne reci")
                .pages(256)
                .publicationPlace("mesto publikacije")
                .genre(genreRepository.findByGenre(GenreEnum.COOKBOOKS))
                .synopsis("Sinobsis")
                .price(302.00)
                .file("Literary-Association-Application/src/main/resources/books/Kljiga.pdf")
                .build();
        book.setWriter(writer);
        bookRepository.save(book);

        Writer writer2 = Writer.writerBuilder()
                .role(UserRole.WRITER)
                .genres(new HashSet<>())
                .location(locationService.createLocation("Serbia", "Novi Sad"))
                .comments(new HashSet<>())
                .transactions(new HashSet<>())
                .verified(true)
                .membershipApproved(true)
                .workingPapers(new HashSet<>())
                .books(new HashSet<>())
                .username("lazata")
                .password(passwordEncoder.encode("Laza1997!"))
                .name("Lazo")
                .surname("Lazic")
                .email("lazata@maildrop.cc")
                .genres(new HashSet<>())
                .build();

        Book book2 = Book.bookBuilder()
                .ISBN("653515341234")
                .publisher("Simgidulum")
                .title("Tajtl")
                .publicationYear(2005)
                .keywords("kljucne reci")
                .pages(256)
                .publicationPlace("mesto publikacije")
                .genre(genreRepository.findByGenre(GenreEnum.COOKBOOKS))
                .synopsis("Sinobsis")
                .price(203.00)
                .file("Literary-Association-Application/src/main/resources/books/Tajtl.pdf")
                .build();
        book2.setWriter(writer2);
        bookRepository.save(book2);

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

        BaseUser editor2 = BaseUser.builder()
                .role(UserRole.EDITOR)
                .username("mujoalen")
                .password(passwordEncoder.encode("Editor1997!"))
                .email("editor2@maildrop.cc")
                .name("Mujo")
                .surname("Alen")
                .verified(true)
                .location(locationService.createLocation("Serbia", "Novi Sad"))
                .build();

        baseUserRepository.save(editor2);

        BaseUser editor3 = BaseUser.builder()
                .role(UserRole.EDITOR)
                .username("jurica")
                .password(passwordEncoder.encode("Editor1997!"))
                .email("editor3@maildrop.cc")
                .name("Jurica")
                .surname("Juric")
                .verified(true)
                .location(locationService.createLocation("Serbia", "Novi Sad"))
                .build();

        baseUserRepository.save(editor3);

        BaseUser editor4 = BaseUser.builder()
                .role(UserRole.EDITOR)
                .username("peronikic")
                .password(passwordEncoder.encode("Editor1997!"))
                .email("editor4@maildrop.cc")
                .name("Pero")
                .surname("Nikic")
                .verified(true)
                .location(locationService.createLocation("Serbia", "Novi Sad"))
                .build();

        baseUserRepository.save(editor4);

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
