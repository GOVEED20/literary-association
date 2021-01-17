import React, {useEffect} from 'react';
import ReaderRegistration from "./components/ReaderRegistration";
import WriterRegistration from "./components/WriterRegistration";
import {Switch, Route, Redirect} from 'react-router-dom';
import Login from "./components/Login";
import {useDispatch} from "react-redux";
import {restore_login} from "./reducers/userReducer";

const App = () => {
    const dispatch = useDispatch()

    useEffect(() => {
        if (window.localStorage.getItem('token') != null
            && window.localStorage.getItem('role') != null
            && window.localStorage.getItem('subject') != null) {
            dispatch(restore_login())
        }
    }, [])

    return (
        <div className="container">
            <Switch>
                <Route path="/login">
                    <Login/>
                </Route>
                <Route path="/reader-registration">
                    <ReaderRegistration/>
                </Route>
                <Route path="/writer-registration">
                    <WriterRegistration/>
                </Route>
                <Route path="*">
                    <Redirect to="/login"/>
                </Route>
            </Switch>
        </div>
    );
};

export default App;
