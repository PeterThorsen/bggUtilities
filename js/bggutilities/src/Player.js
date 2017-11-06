import React, {Component} from 'react';
import LoadingScreen from './util/LoadingScreen';
import "./Main.css";
import "./Player.css";
import Divider from 'material-ui/Divider';
import {Tabs, Tab} from 'material-ui/Tabs';
import {Redirect} from "react-router-dom";
import PlayerInfo from "./PlayerInfo";
import {deepSlice, getPlayersString} from "./util/GeneralUtil";
import SortableTable from "./util/SortableTable";
import {pieColors} from "./util/Colors";
import {Cell, Label, Pie, PieChart, Tooltip} from "recharts";
import {List, ListItem} from 'material-ui/List';

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
        let gameNights = this.renderGameNights();

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
                    <Tab label="Game nights">
                        {gameNights}
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
                data[2].push(getPlayersString(play.playerNames));
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

    renderGameNights() {
        return this.state.player.gameNights.map(
            (gameNight) => {
                let date = gameNight.date;
                let winners = {};
                winners["No winner"] = 0;
                let allGames = [];
                let allPlayers = [];
                gameNight.plays.forEach(
                    (play) => {
                        let gameName = play.game.name;
                        let found = false;
                        allGames.forEach(
                            (game) => {
                                if (game.game === gameName) {
                                    game.plays += play.noOfPlays;
                                    found = true;
                                }
                            }
                        );
                        if (!found) {
                            allGames.push({game: play.game.name, plays: play.noOfPlays});
                        }

                        play.playerNames.forEach(
                            (newPlayer) => {
                                let found = false;
                                allPlayers.forEach(
                                    (oldPlayer) => {
                                        if(newPlayer === oldPlayer) found = true;
                                    }
                                );
                                if(!found) {
                                    allPlayers.push(newPlayer);
                                }
                            }
                        );
                        if (!this.isACooperativeGame(play)) {
                            let playerNames = play.playerNames;
                            let currentWinners = play.winners;
                            let noOfPlays = play.noOfPlays;
                            playerNames.forEach(
                                (playerName) => {
                                    if (!winners.hasOwnProperty(playerName)) {
                                        winners[playerName] = 0;
                                    }
                                }
                            );
                            if (currentWinners.length === 0) {
                                winners["No winner"] = winners["No winner"] + noOfPlays;
                            }
                            else {
                                currentWinners.forEach(
                                    (playerName) => {
                                        winners[playerName] += noOfPlays;
                                    }
                                )
                            }
                        }
                    }
                );
                allPlayers.push("Peter");

                let chart = this.convertWinnersToChart(winners);
                return <div className="game-night-outer" key={date}>
                    {chart}
                    {date}
                    <List>
                        {allGames.map(
                            (gameAndPlays) => {
                                return <ListItem key={gameAndPlays.game + "-listitem-" + date}
                                                  primaryText={gameAndPlays.game + ": " + gameAndPlays.plays}/>;
                            }
                        )}
                    </List>
                    <List>
                        {allPlayers.map(
                            (playerName) => {
                                return <ListItem key={"list-item-" + playerName}
                                          primaryText={playerName}/>                            }
                        )}
                    </List>
                </div>
            }
        )
    }

    isACooperativeGame(play) {
        let found = false;
        play.game.mechanisms.forEach(
            (mechanism) => {
                if (mechanism.mechanism === 'Co-operative Play') {
                    found = true;
                }
            }
        );
        return found;
    }

    convertWinnersToChart(winners) {
        let data = [];
        for (let playerName in winners) {
            if (winners.hasOwnProperty(playerName)) {
                let wins = winners[playerName];
                let name = playerName === "No winner" ? 'Peter' : playerName;
                data.push({name: name, wins: wins})
            }
        }

        let totalWins = 0;
        data.forEach(
            (dataPoint) => {
                totalWins += dataPoint.wins;
            }
        );
        const COLORS = pieColors;

        let chart = <PieChart width={300} height={200}>
            <Pie
                data={data}
                dataKey={"wins"}
                cx={150}
                cy={100}
                innerRadius={30}
                outerRadius={50}
                fill="#8884d8"
                labelLine={false}
                label={(dataPoint) => {
                    return dataPoint.wins > totalWins / 10 ? dataPoint.name : "";
                }}
            >
                <Label value="Wins" position="center"/>
                {
                    data.map((entry, index) => <Cell key={"cell-" + index}
                                                     fill={COLORS[index % COLORS.length]}/>)

                }
            </Pie>
            <Tooltip/>
        </PieChart>;
        return chart;
    }
}


export default Player;
