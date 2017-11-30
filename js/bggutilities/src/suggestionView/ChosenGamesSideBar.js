import React, {Component} from 'react';

import '../Main.css';
import '../PickHelper.css';
import Divider from 'material-ui/Divider';

class ChosenGamesSideBar extends Component {
    // playTime, playerNames, allSelectedGames, fullScreen
    render() {
        if (this.props.fullScreen) {
            return <div className="wide-chosen-games-sidebar">
                <h1>Have fun!</h1>
                <p>You have chosen enough games to fill the time requested. You picked:</p>
                {this.props.allSelectedGames.map((suggestion) => {
                    return <p key={suggestion.game.name}>{suggestion.game.name + " (" + suggestion.approximateTime + " minutes)"}</p>;
                })}
            </div>
        }
        return <div className="chosen-games-sidebar">
            {this.renderPlayTimeAndPlayers()}
            <Divider/>
            {this.props.allSelectedGames.map(
                (suggestion) => {
                    return suggestion.game.name + " (Approx. " + suggestion.approximateTime + " minutes)";
                })}
        </div>
    }

    renderPlayTimeAndPlayers() {
        return <div>
            <h3>Time remaining: {this.props.playTime} minutes.</h3>
            <h3>Players:</h3>
            {this.props.playerNames.map(
                (name) => {
                    return <div key={"player-names-" + name} className="player-name">{name}</div>
                }
            )}
        </div>
    }
}

export default ChosenGamesSideBar;
