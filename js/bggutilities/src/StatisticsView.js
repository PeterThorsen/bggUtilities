import React, {Component} from 'react';
import {BarChart, PieChart} from "react-d3-basic";
import {Hint, RadialChart} from 'react-vis';
import '../node_modules/react-vis/dist/style.css';
import {VictoryBar, VictoryChart, VictoryAxis, VictoryTheme} from 'victory';

class StatisticsView extends Component {

    constructor(props) {
        super(props);
        this.state = {
            radialMouseOver: false
        }
    }

    render() {

        //let d3Charts = this.getOldCharts();

        //let visCharts = this.getVisCharts();

        //let victoryCharts = this.getVictoryCharts();

        let recharts = this.getRecharts();

        return recharts;
    }

    getOldCharts() {

        let generalChartData = [
            {
                name: "Charlotte",
                plays: 339
            },
            {
                name: "Martin",
                plays: 172
            },
            {
                name: "Marian",
                plays: 63
            },
        ];
        let chartSeries = [
            {
                "field": 'plays',
                "name": "# Plays"
            }
        ];
        let chartSeries2 = [
            {
                "field": 'Charlotte',
                "name": "Charlotte"
            },
            {
                "field": 'Martin',
                "name": "Martin"
            },
            {
                "field": 'Marian',
                "name": "Marian"
            },
        ];
        let x = (dataPoint) => {
            return dataPoint.name;
        };

        let value = (dataPoint) => {
            return dataPoint.plays;
        }

        let xScale = 'ordinal',
            xLabel = "Names",
            yLabel = "Plays",
            yTicks = [10];

        return <div>
            <BarChart
                title={"Plays by players"}
                data={generalChartData}
                width={600}
                height={350}
                chartSeries={chartSeries}
                x={x}
                xLabel={xLabel}
                xScale={xScale}
                yTicks={yTicks}
                yLabel={yLabel}
            />
            <div>DIVIDER</div>
            <PieChart
                data={generalChartData}
                width={600}
                height={350}
                chartSeries={chartSeries2}
                value={value}
                name={x}
            />
        </div>
    }

    getVisCharts() {

        let myData = [
            {
                label: "Charlotte",
                angle: 339,
            },
            {
                label: "Martin",
                angle: 172
            },
            {
                label: "Marian",
                angle: 63
            },
        ];
        return <RadialChart
            data={myData}
            innerRadius={100}
            radius={140}
            onValueMouseOver={v => this.setState({radialMouseOver: v})}
            onSeriesMouseOut={v => this.setState({radialMouseOver: false})}
            width={300}
            height={300}>
            {this.state.radialMouseOver && <Hint value={this.state.radialMouseOver}/>}
        </RadialChart>
    }

    getVictoryCharts() {
        let data = [
            {
                name: "Charlotte",
                plays: 339
            },
            {
                name: "Martin",
                plays: 172
            },
            {
                name: "Marian",
                plays: 63
            },
        ];

        return <VictoryChart
            domainPadding={40}
        >
            <VictoryAxis
                tickValues={[1, 2, 3]}
                tickFormat={["Charlotte", "Martin", "Marian"]}
            />
            <VictoryAxis
                dependentAxis
            />
            <VictoryBar
                data={data}
                x="name"
                y="plays"
            />
        </VictoryChart>
    }

    getRecharts() {
        return <div>temp</div>
    }
}

export default StatisticsView;
