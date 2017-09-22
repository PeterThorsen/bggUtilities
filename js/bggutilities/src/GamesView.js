import React, {Component} from 'react';
import LoadingScreen from './LoadingScreen';
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

class GamesView extends Component {

    constructor(props) {
        super(props);
        this.state = {
            found: false,
            result: "",
            game: undefined,
            useExpansion: false,
            useDataView: false
        }
    }

    render() {

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
                        this.setState({found: true, result: result});
                    }
                }
            }.bind(this);
        }

        let gamesView = <div/>;
        if (this.state.found) {
            let result = [];
            let jsonObj = null;
            jsonObj = JSON.parse(this.state.result);

            if (this.state.useDataView) {
                jsonObj.forEach(
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
                gamesView = <Table style={{width: 600}}>
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
                jsonObj.forEach(
                    (game) => {
                        result.push(
                            <GridTile key={game.id}
                                      onTouchTap={() => this.goToGame(game)}
                                      cols={1}
                                      rows={1}>
                                <img src={game.image} alt={""} width={146} height={180}/> </GridTile>)
                    });

                gamesView = <GridList cols={4}
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

        if (!this.state.found) return <LoadingScreen/>;
        if (this.state.game) {
            return <Game goBack={() => this.goToGame(undefined)} game={this.state.game}/>
        }

        return <div>
            <div style={{marginBottom: 10, display: 'flex', flexDirection: 'column', alignItems: 'center'}}>
                <RaisedButton label="Go back" onTouchTap={this.props.goBack}/>
                <Toggle style={{width: 200, marginTop: 10}}
                        defaultToggled={this.state.useExpansion}
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
                        label="Use data view"
                        onToggle={(event, isInputChecked) => {
                            this.setState({
                                useDataView: isInputChecked
                            })
                        }}
                />
            </div>
            <div style={{display: 'flex', flexWrap: 'wrap', justifyContent: 'space-around'}}>
                {gamesView}
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
