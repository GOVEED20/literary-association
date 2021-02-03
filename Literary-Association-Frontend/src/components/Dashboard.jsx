import React, { useEffect } from 'react'
import { Redirect, Route, useHistory } from 'react-router-dom'
import TaskList from './TaskList'
import { useDispatch, useSelector } from 'react-redux'
import Navbar from './Navbar'
import { Spinner } from 'react-bootstrap'
import { restore_login } from '../reducers/userReducer'
import Task from './Task'
import MyBooks from './MyBooks'

const Dashboard = () => {
    const history = useHistory()
    const dispatch = useDispatch()

    const username = useSelector(state => state.user.subject)
    const role = useSelector(state => state.user.role)

    useEffect(() => {
        if (!username || !role) {
            if (!window.localStorage.getItem('token')
                || !window.localStorage.getItem('subject')
                || !window.localStorage.getItem('role')) {
                history.push('/login')
            } else {
                dispatch(restore_login()).then()
            }
        }
    }, [])

    if (!username || !role) {
        return (
            <Spinner className='d-flex flex-column align-items-center' animation="border" role="status">
                <span className="sr-only">Loading...</span>
            </Spinner>
        )
    }

    return (
        <div>
            <Navbar role={role}/>
            <div>
                <Route path='/dashboard/tasks/:id'>
                    <Task/>
                </Route>
                <Route exact path='/dashboard/tasks'>
                    <TaskList username={username}/>
                </Route>
                <Route path='/dashboard'>
                    <Redirect to='/dashboard/tasks'/>
                </Route>
                <Route path='/dashboard/my-books'>
                    <MyBooks/>
                </Route>
            </div>
        </div>
    )
}

export default Dashboard
