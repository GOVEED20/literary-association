package goveed20.LiteraryAssociationApplication.services;

import goveed20.LiteraryAssociationApplication.model.enums.GenreEnum;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class GenreService {

    public Set<String> getGenres() {
        return Stream.of(GenreEnum.values())
                .map(Enum::name).collect(Collectors.toSet());
    }
}
