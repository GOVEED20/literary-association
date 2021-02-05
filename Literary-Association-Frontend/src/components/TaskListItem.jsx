import React from 'react'
import { Button, Col, Row } from 'react-bootstrap'
import { useHistory } from 'react-router-dom'

const TaskListItem = ({ name, dueDate, id }) => {
    const history = useHistory()

    const seeTaskDetails = () => history.push(`/dashboard/tasks/${id}`)

    return (
        <Row>
            <Col>{name}</Col>
            <Col>
                <b>{dueDate === null ? 'no due date' : `due to ${dueDate}`}</b>
            </Col>
            <Col>
                <Button onClick={seeTaskDetails}>Details</Button>
            </Col>
        </Row>
    )
}

export default TaskListItem
