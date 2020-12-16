import React from "react";
import { Switch, Route } from "react-router-dom";
import Register from "../Register/Register";

export const AppRouter = () => {
    return (
        <Switch>
            <Route path="/register" component={Register} />
            <Route path="*" component={() => <div>Page doesn't exist</div>} />
        </Switch>
    );
};
