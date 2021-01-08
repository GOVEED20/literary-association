import React from "react";
import {Form} from 'react-bootstrap'

const FormField = ({formField}) => {
    return (
        <Form.Row>
                <Form.Label>{formField.name}</Form.Label>
                <Form.Control {...formField.validationConstraints} />
        </Form.Row>
    )
}

export default FormField