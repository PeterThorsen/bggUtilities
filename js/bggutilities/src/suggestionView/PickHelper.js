import React, {Component} from 'react';

import '../Main.css';
import '../PickHelper.css';
import SpecifyGameNight from "./SpecifyGameNight";
import LoadingScreen from "../util/LoadingScreen";
import {Card, CardHeader, CardText} from 'material-ui/Card';
import {greenColors, redColors} from "../util/Colors";
import RaisedButton from 'material-ui/RaisedButton';
import ChosenGamesSideBar from "./ChosenGamesSideBar";

class PickHelper extends Component {

    constructor(props) {
        super(props);
        this.state = {
            dataSource: [],
            loadingGames: false,
            nonPickedGames: undefined,
            playerNames: undefined,
            playTime: undefined,
            allSelectedGames: [],
            averagePositiveValue: 0
        };
    }

    render() {

        return <div className="outer-block">
            {!this.state.nonPickedGames ?
                this.state.loadingGames ? <LoadingScreen/> :
                    <SpecifyGameNight
                        download={this.downloadGames.bind(this)}

                    />
                : <div className="pick-helper-outer">
                    <div className="suggestions-block"
                         hidden={!this.state.nonPickedGames || this.state.nonPickedGames.length === 0 || this.state.nonPickedGames[0].value <= this.state.averagePositiveValue}>
                        {this.state.nonPickedGames.map(
                            (suggestion, i) => {
                                suggestion.reasons.sort(
                                    (reason, otherReason) => {
                                        return otherReason.value - reason.value;
                                    });
                                return <Card
                                    style={{backgroundColor: suggestion.value > 0 ? suggestion.value > this.state.averagePositiveValue ? greenColors(i) : '#FFFF46' : redColors(i)}}
                                    key={"card-" + i}>
                                    <CardHeader
                                        title={suggestion.game.name + " (" + suggestion.value.toFixed(2) + ")"}
                                        actAsExpander={true}
                                        showExpandableButton={true}
                                    />
                                    <CardText expandable={true}>
                                        {suggestion.reasons.map(
                                            (reason, j) => {
                                                return <Card key={"card-" + i + "-" + j}
                                                             style={{backgroundColor: reason.value > 0 ? '#EDFFBB' : reason.value === 0 ? '#FFC300' : '#FF5050'}}>
                                                    <CardHeader
                                                        title={reason.reason + " (" + reason.value.toFixed(2) + ")"}
                                                    />
                                                </Card>
                                            })
                                        }
                                        <RaisedButton label="Pick this game"
                                                      primary={true}
                                                      style={{marginTop: '40px'}}
                                                      onTouchTap={() => this.updatePickedGames(suggestion)}/>

                                    </CardText>
                                </Card>
                            })}
                    </div>
                    {this.renderChosenGamesSideBar()}
                </div>
            }
        </div>
    }

    downloadGames(playerNames, playTime) {
        let playersUrl = "";
        playerNames.forEach(
            (name) => {
                playersUrl += '&players=' + name;
            }
        );

        // Set loading to true
        this.setState({
            loadingSuggestion: true,
            playerNames: playersUrl,
            playTime: playTime
        });

        var request = new XMLHttpRequest();
        request.timeout = 60000;

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
                        nonPickedGames: jsonObj,
                        averagePositiveValue: this.calculateAveragePositiveValue(jsonObj)
                    })
                }
            }
        }.bind(this);
    }

    updatePickedGames(suggestion) {
        let allSelectedGames = this.state.allSelectedGames;
        allSelectedGames.push(suggestion);

        let newPlayTime = parseFloat(this.state.playTime - suggestion.approximateTime).toFixed(0);
        this.setState({
            loadingSuggestion: true,
            allSelectedGames: allSelectedGames,
            playTime: newPlayTime
        });
        let idUrl = "";
        allSelectedGames.forEach(
            (suggestion) => {
                idUrl += '&gameIds=' + suggestion.game.id;
            }
        );


        var request = new XMLHttpRequest();
        request.timeout = 60000;
        request.open('GET', 'http://localhost:8080/updatePickedGameNight?playTime=' + newPlayTime + this.state.playerNames + idUrl, true);
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
                        nonPickedGames: jsonObj,
                    })
                }
            }
        }.bind(this);
    }

    calculateAveragePositiveValue(nonPickedGames) {
        let averagePositiveValue = 0;

        if (nonPickedGames) {
            let counter = 0;
            nonPickedGames.forEach(
                (suggestion) => {
                    if (suggestion.value > 0) {
                        averagePositiveValue += suggestion.value;
                        counter++;
                    }
                }
            );

            averagePositiveValue = counter !== 0 ? averagePositiveValue / counter : 0;
        }
        console.log("calculated", averagePositiveValue)
        return averagePositiveValue;
    }

    renderChosenGamesSideBar() {
        let playerNames = [];
        let split = this.state.playerNames.split('&players=');
        for (let i = 1; i < split.length; i++) {
            playerNames.push(split[i]);
        }
        return <ChosenGamesSideBar playTime={this.state.playTime}
                                   playerNames={playerNames}
                                   allSelectedGames={this.state.allSelectedGames}
                                   fullScreen={!this.state.nonPickedGames || this.state.nonPickedGames.length === 0
                                   || this.state.nonPickedGames[0].value <= this.state.averagePositiveValue}
        />;
    }
}

export default PickHelper;
