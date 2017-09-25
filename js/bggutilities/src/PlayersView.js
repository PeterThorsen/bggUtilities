import React, {Component} from 'react';
import LoadingScreen from './util/LoadingScreen';
import RaisedButton from 'material-ui/RaisedButton';

class PlayersView extends Component {

    constructor(props) {
        super(props);
        this.state = {
            found: false,
            result: undefined,
            player: undefined,
        }
    }

    render() {
        let mainBlock = <div/>;

        if (!this.state.found) {
            this.getPlayers();
            mainBlock = <LoadingScreen/>
        }
        return <div>
            <RaisedButton label="Go back" onTouchTap={this.props.goBack}/>
            {mainBlock}
        </div>
    }
    getPlayers() {
        var request = new XMLHttpRequest();
        request.timeout = 60000;
        request.open('GET', 'http://localhost:8080/getAllPlayers', true);
        request.send(null);
        request.onreadystatechange = function () {
            if (request.readyState === 4 && request.status === 200) {
                var type = request.getResponseHeader('Content-Type');
                if (type.indexOf("text") !== 1) {
                    let result = request.responseText;
                    let jsonObj = JSON.parse(result);
                    this.setState({found: true, result: jsonObj});
                }
            }
        }.bind(this);
    }

    goToPlayer(player) {
        this.setState({
            player: player
        })
    }
}


export default PlayersView;
