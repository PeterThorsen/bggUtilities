import React, {Component} from 'react';
import LoadingScreen from './util/LoadingScreen';
import "./Main.css";
import Divider from 'material-ui/Divider';
import {Tabs, Tab} from 'material-ui/Tabs';
import {
    Table,
    TableBody,
    TableHeader,
    TableHeaderColumn,
    TableRow,
    TableRowColumn,
} from 'material-ui/Table';
import {Redirect} from "react-router-dom";
import FlatButton from 'material-ui/FlatButton';
import PlayerInfo from "./PlayerInfo";

class Player extends Component {

    componentWillReceiveProps(nextProps) {
        if (nextProps.match.params.name !== this.state.player.name) {
            this.setState({
                loading: true,
            });
            this.downloadPlayer(nextProps.match.params.name);
        }
    }

    constructor(props) {
        super(props);
        this.state = {
            loading: true,
            player: undefined,

            allGames: undefined,
            currentGamesName: undefined,
            gamesSortedByName: undefined,
            gamesSortedByRating: undefined,

            allPlays: undefined,
            currentPlaysName: undefined,
            playsSortedByDate: undefined,
            playsSortedByName: undefined,
            playsSortedByPlayers: undefined,
            playsSortedByPlays: undefined,

            allPlayers: undefined,
            currentPlayersName: undefined,
            playersSortedByName: undefined,
            playersSortedByPlays: undefined,


        };
        this.downloadPlayer(props.match.params.name);
    }

    render() {
        if (this.state.loading) return <LoadingScreen/>;
        if (!this.state.loading && !this.state.player) return <Redirect to={"/players"}/>;

        let player = this.state.player;

        let gameRatings = this.getGameRatings(player);
        let plays = this.getPlays();
        let players = this.getPlayers(player);

        return <div className="main-block-play-players">
            <div className="main-width">
                <PlayerInfo player={player}/>
                <Divider style={{marginTop: 10, marginBottom: 10}}/>
                <Tabs>
                    <Tab label="Games & ratings">
                        {gameRatings}
                    </Tab>
                    <Tab label="Plays">
                        {plays}
                    </Tab>
                    <Tab label="Players">
                        {players}
                    </Tab>
                </Tabs>
            </div>
        </div>
    }

    /**
     *
     * @param player
     * @returns List with games sorted by their rating
     */
    getGameRatings() {
        let allGames = this.state.allGames;
        let result = [];

        allGames.forEach(
            (gameAndRating, rowNumber) => {
                let gameName = gameAndRating.name;
                let rating = gameAndRating.rating;
                let row = <TableRow onTouchTap={() => this.goToGameByName(gameName)} key={"ratings-" + rowNumber}
                                    selectable={false}>
                    <TableRowColumn>{gameName}</TableRowColumn>
                    <TableRowColumn>{rating}</TableRowColumn>
                </TableRow>;
                result.push(row);
            }
        );

        let buttonStyle = {
            textAlign: 'left',
            marginLeft: -15
        };
        return <div className="main-block">
            <Table style={{width: 600}}>
                <TableHeader displaySelectAll={false} adjustForCheckbox={false}>
                    <TableRow>
                        <TableHeaderColumn>
                            <FlatButton label="Game" fullWidth={true}
                                        labelStyle={{textTransform: 'none'}}
                                        style={buttonStyle}
                                        hoverColor="none" disableTouchRipple={true}
                                        onTouchTap={this.gameSortByName.bind(this)}/>
                        </TableHeaderColumn>
                        <TableHeaderColumn>
                            <FlatButton label="Rating" fullWidth={true}
                                        labelStyle={{textTransform: 'none'}}
                                        style={buttonStyle}
                                        hoverColor="none" disableTouchRipple={true}
                                        onTouchTap={this.gameSortByRating.bind(this)}/>
                        </TableHeaderColumn>
                    </TableRow>
                </TableHeader>
                <TableBody displayRowCheckbox={false}>
                    {result}
                </TableBody>
            </Table>
        </div>
    }

    getPlays() {
        let plays = this.state.allPlays;
        let result = [];

        let rowNumber = 0;
        plays.forEach(
            (play) => {
                let playerNames = play.playerNames[0];
                for (let i = 1; i < play.playerNames.length; i++) {
                    playerNames += ", " + play.playerNames[i];
                }
                result.push(
                    <TableRow onTouchTap={() => this.goToPlay(play.id)} key={"plays-" + rowNumber} selectable={false}>
                        <TableRowColumn style={{
                            width: 60,
                        }}>{play.date}</TableRowColumn>
                        <TableRowColumn style={{
                            width: 120,
                            wordWrap: 'break-word',
                            whiteSpace: 'normal',
                        }}>{play.game.name}</TableRowColumn>
                        <TableRowColumn style={{
                            width: 150,
                            wordWrap: 'break-word',
                            whiteSpace: 'normal',
                        }}>{playerNames}</TableRowColumn>
                        <TableRowColumn style={{
                            width: 30,
                        }}>{play.noOfPlays}</TableRowColumn>
                    </TableRow>);
                rowNumber++;
            }
        );

        let buttonStyle = {
            textAlign: 'left',
            marginLeft: -15
        };
        return <div className="main-block">
            <Table style={{width: 600}}>
                <TableHeader displaySelectAll={false} adjustForCheckbox={false}>
                    <TableRow>
                        <TableHeaderColumn style={{width: 60}}>
                            <FlatButton label="Date" fullWidth={true}
                                        labelStyle={{textTransform: 'none'}}
                                        style={buttonStyle}
                                        hoverColor="none" disableTouchRipple={true}
                                        onTouchTap={this.sortByDate.bind(this)}/>
                        </TableHeaderColumn>
                        <TableHeaderColumn onTouchTap={this.sortByName.bind(this)}
                                           style={{width: 120}}>
                            <FlatButton label="Name" fullWidth={true}
                                        labelStyle={{textTransform: 'none'}}
                                        style={buttonStyle}
                                        hoverColor="none" disableTouchRipple={true}
                                        onTouchTap={this.sortByName.bind(this)}/>
                        </TableHeaderColumn>
                        <TableHeaderColumn
                            style={{width: 150}}>
                            <FlatButton label="Players" fullWidth={true}
                                        labelStyle={{textTransform: 'none'}} hoverColor="none"
                                        style={buttonStyle}
                                        disableTouchRipple={true}
                                        onTouchTap={this.sortByPlayers.bind(this)}/>
                        </TableHeaderColumn>
                        <TableHeaderColumn onTouchTap={this.sortByPlays.bind(this)}
                                           style={{width: 30}}>
                            <FlatButton label="Plays" fullWidth={true}
                                        labelStyle={{textTransform: 'none'}}
                                        style={buttonStyle}
                                        hoverColor="none" disableTouchRipple={true}
                                        onTouchTap={this.sortByPlays.bind(this)}/>
                        </TableHeaderColumn>
                    </TableRow>
                </TableHeader>
                <TableBody displayRowCheckbox={false}>
                    {result}
                </TableBody>
            </Table>
        </div>

    }

    getPlayers() {
        let allPlayers = this.state.allPlayers;
        let result = [];

        allPlayers.forEach(
            (nameAndPlays, rowNumber) => {
                let name = nameAndPlays.name;
                if (name !== this.state.player.name) {
                    let plays = nameAndPlays.plays;

                    result.push(<TableRow onTouchTap={() => this.goToPlayer(name)} key={"players-" + rowNumber}
                                          selectable={false}>
                        <TableRowColumn>{name}</TableRowColumn>
                        <TableRowColumn>{plays}</TableRowColumn>
                    </TableRow>);
                }
            }
        );
        let buttonStyle = {
            textAlign: 'left',
            marginLeft: -15
        };
        return <div className="main-block">
            <Table style={{width: 600}}>
                <TableHeader displaySelectAll={false} adjustForCheckbox={false}>
                    <TableRow>
                        <TableHeaderColumn>
                            <FlatButton label="Name" fullWidth={true}
                                        labelStyle={{textTransform: 'none'}} hoverColor="none"
                                        style={buttonStyle}
                                        disableTouchRipple={true}
                                        onTouchTap={this.playersSortByName.bind(this)}/>
                        </TableHeaderColumn>
                        <TableHeaderColumn>
                            <FlatButton label="Plays" fullWidth={true}
                                        labelStyle={{textTransform: 'none'}} hoverColor="none"
                                        style={buttonStyle}
                                        disableTouchRipple={true}
                                        onTouchTap={this.playersSortByPlays.bind(this)}/>
                        </TableHeaderColumn>
                    </TableRow>
                </TableHeader>
                <TableBody displayRowCheckbox={false}>
                    {result}
                </TableBody>
            </Table>
        </div>
    }

    goToPlayer(name) {
        this.props.history.push("/players/" + name);
    }


    goToGameByName(name) {
        let found = false;
        this.state.allPlays.forEach(
            (play) => {
                if (!found && play.game.name === name) {
                    found = true;
                    this.props.history.push("/games/" + play.game.id);
                    return;
                }
            }
        )
    }

    downloadPlayer(name) {
        var request = new XMLHttpRequest();
        request.timeout = 60000;
        request.open('GET', 'http://localhost:8080/getPlayer?name=' + name, true);
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
                        jsonObj.allPlays.reverse();

                        let games = this.convertGameRatingsMapToArray(jsonObj.gameRatingsMap);
                        let gamesSortedByName = this.gamesBuildSortedByName(games);
                        let gamesSortedByRating = this.gamesBuildSortedByRating(games);

                        let playsSortedByDate = jsonObj.allPlays.slice();
                        let playsSortedByName = this.playsBuildSortedByName(jsonObj.allPlays);
                        let playsSortedByPlayers = this.playsBuildSortedByPlayers(jsonObj.allPlays);
                        let playsSortedByPlays = this.playsBuildSortedByPlays(jsonObj.allPlays);

                        let players = this.convertPlayerNamesMapToArray(jsonObj.playerNameToPlaysMap);
                        let playersSortedByPlays = this.playersBuildSortedByPlays(players);
                        let playersSortedByName = this.playersBuildSortedByName(players);

                        jsonObj.allPlays = undefined;
                        jsonObj.gameRatingsMap = undefined;
                        jsonObj.playerNameToPlaysMap = undefined;

                        this.setState({
                            player: jsonObj,
                            currentGamesName: "rating",
                            allGames: gamesSortedByRating,
                            gamesSortedByName: gamesSortedByName,
                            gamesSortedByRating: gamesSortedByRating,

                            allPlays: playsSortedByDate,
                            currentPlaysName: "date",
                            playsSortedByDate: playsSortedByDate,
                            playsSortedByName: playsSortedByName,
                            playsSortedByPlayers: playsSortedByPlayers,
                            playsSortedByPlays: playsSortedByPlays,

                            allPlayers: playersSortedByPlays,
                            currentPlayersName: "plays",
                            playersSortedByPlays: playersSortedByPlays,
                            playersSortedByName: playersSortedByName,
                            loading: false
                        });
                    }
                }
            }
        }.bind(this);
    }

    convertPlayerNamesMapToArray(map) {
        let arr = [];
        for (let name in map) {
            if (map.hasOwnProperty(name)) {
                arr.push(
                    {
                        name: name,
                        plays: map[name]
                    }
                )
            }
        }
        return arr;
    }

    convertGameRatingsMapToArray(map) {
        let arr = [];
        for (let gameName in map) {
            if (map.hasOwnProperty(gameName)) {
                arr.push(
                    {
                        name: gameName,
                        rating: map[gameName]
                    }
                )
            }
        }
        return arr;
    }

    gamesBuildSortedByName(arr) {
        let byName = arr.slice();
        byName.sort(
            (x, y) => {
                return x.name.localeCompare(y.name);
            }
        );
        return byName;

    }

    gamesBuildSortedByRating(arr) {
        let byRating = arr.slice();
        byRating.sort(
            (x, y) => {
                return y.rating - x.rating;
            }
        );
        return byRating;
    }

    playsBuildSortedByPlayers(allPlays) {
        let byPlayers = allPlays.slice();
        byPlayers.sort(
            (x, y) => {
                let xName = "";
                x.playerNames.forEach(
                    (name) => {
                        xName += name;
                    }
                );
                let yName = "";
                y.playerNames.forEach(
                    (name) => {
                        yName += name;
                    }
                )

                return xName.localeCompare(yName);

            }
        )
        return byPlayers;
    }

    playsBuildSortedByPlays(allPlays) {
        let byPlays = allPlays.slice();
        byPlays.sort(
            (x, y) => {
                return y.noOfPlays - x.noOfPlays;

            }
        )
        return byPlays;
    }

    playsBuildSortedByName(allPlays) {
        let byName = allPlays.slice();
        byName.sort(
            (x, y) => {
                return x.game.name.localeCompare(y.game.name);
            }
        );
        return byName;
    }


    playersBuildSortedByPlays(players) {
        let byPlays = players.slice();
        byPlays.sort(
            (x, y) => {
                return y.plays - x.plays;

            }
        );
        return byPlays;
    }

    playersBuildSortedByName(players) {
        let byName = players.slice();
        byName.sort(
            (x, y) => {
                return x.name.localeCompare(y.name);
            }
        );

        return byName;
    }

    goToPlay(id) {
        this.props.history.push("/plays/" + id);
    }

    sortByDate() {
        if (this.state.currentPlaysName === "date") {
            let reversed = this.state.playsSortedByDate.slice().reverse();
            this.setState({
                allPlays: reversed,
                currentPlaysName: "date-reverse"
            })
        }
        else {
            this.setState({
                allPlays: this.state.playsSortedByDate,
                currentPlaysName: "date"
            })
        }
    }

    sortByName() {
        if (this.state.currentPlaysName === "name") {
            let reversed = this.state.playsSortedByName.slice().reverse();
            this.setState({
                allPlays: reversed,
                currentPlaysName: "name-reverse"
            })
        }
        else {
            this.setState({
                allPlays: this.state.playsSortedByName,
                currentPlaysName: "name"
            })
        }
    }

    sortByPlayers() {
        if (this.state.currentPlaysName === "players") {
            let reversed = this.state.playsSortedByPlayers.slice().reverse();
            this.setState({
                allPlays: reversed,
                currentPlaysName: "players-reverse"
            })
        }
        else {
            this.setState({
                allPlays: this.state.playsSortedByPlayers,
                currentPlaysName: "players"
            })
        }
    }

    sortByPlays() {
        if (this.state.currentPlaysName === "plays") {
            let reversed = this.state.playsSortedByPlays.slice().reverse();
            this.setState({
                allPlays: reversed,
                currentPlaysName: "plays-reverse"
            })
        }
        else {
            this.setState({
                allPlays: this.state.playsSortedByPlays,
                currentPlaysName: "plays"
            })
        }
    }

    gameSortByName() {
        if (this.state.currentGamesName === "name") {
            let reversed = this.state.gamesSortedByName.slice().reverse();
            this.setState({
                allGames: reversed,
                currentGamesName: "name-reverse"
            })
        }
        else {
            this.setState({
                allGames: this.state.gamesSortedByName,
                currentGamesName: "name"
            })
        }
    }

    gameSortByRating() {
        if (this.state.currentGamesName === "rating") {
            let reversed = this.state.gamesSortedByRating.slice().reverse();
            this.setState({
                allGames: reversed,
                currentGamesName: "rating-reverse"
            })
        }
        else {
            this.setState({
                allGames: this.state.gamesSortedByRating,
                currentGamesName: "rating"
            })
        }
    }

    playersSortByName() {
        if (this.state.currentPlayersName === "name") {
            let reversed = this.state.playersSortedByPlays.slice().reverse();
            this.setState({
                allPlayers: reversed,
                currentPlayersName: "name-reverse"
            })
        }
        else {
            this.setState({
                allPlayers: this.state.playersSortedByName,
                currentPlayersName: "name"
            })
        }
    }
    playersSortByPlays() {
        if (this.state.currentPlayersName === "plays") {
            let reversed = this.state.playersSortedByPlays.slice().reverse();
            this.setState({
                allPlayers: reversed,
                currentPlayersName: "plays-reverse"
            })
        }
        else {
            this.setState({
                allPlayers: this.state.playersSortedByPlays,
                currentPlayersName: "plays"
            })
        }
    }
}


export default Player;
