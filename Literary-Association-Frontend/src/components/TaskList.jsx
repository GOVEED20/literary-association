import React from "react";
import {useSelector} from "react-redux";
import {ListGroup} from "react-bootstrap";
import TaskListItem from "./TaskListItem";

const TaskList = () => {
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