import React from 'react';
import ReactDOM from 'react-dom';
import App from './App';
import {BrowserRouter as Router} from 'react-router-dom';
// import {Provider} from 'react-redux';

// TODO: add Provider with store for redux
ReactDOM.render(
    <Router>
        <App/>
    </Router>,
    document.getElementById('root')
);
