import React from 'react'

const TaskListItem = ({ name, dueDate }) => (
    <span>{name} due to <b>{dueDate}</b></span>
)

export default TaskListItem
