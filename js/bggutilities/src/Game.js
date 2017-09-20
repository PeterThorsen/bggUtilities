import React, {Component} from 'react';
import Divider from 'material-ui/Divider';
import RaisedButton from 'material-ui/RaisedButton';
import CircularProgress from 'material-ui/CircularProgress';
import {List, ListItem} from 'material-ui/List';

class Game extends Component {

    constructor(props) {
        super(props);
        this.state = {
            plays: undefined
        }
    }

    render() {
        /*
  public final int maxPlaytime;
  public final int minPlaytime;
  public final int maxPlayers;
  public final int minPlayers;
  public final int id;
  public final String name;
  public final String personalRating;
  public final int numPlays;
  public final String averageRating;
  public double complexity = 0.0;
  public boolean isExpansion;
  public GameCategory[] categories;
  public GameMechanism[] mechanisms;
  public int[] bestWith;
  public int[] recommendedWith;
  public final String type; // Type might be null, always check for null
  public final String image;
        */

        console.log(this.props.game);
        let game = this.props.game;
        let expansions = game.expansions;
        let minPlayers = game.minPlayers;
        let maxPlayers = game.maxPlayers;
        let minPlaytime = game.minPlaytime;
        let maxPlaytime = game.maxPlaytime;
        expansions.forEach(
            (expansion) => {
                if (expansion.minPlayers < minPlayers) minPlayers = expansion.minPlayers;
                if (expansion.maxPlayers > maxPlayers) maxPlayers = expansion.maxPlayers;
                if (expansion.minPlaytime < minPlaytime) minPlaytime = expansion.minPlaytime;
                if (expansion.maxPlaytime > maxPlaytime) maxPlaytime = expansion.maxPlaytime;
            }
        );

        let plays = [];
        let playerRatings = {};
        let playerRatingsArr = [];
        if (this.state.plays === undefined) {
            var request = new XMLHttpRequest();
            request.timeout = 60000;
            request.open('GET', 'http://localhost:8080/getPlays?id=' + game.id, true);
            request.send(null);
            request.onreadystatechange = function () {
                if (request.readyState === 4 && request.status === 200) {
                    var type = request.getResponseHeader('Content-Type');
                    if (type.indexOf("text") !== 1) {
                        let result = request.responseText;
                        this.setState({plays: result});
                    }
                }
            }.bind(this);
        }
        else {
            let jsonObj = null;
            jsonObj = JSON.parse(this.state.plays);
            let iteration = 0;
            jsonObj.forEach(
                (play) => {
                    for (let playerName in play.playerRatings) {
                        if (play.playerRatings.hasOwnProperty(playerName)) {
                            let rating = play.playerRatings[playerName];
                            if (!playerRatings.hasOwnProperty(playerName)) {
                                playerRatings[playerName] = {};
                                if (rating > 0) {
                                    playerRatings[playerName].rating = rating;
                                }
                                playerRatings[playerName].numberOfPlays = 1;
                            }
                            else {
                                if(rating > 0 && !playerRatings[playerName].hasOwnProperty("rating")) {
                                    playerRatings[playerName].rating = rating;
                                }
                                playerRatings[playerName].numberOfPlays = playerRatings[playerName].numberOfPlays + 1;
                            }
                        }
                    }
                    plays.push(<ListItem key={"play-" + iteration} primaryText={play.date + " : " + play.noOfPlays}/>)
                    iteration++;
                }
            )
            iteration = 0;
            for (let playerName in playerRatings) {
                if (playerRatings.hasOwnProperty(playerName)) {
                    playerRatingsArr.push(<ListItem key={"player-rating-" + iteration}
                                                    primaryText={playerName + "(" + playerRatings[playerName].numberOfPlays + " plays): " + playerRatings[playerName].rating}/>)
                    iteration++;
                }
            }
        }

        let playsBlock = <div style={{width: 295, borderRight: '1px solid grey', paddingRight: 5}}>
            <div style={{fontSize: 30}}>Plays</div>
            {this.state.plays !== undefined ?
                <List>
                    {plays}
                </List>
                : <CircularProgress/>} </div>;

        let playerRatingsBlock = <div style={{width: 295, paddingLeft: 5}}>
            <div style={{fontSize: 30}}>Player ratings</div>
            {
                this.state.plays !== undefined ?
                    <List>
                        {playerRatingsArr}
                    </List> : <CircularProgress/>}
        </div>


        return <div
            style={{
                display: 'flex', flexWrap: 'wrap', justifyContent: 'space-around', flexDirection: 'column',
                alignItems: 'center', alignContent: 'center', marginTop: 10
            }}>
            <div style={{width: 600}}>
                <img src={game.image} alt={""} width={300}/>
                <div style={{display: 'flex', flexDirection: 'row'}}>
                    <div style={{marginRight: 5, color: 'grey'}}>Name</div>
                    <div>{game.name}</div>
                </div>
                <div style={{display: 'flex', flexDirection: 'row'}}>
                    <div style={{marginRight: 5, color: 'grey'}}>Playtime</div>
                    <div>{minPlaytime + (minPlaytime !== maxPlaytime ?
                        " - " + maxPlaytime : "") + " minutes"}</div>
                </div>
                <div style={{display: 'flex', flexDirection: 'row'}}>
                    <div style={{marginRight: 5, color: 'grey'}}>Players</div>
                    <div>{minPlayers + (minPlayers !== maxPlayers ?
                        " - " + maxPlayers : "")}</div>
                </div>
                <RaisedButton label="Go back" onTouchTap={this.props.goBack}/>
                <Divider style={{marginTop: 10, marginBottom: 10}}/>
            </div>
            <div style={{display: 'flex', flexDirection: 'row'}}>
                {playsBlock}
                {playerRatingsBlock}
            </div>
        </div>
    }
}


export default Game;
