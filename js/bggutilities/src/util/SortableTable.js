import React, {Component} from 'react';
import {withRouter} from "react-router-dom";
import {
    Table,
    TableBody,
    TableHeader,
    TableHeaderColumn,
    TableRow,
    TableRowColumn,
} from 'material-ui/Table';
import FlatButton from 'material-ui/FlatButton';
import {deepSlice} from "./GeneralUtil";

class SortableTable extends Component {

    constructor(props) {
        super(props);

        let sortArrays = [];
        let tableData = deepSlice(this.props.tableData);
        tableData.forEach(
            (dataPoint) => {
                sortArrays.push(this.getSortedArray(dataPoint));
            }
        );

        this.state = {
            isReversed: false,
            sortByColumn: 0,
            currentTableData: this.sort(0, deepSlice(this.props.tableData), sortArrays),
            sortArrays: sortArrays,
            originalData: deepSlice(this.props.tableData)
        };
    }

    render() {
        let buttonStyle = {
            textAlign: 'left',
            marginLeft: -15
        };

        let dataLength = this.state.currentTableData[0].data.length;
        let rows = [];

        for (let i = 0; i < dataLength; i++) {
            let tableData = this.state.currentTableData;
            rows.push(
                <TableRow key={"row-" + i} selectable={false}
                          onTouchTap={() => this.goToLink(tableData, i)}>
                    {tableData.map(
                        (dataPoint, j) => {
                            if (dataPoint.title !== "id") {

                                return <TableRowColumn
                                    key={"table-data-point-" + i + "-" + j}
                                    style={{wordWrap: 'break-word', whiteSpace: 'normal'}}
                                >
                                    {dataPoint.data[i]}
                                </TableRowColumn>;
                            }
                            else return undefined;
                        }
                    )}
                </TableRow>);
        }

        return <div className="main-block">
            <Table style={{width: 700}}>
                <TableHeader displaySelectAll={false} adjustForCheckbox={false}>
                    <TableRow>
                        {this.state.currentTableData.map(
                            (tableHeader, i) => {
                                if (tableHeader.title !== "id") {
                                    return <TableHeaderColumn key={"header-column-" + i} style={{
                                        wordWrap: 'break-word',
                                        whiteSpace: 'normal'
                                    }}>
                                        <FlatButton label={tableHeader.title} fullWidth={true}
                                                    labelStyle={{textTransform: 'none'}}
                                                    style={buttonStyle}
                                                    hoverColor="none" disableTouchRipple={true}
                                                    onTouchTap={this.sort.bind(this, i)}/>
                                    </TableHeaderColumn>
                                }
                                else return undefined;
                            }
                        )}
                    </TableRow>
                </TableHeader>
                <TableBody displayRowCheckbox={false}>
                    {rows}
                </TableBody>
            </Table>
        </div>;
    }

    sort(column, initialTable, sortArrays) {
        let thisTable = initialTable ? initialTable : this.state.originalData;
        sortArrays = sortArrays ? sortArrays : this.state.sortArrays;
        let newData = [];
        for (let i = 0; i < sortArrays.length; i++) {
            newData.push([]);
        }
        let sortingArray = deepSlice(sortArrays);
        sortingArray = sortingArray[column];
        let shouldReverse = !initialTable && column === this.state.sortByColumn && !this.state.isReversed;

        if (shouldReverse) {
            sortingArray.reverse();
        }

        for (let i = 0; i < newData.length; i++) {
            let newDataArray = newData[i];
            sortingArray.forEach(
                (position) => {
                    let dataSlice = deepSlice(thisTable[i].data);
                    newDataArray.push(dataSlice[position]);
                }
            );
        }


        let saveData = deepSlice(initialTable);
        saveData.forEach(
            (dataPoint, i) => {
                dataPoint.data = newData[i];
            }
        );
        if(initialTable) return saveData;
        else {
            this.setState({
                currentTableData: saveData,
                sortByColumn: column,
                isReversed: shouldReverse
            })
        }

    }

    goToLink(tableData, i) {
        let linkSuffix = this.props.linkSuffixHandler;
        if (linkSuffix) {
            tableData.forEach(
                (dataPoint) => {
                    if (dataPoint.title === linkSuffix) {
                        linkSuffix = dataPoint.data[i];
                    }
                }
            )
        }
        else if (this.props.linkSuffixFunction) {
            this.props.linkSuffixFunction(tableData, i);
            return;
        }
        this.props.history.push(this.props.link + linkSuffix);

    }

    /*
    * DataPoint should have form:
    * title (string)
    * sortFunction (string), either "string" or "number"
    * data (array)
    *
    *
    * Returns a list of indices pointint to the original order
    * */
    getSortedArray(dataPoint) {
        let sortFunction = dataPoint.sortFunction;
        let sliceOfOriginalData = deepSlice(dataPoint.data);
        let len = sliceOfOriginalData.length;
        let indices = new Array(len);
        for (let i = 0; i < len; ++i) indices[i] = i;
        if (sortFunction === "number") {
            indices.sort(
                (x, y) => {
                    return sliceOfOriginalData[y] - sliceOfOriginalData[x]
                });
            return indices;
        }
        else if (sortFunction === "string") {
            indices.sort(function (a, b) {
                return sliceOfOriginalData[a].localeCompare(sliceOfOriginalData[b]);
            });
            return indices;
        }
        else if (sortFunction === "date") {
            indices.sort(function (a, b) {
                return sliceOfOriginalData[b].localeCompare(sliceOfOriginalData[a]);
            });
            return indices;
        }
    }

}


export default withRouter(SortableTable);