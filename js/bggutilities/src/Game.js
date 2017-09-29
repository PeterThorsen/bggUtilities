import React, {Component} from 'react';
import Divider from 'material-ui/Divider';
import BestWithBlock from './util/BestWithBlock';
import LoadingScreen from "./util/LoadingScreen";
import "./Game.css";
import "./Main.css";
import {Redirect, withRouter} from "react-router-dom";
import {
    Table,
    TableBody,
    TableHeader,
    TableHeaderColumn,
    TableRow,
    TableRowColumn,
} from 'material-ui/Table';
import {Tabs, Tab} from 'material-ui/Tabs';

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
                    if (result === "") {
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
        if (this.state.loading) return <LoadingScreen/>;
        if (!this.state.loading && !this.state.game) return <Redirect to={"/games"}/>;
        let game = this.state.game;
        let minPlayers = game.minPlayers;
        let maxPlayers = game.maxPlayers;
        let minPlaytime = game.minPlaytime;
        let maxPlaytime = game.maxPlaytime;

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
        let plays = this.buildPlays();
        let playerRatings = this.buildPlayerRatings();

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
                                   bestWith={game.bestWith} recommendedWith={game.recommendedWith}/>
                </div>
                <Divider style={{marginTop: 10, marginBottom: 10}}/>
            </div>
            <Tabs>
                <Tab label="Plays">
                    {plays}
                </Tab>
                <Tab label="Player ratings">
                    {playerRatings}
                </Tab>
            </Tabs>
        </div>
    }

    goToPlayer(name) {
        this.props.history.push("/players/" + name);
    }

    goToPlay(id) {
        this.props.history.push("/plays/" + id);
    }

    buildPlays() {
        if (!this.state.plays) {
            return <LoadingScreen/>;
        }

        let iteration = 0;
        let plays = [];

        this.state.plays.forEach(
            (play) => {
                let playerNamesOutput = "";
                let lastPlayerName = "";
                for (let inx in play.playerNames) {
                    let playerName = play.playerNames[inx];
                    playerNamesOutput += playerName + ", ";
                    lastPlayerName = playerName;
                }
                if (play.playerNames.length > 1) {
                    playerNamesOutput = playerNamesOutput.substring(0, playerNamesOutput.indexOf(lastPlayerName) - 2) + " and " +
                        playerNamesOutput.substring(playerNamesOutput.indexOf(lastPlayerName), playerNamesOutput.length - 2);
                }
                else if (play.playerNames.length === 1) {
                    playerNamesOutput = playerNamesOutput.substring(0, playerNamesOutput.length - 2);
                }

                plays.push(<TableRow onTouchTap={() => this.goToPlay(play.id)} key={"play-" + iteration} selectable={false}>
                    <TableRowColumn>{play.date}</TableRowColumn>
                    <TableRowColumn style={{
                        wordWrap: 'break-word',
                        whiteSpace: 'normal'
                    }}>{playerNamesOutput}</TableRowColumn>
                    <TableRowColumn>{play.noOfPlays}</TableRowColumn>
                </TableRow>);
                iteration++;
            }
        );
        return <div className="main-block">
            <Table style={{width: 600}}>
                <TableHeader displaySelectAll={false} adjustForCheckbox={false}>
                    <TableRow>
                        <TableHeaderColumn>Date</TableHeaderColumn>
                        <TableHeaderColumn>Players</TableHeaderColumn>
                        <TableHeaderColumn># Plays</TableHeaderColumn>
                    </TableRow>
                </TableHeader>
                <TableBody displayRowCheckbox={false}>
                    {plays}
                </TableBody>
            </Table>
        </div>;
    }

    buildPlayerRatings() {
        if (!this.state.plays) {
            return <LoadingScreen/>;
        }

        let iteration = 0;
        let playerRatings = {};
        let playerRatingsArr = [];

        this.state.plays.forEach(
            (play) => {
                for (let inx in play.playerNames) {
                    let playerName = play.playerNames[inx];
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
                iteration++;
            }
        );

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
            playerRatingsArr.push(<TableRow onTouchTap={() => this.goToPlayer(playerName)} key={"player-ratings-" + iteration} selectable={false}>
                <TableRowColumn style={{
                    wordWrap: 'break-word',
                    whiteSpace: 'normal'
                }}>{playerName}</TableRowColumn>
                <TableRowColumn>{playerRatings[playerName].numberOfPlays}</TableRowColumn>
                <TableRowColumn>{playerRatings[playerName].rating === undefined ? "N/A" : playerRatings[playerName].rating}</TableRowColumn>
            </TableRow>);
            iteration++;
        }

        return <div className="main-block">
            <Table style={{width: 600}}>
                <TableHeader displaySelectAll={false} adjustForCheckbox={false}>
                    <TableRow>
                        <TableHeaderColumn>Name</TableHeaderColumn>
                        <TableHeaderColumn># Plays</TableHeaderColumn>
                        <TableHeaderColumn>Rating</TableHeaderColumn>
                    </TableRow>
                </TableHeader>
                <TableBody displayRowCheckbox={false}>
                    {playerRatingsArr}
                </TableBody>
            </Table>
        </div>;
    }
}


export default withRouter(Game);
