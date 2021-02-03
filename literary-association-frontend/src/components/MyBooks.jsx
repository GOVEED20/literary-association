import React from 'react'
import { startProcess } from '../services/processService'
import { Button, Col, Form, Row } from 'react-bootstrap'
import BookList from './BookList'

const MyBooks = () => {

    const startBookPublishingProcess = async () => {
        await startProcess('Book_publishing')
    }

    const startPlagiarismProcess = async () => {
        await startProcess('Plagiarism_process')
    }

    return (
        <div>
            <BookList myBooks={true}/>
            <Form.Group as={Row}>
                <Col sm={3}>
                    <Button variant="primary" type="submit" onClick={startBookPublishingProcess}>Add new book</Button>
                </Col>
            </Form.Group>
            <Form.Group as={Row}>
                <Col sm={3}>
                    <Button variant="primary" type="submit" onClick={startPlagiarismProcess}>Report plagiarism</Button>
                </Col>
            </Form.Group>
        </div>
    )
}

export default MyBooks
