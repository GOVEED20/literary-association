package goveed20.LiteraryAssociationApplication.repositories;

import goveed20.LiteraryAssociationApplication.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {

    Optional<Book> findByTitle(String title);

    Optional<Book> findByISBN(String ISBN);

    List<Book> findDistinctByTitleIn(List<String> bookTitles);

    List<Book> findByTitleNotIn(List<String> bookTitles);

}
