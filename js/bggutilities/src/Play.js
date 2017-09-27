import React, {Component} from 'react';
import {List, ListItem} from 'material-ui/List';
import Divider from 'material-ui/Divider';
import "./Main.css";
import LoadingScreen from "./util/LoadingScreen";
import {Redirect, withRouter} from "react-router-dom";

class Play extends Component {

    constructor(props) {
        super(props);
        this.state = {
            loading: true,
            play: undefined
        };
        var request = new XMLHttpRequest();
        request.timeout = 60000;
        request.open('GET', 'http://localhost:8080/getPlay?id=' + props.match.params.playId, true);
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
                        this.setState({play: jsonObj, loading: false});
                    }
                }
            }
        }.bind(this);
    }
    render() {
        if(this.state.loading) return <LoadingScreen/>;
        if(!this.state.loading && !this.state.play) return <Redirect to={"/plays"}/>;
        let play = this.state.play;
        let game = play.game;
        let players = [];
        play.playerNames.forEach(
            (name) => {
                if(play.winners.includes(name)) {
                    name += " 👑"
                }
                players.push(<ListItem key={name} primaryText={name} />)
            }
        );

        return <div className="main-block-play-players">
            <div className="main-width">
                <img src={game.image} alt={""} width={300}/>
                <div className="main-standard-description">
                    <div className="main-description-color" style={{marginRight: 65}}>Name</div>
                    <div>{game.name}</div>
                </div>
                <div className="main-standard-description">
                    <div className="main-description-color" style={{marginRight: 73}}>Date</div>
                    <div>{play.date}</div>
                </div>
                <div className="main-standard-description">
                    <div className="main-description-color" style={{marginRight: 54}}># Plays</div>
                    <div>{play.noOfPlays}</div>
                </div>
                <Divider style={{marginTop: 10, marginBottom: 10}}/>
                <List>
                    {players}
                </List>
            </div>
        </div>
    }
}


export default withRouter(Play);