import React, {Component} from 'react';
import "../Main.css";
class BestWithBlock extends Component {
    render() {
        let minPlayers = this.props.minPlayers;
        let maxPlayers = this.props.maxPlayers;
        let bestWith = this.props.bestWith;
        let recommendedWith = this.props.recommendedWith;

        let tempList = [];
        for (let i = minPlayers; i <= maxPlayers; i++) {
            let currentStyle = {};
            currentStyle.padding = 3;
            currentStyle.fontSize = 16;
            if(bestWith.includes(i)) {
                currentStyle.backgroundColor = '#32CD32';
            }
            else if(recommendedWith.includes(i)) {
                currentStyle.backgroundColor = '#7FFF00';
            }
            else {
                currentStyle.backgroundColor = '#DC143C';
            }
            tempList.push(<div key={"players-block-" + i} style={currentStyle}>{i}</div>)

        }

        return <div className="flex-row">{tempList}</div>;
    }
}

export default BestWithBlock;
