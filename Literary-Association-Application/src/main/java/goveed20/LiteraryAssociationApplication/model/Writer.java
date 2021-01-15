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
public class Writer extends BaseUser {
    @ElementCollection(targetClass = Genre.class)
    @JoinTable(name = "genres_writer", joinColumns = @JoinColumn(name = "writer_id"))
    @Column(nullable = false)
    private Set<Genre> genres;

    @Column(nullable = false)
    private Boolean membershipApproved;

    @OneToOne(cascade = CascadeType.ALL)
    private MembershipApplication membershipApplication;

    @OneToMany(cascade = CascadeType.ALL)
    private Set<WorkingPaper> workingPapers;

    @ManyToMany(cascade = CascadeType.PERSIST)
    private Set<Book> books;

    @Builder(builderMethodName = "writerBuilder")
    public Writer(Long id, String name, String surname, String email, String password, String username,
                  Boolean verified, UserRole role, Location location, Set<Genre> genres,
                  Boolean membershipApproved, MembershipApplication membershipApplication, Set<Transaction> transactions,
                  Set<Comment> comments, Set<WorkingPaper> workingPapers, Set<Book> books) {
        super(id, name, surname, email, password, username, verified, role, location, comments, transactions);
        this.genres = genres;
        this.membershipApproved = membershipApproved;
        this.membershipApplication = membershipApplication;
        this.workingPapers = workingPapers;
        this.books = books;
    }
}
