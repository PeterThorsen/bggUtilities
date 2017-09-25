import React, {Component} from 'react';
import CircularProgress from 'material-ui/CircularProgress';

class LoadingScreen extends Component {

    render() {
        return <div style={{
            display: 'flex', flexDirection: 'column', justifyContent: 'center', alignItems: 'center', height: '150px'
        }}>
            <CircularProgress/><p>Currently loading data from BoardGameGeek servers. This may take up to 30 seconds.</p>
        </div>
    }
}

export default LoadingScreen;
