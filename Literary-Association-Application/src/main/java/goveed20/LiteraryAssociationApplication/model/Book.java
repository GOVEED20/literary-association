package goveed20.LiteraryAssociationApplication.model;

import goveed20.LiteraryAssociationApplication.model.enums.WorkingPaperStatus;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Book extends WorkingPaper {
    @Column(nullable = false, unique = true)
    private String ISBN;

    @Column(nullable = false)
    private String keywords;

    @Column(nullable = false)
    private String publisher;

    @Column(nullable = false)
    private Integer publicationYear;

    @Column(nullable = false)
    private Integer pages;

    @Column(nullable = false)
    private String publicationPlace;

    @Column(nullable = false)
    private Double price;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Writer writer;

    @Column(nullable = false)
    private String additionalAuthors;

    @Builder(builderMethodName = "bookBuilder")
    public Book(Long id, String file, String title, Genre genre, String synopsis, WorkingPaperStatus status, String ISBN, String keywords, String publisher, Integer publicationYear, Integer pages, String publicationPlace, Double price, String additionalAuthors) {
        super(id, file, title, genre, synopsis, status);
        this.ISBN = ISBN;
        this.keywords = keywords;
        this.publisher = publisher;
        this.publicationYear = publicationYear;
        this.pages = pages;
        this.publicationPlace = publicationPlace;
        this.price = price;
        this.additionalAuthors = additionalAuthors;
    }
}
