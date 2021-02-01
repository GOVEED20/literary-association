package goveed20.LiteraryAssociationApplication.model;

import goveed20.LiteraryAssociationApplication.model.enums.WorkingPaperStatus;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

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

    @ManyToOne
    private Writer writer;

    @Builder(builderMethodName = "bookBuilder")
    public Book(Long id, String file, String title, Genre genre, String synopsis, WorkingPaperStatus status, String ISBN, String keywords, String publisher, Integer publicationYear, Integer pages, String publicationPlace, Double price) {
        super(id, file, title, genre, synopsis, status);
        this.ISBN = ISBN;
        this.keywords = keywords;
        this.publisher = publisher;
        this.publicationYear = publicationYear;
        this.pages = pages;
        this.publicationPlace = publicationPlace;
        this.price = price;
    }
}
