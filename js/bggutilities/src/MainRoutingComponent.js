import React, {Component} from 'react';
import LoginView from "./LoginView";
import GamesView from "./GamesView";
import PlaysView from "./PlaysView";
import PlayersView from "./PlayersView";
import MainView from "./MainView";
import {Redirect, Route, Switch} from "react-router-dom";
import Game from "./Game";
import Play from "./Play";
import Player from "./Player";

class MainRoutingComponent extends Component {

    render() {
        return <Switch>
            <Route path={"/"} exact component={MainView}/>
            <Route path={"/login"} exact component={LoginView}/>
            <Route path={"/games"} exact component={GamesView}/>
            <Route path={"/games/:gameId"} exact component={Game}/>
            <Route path={"/plays"} exact component={PlaysView}/>
            <Route path={"/plays/:playId"} exact component={Play}/>
            <Route path={"/players"} exact component={PlayersView}/>
            <Route path={"/players/:name"} exact component={Player}/>
            <Redirect to={"/login"}/>
        </Switch>
    }
}

export default MainRoutingComponent;
