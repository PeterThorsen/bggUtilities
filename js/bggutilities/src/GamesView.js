import React, {Component} from 'react';
import LoadingScreen from './util/LoadingScreen';
import {GridList, GridTile} from 'material-ui/GridList';
import Game from './Game';
import RaisedButton from 'material-ui/RaisedButton';
import {
    Table,
    TableBody,
    TableHeader,
    TableHeaderColumn,
    TableRow,
    TableRowColumn,
} from 'material-ui/Table';
import Toggle from 'material-ui/Toggle';
import "./Main.css";
import "./GamesView.css";

class GamesView extends Component {

    constructor(props) {
        super(props);
        this.state = {
            found: false,
            result: undefined,
            game: undefined,
            useExpansion: false,
            useDataView: false
        }
    }

    render() {
        if (this.state.game) {
            return <Game goBack={() => this.goToGame(undefined)} game={this.state.game}/>
        }

        let mainBlock = <div/>;
        if (!this.state.found) {
            var request = new XMLHttpRequest();
            request.timeout = 60000;
            request.open('GET', 'http://localhost:8080/getGames?expansions=' + this.state.useExpansion, true);
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
            mainBlock = <LoadingScreen/>;
        }

        if (this.state.found) {
            let result = [];

            if (this.state.useDataView) {
                this.state.result.forEach(
                    (game) => {

                        let players = game.minPlayers;
                        if (game.minPlayers !== game.maxPlayers) players += " - " + game.maxPlayers;

                        let playTime = game.minPlaytime;
                        if (game.minPlaytime !== game.maxPlaytime) playTime += " - " + game.maxPlaytime;

                        result.push(
                            <TableRow key={"row-" + game.id} selectable={false} onTouchTap={() => this.goToGame(game)}>
                                <TableRowColumn style={{width: 120}}>{game.name}</TableRowColumn>
                                <TableRowColumn style={{width: 60}}>{players}</TableRowColumn>
                                <TableRowColumn style={{width: 60}}>{playTime}</TableRowColumn>
                                <TableRowColumn style={{width: 60}}>{game.numPlays}</TableRowColumn>
                                <TableRowColumn style={{width: 60}}>{parseFloat(game.personalRating).toFixed(2)}</TableRowColumn>
                            </TableRow>)
                    });
                mainBlock = <Table style={{width: 600}}>
                    <TableHeader displaySelectAll={false} adjustForCheckbox={false}>
                        <TableRow>
                            <TableHeaderColumn style={{width: 120}}>Name</TableHeaderColumn>
                            <TableHeaderColumn style={{width: 60}}>Players</TableHeaderColumn>
                            <TableHeaderColumn style={{width: 60}}>Playtime</TableHeaderColumn>
                            <TableHeaderColumn style={{width: 60}}># Plays</TableHeaderColumn>
                            <TableHeaderColumn style={{width: 60}}>Rating</TableHeaderColumn>
                        </TableRow>
                    </TableHeader>
                    <TableBody displayRowCheckbox={false}>
                        {result}
                    </TableBody>
                </Table>;

            }

            else {
                this.state.result.forEach(
                    (game) => {
                        result.push(
                            <GridTile key={game.id}
                                      onTouchTap={() => this.goToGame(game)}
                                      cols={1}
                                      rows={1}>
                                <img src={game.image} alt={""} width={146} height={180}/> </GridTile>)
                    });

                mainBlock = <GridList cols={4}
                                      style={{
                                          width: 600,
                                          overflow: 'hidden',
                                          display: 'flex',
                                          overflowY: 'auto'
                                      }}>

                    {result}
                </GridList>

            }
        }

        return <div>
            <div className="options-block">
                <RaisedButton label="Go back" onTouchTap={this.props.goBack}/>
                <Toggle style={{width: 200, marginTop: 10}}
                        defaultToggled={this.state.useExpansion}
                        disabled={!this.state.found}
                        label="Show expansions"
                        onToggle={(event, isInputChecked) => {
                            this.setState({
                                useExpansion: isInputChecked,
                                found: false
                            })
                        }}
                />
                <Toggle style={{width: 200, marginTop: 10}}
                        defaultToggled={this.state.useDataView}
                        disabled={!this.state.found}
                        label="Use data view"
                        onToggle={(event, isInputChecked) => {
                            this.setState({
                                useDataView: isInputChecked
                            })
                        }}
                />
            </div>
            <div className="main-block">
                {mainBlock}
            </div>
        </div>
    }

    goToGame(game) {
        this.setState({
            game: game
        })

    }
}


export default GamesView;
