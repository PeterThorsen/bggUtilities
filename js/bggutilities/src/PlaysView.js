import React, {Component} from 'react';
import {
    Table,
    TableBody,
    TableHeader,
    TableHeaderColumn,
    TableRow,
    TableRowColumn,
} from 'material-ui/Table';
import LoadingScreen from './util/LoadingScreen';
import './Main.css';
import {withRouter} from "react-router-dom";

class PlaysView extends Component {

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
            this.getPlays();
            mainBlock = <LoadingScreen/>
        }
        else {
            let result = [];
            let rowNumber = 0;
            this.state.result.forEach(
                (play) => {
                    let playerNames = play.playerNames[0];
                    for (let i = 1; i < play.playerNames.length; i++) {
                        playerNames += ", " + play.playerNames[i];
                    }
                    result.push(
                        <TableRow key={"row-" + rowNumber} selectable={false} onTouchTap={() => this.goToPlay(play)}>
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
            );

            mainBlock = <div className="main-block">
                <Table style={{width: 700}}>
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

        return <div>
            {mainBlock}
        </div>

    }

    getPlays() {
        var request = new XMLHttpRequest();
        request.timeout = 60000;
        request.open('GET', 'http://localhost:8080/getAllPlays', true);
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

    goToPlay(play) {
        this.props.history.push("/plays/" + play.id);
    }
}


export default withRouter(PlaysView);
