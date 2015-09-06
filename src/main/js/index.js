import React from 'react';
import HelloWorld from './components.js';
import EventManager from './eventmanager.js';

React.render(
    <HelloWorld events={new EventManager()} />,
    document.getElementById('example')
);