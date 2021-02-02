package goveed20.LiteraryAssociationApplication.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import goveed20.LiteraryAssociationApplication.dtos.FormSubmissionFieldDTO;
import goveed20.LiteraryAssociationApplication.dtos.OptionDTO;
import goveed20.LiteraryAssociationApplication.model.BetaReaderStatus;
import goveed20.LiteraryAssociationApplication.model.Genre;
import org.camunda.bpm.engine.form.FormField;

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
        return gson.toJson(genres.stream()
                .map(g -> new OptionDTO(g.getGenre().serbianName, g.getGenre()))
                .sorted(Comparator.comparing(OptionDTO::getName))
                .collect(Collectors.toList()));
    }

    public static String serializeBetaReaders(HashSet<BetaReaderStatus> betaReaderStatuses) {
        Gson gson = new Gson();
        return gson.toJson(betaReaderStatuses.stream().map(b -> OptionDTO.builder().name(b.getReader().getName() + " " + b.getReader().getSurname())
                .value(b.getReader().getUsername())));
    }

    private static String serializeOptions(Set<String> options) {
        Gson gson = new Gson();
        return gson.toJson(options.stream().map(o -> OptionDTO.builder().name(o).value(o).build()));
    }

    public static Set<Genre> parseGenres(String genres) {
        if (genres.equals("")) {
            return new HashSet<>();
        }
        Gson gson = new Gson();
        Type genreSet = new TypeToken<Set<Genre>>() {}.getType();
        return gson.fromJson(genres, genreSet);
    }

    public static Set<String> parseBetaReaders(String betaReaders) {
        if (betaReaders.equals("")) {
            return new HashSet<>();
        }
        Gson gson = new Gson();
        Type betaReaderSet = new TypeToken<Set<String>>() {}.getType();
        return gson.fromJson(betaReaders, betaReaderSet);
    }

    public static void setOptions(String optionField, Set<String> options, List<FormField> properties) {
        properties.forEach(p -> {
            if (p.getId().equals(optionField)) {
                p.getProperties().put("options", UtilService
                        .serializeOptions(options));
            }
        });
    }
}
