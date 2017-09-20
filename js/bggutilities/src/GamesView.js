import React, {Component} from 'react';
import LoadingScreen from './LoadingScreen';
import {GridList, GridTile} from 'material-ui/GridList';


class GamesView extends Component {

    constructor(props) {
        super(props);
        this.state = {found: false, result: ""}
    }

    render() {

        if (!this.state.found) {
            var request = new XMLHttpRequest();
            request.timeout = 60000;
            request.open('GET', 'http://localhost:8080/getGames', true);
            request.send(null);
            request.onreadystatechange = function () {
                if (request.readyState === 4 && request.status === 200) {
                    var type = request.getResponseHeader('Content-Type');
                    if (type.indexOf("text") !== 1) {
                        let result = request.responseText;
                        this.setState({found: true, result: result});
                    }
                }
            }.bind(this);
        }
        let result = [];
        if (this.state.found) {
            let jsonObj = null;
            jsonObj = JSON.parse(this.state.result);
            jsonObj.forEach(
                (game) => {
                    result.push(
                        <GridTile key={game.id}
                                  cols={1}
                                  rows={1}>
                            <img src={game.image} alt={""} width={146} height={180}/> </GridTile>)
                });
        }

        if (!this.state.found) return <LoadingScreen/>;

        return <div style={{display: 'flex', flexWrap: 'wrap', justifyContent: 'space-around'}}>
            <GridList cols={4}
                      style={{
                          width: 600,
                          overflow: 'hidden',
                          display: 'flex',
                          overflowY: 'auto'
                      }}>

                {result}
            </GridList>
        </div>
    }
}


export default GamesView;
