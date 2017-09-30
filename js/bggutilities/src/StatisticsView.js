import React, {Component} from 'react';
import {BarChart, PieChart} from "react-d3-basic";

class StatisticsView extends Component {
    render() {

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
}

export default StatisticsView;
