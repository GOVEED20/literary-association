package goveed20.LiteraryAssociationApplication.model;

import goveed20.LiteraryAssociationApplication.model.enums.UserRole;
import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Reader extends BaseUser {
    @ElementCollection(targetClass = Genre.class)
    @JoinTable(name = "genres_reader", joinColumns = @JoinColumn(name = "reader_id"))
    @Column(nullable = false)
    private Set<Genre> genres;

    @Column(nullable = false)
    private Boolean betaReader;

    @OneToOne(cascade = CascadeType.ALL)
    private BetaReaderStatus betaReaderStatus;

    @Builder(builderMethodName = "readerBuilder")
    public Reader(Long id, String name, String surname, String email, String password, String username, Boolean verified,
                  UserRole role, Location location, Set<Comment> comments, Set<Transaction> transactions,
                  Set<Genre> genres, Boolean betaReader, BetaReaderStatus betaReaderStatus) {
        super(id, name, surname, email, password, username, verified, role, location, comments, transactions);
        this.genres = genres;
        this.betaReader = betaReader;
        this.betaReaderStatus = betaReaderStatus;
    }
}
