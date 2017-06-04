import React, {Component} from 'react';
import App from './App';
import './LoginView.css';
import TextField from 'material-ui/TextField';
import FloatingActionButton from 'material-ui/FloatingActionButton';
import ContentAdd from 'material-ui/svg-icons/content/add';
import RaisedButton from 'material-ui/RaisedButton';

class Question extends Component {

    constructor(props) {
        super(props);
        this.state = {
            showApp: false,
        };
    }

    render() {
        if (this.state.showApp) return <App teams={this.state.teams}/>;
        return <div className="login-view">
            <div className="login-view-main-content">
                <RaisedButton label="Start the app"
                              primary={true}
                              style={{marginTop: '40px'}}
                              onTouchTap={this.showApp.bind(this)}/>

            </div>
        </div>
    }

    showApp() {
        this.setState({
            showApp: true
        })
    }
}

export default Question;
