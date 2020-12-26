import React from 'react';
import Register from "./components/Register";
import {Switch, Route} from 'react-router-dom';

const App = () => {
    return (
        <div className="container">
            <Switch>
                <Route path="/register">
                    <Register/>
                </Route>
                <Route path="*">
                    <div>Page doesn't exist</div>
                </Route>
            </Switch>
        </div>
    );
};

export default App;
