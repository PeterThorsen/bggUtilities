import React, {Component} from 'react';

import './LeftSideBar.css';
import {withRouter} from "react-router-dom";
import FlatButton from 'material-ui/FlatButton';

class LeftSideBar extends Component {

    render() {

        let urls = [
            {
                url: "/",
                text: "Home"
            },
            {
                url: "/games",
                text: "All games"
            },
            {
                url: "/plays",
                text: "All plays"
            },
            {
                url: "/players",
                text: "All players"
            },
            /*{
                url: "/statistics",
                text: "Statistics"
            },*/
            {
                url: "/suggest",
                text: "Suggest game night"
            },
        ];

        return <div className="left-side-bar">
            {urls.map(
                (object) => {
                    return <FlatButton label={object.text} onTouchTap={() => {this.props.history.push(object.url)}}/>
                }
            )}
        </div>
    }
}

export default withRouter(LeftSideBar);
