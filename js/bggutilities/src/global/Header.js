import React, {Component} from 'react';

import './Header.css';
import LeftSideBar from "./LeftSideBar";
import RightSideBar from "./RightSideBar";
import {withRouter} from "react-router-dom";

class Header extends Component {

    render() {

        return <div>
            <div className="header-bar">
                <div className="header-bar-title" onClick={() => {
                    this.props.history.push("/");
                }}>bggUtilities
                </div>
            </div>
            {/*
            <LeftSideBar />
            <RightSideBar/>*/}
        </div>
    }
}

export default withRouter(Header);
