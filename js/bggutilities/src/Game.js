import React, {Component} from 'react';
import Divider from 'material-ui/Divider';
import {List, ListItem} from 'material-ui/List';
import BestWithBlock from './util/BestWithBlock';
import LoadingScreen from "./util/LoadingScreen";
import "./Game.css";
import "./Main.css";
import {Redirect, withRouter} from "react-router-dom";

class Game extends Component {

    constructor(props) {
        super(props);
        this.state = {
            plays: undefined,
            loading: true,
            game: undefined
        }

        var request = new XMLHttpRequest();
        request.timeout = 60000;
        request.open('GET', 'http://localhost:8080/getGame?id=' + props.match.params.gameId, true);
        request.send(null);
        request.onreadystatechange = function () {
            if (request.readyState === 4 && request.status === 200) {
                var type = request.getResponseHeader('Content-Type');
                if (type.indexOf("text") !== 1) {
                    let result = request.responseText;
                    if(result === "") {
                        this.setState({loading: false});
                    }
                    else {
                        let jsonObj = JSON.parse(result);
                        this.setState({game: jsonObj, loading: false});
                    }
                }
            }
        }.bind(this);
    }

    render() {
        if(this.state.loading) return <LoadingScreen/>;
        if(!this.state.loading && !this.state.game) return <Redirect to={"/games"}/>;
        let game = this.state.game;
        let expansions = game.expansions;
        let minPlayers = game.minPlayers;
        let maxPlayers = game.maxPlayers;
        let minPlaytime = game.minPlaytime;
        let maxPlaytime = game.maxPlaytime;
        expansions.forEach(
            (expansion) => {
                if (expansion.minPlayers < minPlayers) minPlayers = expansion.minPlayers;
                if (expansion.maxPlayers > maxPlayers) maxPlayers = expansion.maxPlayers;
                if (expansion.minPlaytime < minPlaytime) minPlaytime = expansion.minPlaytime;
                if (expansion.maxPlaytime > maxPlaytime) maxPlaytime = expansion.maxPlaytime;
            }
        );

        let plays = [];
        let playerRatings = {};
        let playerRatingsArr = [];
        if (this.state.plays === undefined) {
            var request = new XMLHttpRequest();
            request.timeout = 60000;
            request.open('GET', 'http://localhost:8080/getPlays?id=' + game.id, true);
            request.send(null);
            request.onreadystatechange = function () {
                if (request.readyState === 4 && request.status === 200) {
                    var type = request.getResponseHeader('Content-Type');
                    if (type.indexOf("text") !== 1) {
                        let result = request.responseText;
                        let jsonObj = JSON.parse(result);
                        this.setState({plays: jsonObj});
                    }
                }
            }.bind(this);
        }
        else {
            let iteration = 0;

            this.state.plays.forEach(
                (play) => {
                    let playerNamesOutput = "";
                    let lastPlayerName = "";
                    for (let inx in play.playerNames) {
                        let playerName = play.playerNames[inx];
                        playerNamesOutput += playerName + ", ";
                        lastPlayerName = playerName;
                        if (play.playerRatings.hasOwnProperty(playerName)) {
                            let rating = play.playerRatings[playerName];
                            if (!playerRatings.hasOwnProperty(playerName)) {
                                playerRatings[playerName] = {};
                                if (rating > 0) {
                                    playerRatings[playerName].rating = rating;
                                }
                                playerRatings[playerName].numberOfPlays = 1;
                            }
                            else {
                                if (rating > 0 && !playerRatings[playerName].hasOwnProperty("rating")) {
                                    playerRatings[playerName].rating = rating;
                                }
                                playerRatings[playerName].numberOfPlays = playerRatings[playerName].numberOfPlays + play.noOfPlays;
                            }
                        }
                    }
                    if (play.playerNames.length > 1) {
                        playerNamesOutput = playerNamesOutput.substring(0, playerNamesOutput.indexOf(lastPlayerName) - 2) + " and " +
                            playerNamesOutput.substring(playerNamesOutput.indexOf(lastPlayerName), playerNamesOutput.length - 2);
                    }
                    else if (play.playerNames.length === 1) {
                        playerNamesOutput = playerNamesOutput.substring(0, playerNamesOutput.length - 2);
                    }

                    plays.push(<ListItem key={"play-" + iteration}
                                         primaryText={play.date + "  (" + playerNamesOutput + "): " + play.noOfPlays}/>)
                    iteration++;
                }
            )
            let playerRatingsNamesToSort = [];
            for (let playerName in playerRatings) {
                if (playerRatings.hasOwnProperty(playerName)) {
                    playerRatingsNamesToSort.push(playerName);
                }
            }
            playerRatingsNamesToSort.sort();
            iteration = 0;
            for (let inx in playerRatingsNamesToSort) {
                let playerName = playerRatingsNamesToSort[inx];
                let playText = playerRatings[playerName].numberOfPlays === 1 ? " play" : " plays";
                playerRatingsArr.push(<ListItem key={"player-rating-" + iteration}
                                                primaryText={playerName + " (" + playerRatings[playerName].numberOfPlays + playText + "): "
                                                + (playerRatings[playerName].rating === undefined ? "N/A" : playerRatings[playerName].rating)}/>)
                iteration++;
            }
        }

        let playsBlock = <div className="plays-block">
            <div className="title">Plays</div>
            {this.state.plays !== undefined ?
                <List>
                    {plays}
                </List>
                : <LoadingScreen/>} </div>;

        let playerRatingsBlock = <div className="player-ratings-block">
            <div className="title">Player ratings</div>
            {
                this.state.plays !== undefined ?
                    <List>
                        {playerRatingsArr}
                    </List> : <LoadingScreen/>}
        </div>


        return <div className="outer-block">
            <div className="main-width">
                <img src={game.image} alt={""} width={300}/>
                <div className="main-standard-description">
                    <div className="main-description-color" style={{marginRight: 65}}>Name</div>
                    <div>{game.name}</div>
                </div>
                <div className="main-standard-description">
                    <div className="main-description-color" style={{marginRight: 46}}>Playtime</div>
                    <div>{minPlaytime + (minPlaytime !== maxPlaytime ?
                        " - " + maxPlaytime : "") + " minutes"}</div>
                </div>
                <div className="main-standard-description">
                    <div className="main-description-color" style={{marginRight: 32}}>Your rating</div>
                    <div>{parseFloat(game.personalRating).toFixed(2) + "/10"}</div>
                </div>
                <div className="main-standard-description">
                    <div className="main-description-color" style={{marginRight: 21}}>Public rating</div>
                    <div>{parseFloat(game.averageRating).toFixed(2) + "/10"}</div>
                </div>
                <div className="main-standard-description">
                    <div className="main-description-color" style={{marginRight: 30}}>Complexity</div>
                    <div>{parseFloat(game.complexity).toFixed(2) + "/5"}</div>
                </div>
                <div className="main-standard-description">
                    <div className="main-description-color" style={{marginRight: 57}}># Plays</div>
                    <div>{game.numPlays}</div>
                </div>
                <div className="main-standard-description">
                    <div className="main-description-color" style={{marginRight: 55}}>Players</div>
                    <BestWithBlock minPlayers={minPlayers} maxPlayers={maxPlayers}
                                  bestWith={game.bestWith} recommendedWith={game.recommendedWith} />
                </div>
                <Divider style={{marginTop: 10, marginBottom: 10}}/>
            </div>
            <div className="flex-row">
                {playsBlock}
                {playerRatingsBlock}
            </div>
        </div>
    }
}


export default withRouter(Game);
