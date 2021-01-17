import React, { useEffect } from 'react'
import ReaderRegistration from './components/ReaderRegistration'
import WriterRegistration from './components/WriterRegistration'
import { Switch, Route } from 'react-router-dom'
import Login from './components/Login'
import { useDispatch } from 'react-redux'
import { restore_login } from './reducers/userReducer'
import TaskList from './components/TaskList'
import { useHistory } from 'react-router-dom'

const App = () => {
    const dispatch = useDispatch()
    const history = useHistory()

    useEffect(() => {
        if (window.localStorage.getItem('token') !== null
            && window.localStorage.getItem('role') !== null
            && window.localStorage.getItem('subject') !== null) {
            dispatch(restore_login())
            history.push('/dashboard/tasks')
        } else {
            history.push('/login')
        }
    }, [dispatch, history])

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
                <Route path="/dashboard/tasks">
                    <TaskList/>
                </Route>
            </Switch>
        </div>
    )
}

export default App
