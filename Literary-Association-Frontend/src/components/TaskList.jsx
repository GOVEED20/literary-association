import React, { useEffect } from 'react'
import { useDispatch, useSelector } from 'react-redux'
import { ListGroup } from 'react-bootstrap'
import TaskListItem from './TaskListItem'
import { getActiveTasks } from '../reducers/userTaskReducer'

const TaskList = () => {
    const dispatch = useDispatch()

    const username = useSelector(state => state.user.subject)

    useEffect(() => {
        dispatch(getActiveTasks(username))
    }, [dispatch, username])

    const tasks = useSelector(state => state.userTasks)

    return (
        <ListGroup>
            {
                tasks.map(task =>
                    <ListGroup.Item key={task.id}>
                        <TaskListItem dueDate={task.dueDate} name={task.name}/>
                    </ListGroup.Item>)
            }
        </ListGroup>
    )
}

export default TaskList