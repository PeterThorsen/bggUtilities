import React, {Component} from 'react';
import AutoComplete from 'material-ui/AutoComplete';
import RaisedButton from 'material-ui/RaisedButton';
import Divider from 'material-ui/Divider';
import Slider from 'material-ui/Slider';
import {toTitleCase} from "../util/GeneralUtil";

import '../Main.css';
import LoadingScreen from "../util/LoadingScreen";

class SuggestByMagic extends Component {

    constructor(props) {
        super(props);
        this.state = {
            dataSource: [],
            loadingPlayers: true,
            textFields: [""],
            playTime: 60,
        };
        this.downloadAllPlayers();
    }

    render() {
        if (this.state.loadingPlayers) return <LoadingScreen/>;

        let textFields = this.buildTextFields();
        let playTime = this.buildPlayTime();
        return <div className="outer-block">
            <div className="main-width">

                <div>
                    {textFields}
                    <Divider style={{marginTop: 10, marginBottom: 10}}/>
                    {playTime}
                    <RaisedButton primary={true} label={"Suggest games"} onTouchTap={this.validateAndDownload.bind(this)}/>
                </div>
            </div>
        </div>
    }

    handleUpdateInput(text, fieldNumber) {
        let textFields = this.state.textFields;
        textFields[fieldNumber] = text;
        this.setState(
            {
                textFields: textFields
            }
        )
    }

    downloadAllPlayers() {
        var request = new XMLHttpRequest();
        request.timeout = 60000;
        request.open('GET', 'http://localhost:8080/getAllPlayers?nameOnly=true', true);
        request.send(null);
        request.onreadystatechange = function () {
            if (request.readyState === 4 && request.status === 200) {
                var type = request.getResponseHeader('Content-Type');
                if (type.indexOf("text") !== 1) {
                    let result = request.responseText;
                    let jsonObj = JSON.parse(result);

                    let dataSource = [];
                    jsonObj.forEach(
                        (player) => {
                            dataSource.push(toTitleCase(player))
                        }
                    );
                    this.setState({loadingPlayers: false, dataSource: dataSource});
                }
            }
        }.bind(this);
    }

    buildTextFields() {
        return <div className="flex-column-center">
            <h2>Type in player names</h2>
            {this.state.textFields.map(
                (text, i) => {
                    return <AutoComplete
                        key={"auto-complete-field-" + i}
                        hintText="Type a name"
                        filter={AutoComplete.caseInsensitiveFilter}
                        dataSource={this.state.dataSource}
                        onUpdateInput={(text) => {
                            this.handleUpdateInput(text, i)
                        }}
                        maxSearchResults={5}
                    />;
                })}

            <RaisedButton
                onTouchTap={this.addField.bind(this)}
                label="Add a player"
                style={{marginTop: 10, width: 200}}/>
        </div>
    }

    addField() {
        let textFields = this.state.textFields;
        textFields.push("");
        this.setState(
            {
                textFields: textFields
            }
        )
    }


    buildPlayTime() {
        let multiplier = 180; // max = 180 minutes
        return <div>
            <h2>Select play time</h2>
            <Slider min={15 / multiplier} onChange={this.changePlayTime.bind(this)} step={15 / multiplier}
                    defaultValue={60 / multiplier}/>
            <div>Playtime: {this.state.playTime} minutes.</div>
        </div>
    }

    changePlayTime(event, time) {
        let multiplier = 180; // max 180 minutes
        let playTime = parseFloat(time * multiplier).toFixed(0);
        this.setState({playTime: playTime});
    }


    validateAndDownload() {
        // Validate input
        let invalidInputs = [];
        this.state.textFields.forEach(
            (inputText, i) => {
                let found = false;
                this.state.dataSource.forEach(
                    (name) => {
                        if (inputText.toLowerCase() === name.toLowerCase()) {
                            found = true;
                        }
                    }
                );
                if (!found) {
                    invalidInputs.push(i);
                }
            }
        );
        if (invalidInputs.length !== 0) {
            // set snackbar
            console.log("invalidInputs", invalidInputs)
            return;
        }

        // Finally, download
        this.props.download(this.state.textFields, this.state.playTime);
    }
}

export default SuggestByMagic;
