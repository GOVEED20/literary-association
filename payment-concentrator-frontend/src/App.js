import Register from "./components/Register";
import {Switch, Route, Redirect} from "react-router-dom";
import React from "react";
import Login from "./components/Login";
import AdminDashboard from "./components/AdminDashboard";

const App = () => {
    return (
        <div className="container">
            <Switch>
                <Route path="/login">
                    <Login/>
                </Route>
                <Route path="/register">
                    <Register/>
                </Route>
                <Route path="/dashboard">
                    <AdminDashboard/>
                </Route>
                <Route exact path='/'>
                    <Redirect to='/login'/>
                </Route>
            </Switch>
        </div>
    );
};

export default App;
