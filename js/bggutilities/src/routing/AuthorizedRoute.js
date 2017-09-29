import React, {Component} from 'react';
import {Redirect, Route} from "react-router-dom";
import LoadingScreen from "../util/LoadingScreen";

class AuthorizedRoute extends Component {
    componentWillMount() {
        this.verifyLogin();
    }

    constructor(props) {
        super(props);
        this.state = {
            isContactingServer: true,
            isLoggedIn: false
        };
    }

    render() {
        if (this.state.isContactingServer) {
            return <LoadingScreen/>
        }

        if(this.state.isLoggedIn) {
            return <Route path={this.props.path} component={this.props.component}/>
        }
        return <Redirect to={"/login"}/>
    }

    verifyLogin() {
        var request = new XMLHttpRequest();
        request.timeout = 60000;
        request.open('GET', 'http://localhost:8080/verifyLoggedIn', true);
        request.send(null);
        request.onreadystatechange = function () {
            if (request.readyState === 4 && request.status === 200) {
                var type = request.getResponseHeader('Content-Type');
                if (type.indexOf("text") !== 1) {
                    let result = request.responseText === "true";
                    this.setState({
                        isContactingServer:false,
                        isLoggedIn: result
                    });
                }
            }
        }.bind(this);
    }
}

export default AuthorizedRoute