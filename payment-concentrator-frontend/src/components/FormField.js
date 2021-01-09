import React from "react";
import {Form} from 'react-bootstrap'

const FormField = ({formField, onChange}) => {
    return (
        <Form.Row>
                <Form.Label>{formField.name}</Form.Label>
                <Form.Control {...formField.validationConstraints} onChange={onChange} id={formField.name}/>
        </Form.Row>
    )
}

export default FormField