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

class Player extends Component {

    constructor(props) {
        super(props);
        this.state = {
            loading: true,
            player: undefined
        };

        var request = new XMLHttpRequest();
        request.timeout = 60000;
        request.open('GET', 'http://localhost:8080/getPlayer?name=' + props.match.params.name, true);
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
                        this.setState({player: jsonObj, loading: false});
                    }
                }
            }
        }.bind(this);
    }

    render() {
        if(this.state.loading) return <LoadingScreen/>;
        if(!this.state.loading && !this.state.player) return <Redirect to={"/players"}/>;

        let player = this.state.player;

        let gameRatings = this.getGameRatings(player);
        let plays = this.getPlays(player);
        let players = this.getPlayers(player);

        return <div className="main-block-play-players">
            <div className="main-width">
                <h1>{player.name} </h1>
                <div className="main-standard-description">
                    <div className="main-description-color" style={{marginRight: 115}}># Plays</div>
                    <div>{player.totalPlays}</div>
                </div>
                <div className="main-standard-description">
                    <div className="main-description-color" style={{marginRight: 57}}>Min. complexity</div>
                    <div>{parseFloat(player.minComplexity).toFixed(2)}</div>
                </div>
                <div className="main-standard-description">
                    <div className="main-description-color" style={{marginRight: 54}}>Max. complexity</div>
                    <div>{parseFloat(player.maxComplexity).toFixed(2)}</div>
                </div>
                <div className="main-standard-description">
                    <div className="main-description-color" style={{marginRight: 28}}>Average complexity</div>
                    <div>{parseFloat(player.averageComplexity).toFixed(2)}</div>
                </div>
                <div className="main-standard-description">
                    <div className="main-description-color" style={{marginRight: 46}}>Magic complexity</div>
                    <div>{parseFloat(player.magicComplexity).toFixed(2)}</div>
                </div>
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
    getGameRatings(player) {
        let gameRatingsMap = player.gameRatingsMap;
        let resultStrings = [];
        let result = [];
        for (let gameName in gameRatingsMap) {
            if (gameRatingsMap.hasOwnProperty(gameName)) {
                resultStrings.push(gameRatingsMap[gameName] + ": " + gameName);
            }
        }
        resultStrings.sort();
        resultStrings.reverse();

        let rowNumber = 0;
        for (let i = 0; i < resultStrings.length; i++) {

            let index = resultStrings[i].indexOf(": ");
            let gameName = resultStrings[i].substring(index + 2, resultStrings[i].length);
            let rating = resultStrings[i].substring(0, index);

            let row = <TableRow key={"ratings-" + rowNumber} selectable={false}>
                <TableRowColumn>{gameName}</TableRowColumn>
                <TableRowColumn>{rating}</TableRowColumn>
            </TableRow>;
            rowNumber++;
            if (rating == "10") {
                result.unshift(row); // adds to start, needed because of string sorting
            }
            else {
                result.push(row);
            }
        }

        return <div className="main-block">
            <Table style={{width: 600}}>
                <TableHeader displaySelectAll={false} adjustForCheckbox={false}>
                    <TableRow>
                        <TableHeaderColumn>Game</TableHeaderColumn>
                        <TableHeaderColumn>Rating</TableHeaderColumn>
                    </TableRow>
                </TableHeader>
                <TableBody displayRowCheckbox={false}>
                    {result}
                </TableBody>
            </Table>
        </div>
    }

    getPlays(player) {
        let plays = player.allPlays;
        let result = [];
        plays.reverse();

        let rowNumber = 0;
        plays.forEach(
            (play) => {
                let playerNames = play.playerNames[0];
                for (let i = 1; i < play.playerNames.length; i++) {
                    playerNames += ", " + play.playerNames[i];
                }
                result.push(
                    <TableRow key={"plays-" + rowNumber} selectable={false}>
                        <TableRowColumn style={{width: 60}}>{play.date}</TableRowColumn>
                        <TableRowColumn style={{
                            width: 120,
                            wordWrap: 'break-word',
                            whiteSpace: 'normal'
                        }}>{play.game.name}</TableRowColumn>
                        <TableRowColumn style={{
                            width: 150,
                            wordWrap: 'break-word',
                            whiteSpace: 'normal'
                        }}>{playerNames}</TableRowColumn>
                        <TableRowColumn style={{width: 30}}>{play.noOfPlays}</TableRowColumn>
                    </TableRow>);
                rowNumber++;
            }
        )
        return <div className="main-block">
            <Table style={{width: 600}}>
                <TableHeader displaySelectAll={false} adjustForCheckbox={false}>
                    <TableRow>
                        <TableHeaderColumn style={{width: 60}}>Date</TableHeaderColumn>
                        <TableHeaderColumn style={{width: 120}}>Name</TableHeaderColumn>
                        <TableHeaderColumn style={{width: 150}}>Players</TableHeaderColumn>
                        <TableHeaderColumn style={{width: 30}}># Plays</TableHeaderColumn>
                    </TableRow>
                </TableHeader>
                <TableBody displayRowCheckbox={false}>
                    {result}
                </TableBody>
            </Table>
        </div>

    }

    getPlayers(player) {
        let playerNameToPlaysMap = player.playerNameToPlaysMap;
        let strings = [];
        let result = [];
        for (let name in playerNameToPlaysMap) {
            if (playerNameToPlaysMap.hasOwnProperty(name)) {
                strings.push(playerNameToPlaysMap[name] + ": " + name);
            }
        }

        var theSorted = strings.sort(function(a,b)
        {
            var c = parseInt(a,10);
            var d = parseInt(b,10);
            return d-c;
        });

        let rowNumber = 0;
        theSorted.forEach(
            (element) => {
                let index = element.indexOf(": ");
                let name = element.substring(index + 2, element.length);
                if(name !== player.name) {
                    let rating = element.substring(0, index);

                    result.push(<TableRow key={"players-" + rowNumber} selectable={false}>
                        <TableRowColumn>{name}</TableRowColumn>
                        <TableRowColumn>{rating}</TableRowColumn>
                    </TableRow>);
                    rowNumber++;
                }
            }
        );
        return <div className="main-block">
            <Table style={{width: 600}}>
                <TableHeader displaySelectAll={false} adjustForCheckbox={false}>
                    <TableRow>
                        <TableHeaderColumn>Name</TableHeaderColumn>
                        <TableHeaderColumn>Rating</TableHeaderColumn>
                    </TableRow>
                </TableHeader>
                <TableBody displayRowCheckbox={false}>
                    {result}
                </TableBody>
            </Table>
        </div>    }
}


export default Player;
