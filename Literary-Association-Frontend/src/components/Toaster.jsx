import React from 'react'
import { useSelector } from 'react-redux'
import { Alert } from 'react-bootstrap'

const Toaster = () => {
    const notification = useSelector(state => state.notification)

    if (!notification) {
        return null
    }

    return (
        <Alert variant={notification.notificationType}>
            {notification.notification}
        </Alert>
    )
}

export default Toaster