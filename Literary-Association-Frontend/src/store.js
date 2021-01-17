import userReducer from './reducers/userReducer'
import userTaskReducer from './reducers/userTaskReducer'
import {createStore, combineReducers, applyMiddleware} from 'redux'
import {composeWithDevTools} from 'redux-devtools-extension'
import thunk from 'redux-thunk'

const reducer = combineReducers({
    user: userReducer,
    userTasks: userTaskReducer
})

const store = createStore(
    reducer,
    composeWithDevTools(
        applyMiddleware(thunk)
    )
)

export default store