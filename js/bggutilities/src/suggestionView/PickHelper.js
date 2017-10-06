import React, {Component} from 'react';

import '../Main.css';
import SpecifyGameNight from "./SpecifyGameNight";
import LoadingScreen from "../util/LoadingScreen";
import {Card, CardHeader, CardText} from 'material-ui/Card';
import {greenColors, redColors} from "../util/Colors";

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
        let averagePositiveValue = 0;

        if(this.state.nonPickedGames) {
            let counter = 0;
            this.state.nonPickedGames.forEach(
                (suggestion) => {
                    if (suggestion.value > 0) {
                        averagePositiveValue += suggestion.value;
                        counter++;
                    }
                }
            );

            averagePositiveValue = averagePositiveValue / counter;
        }

        return <div className="outer-block">
            <div className="main-width">
                {!this.state.nonPickedGames ?
                    this.state.loadingGames ? <LoadingScreen/> :
                        <SpecifyGameNight
                            download={this.downloadGames.bind(this)}

                        />
                    : this.state.nonPickedGames.map(
                        (suggestion, i) => {
                            suggestion.reasons.sort(
                                (reason, otherReason) =>
                                {
                                    return otherReason.value - reason.value;
                                });
                            return <Card style={{backgroundColor: suggestion.value > 0 ? suggestion.value > averagePositiveValue ? greenColors(i) : 'yellow' : redColors(i)}} key={"card-" + i}>
                                <CardHeader
                                    title={suggestion.game.name + " (" + suggestion.value + ")"}
                                    actAsExpander={true}
                                    showExpandableButton={true}
                                />
                                <CardText expandable={true}>
                                    {suggestion.reasons.map(
                                        (reason, j) => {
                                            return <Card key={"card-" + i + "-" + j}  style={{backgroundColor: suggestion.value > 0 ?greenColors(9999) : redColors(9999)}} >
                                                <CardHeader
                                                    title={reason.reason + " (" + reason.value + ")"}
                                                />
                                            </Card>
                                        })
                                    }

                                </CardText>
                            </Card>
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
