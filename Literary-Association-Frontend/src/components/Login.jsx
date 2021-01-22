import React, { useEffect, useState } from 'react'
import { Button, Form } from 'react-bootstrap'
import { login, restore_login } from '../reducers/userReducer'
import { useDispatch } from 'react-redux'
import { Link, useHistory } from 'react-router-dom'

const Login = () => {
    const formStyle = {
        width: '60%',
        marginLeft: 'auto',
        marginRight: 'auto',
        marginTop: '5%'
    }

    const dispatch = useDispatch()
    const history = useHistory()

    const [username, setUsername] = useState('')
    const [password, setPassword] = useState('')

    useEffect(() => {
        if (window.localStorage.getItem('token') !== null
            && window.localStorage.getItem('role') !== null
            && window.localStorage.getItem('subject') !== null) {
            dispatch(restore_login()).then(() => history.push('/dashboard'))
        }
    }, [dispatch, history])

    const submitForm = async (event) => {
        event.preventDefault()
        await dispatch(login(username, password))
        history.push('/dashboard')
    }

    /* eslint-disable indent*/
    return (
        <Form style={formStyle} onSubmit={submitForm}>
            <h1>Login</h1>
            <Form.Group controlId="loginForm.username">
                <Form.Label>Username</Form.Label>
                <Form.Control type="text" placehold="Enter username"
                              onChange={({ target }) => setUsername(target.value)}/>
            </Form.Group>
            <Form.Group controlId="loginForm.password">
                <Form.Label>Password</Form.Label>
                <Form.Control type="password" placehold="Enter password"
                              onChange={({ target }) => setPassword(target.value)}/>
            </Form.Group>
            <Button variant="primary" type="submit">Submit</Button>
            <br/>
            <Link to="/reader-registration">Reader registration</Link>
            <br/>
            <Link to="/writer-registration">Writer registration</Link>
        </Form>
    )
}

export default Login