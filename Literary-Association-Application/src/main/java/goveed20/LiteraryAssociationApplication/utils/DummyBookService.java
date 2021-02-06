package goveed20.LiteraryAssociationApplication.utils;

import goveed20.LiteraryAssociationApplication.model.*;
import goveed20.LiteraryAssociationApplication.model.enums.GenreEnum;
import goveed20.LiteraryAssociationApplication.model.enums.TransactionStatus;
import goveed20.LiteraryAssociationApplication.model.enums.UserRole;
import goveed20.LiteraryAssociationApplication.repositories.*;
import goveed20.LiteraryAssociationApplication.services.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Service
public class DummyBookService {
    private static final String booksFolder = "Literary-Association-Application/src/main/resources/books/";

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private RetailerRepository retailerRepository;

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private LocationService locationService;

    @Autowired
    private BaseUserRepository baseUserRepository;

    @EventListener(ApplicationReadyEvent.class)
    public void addDummyBooksAndTransaction() {
        if (bookRepository.findAll().isEmpty()) {
            if (genreRepository.findAll().isEmpty()) {
                Arrays.stream(GenreEnum.values()).forEach(e -> genreRepository.save(new Genre(null, e)));
            }

            Genre g1 = genreRepository.findByGenre(GenreEnum.ADVENTURE);

            Book b1 = Book.bookBuilder()
                    .file(String.format("%sUpravljanje digitalnim dokumentima.pdf", booksFolder))
                    .title("Upravljanje digitalnim dokumentima")
                    .synopsis("synopsis1")
                    .genre(g1)
                    .ISBN("0-3818-9816-4")
                    .keywords("burek,meso")
                    .publisher("FTN")
                    .publicationYear(2014)
                    .pages(240)
                    .publicationPlace("Novi Sad, Srbija")
                    .price(0.0)
                    .build();

            Genre g2 = genreRepository.findByGenre(GenreEnum.FANTASY);

            Book b2 = Book.bookBuilder()
                    .file(String.format("%sKrv vilenjaka.pdf", booksFolder))
                    .title("Krv vilenjaka")
                    .synopsis("synopsis1")
                    .genre(g2)
                    .ISBN("0-8823-8460-0")
                    .keywords("burek,sir")
                    .publisher("Carobna knjiga")
                    .publicationYear(2014)
                    .pages(317)
                    .price(16.0)
                    .publicationPlace("Beograd, Srbija")
                    .build();

            b1 = bookRepository.save(b1);
            b2 = bookRepository.save(b2);

            Genre g3 = genreRepository.findByGenre(GenreEnum.EROTIC);

            Book b3 = Book.bookBuilder()
                    .file(String.format("%sSistemi elektronskog poslovanja.pdf", booksFolder))
                    .title("Sistemi elektronskog poslovanja")
                    .synopsis("Sinobsis")
                    .genre(g3)
                    .ISBN("0-6918-9816-4")
                    .keywords("burek,sir")
                    .publisher("FTN")
                    .publicationYear(2015)
                    .pages(690)
                    .publicationPlace("Novi Sad, Srbija")
                    .price(30.0)
                    .build();

            b3 = bookRepository.save(b3);

            Retailer r = Retailer.builder()
                    .name("Laguna")
                    .email("laguna@maildrop.cc")
                    .books(new HashSet<>(Arrays.asList(b1, b2, b3)))
                    .build();

            retailerRepository.save(r);

            Reader reader = Reader.readerBuilder()
                    .role(UserRole.READER)
                    .username("reader1")
                    .password(passwordEncoder.encode("password1"))
                    .name("reader1")
                    .surname("reader1")
                    .email("reader1@maildrop.cc")
                    .comments(new HashSet<>())
                    .transactions(new HashSet<>())
                    .genres(new HashSet<>())
                    .betaReader(false)
                    .location(locationService.createLocation("dummyland", "dummytown"))
                    .verified(true)
                    .build();

            InvoiceItem item = InvoiceItem.builder().name(b3.getTitle())
                    .price(b3.getPrice()).quantity(1).itemID(b3.getId()).build();
            Set<InvoiceItem> items = new HashSet<>();
            items.add(item);

            Invoice invoice = Invoice.builder().retailer(r).invoiceItems(items).build();

            Transaction transaction = Transaction.builder().completedOn(new Date()).createdOn(new Date())
                    .initializedOn(new Date()).done(true).paidWith("bank-service").total(b3.getPrice()).invoice(invoice)
                    .status(TransactionStatus.COMPLETED).build();
            invoice.setTransaction(transaction);

            reader.getTransactions().add(transaction);
            baseUserRepository.save(reader);

            System.out.println("Created dummy retailer with 3 books and created 1 transaction!");
        }
    }
}
