import React from 'react'
import { useSelector } from 'react-redux'
import { Alert } from 'react-bootstrap'

const Toaster = () => {
    const notification = useSelector(state => state.notification)

    if (!notification.notification) {
        return null
    }

    const type = notification.notificationType === 'error' ? 'danger' : 'success'

    return (
        <Alert variant={type} style={{
            position: 'absolute',
            bottom: 0,
            right: '25%',
            left: '50%',
            marginLeft: '-150px',
            width: '350px'
        }}>
            {notification.notification}
        </Alert>
    )
}

export default Toaster
