import React from "react";
import {Form} from 'react-bootstrap'

const FormField = ({formField, onChange, value}) => {
    switch (formField.properties.type) {
        case "select":
            return (
                <Form.Group controlId={formField.id}>
                    <Form.Label>{formField.label}</Form.Label>
                    <Form.Control as="select" {...formField.properties} onChange={onChange} value={value}>
                        {
                            JSON.parse(formField.properties.values)
                                .map((option) =>
                                    <option value={option.value}>{option.name}</option>
                                )
                        }
                    </Form.Control>
                </Form.Group>
            )
        default:
            return (
                <Form.Group controlId={formField.id}>
                    <Form.Label>{formField.label}</Form.Label>
                    <Form.Control {...formField.properties} onChange={onChange} value={value}/>
                </Form.Group>
            )
    }
}

export default FormField