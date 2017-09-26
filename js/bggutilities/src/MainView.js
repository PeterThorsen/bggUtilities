import React, {Component} from 'react';
import {GridList, GridTile} from 'material-ui/GridList';
import GamesView from './GamesView';
import PlaysView from './PlaysView';
import PlayersView from './PlayersView';
import Snackbar from 'material-ui/Snackbar';
import RaisedButton from 'material-ui/RaisedButton';
import './Main.css';
import {Link, Route} from "react-router-dom";

class MainView extends Component {

    constructor(props) {
        super(props);
        this.state = {
            menu: null
        }
    }

    render() {

        if (this.state.menu) {
            let menu = this.state.menu;

            if (menu === "games") {
                return <GamesView userName={this.props.userName} goBack={() => this.goTo(null)}/>
            }
            if (menu === "plays") {
                return <PlaysView userName={this.props.userName} goBack={() => this.goTo(null)}/>
            }
            if (menu === "players") {
                return <PlayersView userName={this.props.userName} goBack={() => this.goTo(null)}/>
            }
        }

        return <div className="main-block">
            <GridList cols={2}
                      style={{
                          width: 600,
                          overflow: 'hidden',
                          display: 'flex',
                          overflowY: 'auto'
                      }}>
                <GridTile key={"games-tile"}
                          title={"Games"}
                          onTouchTap={() => this.goTo("games")}
                          titlePosition="top"
                          titleBackground="linear-gradient(to bottom, rgba(0,0,0,0.7) 0%,rgba(0,0,0,0.3) 70%,rgba(0,0,0,0) 100%)"
                          cols={1}
                          rows={2}>
                    <img src={"https://i.imgur.com/Hhb7rsk.jpg"} alt={""}/>
                </GridTile>
                <GridTile key={"plays-tile"}
                          title={"Plays"}
                          onTouchTap={() => this.goTo("plays")}
                          titlePosition="top"
                          titleBackground="linear-gradient(to bottom, rgba(0,0,0,0.7) 0%,rgba(0,0,0,0.3) 70%,rgba(0,0,0,0) 100%)"
                          cols={1}
                          rows={2}>
                    <img src={"https://i.imgur.com/Laqqk9p.png"} alt={""}/>
                </GridTile>
                <GridTile key={"players-tile"}
                          title={"Players"}
                          onTouchTap={() => this.goTo("players")}
                          titlePosition="top"
                          titleBackground="linear-gradient(to bottom, rgba(0,0,0,0.7) 0%,rgba(0,0,0,0.3) 70%,rgba(0,0,0,0) 100%)"
                          cols={1}
                          rows={1}>
                    <img src={"https://i.imgur.com/B1QglHa.jpg"} alt={""}/>
                </GridTile>
                <GridTile key={"stats-tile"}
                          title={"Game statistics"}
                          titlePosition="top"
                          titleBackground="linear-gradient(to bottom, rgba(0,0,0,0.7) 0%,rgba(0,0,0,0.3) 70%,rgba(0,0,0,0) 100%)"
                          cols={1}
                          rows={1}>
                    <img src={"https://i.imgur.com/jHlqEq1.png"} alt={""}/>
                </GridTile>
                <GridTile key={"suggest-game-night-tile"}
                          title={"Suggest a game night"}
                          titlePosition="top"
                          titleBackground="linear-gradient(to bottom, rgba(0,0,0,0.7) 0%,rgba(0,0,0,0.3) 70%,rgba(0,0,0,0) 100%)"
                          cols={2}
                          rows={2}>
                    <img src={"https://i.imgur.com/95KOXM7.jpg"} alt={""}/>
                </GridTile>
            </GridList>
            <div>
                <Link to="/about">About</Link>
                <Route path="/about" component={GamesView}/>
            </div>
            {/*<RaisedButton label="Reload data" onTouchTap={this.forceReload}/>*/}
        </div>
    }

    goTo(menu) {
        this.setState({menu: menu})
    }

    forceReload() {
        var request = new XMLHttpRequest();
        request.timeout = 60000;
        request.open('GET', 'http://localhost:8080/forceLogin', true);
        request.send(null);
        request.onreadystatechange = function () {
            if (request.readyState === 4 && request.status === 200) {
                var type = request.getResponseHeader('Content-Type');
                if (type.indexOf("text") !== 1) {
                    let result = request.responseText;
                    console.log("result", result)
                    if (result) {
                        <Snackbar
                            open={true}
                            message="Data reloaded"
                            autoHideDuration={3000}/>
                    }
                    else {
                        <Snackbar
                            open={true}
                            message="An error occurred. Please try again"
                            autoHideDuration={3000}
                        />
                    }
                }
            }
        }.bind(this);
    }
}

/*
<GamesView userName={this.props.userName} goBack={() => this.goTo(null)}/>
* */


export default MainView;
