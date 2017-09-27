import React, {Component} from 'react';
import LoadingScreen from './util/LoadingScreen';
import RaisedButton from 'material-ui/RaisedButton';

import {
    Table,
    TableBody,
    TableHeader,
    TableHeaderColumn,
    TableRow,
    TableRowColumn,
} from 'material-ui/Table';
import {withRouter} from "react-router-dom";

class PlayersView extends Component {

    constructor(props) {
        super(props);
        this.state = {
            found: false,
            result: undefined,
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
            this.state.result.forEach(
                (player) => {
                    let magicComplexity = parseFloat(player.magicComplexity).toFixed(2);
                    result.push(
                        <TableRow key={"row-" + rowNumber} selectable={false}
                                  onTouchTap={() => this.goToPlayer(player)}>
                            <TableRowColumn style={{width: 80}}>{player.name}</TableRowColumn>
                            <TableRowColumn style={{width: 40}}>{player.totalPlays}</TableRowColumn>
                            <TableRowColumn style={{width: 50}}>{parseFloat(player.minComplexity).toFixed(2)}</TableRowColumn>
                            <TableRowColumn style={{width: 50}}>{parseFloat(player.maxComplexity).toFixed(2)}</TableRowColumn>
                            <TableRowColumn style={{width: 60}}>{parseFloat(player.averageComplexity).toFixed(2)}</TableRowColumn>
                            <TableRowColumn style={{width: 60}}>{magicComplexity != 0 ? magicComplexity : "N/A"}</TableRowColumn>
                        </TableRow>);
                    rowNumber++;
                }
            )

            mainBlock = <div className="main-block">
                <Table style={{width: 600}}>
                    <TableHeader displaySelectAll={false} adjustForCheckbox={false}>
                        <TableRow>
                            <TableHeaderColumn style={{width: 80}}>Name</TableHeaderColumn>
                            <TableHeaderColumn style={{width: 40}}># Plays</TableHeaderColumn>
                            <TableHeaderColumn style={{width: 50}}>Min complexity</TableHeaderColumn>
                            <TableHeaderColumn style={{width: 50}}>Max complexity</TableHeaderColumn>
                            <TableHeaderColumn style={{
                                width: 60,
                                wordWrap: 'break-word',
                                whiteSpace: 'normal'
                            }}>Average complexity</TableHeaderColumn>
                            <TableHeaderColumn style={{
                                width: 60,
                                wordWrap: 'break-word',
                                whiteSpace: 'normal'
                            }}>Magic complexity</TableHeaderColumn>
                        </TableRow>
                    </TableHeader>
                    <TableBody displayRowCheckbox={false}>
                        {result}
                    </TableBody>
                </Table>
            </div>
        }
        return <div>
            <RaisedButton style={{marginTop: 10}} label="Go back" onTouchTap={this.props.goBack}/>
            {mainBlock}
        </div>
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
                    this.setState({found: true, result: jsonObj});
                }
            }
        }.bind(this);
    }

    goToPlayer(player) {
        this.props.history.push("players/" + player.name);
    }
}


export default withRouter(PlayersView);
