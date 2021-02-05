package goveed20.LiteraryAssociationApplication.repositories;

import goveed20.LiteraryAssociationApplication.model.ApplicationPaper;
import goveed20.LiteraryAssociationApplication.model.Comment;
import goveed20.LiteraryAssociationApplication.model.enums.CommentType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByTypeAndApplicationPapersContaining(CommentType type, ApplicationPaper applicationPaper);
}
