import React from 'react';
import ReactDOM from 'react-dom';
import './index.css';
import MuiThemeProvider from 'material-ui/styles/MuiThemeProvider';
import injectTapEventPlugin from 'react-tap-event-plugin';
import registerServiceWorker from './registerServiceWorker';
import {BrowserRouter as Router} from "react-router-dom";
import MainRoutingComponent from "./MainRoutingComponent";

injectTapEventPlugin();
registerServiceWorker();

const ThemeApp = () => (
    <MuiThemeProvider>
        <Router>
            <MainRoutingComponent/>
        </Router>
    </MuiThemeProvider>
);
ReactDOM.render(
    <ThemeApp/>,
    document.getElementById('root')
);