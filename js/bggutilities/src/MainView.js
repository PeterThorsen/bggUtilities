import React, {Component} from 'react';
import {GridList, GridTile} from 'material-ui/GridList';
import Snackbar from 'material-ui/Snackbar';
import './Main.css';
import {withRouter} from "react-router-dom";
class MainView extends Component {
    render() {
        let titlePosition = "top";
        let titleBackground = "linear-gradient(to bottom, rgba(0,0,0,0.7) 0%,rgba(0,0,0,0.3) 70%,rgba(0,0,0,0) 100%)";
        return <div className="main-block">
            <GridList cols={2}
                      style={{
                          width: 600,
                          overflow: 'hidden',
                          display: 'flex',
                          overflowY: 'auto'
                      }}>
                <GridTile key={"games-tile"}
                          title={"Games"}
                          onTouchTap={() => this.props.history.push("/games")}
                          titlePosition={titlePosition}
                          titleBackground={titleBackground}
                          cols={1}
                          rows={2}>
                    <img src={"https://i.imgur.com/Hhb7rsk.jpg"} alt={""}/>
                </GridTile>
                <GridTile key={"plays-tile"}
                          title={"Plays"}
                          onTouchTap={() => this.props.history.push("/plays")}
                          titlePosition={titlePosition}
                          titleBackground={titleBackground}
                          cols={1}
                          rows={2}>
                    <img src={"https://i.imgur.com/Laqqk9p.png"} alt={""}/>
                </GridTile>
                <GridTile key={"players-tile"}
                          title={"Players"}
                          onTouchTap={() => this.props.history.push("/players")}
                          titlePosition={titlePosition}
                          titleBackground={titleBackground}
                          cols={1}
                          rows={1}>
                    <img src={"https://i.imgur.com/B1QglHa.jpg"} alt={""}/>
                </GridTile>
                <GridTile key={"stats-tile"}
                          title={"Game statistics"}
                          onTouchTap={() => this.props.history.push("/statistics")}
                          titlePosition={titlePosition}
                          titleBackground={titleBackground}
                          cols={1}
                          rows={1}>
                    <img src={"https://i.imgur.com/jHlqEq1.png"} alt={""}/>
                </GridTile>
                <GridTile key={"suggest-game-night-tile"}
                          title={"Suggest a game night"}
                          onTouchTap={() => this.props.history.push("/suggest")}
                          titlePosition={titlePosition}
                          titleBackground={titleBackground}
                          cols={2}
                          rows={2}>
                    <img src={"https://i.imgur.com/95KOXM7.jpg"} alt={""}/>
                </GridTile>
            </GridList>
        </div>
    }

    forceReload() {
        var request = new XMLHttpRequest();
        request.timeout = 60000;
        request.open('GET', 'http://localhost:8080/forceLogin', true);
        request.send(null);
        request.onreadystatechange = function () {
            if (request.readyState === 4 && request.status === 200) {
                var type = request.getResponseHeader('Content-Type');
                if (type.indexOf("text") !== 1) {
                    let result = request.responseText;
                    if (result) {
                        return <Snackbar
                            open={true}
                            message="Data reloaded"
                            autoHideDuration={3000}/>
                    }
                    else {
                        return <Snackbar
                            open={true}
                            message="An error occurred. Please try again"
                            autoHideDuration={3000}
                        />
                    }
                }
            }
        }
    }
}

export default withRouter(MainView);
