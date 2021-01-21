import React, { useEffect } from 'react'
import { useDispatch, useSelector } from 'react-redux'
import { ListGroup } from 'react-bootstrap'
import TaskListItem from './TaskListItem'
import { getActiveTasks } from '../reducers/userTaskReducer'

const TaskList = ({username}) => {
    const dispatch = useDispatch()

    useEffect(() => {
        dispatch(getActiveTasks(username))
    }, [dispatch, username])

    const tasks = useSelector(state => state.userTasks)

    return (
        <div>
            <h2>Active tasks</h2>
            <ListGroup>
                {
                    tasks.map(task =>
                        <ListGroup.Item key={task.id}>
                            <TaskListItem dueDate={task.dueDate} name={task.name}/>
                        </ListGroup.Item>)
                }
            </ListGroup>
        </div>
    )
}

export default TaskList