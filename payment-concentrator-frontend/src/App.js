import Register from "./components/Register";
import {Redirect, Route, Switch} from "react-router-dom";
import React from "react";
import Login from "./components/Login";

const App = () => {
    return (
        <div className="container">
            <Switch>
                <Route path="/login">
                    <Login/>
                </Route>
                <Route path="/register-retailer">
                    <Register/>
                </Route>
                <Route exact path='/'>
                    <Redirect to='/login'/>
                </Route>
                <Route exact path='*'>
                    <Redirect to='/login'/>
                </Route>
            </Switch>
        </div>
    );
};

export default App;
