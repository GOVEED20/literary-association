package goveed20.LiteraryAssociationApplication.repositories;

import goveed20.LiteraryAssociationApplication.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface BookRepository extends JpaRepository<Book, Long> {
    Optional<Book> findByTitle(String title);

    Optional
<Book> findByISBN(String ISBN);

    List<Book> findByTitleIn(Set<String> bookTitles);

    List<Book> findByTitleNotIn(Set<String> bookTitles);

}
