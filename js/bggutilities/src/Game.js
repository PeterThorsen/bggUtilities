import React, {Component} from 'react';
import Divider from 'material-ui/Divider';
import RaisedButton from 'material-ui/RaisedButton';

class Game extends Component {

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
                    <div>{game.minPlaytime + (game.minPlaytime !== game.maxPlaytime ?
                        " - " + game.maxPlaytime : "") + " minutes"}</div>
                </div>
                <RaisedButton label="Go back" onTouchTap={this.props.goBack}/>
                <Divider style={{marginTop: 10, marginBottom: 10}}/>
            </div>
        </div>
    }
}


export default Game;
