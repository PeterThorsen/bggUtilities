import React, {Component} from 'react';

import '../Main.css';
import LoadingScreen from "../util/LoadingScreen";
import SpecifyGameNight from "./SpecifyGameNight";

class SuggestByMagic extends Component {

    constructor(props) {
        super(props);
        this.state = {
            dataSource: [],
            loadingSuggestion: false,
            suggestion: undefined,
        };
    }

    render() {
        return <div className="outer-block">
            <div className="main-width">
                {!this.state.suggestion ?
                    this.state.loadingSuggestion ? <LoadingScreen/> :
                    <SpecifyGameNight
                        download={this.downloadSuggestions.bind(this)}
                    />
                    : this.state.suggestion.map(
                        (suggestion) => {
                            return suggestion.game.name + "; "
                        }
                    )
                }
            </div>
        </div>
    }




    downloadSuggestions(playerNames, playTime) {
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
        request.open('GET', 'http://localhost:8080/getSuggestion?playTime=' + playTime + playersUrl, true);
        request.send(null);
        request.onreadystatechange = function () {
            if (request.readyState === 4 && request.status === 200) {
                var type = request.getResponseHeader('Content-Type');
                if (type.indexOf("text") !== 1) {
                    let result = request.responseText;
                    let jsonObj = JSON.parse(result);

                    this.setState({
                        loadingSuggestion: false,
                        displaySuggestions: true,
                        suggestion: jsonObj
                    })
                }
            }
        }.bind(this);
    }
}

export default SuggestByMagic;
