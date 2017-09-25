import React, {Component} from 'react';
import {List, ListItem} from 'material-ui/List';
import Divider from 'material-ui/Divider';
import RaisedButton from 'material-ui/RaisedButton';
import "./Play.css";
import "./Main.css";

class Play extends Component {
    render() {
        let play = this.props.play;
        let game = play.game;
        let players = [];
        play.playerNames.forEach(
            (name) => {
                if(play.winners.includes(name)) {
                    name += " 👑"
                }
                players.push(<ListItem key={name} primaryText={name} />)
            }
        )

        return <div className="play-main-block">
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
                <RaisedButton style={{marginTop: 10}} label="Go back" onTouchTap={this.props.goBack}/>
                <Divider style={{marginTop: 10, marginBottom: 10}}/>
                <List>
                    {players}
                </List>
            </div>
        </div>
    }
}


export default Play;