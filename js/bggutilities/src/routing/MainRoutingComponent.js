import React, {Component} from 'react';
import LoginView from "../LoginView";
import GamesView from "../GamesView";
import PlaysView from "../PlaysView";
import PlayersView from "../PlayersView";
import MainView from "../MainView";
import {Redirect, Route, Switch} from "react-router-dom";
import Game from "../Game";
import Play from "../Play";
import Player from "../Player";
import AuthorizedRoute from "./AuthorizedRoute";
import StatisticsView from "../StatisticsView";
import SuggestionsView from "../suggestionView/SuggestionsView";
import PickHelper from "../suggestionView/PickHelper";
import SuggestByMagic from "../suggestionView/SuggestByMagic";
class MainRoutingComponent extends Component {

    render() {
        return <Switch>
            <Route path={"/login"} exact component={LoginView}/>
            <AuthorizedRoute path={"/"} exact component={MainView}/>
            <AuthorizedRoute path={"/games"} exact component={GamesView}/>
            <AuthorizedRoute path={"/games/:gameId"} exact component={Game}/>
            <AuthorizedRoute path={"/plays"} exact component={PlaysView}/>
            <AuthorizedRoute path={"/plays/:playId"} exact component={Play}/>
            <AuthorizedRoute path={"/players"} exact component={PlayersView}/>
            <AuthorizedRoute path={"/players/:name"} exact component={Player}/>
            <AuthorizedRoute path={"/statistics"} exact component={StatisticsView}/>
            <AuthorizedRoute path={"/suggest"} exact component={SuggestionsView}/>
            <AuthorizedRoute path={"/suggest/help"} exact component={PickHelper}/>
            <AuthorizedRoute path={"/suggest/magic"} exact component={SuggestByMagic}/>
            <Redirect to={"/login"}/>
        </Switch>
    }
}

export default MainRoutingComponent;
