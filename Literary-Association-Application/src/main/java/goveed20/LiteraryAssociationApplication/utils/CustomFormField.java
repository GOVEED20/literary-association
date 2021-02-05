package goveed20.LiteraryAssociationApplication.utils;

import lombok.*;
import org.camunda.bpm.engine.form.FormField;
import org.camunda.bpm.engine.form.FormFieldValidationConstraint;
import org.camunda.bpm.engine.form.FormType;
import org.camunda.bpm.engine.variable.value.TypedValue;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class CustomFormField implements FormField {
    private String id;
    private String label;
    private String typeName;
    private Map<String, String> properties;
    private List<FormFieldValidationConstraint> validationConstraints;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getLabel() {
        return label;
    }

    @Override
    public FormType getType() {
        return new FormType() {
            @Override
            public String getName() {
                return typeName;
            }

            @Override
            public Object getInformation(String s) {
                return null;
            }
        };
    }

    @Override
    public String getTypeName() {
        return typeName;
    }

    @Override
    public Object getDefaultValue() {
        return null;
    }

    @Override
    public TypedValue getValue() {
        return null;
    }

    @Override
    public List<FormFieldValidationConstraint> getValidationConstraints() {
        return validationConstraints;
    }

    @Override
    public Map<String, String> getProperties() {
        return properties;
    }

    @Override
    public boolean isBusinessKey() {
        return false;
    }
}
