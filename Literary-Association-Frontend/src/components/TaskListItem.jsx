import React from 'react'
import { Button, Col, Row } from 'react-bootstrap'

const TaskListItem = ({ name, dueDate }) => (
    <Row>
        <Col>{name}</Col>
        <Col>
            <b>{dueDate === null ? 'no due date' : `due to ${dueDate}`}</b>
        </Col>
        <Col>
            <Button>Details</Button>
        </Col>
    </Row>
)

export default TaskListItem
