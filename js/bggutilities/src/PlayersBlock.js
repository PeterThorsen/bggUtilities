import React, {Component} from 'react';

class PlayersBlock extends Component {
    render() {
        let minPlayers = this.props.minPlayers;
        let maxPlayers = this.props.maxPlayers;
        let bestWith = this.props.bestWith;
        let recommendedWith = this.props.recommendedWith;

        let tempList = [];
        for (let i = minPlayers; i <= maxPlayers; i++) {
            let currentStyle = {};
            currentStyle.padding = 5;
            currentStyle.fontSize = 16;
            if(bestWith.includes(i)) {
                currentStyle.backgroundColor = '#32CD32';
            }
            else if(recommendedWith.includes(i)) {
                currentStyle.backgroundColor = '#00FF00';
            }
            else {
                currentStyle.backgroundColor = '#8B0000';
            }
            tempList.push(<div style={currentStyle}>{i}</div>)

        }

        return <div style={{display: 'flex', flexDirection: 'row'}}>{tempList}</div>;
    }
}

export default PlayersBlock;
