import userTaskService from '../services/userTaskService'

export const getActiveTasks = (username) => {
    return async dispatch => {
        const userTasks = await userTaskService.getActiveTasks(username)

        dispatch({
            type: 'GET_ACTIVE_TASKS',
            userTasks
        })
    }
}

export const setTask = (id) => {
    return async dispatch => {
        const task = await userTaskService.getTask(id)

        dispatch({
            type: 'SET_TASK',
            task
        })
    }
}

const reducer = (state = { list: [], active: null }, action) => {
    switch (action.type) {
    case 'GET_ACTIVE_TASKS':
        return {
            ...state,
            list: action.userTasks
        }
    case 'SET_TASK':
        return {
            ...state,
            active: action.task
        }
    default:
        return state
    }
}

export default reducer