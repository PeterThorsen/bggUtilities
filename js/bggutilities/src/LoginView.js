import React, {Component} from 'react';
import App from './App';
import './LoginView.css';
import TextField from 'material-ui/TextField';
import RaisedButton from 'material-ui/RaisedButton';

class Question extends Component {

    constructor(props) {
        super(props);
        this.state = {
            showApp: false,
            userName: "cwaq",
            loginFailed: false
        };
    }

    render() {
        if (this.state.showApp) return <App loginFailed={this.loginFailed.bind(this)} userName={this.state.userName}/>;
        return <div className="login-view">
            <div className="login-view-main-content">
                <TextField id={"login-text-field"} onChange={(event, text) => this.saveText(text)} defaultValue={"cwaq"}/>
                <RaisedButton label="Start the app"
                              primary={true}
                              style={{marginTop: '40px'}}
                              onTouchTap={this.showApp.bind(this)}/>
                {this.state.loginFailed ? <div>Login failed! </div> : undefined}

            </div>
        </div>
    }

    showApp() {
        this.setState({
            showApp: true
        })
    }

    saveText(text) {
        this.setState({
            userName: text
        })
    }

    loginFailed() {
        this.setState({
            showApp: false,
            loginFailed: true,
            userName: ""
        })
    }
}

export default Question;
