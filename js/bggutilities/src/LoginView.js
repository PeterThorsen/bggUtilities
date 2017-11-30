import React, {Component} from 'react';
import './LoginView.css';
import TextField from 'material-ui/TextField';
import RaisedButton from 'material-ui/RaisedButton';
import LoadingScreen from "./util/LoadingScreen";
import {withRouter} from "react-router-dom";

class LoginView extends Component {

    constructor(props) {
        super(props);
        this.state = {
            tryLogin: false,
            userName: "cwaq",
            loginFailed: false,
            result: undefined,
        };
    }

    render() {
        if (this.state.tryLogin) {
            this.tryLogin();
            return <LoadingScreen/>
        }
        if(this.state.result) {
            this.props.history.push("/")
        }

        return <div className="login-view">
            <div className="login-view-main-content">
                <TextField id={"login-text-field"} onChange={(event, text) => this.saveText(text)}
                           value={this.state.userName}/>
                <RaisedButton label="Start the app"
                              primary={true}
                              style={{marginTop: '40px'}}
                              onTouchTap={this.setTryLogin.bind(this)}/>
                {this.state.loginFailed ? <div>Login failed! </div> : undefined}

            </div>
        </div>
    }

    tryLogin() {
        if (this.state.userName.indexOf(' ') !== -1) {
            this.loginFailed();
        }
        else {
            var request = new XMLHttpRequest();
            request.timeout = 60000;
            request.open('GET', 'http://localhost:8080/login?userName=' + this.state.userName, true);
            request.send(null);
            request.onreadystatechange = function () {
                if (request.readyState === 4 && request.status === 200) {
                    var type = request.getResponseHeader('Content-Type');
                    if (type.indexOf("text") !== 1) {
                        let result = request.responseText === "true";
                        if (!result) {
                            this.loginFailed();
                        }
                        else {
                            this.loginSuccess();
                        }
                    }
                }
            }.bind(this);
        }
    }

    setTryLogin() {
        this.setState({
            tryLogin: true
        })
    }

    saveText(text) {
        this.setState({
            userName: text
        })
    }

    loginFailed() {
        this.setState({
            tryLogin: false,
            loginFailed: true,
            userName: "",
            result: undefined,
        })
    }

    loginSuccess() {
        this.setState({
            tryLogin: false,
            result: true,
            loginFailed: false
        })
    }
}

export default withRouter(LoginView);
