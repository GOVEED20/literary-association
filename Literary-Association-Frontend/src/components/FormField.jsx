import React from 'react'
import { Col, Form, Row } from 'react-bootstrap'

const FormField = ({ formField, onChange, value }) => {
    switch (formField.properties.type) {
    case 'select': {
        const nativeProps = {
            multiple: formField.properties.multiple,
            required: formField.properties.required
        }

        return (
            <Form.Group as={Row} controlId={formField.id}>
                <Form.Label sm={1} column>{formField.label}</Form.Label>
                <Col sm={3}>
                    <Form.Control as="select" {...nativeProps} onChange={onChange} value={value}>
                        {
                            JSON.parse(formField.properties.options)
                                .map((option) =>
                                    <option key={option.value} value={option.value}>{option.name}</option>
                                )
                        }
                    </Form.Control>
                </Col>
            </Form.Group>
        )
    }
    default: {
        return (
            <Form.Group as={Row} controlId={formField.id}>
                <Form.Label sm={1} column>{formField.label}</Form.Label>
                <Col sm={3}>
                    <Form.Control {...formField.properties} onChange={onChange} value={value}/>
                </Col>
            </Form.Group>
        )
    }
    }
}

export default FormField