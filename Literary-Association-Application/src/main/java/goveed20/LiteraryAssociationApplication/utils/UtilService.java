package goveed20.LiteraryAssociationApplication.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import goveed20.LiteraryAssociationApplication.dtos.FormSubmissionFieldDTO;
import goveed20.LiteraryAssociationApplication.dtos.OptionDTO;
import goveed20.LiteraryAssociationApplication.model.Genre;

import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;

public class UtilService {

    public static Map<String, Object> mapListToDto(List<FormSubmissionFieldDTO> list) {
        return list.stream()
                .collect(
                        Collectors.toMap(
                                FormSubmissionFieldDTO::getFieldId,
                                FormSubmissionFieldDTO::getFieldValue
                        )
                );
    }

    public static String serializeGenres(Set<Genre> genres) {
        Gson gson = new Gson();
        return gson.toJson(genres.stream().map(g -> new OptionDTO(g.getGenre().serbianName, g.getGenre()))
                .collect(Collectors.toList()));
    }

    public static Set<Genre> parseGenres(String genres) {
        if (genres.equals("")) {
            return new HashSet<>();
        }
        Gson gson = new Gson();
        Type genreSet = new TypeToken<Set<Genre>>() {
        }.getType();
        return gson.fromJson(genres, genreSet);
    }
}
