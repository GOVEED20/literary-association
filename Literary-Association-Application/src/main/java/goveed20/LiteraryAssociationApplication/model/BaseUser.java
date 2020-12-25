package goveed20.LiteraryAssociationApplication.model;

import goveed20.LiteraryAssociationApplication.model.enums.UserRole;
import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class BaseUser {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String surname;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private Boolean verified;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserRole role;

    @ManyToOne(cascade = CascadeType.ALL, optional = false)
    private Location location;

    @OneToMany(cascade = CascadeType.ALL)
    private Set<Comment> comments;

    @OneToMany(cascade = CascadeType.PERSIST)
    private Set<Transaction> transactions;
}
