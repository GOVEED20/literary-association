import loginService from '../services/loginService'
import * as jwt_decode from 'jwt-decode'

const local_login = (token, role, subject) => {
    window.localStorage.setItem('token', token)
    window.localStorage.setItem('role', role)
    window.localStorage.setItem('subject', subject)
}

const local_logout = () => {
    window.localStorage.removeItem('token')
    window.localStorage.removeItem('role')
    window.localStorage.removeItem('subject')
}

export const restore_login = () => {
    return async dispatch => {
        const token = window.localStorage.getItem('token')
        const role = window.localStorage.getItem('role')
        const subject = window.localStorage.getItem('subject')

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
        const role = jwt_decode(token).role
        const subject = jwt_decode(token).subject

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
    }
}

const reducer = (state = {token: null, role: null, subject: null}, action) => {
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
