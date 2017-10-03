import React, {Component} from 'react';
import {GridList, GridTile} from 'material-ui/GridList';

class SuggestionsView extends Component {

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
                <GridTile key={"present-data-tile"}
                          title={"Help me pick!"}
                          onTouchTap={() => this.props.history.push("/suggest/help")}
                          titlePosition={titlePosition}
                          titleBackground={titleBackground}
                          cols={2}
                          rows={2}>
                    <img src={"https://c1.staticflickr.com/4/3695/9681099086_accb793d2b_z.jpg"} alt={""}/>
                </GridTile>
                <GridTile key={"magic-tile"}
                          title={"Pick for me!"}
                          onTouchTap={() => this.props.history.push("/suggest/magic")}
                          titlePosition={titlePosition}
                          titleBackground={titleBackground}
                          cols={2}
                          rows={2}>
                    <img src={"https://upload.wikimedia.org/wikipedia/commons/e/ec/Awesome-beautiful-wallpaper_124413582_294.jpg"} alt={""}/>
                </GridTile>
            </GridList>
        </div>
    }
}

export default SuggestionsView;
