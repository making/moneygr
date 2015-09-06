import React from 'react';
import EventManager from './EventManager.js';
import App from './components/App.jsx';

React.render(
    <App events={new EventManager()} />,
    document.getElementById('example')
);