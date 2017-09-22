import React, {Component} from 'react';

class PlaysView extends Component {

    constructor(props) {
        super(props);
        this.state = {
            found: false,
            result: undefined
        }
    }

    render() {
        if (!this.state.found) {
            this.getPlays();

        }
        else {
            let jsonObj = JSON.parse(this.state.result);
            jsonObj.forEach(
                (play) => {
                    console.log("a")
                }
            )
        }
        return <div>PlaysView</div>
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
                    this.setState({found: true, result: result});
                }
            }
        }.bind(this);
    }
}


export default PlaysView;
