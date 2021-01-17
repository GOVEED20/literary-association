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

const reducer = (state = [], action) => {
    switch (action.type) {
        case 'GET_ACTIVE_TASKS':
            return action.userTasks
        default:
            return state
    }
}

export default reducer