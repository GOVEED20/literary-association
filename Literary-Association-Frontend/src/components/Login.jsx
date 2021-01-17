import React, {useState} from 'react';
import {Button, Form} from "react-bootstrap";
import {login} from "../reducers/userReducer";
import {useDispatch} from "react-redux";

const Login = () => {
    const formStyle = {
        width: "60%",
        marginLeft: "auto",
        marginRight: "auto",
        marginTop: "5%"
    }

    const dispatch = useDispatch()

    const [username, setUsername] = useState("")
    const [password, setPassword] = useState("")

    const submitForm = async () => {
        dispatch(login(username, password))
    }

    return (
        <Form style={formStyle} onSubmit={submitForm}>
            <h1>Login</h1>
            <Form.Group controlId="loginForm.username">
                <Form.Label>Username</Form.Label>
                <Form.Control type="text" placehold="Enter username"
                              onChange={({target}) => setUsername(target.value)}/>
            </Form.Group>
            <Form.Group controlId="loginForm.password">
                <Form.Label>Password</Form.Label>
                <Form.Control type="password" placehold="Enter password"
                              onChange={({target}) => setPassword(target.value)}/>
            </Form.Group>
            <Button variant="primary" type="submit">Submit</Button>
            <br/>
            <a href="/reader-registration">Reader registration</a>
            <br/>
            <a href="/writer-registration">Writer registration</a>
        </Form>
    )
}

export default Login