/*eslint-disable no-unused-vars*/
import React, { useEffect } from 'react'
/*eslint-disable no-unused-vars*/
import { useDispatch } from 'react-redux'
import { logout } from '../reducers/userReducer'
import { useHistory } from 'react-router-dom'

const Logout = () => {
    const dispatch = useDispatch()
    const history = useHistory()

    useEffect(() => {
        dispatch(logout()).then(history.push('/login'))
    }, [dispatch])

    return null
}

export default Logout