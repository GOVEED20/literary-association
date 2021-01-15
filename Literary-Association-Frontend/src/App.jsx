import React from 'react';
import ReaderRegistration from "./components/ReaderRegistration";
import WriterRegistration from "./components/WriterRegistration";
import {Switch, Route, Redirect} from 'react-router-dom';
import Login from "./components/Login";

const App = () => {
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
