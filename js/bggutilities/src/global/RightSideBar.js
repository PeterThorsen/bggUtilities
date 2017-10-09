import React, {Component} from 'react';

import './RightSideBar.css';

class RightSideBar extends Component {

    render() {

        let factSource = [
            "Fun fact 1",
            "Fun fact 2",
            "Fun fact 3",
            "Fun fact 4",
        ];
        return <div className="right-side-bar">
            <h3>Did you know?</h3>
            {factSource.map(
                (fact, i) => {
                    return <div key={"fact-" + i}>- {fact}</div>
                }
            )}
        </div>
    }
}

export default RightSideBar;
