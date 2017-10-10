import React, {Component} from 'react';
import LoadingScreen from './util/LoadingScreen';

import {
    Table,
    TableBody,
    TableHeader,
    TableHeaderColumn,
    TableRow,
    TableRowColumn,
} from 'material-ui/Table';
import {withRouter} from "react-router-dom";
import FlatButton from 'material-ui/FlatButton';

class PlayersView extends Component {

    constructor(props) {
        super(props);
        this.state = {
            found: false,
            currentName: undefined,
            players: undefined,
            playersSortedByName: undefined,
            playersSortedByPlays: undefined,
            playersSortedByMinComplexity: undefined,
            playersSortedByMaxComplexity: undefined,
            playersSortedByAverageComplexity: undefined,
            playersSortedByMagicComplexity: undefined
        }
    }

    render() {
        let mainBlock = <div/>;
        if (!this.state.found) {
            this.getPlayers();
            mainBlock = <LoadingScreen/>
        }
        else {
            let result = [];
            let rowNumber = 0;
            this.state.players.forEach(
                (player) => {
                    let magicComplexity = parseFloat(player.magicComplexity).toFixed(2);
                    result.push(
                        <TableRow key={"row-" + rowNumber} selectable={false}
                                  onTouchTap={() => this.goToPlayer(player)}>
                            <TableRowColumn style={{width: 80}}>{player.name}</TableRowColumn>
                            <TableRowColumn style={{width: 40}}>{player.totalPlays}</TableRowColumn>
                            <TableRowColumn
                                style={{width: 50}}>{parseFloat(player.minComplexity).toFixed(2)}</TableRowColumn>
                            <TableRowColumn
                                style={{width: 50}}>{parseFloat(player.maxComplexity).toFixed(2)}</TableRowColumn>
                            <TableRowColumn
                                style={{width: 60}}>{parseFloat(player.averageComplexity).toFixed(2)}</TableRowColumn>
                            <TableRowColumn
                                style={{width: 60}}>{magicComplexity !== '0.00' ? magicComplexity : "N/A"}</TableRowColumn>
                        </TableRow>);
                    rowNumber++;
                }
            );
            let buttonStyle = {
                textAlign: 'left',
                marginLeft: -15
            };
            mainBlock = <div className="main-block">
                <Table style={{width: 600}}>
                    <TableHeader displaySelectAll={false} adjustForCheckbox={false}>
                        <TableRow>
                            <TableHeaderColumn style={{width: 80}}>
                                <FlatButton label="Name" fullWidth={true}
                                            labelStyle={{textTransform: 'none'}}
                                            style={buttonStyle}
                                            hoverColor="none" disableTouchRipple={true}
                                            onTouchTap={this.sortByName.bind(this)}/>
                            </TableHeaderColumn>
                            <TableHeaderColumn style={{width: 40}}>
                                <FlatButton label="Plays" fullWidth={true}
                                            labelStyle={{textTransform: 'none'}}
                                            style={buttonStyle}
                                            hoverColor="none" disableTouchRipple={true}
                                            onTouchTap={this.sortByPlays.bind(this)}/>
                            </TableHeaderColumn>
                            <TableHeaderColumn style={{width: 50}}>
                                <FlatButton label="Min" fullWidth={true}
                                            labelStyle={{textTransform: 'none'}}
                                            style={buttonStyle}
                                            hoverColor="none" disableTouchRipple={true}
                                            onTouchTap={this.sortByMinComplexity.bind(this)}/>
                            </TableHeaderColumn>
                            <TableHeaderColumn style={{width: 50}}>
                                <FlatButton label="Max" fullWidth={true}
                                            labelStyle={{textTransform: 'none'}}
                                            style={buttonStyle}
                                            hoverColor="none" disableTouchRipple={true}
                                            onTouchTap={this.sortByMaxComplexity.bind(this)}/>
                            </TableHeaderColumn>
                            <TableHeaderColumn style={{
                                width: 60,
                                wordWrap: 'break-word',
                                whiteSpace: 'normal'
                            }}>
                                <FlatButton label="Average" fullWidth={true}
                                            labelStyle={{textTransform: 'none'}}
                                            style={buttonStyle}
                                            hoverColor="none" disableTouchRipple={true}
                                            onTouchTap={this.sortByAverageComplexity.bind(this)}/>
                            </TableHeaderColumn>
                            <TableHeaderColumn style={{
                                width: 60,
                                wordWrap: 'break-word',
                                whiteSpace: 'normal'
                            }}>
                                <FlatButton label="Magic" fullWidth={true}
                                            labelStyle={{textTransform: 'none'}}
                                            style={buttonStyle}
                                            hoverColor="none" disableTouchRipple={true}
                                            onTouchTap={this.sortByMagicComplexity.bind(this)}/>
                            </TableHeaderColumn>
                        </TableRow>
                    </TableHeader>
                    <TableBody displayRowCheckbox={false}>
                        {result}
                    </TableBody>
                </Table>
            </div>
        }
        return <div>
            {mainBlock}
        </div>
    }

    goToPlayer(player) {
        this.props.history.push("players/" + player.name);
    }

    getPlayers() {
        var request = new XMLHttpRequest();
        request.timeout = 60000;
        request.open('GET', 'http://localhost:8080/getAllPlayers', true);
        request.send(null);
        request.onreadystatechange = function () {
            if (request.readyState === 4 && request.status === 200) {
                var type = request.getResponseHeader('Content-Type');
                if (type.indexOf("text") !== 1) {
                    let result = request.responseText;
                    let jsonObj = JSON.parse(result);
                    jsonObj.forEach(
                        (obj) => {
                            obj.allPlays = undefined;
                            obj.gameRatingsMap = undefined;
                            obj.gameToPlaysMap = undefined;
                            obj.playerNameToPlaysMap = undefined;
                        }
                    );
                    let playersSortedByName = this.buildSortedByName(jsonObj);
                    let playersSortedByPlays = this.buildSortedByPlays(jsonObj);
                    let playersSortedByMinComplexity = this.buildSortedByMinComplexity(jsonObj);
                    let playersSortedByMaxComplexity = this.buildSortedByMaxComplexity(jsonObj);
                    let playersSortedByAverageComplexity = this.buildSortedByAverageComplexity(jsonObj);
                    let playersSortedByMagicComplexity = this.buildSortedByMagicComplexity(jsonObj);

                    this.setState({
                        found: true,
                        currentName: "name",
                        players: playersSortedByName,
                        playersSortedByName: playersSortedByName,
                        playersSortedByPlays: playersSortedByPlays,
                        playersSortedByMinComplexity: playersSortedByMinComplexity,
                        playersSortedByMaxComplexity: playersSortedByMaxComplexity,
                        playersSortedByAverageComplexity: playersSortedByAverageComplexity,
                        playersSortedByMagicComplexity: playersSortedByMagicComplexity
                    });
                }
            }
        }.bind(this);
    }

    buildSortedByName(allPlayers) {
        let byName = allPlayers.slice();

        // Sorted by server
        /*byName.sort(
            (x, y) => {
                return x.name.localeCompare(y.name);
            }
        );*/
        return byName;
    }

    buildSortedByPlays(allPlayers) {
        let byPlays = allPlayers.slice();
        byPlays.sort(
            (x, y) => {
                return y.totalPlays - x.totalPlays;

            }
        );
        return byPlays;
    }

    buildSortedByMinComplexity(allPlayers) {
        let byComplexity = allPlayers.slice();
        byComplexity.sort(
            (x, y) => {
                return y.minComplexity - x.minComplexity;

            }
        );
        return byComplexity;

    }

    buildSortedByMaxComplexity(allPlayers) {
        let byComplexity = allPlayers.slice();
        byComplexity.sort(
            (x, y) => {
                return y.maxComplexity - x.maxComplexity;

            }
        );
        return byComplexity;
    }

    buildSortedByAverageComplexity(allPlayers) {
        let byComplexity = allPlayers.slice();
        byComplexity.sort(
            (x, y) => {
                return y.averageComplexity - x.averageComplexity;

            }
        );
        return byComplexity;
    }

    buildSortedByMagicComplexity(allPlayers) {
        let byComplexity = allPlayers.slice();
        byComplexity.sort(
            (x, y) => {
                return y.magicComplexity - x.magicComplexity;

            }
        );
        return byComplexity;
    }


    // Sorting table

    sortByName() {
        if (this.state.currentName === "name") {
            let reversed = this.state.playersSortedByName.slice().reverse();
            this.setState({
                players: reversed,
                currentName: "name-reverse"
            })
        }
        else {
            this.setState({
                players: this.state.playersSortedByName,
                currentName: "name"
            })
        }
    }

    sortByPlays() {
        if (this.state.currentName === "plays") {
            let reversed = this.state.playersSortedByPlays.slice().reverse();
            this.setState({
                players: reversed,
                currentName: "plays-reverse"
            })
        }
        else {
            this.setState({
                players: this.state.playersSortedByPlays,
                currentName: "plays"
            })
        }
    }

    sortByMinComplexity() {
        if (this.state.currentName === "min") {
            let reversed = this.state.playersSortedByMinComplexity.slice().reverse();
            this.setState({
                players: reversed,
                currentName: "min-reverse"
            })
        }
        else {
            this.setState({
                players: this.state.playersSortedByMinComplexity,
                currentName: "min"
            })
        }
    }

    sortByMaxComplexity() {
        if (this.state.currentName === "max") {
            let reversed = this.state.playersSortedByMaxComplexity.slice().reverse();
            this.setState({
                players: reversed,
                currentName: "max-reverse"
            })
        }
        else {
            this.setState({
                players: this.state.playersSortedByMaxComplexity,
                currentName: "max"
            })
        }
    }

    sortByAverageComplexity() {
        if (this.state.currentName === "avg") {
            let reversed = this.state.playersSortedByAverageComplexity.slice().reverse();
            this.setState({
                players: reversed,
                currentName: "avg-reverse"
            })
        }
        else {
            this.setState({
                players: this.state.playersSortedByAverageComplexity,
                currentName: "avg"
            })
        }
    }

    sortByMagicComplexity() {
        if (this.state.currentName === "magic") {
            let reversed = this.state.playersSortedByMagicComplexity.slice().reverse();
            this.setState({
                players: reversed,
                currentName: "magic-reverse"
            })
        }
        else {
            this.setState({
                players: this.state.playersSortedByMagicComplexity,
                currentName: "magic"
            })
        }
    }
}


export default withRouter(PlayersView);
