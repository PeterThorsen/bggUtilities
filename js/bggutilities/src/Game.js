import React, {Component} from 'react';
import Divider from 'material-ui/Divider';
import BestWithBlock from './util/BestWithBlock';
import LoadingScreen from "./util/LoadingScreen";
import "./Main.css";
import {Redirect, withRouter} from "react-router-dom";
import {Tabs, Tab} from 'material-ui/Tabs';
import {pieColors} from "./util/Colors";
import {PieChart, Pie, Cell, Tooltip, Label} from "recharts";
import {getPlayersString} from "./util/GeneralUtil";
import SortableTable from "./util/SortableTable";

class Game extends Component {

    constructor(props) {
        super(props);
        this.state = {
            tableDataPlays: undefined,
            tableDataRatings: undefined,
            loading: true,
            game: undefined,
            plays: undefined
        };
        this.downloadGame();
    }

    render() {
        if (this.state.loading) return <LoadingScreen/>;
        if (!this.state.loading && !this.state.game) return <Redirect to={"/games"}/>;
        if (this.state.plays === undefined) {
            return <LoadingScreen/>;
        }
        let infoBlock = this.buildInfoBlock();
        let winnerChart = this.getRecharts();
        return <div className="outer-block">
            <div className="main-width">
                <img src={this.state.game.image} alt={""} width={300}/>
                <div className="flex-row">
                    {infoBlock}
                    {winnerChart}
                </div>
                <Divider style={{marginTop: 10, marginBottom: 10}}/>
            </div>
            <Tabs>
                <Tab label="Plays">
                    <SortableTable tableData={this.state.tableDataPlays} link="/plays/" linkSuffixHandler="id"/>
                </Tab>
                <Tab label="Player ratings">
                    <SortableTable tableData={this.state.tableDataRatings} link="/players/" linkSuffixHandler="Name"/>
                </Tab>
            </Tabs>
        </div>
    }

    buildInfoBlock() {
        let game = this.state.game;
        let minPlayers = game.minPlayers;
        let maxPlayers = game.maxPlayers;
        let minPlaytime = game.minPlaytime;
        let maxPlaytime = game.maxPlaytime;

        return <div>
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
        </div>
    }

    getRecharts() {

        if (!this.state.plays) {
            return <LoadingScreen/>;
        }
        let data = [];
        this.state.plays.forEach(
            (play) => {
                let noOfPlays = play.noOfPlays;
                play.playerNames.forEach(
                    (player) => {
                        let found = false;

                        data.forEach(
                            (dataPoint) => {
                                if (dataPoint.name === player) {
                                    dataPoint.plays += noOfPlays;
                                    found = true;
                                }
                            }
                        );

                        if (!found) {
                            data.push({name: player, plays: noOfPlays});
                        }
                    }
                );
            }
        );


        let totalPlays = 0;
        data.forEach(
            (element) => {
                totalPlays += element.plays;
            }
        );
        const COLORS = pieColors;

        return <PieChart width={300} height={200}>
            <Pie
                data={data}
                dataKey={"plays"}
                cx={150}
                cy={100}
                innerRadius={30}
                outerRadius={50}
                fill="#8884d8"
                labelLine={false}
                label={(dataPoint) => {
                    return dataPoint.plays > totalPlays / 10 ? dataPoint.name : "";
                }}
            >
                <Label value="Plays" position="center"/>
                {
                    data.map((entry, index) => <Cell key={"cell-" + index} fill={COLORS[index % COLORS.length]}/>)

                }
            </Pie>
            <Tooltip/>
        </PieChart>
    }

    downloadGame() {
        var request = new XMLHttpRequest();
        request.timeout = 60000;
        request.open('GET', 'http://localhost:8080/getGame?id=' + this.props.match.params.gameId, true);
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
                        this.downloadPlays();
                    }
                }
            }
        }.bind(this);
    }

    downloadPlays() {
        var request = new XMLHttpRequest();
        request.timeout = 60000;
        request.open('GET', 'http://localhost:8080/getPlays?id=' + this.state.game.id, true);
        request.send(null);
        request.onreadystatechange = function () {
            if (request.readyState === 4 && request.status === 200) {
                var type = request.getResponseHeader('Content-Type');
                if (type.indexOf("text") !== 1) {
                    let result = request.responseText;
                    let jsonObj = JSON.parse(result);
                    let tableDataPlays = this.buildTableDataPlays(jsonObj);
                    let tableDataRatings = this.buildTableDataRatings(jsonObj);
                    this.setState({
                        plays: jsonObj,
                        tableDataPlays: tableDataPlays,
                        tableDataRatings: tableDataRatings
                    });
                }
            }
        }.bind(this);
    }

    buildTableDataRatings(allPlays) {
        let playerRatingsArr = this.buildPlayerRatingAndPlaysArray(allPlays);
        let data = [];
        for (let i = 0; i < 3; i++) {
            data.push([]);
        }

        playerRatingsArr.forEach(
            (player) => {
                data[0].push(player.name);
                data[1].push(player.plays);
                data[2].push(player.rating);
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
            },
            {
                title: "Rating",
                sortFunction: "number",
                data: data[2]
            }
        ];
    }

    buildPlayerRatingAndPlaysArray(allPlays) {
        let playerRatings = {};

        allPlays.forEach(
            (play, iteration) => {
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
            }
        );

        let arr = [];
        for (let playerName in playerRatings) {
            if (playerRatings.hasOwnProperty(playerName)) {
                arr.push(
                    {
                        name: playerName,
                        plays: playerRatings[playerName]["numberOfPlays"],
                        rating: playerRatings[playerName]["rating"],
                    }
                )
            }
        }
        return arr;
    }

    buildTableDataPlays(allPlays) {
        let data = [];
        for (let i = 0; i < 4; i++) {
            data.push([]);
        }

        allPlays.forEach(
            (play) => {
                data[0].push(play.date);
                data[1].push(getPlayersString(play.playerNames));
                data[2].push(play.noOfPlays);
                data[3].push(play.id);
            }
        );

        return [
            {
                title: "Date",
                sortFunction: "date",
                data: data[0]
            },
            {
                title: "Players",
                sortFunction: "string",
                data: data[1]
            },
            {
                title: "Plays",
                sortFunction: "number",
                data: data[2]
            },
            {
                title: "id",
                sortFunction: "none",
                data: data[3]
            }
        ];
    }

}


export default withRouter(Game);
