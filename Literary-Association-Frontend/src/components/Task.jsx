import React, { useEffect } from 'react'
import { useRouteMatch } from 'react-router-dom'
import { useDispatch, useSelector } from 'react-redux'
import { setTask } from '../reducers/userTaskReducer'
import userTaskService from '../services/userTaskService'
import Form from './Form'
import { Col, Spinner } from 'react-bootstrap'
import PdfViewer from './PdfViewer'

const Task = () => {
    const dispatch = useDispatch()
    const task = useSelector(state => state.userTasks.active)

    const idMatch = useRouteMatch('/dashboard/tasks/:id')

    useEffect(() => {
        if (!task) {
            idMatch && dispatch(setTask(idMatch.params.id))
        }
    }, [])

    const onSubmit = async (state) => await userTaskService.submitTask(task.id, state)

    if (!task) {
        return (
            <Spinner animation="border" role="status">
                <span className="sr-only">Loading...</span>
            </Spinner>
        )
    }

    switch (task.type) {
    case 'FORM': {
        return (
            <div>
                <h2>{task.name}</h2>
                {task.documents && task.documents.map((d, index) => <Col key={index}><PdfViewer pdf={d}/>&nbsp;</Col>)}
                <Form form={task} onSubmit={onSubmit}/>
            </div>
        )
    }
    case 'PAYMENT': {
        return (
            <p>PAYMENT</p>
        )
    }
    default: {
        return null
    }
    }
}

export default Task