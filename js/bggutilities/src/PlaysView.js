import React, {Component} from 'react';
import LoadingScreen from './util/LoadingScreen';
import './Main.css';
import {getPlayersString} from "./util/GeneralUtil";
import SortableTable from "./util/SortableTable";

class PlaysView extends Component {

    constructor(props) {
        super(props);
        this.state = {
            found: false,
            tableData: undefined,
        }
        this.getPlays();
    }

    render() {
        let mainBlock = <div/>;

        if (!this.state.found) {
            mainBlock = <LoadingScreen/>
        }
        else {
            mainBlock = <SortableTable tableData={this.state.tableData} link="/plays/" linkSuffixHandler="id"/>
        }
        return <div>
            {mainBlock}
        </div>
    }

    getPlays() {
        var request = new XMLHttpRequest();
        request.timeout = 60000;
        request.open('GET', 'http://localhost:8080/getAllPlays', true);
        request.send(null);
        request.onreadystatechange = function () {
            if (request.readyState === 4 && request.status === 200) {
                var type = request.getResponseHeader('Content-Type');
                if (type.indexOf("text") !== 1) {
                    let result = request.responseText;
                    let jsonObj = JSON.parse(result);
                    let tableData = this.buildTableData(jsonObj);
                    this.setState({found: true, tableData: tableData});
                }
            }
        }.bind(this);
    }

    buildTableData(allPlays) {
        let data = [];
        for (let i = 0; i < 5; i++) {
            data.push([]);
        }

        allPlays.forEach(
            (play) => {
                data[0].push(play.date);
                data[1].push(play.game.name);
                data[2].push(getPlayersString(play.playerNames));
                data[3].push(play.noOfPlays);
                data[4].push(play.id);
            }
        );

        return [
            {
                title: "Date",
                sortFunction: "date",
                data: data[0]
            },
            {
                title: "Name",
                sortFunction: "string",
                data: data[1]
            },
            {
                title: "Players",
                sortFunction: "string",
                data: data[2]
            },
            {
                title: "Plays",
                sortFunction: "number",
                data: data[3]
            },
            {
                title: "id",
                sortFunction: "none",
                data: data[4]
            }
        ];
    }
}


export default PlaysView;
