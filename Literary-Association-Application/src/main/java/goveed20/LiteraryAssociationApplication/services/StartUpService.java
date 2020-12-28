package goveed20.LiteraryAssociationApplication.services;

import goveed20.LiteraryAssociationApplication.model.Genre;
import goveed20.LiteraryAssociationApplication.model.enums.GenreEnum;
import goveed20.LiteraryAssociationApplication.repositories.GenreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class StartUpService {

    @Autowired
    private GenreRepository genreRepository;

    @EventListener(ApplicationReadyEvent.class)
    public void createDataOnStartUp() {
        if (genreRepository.findAll().isEmpty()) {
            Arrays.stream(GenreEnum.values()).forEach(e -> genreRepository.save(new Genre(null, e)));
        }
    }
}
