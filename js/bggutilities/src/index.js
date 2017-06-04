import React from 'react';
import ReactDOM from 'react-dom';
import LoginView from './LoginView';
import './index.css';
import MuiThemeProvider from 'material-ui/styles/MuiThemeProvider';
import injectTapEventPlugin from 'react-tap-event-plugin';
import registerServiceWorker from './registerServiceWorker';

injectTapEventPlugin();
registerServiceWorker();

const ThemeApp = () => (
    <MuiThemeProvider>
        <LoginView />
    </MuiThemeProvider>
);
ReactDOM.render(
  <ThemeApp />,
  document.getElementById('root')
);