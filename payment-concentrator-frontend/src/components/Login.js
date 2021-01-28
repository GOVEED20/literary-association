import React, { useState } from "react";
import {Button, Form as BootstrapForm} from "react-bootstrap";
import { login } from '../services/loginService'
import { useHistory } from 'react-router-dom'

const Login = () => {

    const history = useHistory()

    const [username, setUsername] = useState('')
    const [password, setPassword] = useState('')

    const onSubmit = async (e) => {
        e.preventDefault()
        login({username: username, password: password})
            .then(res => {
                history.push('/dashboard')
            })
            .catch(error => {
                console.log(error)
            })
    }

    return (
        <div>
            <div className={"container"}>
                <BootstrapForm onSubmit={onSubmit}>
                    <h1>Login</h1>
                    <BootstrapForm.Group controlId="loginForm.username">
                        <BootstrapForm.Label>Username</BootstrapForm.Label>
                        <BootstrapForm.Control type="text" placeholder="Enter username"
                                      onChange={({ target }) => setUsername(target.value)}/>
                    </BootstrapForm.Group>
                    <BootstrapForm.Group controlId="loginForm.password">
                        <BootstrapForm.Label>Password</BootstrapForm.Label>
                        <BootstrapForm.Control type="password" placeholder="Enter password"
                                      onChange={({ target }) => setPassword(target.value)}/>
                    </BootstrapForm.Group>
                    <Button variant="primary" type="submit">Submit</Button>
                </BootstrapForm>
            </div>
        </div>

    )
}

export default Login;