import React from 'react';
import ReactDOM from 'react-dom';
import LoginView from './LoginView';
import './index.css';
import MuiThemeProvider from 'material-ui/styles/MuiThemeProvider';
import injectTapEventPlugin from 'react-tap-event-plugin';
import registerServiceWorker from './registerServiceWorker';
import {BrowserRouter as Router} from "react-router-dom";

injectTapEventPlugin();
registerServiceWorker();

const ThemeApp = () => (
    <MuiThemeProvider>
        <Router>
            <LoginView/>
        </Router>
    </MuiThemeProvider>
);
ReactDOM.render(
    <ThemeApp/>,
    document.getElementById('root')
);