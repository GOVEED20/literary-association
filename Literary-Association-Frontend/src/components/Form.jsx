import React, { useState } from 'react'
import { Button, Col, Form as BootstrapForm, Row } from 'react-bootstrap'
import FormField from './FormField'

const Form = ({ form, onSubmit }) => {
    const [state, setState] = useState(createFormState(form.formFields))

    const changeState = ({ target }) => {
        const newState = { ...state }
        if (Array.isArray(newState[target.id])) {
            if (target.value === '') {
                return
            }

            if (newState[target.id].includes(target.value)) {
                newState[target.id] = newState[target.id].filter(v => v !== target.value)
            } else {
                newState[target.id] = [...newState[target.id], target.value]
            }
        } else if (typeof newState[target.id] === 'boolean') {
            newState[target.id] = !newState[target.id]
        } else {
            newState[target.id] = target.value
        }
        setState(newState)
    }

    const toBase64 = (file) => new Promise((resolve) => {
        const reader = new FileReader()
        reader.readAsDataURL(file)
        reader.onload = () => resolve(reader.result)
    })

    const isVisible = (formField) => {
        return formField.properties.dependsOn === undefined ? true : state[formField.properties.dependsOn]
    }

    const encodeSingleField = async (field, state) => {
        const fileInput = document.getElementById(field.id)
        const encodedFiles = []

        for (let i = 0; i < fileInput.files.length; ++i) {
            encodedFiles.push(await toBase64(fileInput.files[i]))
        }

        state[field.id] = encodedFiles.join(' ')
    }

    const submit = (event) => {
        event.preventDefault()
        const preparedState = { ...state }

        Promise.all(form.formFields
            .filter(ff => ff.properties.type === 'file')
            .map(ff => encodeSingleField(ff, preparedState))
        ).then(() => onSubmit(preparedState))
    }

    return (
        <div>
            <BootstrapForm onSubmit={submit}>
                {
                    form.formFields.map((formField) =>
                        isVisible(formField) &&
                        <FormField key={formField.id} formField={formField} onChange={changeState}
                                   value={state[formField.id]}/>
                    )
                }
                <BootstrapForm.Group as={Row}>
                    <Col sm={{ span: 4, offset: 2 }}>
                        <Button size="lg" variant="primary" type="submit">Submit</Button>
                    </Col>
                </BootstrapForm.Group>
            </BootstrapForm>
        </div>
    )
}

const createFormState = (formFields) => {
    const reducer = (accumulator, currentValue) => {
        const field = currentValue.id
        let value = currentValue.defaultValue
        if (value === null) {
            switch (currentValue.properties.type) {
            case 'select':
                if (currentValue.properties.multiple) {
                    value = []
                } else {
                    value = ''
                }
                break
            case 'checkbox':
                value = false
                break
            default:
                value = ''
            }
        }
        const newAccumulator = { ...accumulator }
        newAccumulator[field] = value
        return newAccumulator
    }
    return formFields.reduce(reducer, {})
}

export default Form