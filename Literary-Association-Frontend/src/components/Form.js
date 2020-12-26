import React, {useState} from "react";
import {Form as BootstrapForm} from 'react-bootstrap'
import FormField from "./FormField";

const createFormState = ({formFields}) => {
    const reducer = (accumulator, currentValue) => accumulator[currentValue.id] = currentValue.defaultValue;
    return formFields.reduce(reducer, {})
}

const Form = ({form}) => {
    const [state, setState] = useState(createFormState(form.formFields))

    const changeState = ({target}) => {
        console.log(target)
    }

    return (
        <BootstrapForm>
            {form.formFields.map((formField) =>
                <BootstrapForm.Row>
                    <FormField formField={formField} onChange={changeState} value={state[formField.id]}/>
                </BootstrapForm.Row>)}
        </BootstrapForm>
    )
}

export default Form