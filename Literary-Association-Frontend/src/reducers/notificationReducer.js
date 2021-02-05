export const setNotification = (notification, type, duration) => {
    return async dispatch => {
        clearTimeout()

        dispatch({
            type: 'SET_NOTIFICATION',
            notification,
            notificationType: type
        })

        setTimeout(() => dispatch({
            type: 'CLEAR_NOTIFICATION'
        }), duration)
    }
}

const reducer = (state = { notification: null, notificationType: null }, action) => {
    switch (action.type) {
    case 'SET_NOTIFICATION': {
        return {
            notification: action.notification,
            notificationType: action.notificationType
        }
    }
    case 'CLEAR_NOTIFICATION': {
        return {
            notification: null,
            notificationType: null
        }
    }
    default:
        return state
    }
}

export default reducer
