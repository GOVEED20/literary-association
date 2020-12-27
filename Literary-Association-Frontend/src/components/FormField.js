import React from "react";
import {Form} from 'react-bootstrap'

const FormField = ({formField, onChange, value}) => {
    switch (formField.properties.type) {
        case "select":
            const nativeProps = {
                multiple: formField.properties.multiple,
                required: formField.properties.required
            }

            return (
                <Form.Row>
                    <Form.Group controlId={formField.id}>
                        <Form.Label>{formField.label}</Form.Label>
                        <Form.Control as="select" {...nativeProps} onChange={onChange} value={value}>
                            {
                                JSON.parse(formField.properties.options)
                                    .map((option) =>
                                        <option key={option.value} value={option.value}>{option.name}</option>
                                    )
                            }
                        </Form.Control>
                    </Form.Group>
                </Form.Row>
            )
        default:
            return (
                <Form.Row>
                    <Form.Group controlId={formField.id}>
                        <Form.Label>{formField.label}</Form.Label>
                        <Form.Control {...formField.properties} onChange={onChange} value={value}/>
                    </Form.Group>
                </Form.Row>
            )
    }
}

export default FormField