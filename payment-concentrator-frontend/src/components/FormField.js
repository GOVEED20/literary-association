import React from "react";
import {Form} from 'react-bootstrap'
import {formFieldStyle} from "../css/registerStyles";

const FormField = ({formField, onChange}) => {
    return (
        <Form.Row style={formFieldStyle}>
                <Form.Label>{formField.name}</Form.Label>
                <Form.Control {...formField.validationConstraints}
                              onChange={onChange}
                              id={formField.name} />
        </Form.Row>
    )
}

export default FormField