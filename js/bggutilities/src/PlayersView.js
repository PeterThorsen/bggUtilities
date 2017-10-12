import React, {Component} from 'react';
import LoadingScreen from './util/LoadingScreen';
import SortableTable from "./util/SortableTable";

class PlayersView extends Component {

    constructor(props) {
        super(props);
        this.state = {
            found: false,
            tableData: undefined
        }
    }

    render() {
        let mainBlock = <div/>;
        if (!this.state.found) {
            this.getPlayers();
            mainBlock = <LoadingScreen/>
        }
        else {
            mainBlock = <SortableTable tableData={this.state.tableData} link="/players/" linkSuffixHandler="Name" />;
        }


        return <div>
            {mainBlock}
        </div>
    }

    getPlayers() {
        var request = new XMLHttpRequest();
        request.timeout = 60000;
        request.open('GET', 'http://localhost:8080/getAllPlayers', true);
        request.send(null);
        request.onreadystatechange = function () {
            if (request.readyState === 4 && request.status === 200) {
                var type = request.getResponseHeader('Content-Type');
                if (type.indexOf("text") !== 1) {
                    let result = request.responseText;
                    let jsonObj = JSON.parse(result);
                    jsonObj.forEach(
                        (obj) => {
                            obj.allPlays = undefined;
                            obj.gameRatingsMap = undefined;
                            obj.gameToPlaysMap = undefined;
                            obj.playerNameToPlaysMap = undefined;
                        }
                    );

                    let data = [];
                    for(let i = 0; i< 6; i++) {
                        data.push([]);
                    }

                    jsonObj.forEach(
                        (player) => {
                            data[0].push(player.name);
                            data[1].push(player.totalPlays);
                            data[2].push(parseFloat(player.minComplexity).toFixed(2));
                            data[3].push(parseFloat(player.maxComplexity).toFixed(2));
                            data[4].push(parseFloat(player.averageComplexity).toFixed(2));

                            let magicComplexity = parseFloat(player.magicComplexity).toFixed(2);
                            data[5].push(magicComplexity !== '0.00' ? magicComplexity : "N/A");
                        }
                    );

                    let tableData = [
                        {
                            title: "Name",
                            sortFunction: "string",
                            data: data[0]
                        },
                        {
                            title: "Plays",
                            sortFunction: "number",
                            data: data[1]
                        },
                        {
                            title: "Min",
                            sortFunction: "number",
                            data: data[2]
                        },
                        {
                            title: "Max",
                            sortFunction: "number",
                            data: data[3]
                        },
                        {
                            title: "Average",
                            sortFunction: "number",
                            data: data[4]
                        },
                        {
                            title: "Magic",
                            sortFunction: "number",
                            data: data[5]
                        },

                    ];

                    this.setState({
                        found: true,
                        currentName: "name",
                        tableData: tableData
                    });
                }
            }
        }.bind(this);
    }
}


export default PlayersView;
