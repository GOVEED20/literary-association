import React from 'react'
import { Button, Col, Form, Row } from 'react-bootstrap'
import axios from 'axios'

const FormField = ({ formField, onChange, value }) => {

    const download = async (downloadURL, title) => {
        await axios({
            url: downloadURL,
            method: 'GET',
            responseType: 'blob',
        }).then((response) => {
            const url = window.URL.createObjectURL(new Blob([response.data]))
            const link = document.createElement('a')
            link.href = url
            link.setAttribute('download', title + '.pdf')
            document.body.appendChild(link)
            link.click()
        })
    }

    const checkForButtons = (formField) => {
        if (formField.properties.buttons) {
            return (
                JSON.parse(formField.properties.buttons).map(button => {
                    return (
                        <Form.Group as={Row} key={button.id}>
                            <Col sm={3}>
                                <Button onClick={() => download(button.downloadURL, button.title)}>{button.label}</Button>
                            </Col>
                        </Form.Group>
                    )
                })
            )
        }
    }

    switch (formField.properties.type) {
    case 'select': {
        const nativeProps = {
            multiple: formField.properties.multiple,
            required: formField.properties.required
        }

        switch (formField.type.name) {
        case 'string': {
            return (
                <>{checkForButtons(formField)}
                    <Form.Group as={Row} controlId={formField.id}>
                        <Form.Label sm={1} column>{formField.label}</Form.Label>
                        <Col sm={3}>
                            <Form.Control as="select" {...nativeProps} onChange={onChange} value={value}>
                                <option key='empty' value=''>-</option>
                                {
                                    JSON.parse(formField.properties.options)
                                        .map((option) =>
                                            <option key={option.value} value={option.value}>{option.name}</option>
                                        )
                                }
                            </Form.Control>
                        </Col>
                    </Form.Group></>
            )
        }
        case 'enum': {
            return (
                <Form.Group as={Row} controlId={formField.id}>
                    <Form.Label sm={1} column>{formField.label}</Form.Label>
                    <Col sm={3}>
                        <Form.Control as="select" {...nativeProps} onChange={onChange} value={value}>
                            <option key='empty' value=''>-</option>
                            {
                                Object.keys(formField.type.values)
                                    .map((option) =>
                                        <option key={option}
                                            value={option}>{formField.type.values[option]}</option>
                                    )
                            }
                        </Form.Control>
                    </Col>
                </Form.Group>
            )
        }
        }
        break
    }
    case 'textarea': {
        return (
            <>{checkForButtons(formField)}
            <Form.Group as={Row} controlId={formField.id}>
                <Form.Label sm={1} column>{formField.label}</Form.Label>
                <Col sm={3}>
                    <Form.Control as='textarea' onChange={onChange} value={value} rows={formField.properties.rows}/>
                </Col>
            </Form.Group>
            </>
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
