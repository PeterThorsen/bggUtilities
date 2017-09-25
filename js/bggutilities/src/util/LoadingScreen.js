import React, {Component} from 'react';
import CircularProgress from 'material-ui/CircularProgress';
import './LoadingScreen.css';

class LoadingScreen extends Component {

    render() {
        return <div className={"loading-screen"}>
            <CircularProgress/><p>Currently loading data from BoardGameGeek servers. This may take up to 30 seconds.</p>
        </div>
    }
}

export default LoadingScreen;
