import React from 'react'

const TaskListItem = ({ name, dueDate }) => (
    <span>{name} <b>{dueDate === null ? 'no due date' : `due to ${dueDate}`}</b></span>
)

export default TaskListItem
