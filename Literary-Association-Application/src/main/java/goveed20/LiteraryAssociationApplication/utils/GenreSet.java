package goveed20.LiteraryAssociationApplication.utils;

import goveed20.LiteraryAssociationApplication.model.Genre;
import org.camunda.bpm.engine.ProcessEngineException;
import org.camunda.bpm.engine.impl.form.type.AbstractFormFieldType;
import org.camunda.bpm.engine.variable.Variables;
import org.camunda.bpm.engine.variable.value.TypedValue;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class GenreSet extends AbstractFormFieldType {

    @Override
    public String getName() {
        return "enumSet";
    }

    @SuppressWarnings("unchecked")
    @Override
    public TypedValue convertToFormValue(TypedValue typedValue) {
        if (typedValue.getValue() == null) {
            return Variables.stringValue(null, typedValue.isTransient());
        } else {
            String value = String.join(",", (String[]) List.copyOf((Set<Genre>) typedValue.getValue())
                    .stream().map(e -> e.toString().toLowerCase()).toArray());
            return Variables.stringValue(value, typedValue.isTransient());
        }
    }

    @Override
    public TypedValue convertToModelValue(TypedValue typedValue) {
        Object value = typedValue.getValue();
        if (value == null) {
            return Variables.untypedNullValue();
        } else {
            String[] strValues = ((String) value).trim().split(",");
            if (strValues.length == 0) {
                return Variables.untypedNullValue();
            }
            try {
                Set<Genre> genreValues = Arrays.stream(strValues).map(Genre::valueOf).collect(Collectors.toSet());
                return Variables.untypedValue(genreValues, typedValue.isTransient());
            } catch (Exception e) {
                throw new ProcessEngineException("Could not parse value '" + value + "' as GenreSet.");
            }
        }
    }

    @Override
    public Object convertFormValueToModelValue(Object o) {
        return null;
    }

    @Override
    public String convertModelValueToFormValue(Object o) {
        return null;
    }
}
