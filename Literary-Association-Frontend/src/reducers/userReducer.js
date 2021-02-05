import loginService from '../services/loginService'
import jwt_decode from 'jwt-decode'

const local_login = (token, role, subject) => {
    window.localStorage.setItem('token', JSON.stringify(token))
    window.localStorage.setItem('role', JSON.stringify(role))
    window.localStorage.setItem('subject', JSON.stringify(subject))
}

const local_logout = () => {
    window.localStorage.removeItem('token')
    window.localStorage.removeItem('role')
    window.localStorage.removeItem('subject')
}

export const restore_login = () => {
    return async dispatch => {
        const token = JSON.parse(window.localStorage.getItem('token'))
        const role = JSON.parse(window.localStorage.getItem('role'))
        const subject = JSON.parse(window.localStorage.getItem('subject'))

        dispatch({
            type: 'LOGIN',
            token,
            role,
            subject
        })
    }
}

export const login = (username, password) => {
    return async dispatch => {
        const token = await loginService.login(username, password)
        const role = jwt_decode(token).role[0]
        const subject = jwt_decode(token).sub

        local_login(token, role, subject)

        dispatch({
            type: 'LOGIN',
            token,
            role,
            subject
        })
    }
}

export const logout = () => {
    return async dispatch => {
        local_logout()

        await loginService.logout()

        dispatch({
            type: 'LOGOUT'
        })

        dispatch({
            type: 'DESTROY_SESSION'
        })
    }
}

const reducer = (state = { token: null, role: null, subject: null }, action) => {
    switch (action.type) {
    case 'LOGIN':
        return {
            token: action.token,
            role: action.role,
            subject: action.subject
        }
    case 'LOGOUT':
        return {
            token: null,
            role: null,
            subject: null
        }
    default:
        return state
    }
}

export default reducer
