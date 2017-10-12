import React, {Component} from 'react';
import LoadingScreen from './util/LoadingScreen';
import "./Main.css";
import Divider from 'material-ui/Divider';
import {Tabs, Tab} from 'material-ui/Tabs';
import {Redirect} from "react-router-dom";
import PlayerInfo from "./PlayerInfo";
import {deepSlice} from "./util/GeneralUtil";
import SortableTable from "./util/SortableTable";

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
            tableDataGames: undefined,
            tableDataPlays: undefined,
            tableDataPlayers: undefined,

        };
        this.downloadPlayer(props.match.params.name);
    }

    render() {
        if (this.state.loading) return <LoadingScreen/>;
        if (!this.state.loading && !this.state.player) return <Redirect to={"/players"}/>;

        let player = this.state.player;
        return <div className="main-block-play-players">
            <div className="main-width">
                <PlayerInfo player={player}/>
                <Divider style={{marginTop: 10, marginBottom: 10}}/>
                <Tabs>
                    <Tab label="Games & ratings">
                        <SortableTable tableData={this.state.tableDataGames} link="/games/"
                                       linkSuffixFunction={this.goToGameByName.bind(this)}/>
                    </Tab>
                    <Tab label="Plays">
                        <SortableTable tableData={this.state.tableDataPlays} link="/plays/" linkSuffixHandler="id"/>
                    </Tab>
                    <Tab label="Players">
                        <SortableTable tableData={this.state.tableDataPlayers} link="/players/"
                                       linkSuffixHandler="Name"/>
                    </Tab>
                </Tabs>
            </div>
        </div>
    }

    goToPlayer(name) {
        this.props.history.push("/players/" + name);
    }


    goToGameByName(tableData, i) {
        let name = tableData[0].data[i];
        let found = false;
        this.state.player.allPlays.forEach(
            (play) => {
                if (!found && play.game.name === name) {
                    found = true;
                    this.props.history.push("/games/" + play.game.id);
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
                        let tableDataGames = this.getTableDataGames(jsonObj.gameRatingsMap);
                        let tableDataPlays = this.getTableDataPlays(deepSlice(jsonObj.allPlays));
                        let tableDataPlayers = this.getTableDataPlayers(jsonObj.playerNameToPlaysMap);
                        this.setState({
                            player: jsonObj,
                            currentGamesName: "rating",
                            tableDataGames: tableDataGames,
                            tableDataPlays: tableDataPlays,
                            tableDataPlayers: tableDataPlayers,
                            loading: false
                        });
                    }
                }
            }
        }.bind(this);
    }

    getTableDataPlayers(playerNameToPlaysMap) {
        let asArr = this.convertPlayerNamesMapToArray(playerNameToPlaysMap);

        let data = [];
        for (let i = 0; i < 2; i++) {
            data.push([]);
        }

        asArr.forEach(
            (player) => {
                if (player.name !== this.props.match.params.name) {
                    data[0].push(player.name);
                    data[1].push(player.plays);
                }
            }
        );

        return [
            {
                title: "Name",
                sortFunction: "string",
                data: data[0]
            },
            {
                title: "Plays",
                sortFunction: "number",
                data: data[1]
            }
        ];
    }

    getTableDataPlays(allPlays) {
        let data = [];
        for (let i = 0; i < 5; i++) {
            data.push([]);
        }

        allPlays.forEach(
            (play) => {
                data[0].push(play.date);
                data[1].push(play.game.name);
                data[2].push(this.getPlayersString(play.playerNames));
                data[3].push(play.noOfPlays);
                data[4].push(play.id);
            }
        );

        return [
            {
                title: "Date",
                sortFunction: "date",
                data: data[0]
            },
            {
                title: "Name",
                sortFunction: "string",
                data: data[1]
            },
            {
                title: "Players",
                sortFunction: "string",
                data: data[2]
            },
            {
                title: "Plays",
                sortFunction: "number",
                data: data[3]
            },
            {
                title: "id",
                sortFunction: "none",
                data: data[4]
            }
        ];
    }

    getTableDataGames(gameRatingsMap) {
        let asArr = this.convertGameRatingsMapToArray(gameRatingsMap);
        let data = [];
        for (let i = 0; i < 2; i++) {
            data.push([]);
        }

        asArr.forEach(
            (game) => {
                data[0].push(game.name);
                data[1].push(game.rating);
            }
        );

        return [
            {
                title: "Name",
                sortFunction: "string",
                data: data[0]
            },
            {
                title: "Rating",
                sortFunction: "number",
                data: data[1]
            }
        ];
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

    getPlayersString(playerNames) {
        let players = "";
        playerNames.forEach(
            (otherPlayer) => {
                players += otherPlayer + ", ";
            }
        );
        players = players.substring(0, players.length - 2);
        return players;
    }
}


export default Player;
