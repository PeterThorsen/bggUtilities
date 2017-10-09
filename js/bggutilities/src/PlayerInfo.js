import React, {Component} from 'react';
import "./Main.css";

class PlayerInfo extends Component {
    render() {
        let player = this.props.player;
        return <div>
            <h1>{player.name} </h1>
            <div className="main-standard-description">
                <div className="main-description-color" style={{marginRight: 115}}># Plays</div>
                <div>{player.totalPlays}</div>
            </div>
            <div className="main-standard-description">
                <div className="main-description-color" style={{marginRight: 57}}>Min. complexity</div>
                <div>{parseFloat(player.minComplexity).toFixed(2)}</div>
            </div>
            <div className="main-standard-description">
                <div className="main-description-color" style={{marginRight: 54}}>Max. complexity</div>
                <div>{parseFloat(player.maxComplexity).toFixed(2)}</div>
            </div>
            <div className="main-standard-description">
                <div className="main-description-color" style={{marginRight: 28}}>Average complexity</div>
                <div>{parseFloat(player.averageComplexity).toFixed(2)}</div>
            </div>
            <div className="main-standard-description">
                <div className="main-description-color" style={{marginRight: 46}}>Magic complexity</div>
                <div>{parseFloat(player.magicComplexity).toFixed(2)}</div>
            </div>
        </div>
    }
}


export default PlayerInfo;
