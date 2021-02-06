import React from 'react'
import { startProcess } from '../services/processService'
import { Button, Col, Form, Row } from 'react-bootstrap'
import BookList from './BookList'
import { useHistory } from 'react-router-dom'
import { useSelector } from 'react-redux'

const MyBooks = () => {
    const history = useHistory()
    const role = useSelector(state => state.user.role)

    const startBookPublishingProcess = async () => {
        await startProcess('Book_publishing')
        history.push('/dashboard/tasks')
    }

    const startPlagiarismProcess = async () => {
        await startProcess('Plagiarism_process')
        history.push('/dashboard/tasks')
    }

    return (
        <div>
            <BookList myBooks={true}/>
            {role !== 'READER' ? <><Form.Group as={Row}>
                <Col sm={3}>
                    <Button variant="primary" type="submit" onClick={startBookPublishingProcess}>Add new book</Button>
                </Col>
            </Form.Group>
            <Form.Group as={Row}>
                <Col sm={3}>
                    <Button variant="primary" type="submit" onClick={startPlagiarismProcess}>Report plagiarism</Button>
                </Col>
            </Form.Group></> : null}
        </div>
    )
}

export default MyBooks
