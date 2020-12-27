import React, {useState} from "react";
import {Button, Form as BootstrapForm} from 'react-bootstrap'
import FormField from "./FormField";

const Form = ({form, onSubmit}) => {
    const [state, setState] = useState(createFormState(form.formFields))

    const changeState = ({target}) => {
        if (target.value === "") {
            return
        }

        const newState = {...state}
        if (Array.isArray(newState[target.id])) {
            if (newState[target.id].includes(target.value)) {
                newState[target.id] = newState[target.id].filter(v => v !== target.value)
            } else {
                newState[target.id] = [...newState[target.id], target.value]
            }
        } else {
            newState[target.id] = target.value
        }
        setState(newState)
    }

    return (
        <BootstrapForm onSubmit={() => onSubmit(state)}>
            {form.formFields.map((formField) =>
                <BootstrapForm.Row key={formField.id}>
                    <FormField formField={formField} onChange={changeState} value={state[formField.id]}/>
                </BootstrapForm.Row>)}
            <Button variant="primary" type="submit">Submit</Button>
        </BootstrapForm>
    )
}

const createFormState = (formFields) => {
    const reducer = (accumulator, currentValue) => {
        const field = currentValue.id
        let value = currentValue.defaultValue;
        if (value == null && currentValue.properties.type === "select") {
            value = []
        }
        value = value == null ? "" : value
        const newAccumulator = {...accumulator}
        newAccumulator[field] = value
        return newAccumulator
    }
    return formFields.reduce(reducer, {})
}

export default Form