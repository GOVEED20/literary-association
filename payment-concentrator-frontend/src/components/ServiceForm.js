import React from "react";
import FormField from "./FormField";

const ServiceForm = ({serviceName, formFields}) => {

    return (
        <div>
            {formFields?.map((formField) =>
                < FormField key={formField.name} formField={formField} />
            )}
        </div>
    )
}

export default ServiceForm;