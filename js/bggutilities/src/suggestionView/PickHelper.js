import React, {Component} from 'react';

import '../Main.css';
import SpecifyGameNight from "./SpecifyGameNight";
import LoadingScreen from "../util/LoadingScreen";

class PickHelper extends Component {

    constructor(props) {
        super(props);
        this.state = {
            dataSource: [],
            loadingGames: false,
            nonPickedGames: undefined,
        };
    }

    render() {
        return <div className="outer-block">
            <div className="main-width">
                {!this.state.nonPickedGames ?
                    this.state.loadingGames ? <LoadingScreen/> :
                        <SpecifyGameNight
                            download={this.downloadGames.bind(this)}

                        />
                    : this.state.nonPickedGames.map(
                        (suggestion) => {
                            return suggestion.game.name + "; "
                        }
                    )
                }
            </div>
        </div>
    }

    downloadGames(playerNames, playTime) {
        // Set loading to true
        this.setState({
            loadingSuggestion: true,
        });

        var request = new XMLHttpRequest();
        request.timeout = 60000;
        let playersUrl = "";
        playerNames.forEach(
            (name) => {
                playersUrl += '&players=' + name;
            }
        )
        request.open('GET', 'http://localhost:8080/helpPickGameNight?playTime=' + playTime + playersUrl, true);
        request.send(null);
        request.onreadystatechange = function () {
            if (request.readyState === 4 && request.status === 200) {
                var type = request.getResponseHeader('Content-Type');
                if (type.indexOf("text") !== 1) {
                    let result = request.responseText;
                    let jsonObj = JSON.parse(result);
                    console.log(jsonObj)

                    this.setState({
                        loadingSuggestion: false,
                        displaySuggestions: true,
                        nonPickedGames: jsonObj
                    })
                }
            }
        }.bind(this);
    }
}

export default PickHelper;
