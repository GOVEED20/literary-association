package goveed20.LiteraryAssociationApplication.utils;

import goveed20.LiteraryAssociationApplication.model.Book;
import goveed20.LiteraryAssociationApplication.model.Genre;
import goveed20.LiteraryAssociationApplication.model.Retailer;
import goveed20.LiteraryAssociationApplication.model.enums.GenreEnum;
import goveed20.LiteraryAssociationApplication.repositories.BookRepository;
import goveed20.LiteraryAssociationApplication.repositories.GenreRepository;
import goveed20.LiteraryAssociationApplication.repositories.RetailerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;

@Service
public class DummyBookService {
    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private RetailerRepository retailerRepository;

    @Autowired
    private GenreRepository genreRepository;

    private static final String booksFolder = "Literary-Association-Application/src/main/resources/books/";

    @EventListener(ApplicationReadyEvent.class)
    public void addDummyBooks() {
        if (bookRepository.findAll().isEmpty()) {
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
                    .price(1400.0)
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
                    .price(1600.0)
                    .publicationPlace("Beograd, Srbija")
                    .build();

            b1 = bookRepository.save(b1);
            b2 = bookRepository.save(b2);

            Retailer r = Retailer.builder()
                    .name("Laguna")
                    .books(new HashSet<>(Arrays.asList(b1, b2)))
                    .build();

            retailerRepository.save(r);

            System.out.println("Created dummy retailer with 2 books!");
        }
    }

}
