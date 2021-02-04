import userReducer from './reducers/userReducer'
import userTaskReducer from './reducers/userTaskReducer'
import bookReducer from './reducers/bookReducer'
import notificationReducer from './reducers/notificationReducer'
import { createStore, combineReducers, applyMiddleware } from 'redux'
import { composeWithDevTools } from 'redux-devtools-extension'
import thunk from 'redux-thunk'

const reducer = combineReducers({
    user: userReducer,
    userTasks: userTaskReducer,
    books: bookReducer,
    notification: notificationReducer
})

const rootReducer = (state, action) => {
    if (action.type === 'DESTROY_SESSION') {
        state = undefined
    }
    return reducer(state, action)
}

const store = createStore(
    rootReducer,
    composeWithDevTools(
        applyMiddleware(thunk)
    )
)

export default store