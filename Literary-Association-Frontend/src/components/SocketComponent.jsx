import React from 'react'
import StompClient from 'react-stomp-client'
import { useDispatch } from 'react-redux'
import { setNotification } from '../reducers/notificationReducer'
import { useHistory } from 'react-router-dom'

const SocketComponent = () => {
    const dispatch = useDispatch()
    const history = useHistory()

    const handleNotification = (notification) => {
        dispatch(setNotification(JSON.parse(notification.body).notification,
            JSON.parse(notification.body).notificationType, 3500))
        history.push('/dashboard/tasks')
    }

    return (
        <StompClient
            endpoint="ws://localhost:9090/api/notificationEndpoint"
            topic="notification"
            onMessage={handleNotification}
        ><div></div>
        </StompClient>
    )
}

export default SocketComponent
