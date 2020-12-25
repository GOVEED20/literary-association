package goveed20.LiteraryAssociationApplication.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import goveed20.LiteraryAssociationApplication.dtos.FormSubmissionFieldDTO;
import goveed20.LiteraryAssociationApplication.model.Genre;
import goveed20.LiteraryAssociationApplication.model.enums.GenreEnum;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UtilService {

    public static HashMap<String, Object> mapListToDto(List<FormSubmissionFieldDTO> list)
    {
        HashMap<String, Object> map = new HashMap<>();
        for(FormSubmissionFieldDTO temp : list){
            map.put(temp.getFieldId(), temp.getFieldValue());
        }

        return map;
    }

    public static String serializeGenres(Set<Genre> genres)
    {
        Gson gson = new Gson();
        return gson.toJson(genres);
    }

    public static Set<Genre> parseGenres(String genres) {
        if (genres.equals("")) {
            return new HashSet<>();
        }
        Gson gson = new Gson();
        Type genreSet = new TypeToken<Set<Genre>>(){}.getType();
        return gson.fromJson(genres, genreSet);
    }
}
