import React from "react";
import FormField from "./FormField";

const ServiceForm = ({serviceName, formFields, onChange}) => {

    return (
        <div>
            {formFields?.map((formField) =>
                < FormField key={formField.name} formField={formField} onChange={onChange}/>
            )}
        </div>
    )
}

export default ServiceForm;