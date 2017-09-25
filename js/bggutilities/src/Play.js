import React, {Component} from 'react';
import {List, ListItem} from 'material-ui/List';
import Divider from 'material-ui/Divider';
import RaisedButton from 'material-ui/RaisedButton';

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

        return <div style={{
            display: 'flex', flexWrap: 'wrap', justifyContent: 'space-around',
            flexDirection: 'column', alignItems: 'center', alignContent: 'center', marginTop: 10
        }}>
            <div style={{width: 600}}>
                <img src={game.image} alt={""} width={300}/>
                <div style={{display: 'flex', flexDirection: 'row', marginTop: 2}}>
                    <div style={{marginRight: 65, color: 'grey'}}>Name</div>
                    <div>{game.name}</div>
                </div>
                <div style={{display: 'flex', flexDirection: 'row', marginTop: 2}}>
                    <div style={{marginRight: 73, color: 'grey'}}>Date</div>
                    <div>{play.date}</div>
                </div>
                <div style={{display: 'flex', flexDirection: 'row', marginTop: 2}}>
                    <div style={{marginRight: 54, color: 'grey'}}># Plays</div>
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