import React from 'react'
import ReaderRegistration from './components/ReaderRegistration'
import WriterRegistration from './components/WriterRegistration'
import { Switch, Route, Redirect } from 'react-router-dom'
import Login from './components/Login'
import TaskList from './components/TaskList'

const App = () => {
    return (
        <div className='container'>
            <Switch>
                <Route path='/login'>
                    <Login/>
                </Route>
                <Route path='/reader-registration'>
                    <ReaderRegistration/>
                </Route>
                <Route path='/writer-registration'>
                    <WriterRegistration/>
                </Route>
                <Route path='/dashboard/tasks'>
                    <TaskList/>
                </Route>
                <Route exact path='/'>
                    <Redirect to='/login'/>
                </Route>
            </Switch>
        </div>
    )
}

export default App
