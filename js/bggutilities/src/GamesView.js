import React, {Component} from 'react';
import LoadingScreen from './util/LoadingScreen';
import {GridList, GridTile} from 'material-ui/GridList';
import Toggle from 'material-ui/Toggle';
import "./Main.css";
import "./GamesView.css";
import {withRouter} from "react-router-dom";
import SortableTable from "./util/SortableTable";

class GamesView extends Component {

    constructor(props) {
        super(props);
        this.state = {
            found: false,
            result: undefined,
            tableData: undefined,
            useExpansion: false,
            useDataView: false
        };
        this.downloadGames();
    }

    render() {
        let mainBlock = <div/>;
        if (!this.state.found) {
            mainBlock = <LoadingScreen/>;
        }

        if (this.state.found) {
            let result = [];

            if (this.state.useDataView) {
                mainBlock = <SortableTable tableData={this.state.tableData} link="/games/" linkSuffixHandler="id"/>
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
                                          width: 700,
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

    buildTableData(allGames) {
        let data = [];
        for (let i = 0; i < 6; i++) {
            data.push([]);
        }

        allGames.forEach(
            (game) => {
                data[0].push(game.name);
                let players = game.minPlayers;
                if(game.minPlayers !== game.maxPlayers) {
                    players +=  " - " + game.maxPlayers;
                }
                data[1].push(players);
                let playTime = game.minPlaytime;
                if(game.minPlaytime !== game.maxPlaytime) {
                    if(playTime === 0) {
                        playTime = game.maxPlaytime;
                    }
                    else {
                        playTime += " - " + game.maxPlaytime;
                    }
                }
                data[2].push(playTime);
                data[3].push(game.numPlays);
                data[4].push(game.personalRating);
                data[5].push(game.id);

            }
        );

        return [
            {
                title: "Name",
                sortFunction: "string",
                data: data[0]
            },
            {
                title: "Players",
                sortFunction: "minmax",
                data: data[1]
            },
            {
                title: "Playtime",
                sortFunction: "minmax",
                data: data[2]
            },
            {
                title: "Plays",
                sortFunction: "number",
                data: data[3]
            },
            {
                title: "Rating",
                sortFunction: "number",
                data: data[4]
            },
            {
                title: "id",
                sortFunction: "none",
                data: data[5]
            }
        ];
    }

    goToGame(game) {
        this.props.history.push("/games/" + game.id);

    }

    downloadGames() {
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
                    let tableData = this.buildTableData(jsonObj);

                    this.setState({found: true, result: jsonObj, tableData: tableData});
                }
            }
        }.bind(this);
    }
}

export default withRouter(GamesView);