import React, {Component} from 'react';
import Divider from 'material-ui/Divider';
import RaisedButton from 'material-ui/RaisedButton';
import CircularProgress from 'material-ui/CircularProgress';
import {List, ListItem} from 'material-ui/List';
import PlayersBlock from './PlayersBlock';
class Game extends Component {

    constructor(props) {
        super(props);
        this.state = {
            plays: undefined
        }
    }

    render() {
        let game = this.props.game;
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

        let playsBlock = <div style={{width: 295, borderRight: '1px solid grey', paddingRight: 5}}>
            <div style={{fontSize: 30}}>Plays</div>
            {this.state.plays !== undefined ?
                <List>
                    {plays}
                </List>
                : <CircularProgress/>} </div>;

        let playerRatingsBlock = <div style={{width: 295, paddingLeft: 5}}>
            <div style={{fontSize: 30}}>Player ratings</div>
            {
                this.state.plays !== undefined ?
                    <List>
                        {playerRatingsArr}
                    </List> : <CircularProgress/>}
        </div>


        return <div
            style={{
                display: 'flex', flexWrap: 'wrap', justifyContent: 'space-around', flexDirection: 'column',
                alignItems: 'center', alignContent: 'center', marginTop: 10
            }}>
            <div style={{width: 600}}>
                <img src={game.image} alt={""} width={300}/>
                <div style={{display: 'flex', flexDirection: 'row', marginTop: 2}}>
                    <div style={{marginRight: 65, color: 'grey'}}>Name</div>
                    <div>{game.name}</div>
                </div>
                <div style={{display: 'flex', flexDirection: 'row', marginTop: 2}}>
                    <div style={{marginRight: 46, color: 'grey'}}>Playtime</div>
                    <div>{minPlaytime + (minPlaytime !== maxPlaytime ?
                        " - " + maxPlaytime : "") + " minutes"}</div>
                </div>
                <div style={{display: 'flex', flexDirection: 'row', marginTop: 2}}>
                    <div style={{marginRight: 32, color: 'grey'}}>Your rating</div>
                    <div>{parseFloat(game.personalRating).toFixed(2) + "/10"}</div>
                </div>
                <div style={{display: 'flex', flexDirection: 'row', marginTop: 2}}>
                    <div style={{marginRight: 21, color: 'grey'}}>Public rating</div>
                    <div>{parseFloat(game.averageRating).toFixed(2) + "/10"}</div>
                </div>
                <div style={{display: 'flex', flexDirection: 'row', marginTop: 2}}>
                    <div style={{marginRight: 30, color: 'grey'}}>Complexity</div>
                    <div>{parseFloat(game.complexity).toFixed(2) + "/5"}</div>
                </div>
                <div style={{display: 'flex', flexDirection: 'row', marginTop: 2}}>
                    <div style={{marginRight: 57, color: 'grey'}}># Plays</div>
                    <div>{game.numPlays}</div>
                </div>
                <div style={{display: 'flex', flexDirection: 'row', alignItems: 'center', marginTop: 2}}>
                    <div style={{marginRight: 55, color: 'grey'}}>Players</div>
                    <PlayersBlock minPlayers={minPlayers} maxPlayers={maxPlayers}
                                  bestWith={game.bestWith} recommendedWith={game.recommendedWith} />
                </div>
                <RaisedButton style={{marginTop: 10}} label="Go back" onTouchTap={this.props.goBack}/>
                <Divider style={{marginTop: 10, marginBottom: 10}}/>
            </div>
            <div style={{display: 'flex', flexDirection: 'row'}}>
                {playsBlock}
                {playerRatingsBlock}
            </div>
        </div>
    }
}


export default Game;
