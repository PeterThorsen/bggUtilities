import React, {Component} from 'react';
import './App.css';
import LoadingScreen from './LoadingScreen';
import MainView from './MainView';

class App extends Component {

    constructor(props) {
        super(props);
        this.state = {found: false, result: ""}
    }


    render() {

        if (!this.state.found) {
            if(this.props.userName.indexOf(' ') !== -1) {
                this.props.loginFailed();
            }
            else {
                var request = new XMLHttpRequest();
                request.timeout = 60000;
                request.open('GET', 'http://localhost:8080/login?userName=' + this.props.userName, true);
                request.send(null);
                request.onreadystatechange = function () {
                    if (request.readyState === 4 && request.status === 200) {
                        var type = request.getResponseHeader('Content-Type');
                        if (type.indexOf("text") !== 1) {
                            let result = request.responseText;
                            this.setState({found: true, result: result === "true"});
                        }
                    }
                }.bind(this);
            }
        }
        if(this.state.found && this.state.result === false) {
            this.props.loginFailed();
        }


        return <div className="App"> {this.state.found ? this.state.result ? <MainView userName={this.props.userName}/> :
            <LoadingScreen/> :<LoadingScreen/>}
        </div>
    }
}

export default App;
