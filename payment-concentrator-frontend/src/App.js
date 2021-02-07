import Register from './components/Register'
import { Redirect, Switch } from 'react-router-dom'
import React from 'react'
import Login from './components/Login'
import { GuardedRoute, GuardProvider } from 'react-router-guards'

const App = () => {
    const role = JSON.parse(window.localStorage.getItem('role'))

    const requireRole = (to, from, next) => {
        if (to.meta.roles) {
            if (to.meta.roles.includes(role)) {
                next()
            }
            next.redirect(from)
        } else {
            next()
        }
    }

    return (
        <div className="container">
            <Switch>
                <GuardProvider guards={[requireRole]}>
                    <GuardedRoute path="/login">
                        <Login/>
                    </GuardedRoute>
                    <GuardedRoute path="/register-retailer" meta={{ roles: ['ADMIN'] }}>
                        <Register/>
                    </GuardedRoute>
                </GuardProvider>
            </Switch>
        </div>
    )
}

export default App
