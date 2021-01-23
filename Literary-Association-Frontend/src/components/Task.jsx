import React from 'react'
import { useRouteMatch } from 'react-router-dom'
import { useDispatch, useSelector } from 'react-redux'
import { setTask } from '../reducers/userTaskReducer'
import userTaskService from '../services/userTaskService'
import Form from './Form'

const Task = () => {
    const dispatch = useDispatch()
    const task = useSelector(state => state.userTasks.active)

    const idMatch = useRouteMatch('/dashboard/tasks/:id')
    idMatch && dispatch(setTask(idMatch.params.id))

    const onSubmit = async (state) => await userTaskService.submitTask(task.submitUrl, task.id, state)

    switch (task.type) {
    case 'FORM': {
        return (
            <Form form={task.formFields} onSubmit={onSubmit}/>
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