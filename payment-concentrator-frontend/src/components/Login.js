import React, {useState} from "react";
import {Button, Form as BootstrapForm} from "react-bootstrap";
import {login} from '../services/loginService'
import {useHistory} from 'react-router-dom'
import {h1Style, mainDivStyle, submitButtonStyle} from "../css/loginStyles";
import Toaster from "./Toaster";

const Login = () => {

    const history = useHistory()

    const [username, setUsername] = useState('')
    const [password, setPassword] = useState('')
    const [toastData, setToastData] = useState({
        show: false,
        message: '',
        type: '',
        color: ''
    })

    const closeToaster = (message, type) => {
        setTimeout(() => {
            setToastData({show: false, message: message, type: type, color: ''})
        }, 3000)
    }

    const onSubmit = async (e) => {
        e.preventDefault()
        login({username: username, password: password})
            .then(res => {
                history.push('/register-retailer')
            })
            .catch(error => {
                // setToastData({show: true, message: error.response.data, type: 'error', color: 'red'})
                // closeToaster(error.response.data, 'error')
                console.log(error)
            })
    }

    document.body.style.backgroundColor = "#010d3b"

    return (
        <div>
            <div className={"container"} style={mainDivStyle}>
                <BootstrapForm onSubmit={onSubmit}>
                    <h1 style={h1Style}>Login</h1>
                    <BootstrapForm.Group controlId="loginForm.username">
                        <BootstrapForm.Label>Username:</BootstrapForm.Label>
                        <BootstrapForm.Control type="text" placeholder="Enter username"
                                               onChange={({target}) => setUsername(target.value)}/>
                    </BootstrapForm.Group>
                    <BootstrapForm.Group controlId="loginForm.password">
                        <BootstrapForm.Label>Password:</BootstrapForm.Label>
                        <BootstrapForm.Control type="password" placeholder="Enter password"
                                               onChange={({target}) => setPassword(target.value)}/>
                    </BootstrapForm.Group>
                    <Button variant="primary" type="submit" style={submitButtonStyle}>Login</Button>
                </BootstrapForm>
            </div>
            {toastData.show ?
                <Toaster type={toastData.type} message={toastData.message} color={toastData.color}/> : null}
        </div>

    )
}

export default Login;