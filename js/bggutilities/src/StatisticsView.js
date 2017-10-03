import React, {Component} from 'react';
import {pieColors} from "./util/graphs/GraphUtil";
import LoadingScreen from "./util/LoadingScreen";
import {
    Bar, BarChart, Cell, Legend, Line, LineChart, Pie, PieChart, Tooltip, XAxis,
    YAxis
} from "recharts";

class StatisticsView extends Component {

    constructor(props) {
        super(props);
        this.state = {
            allGames: undefined,
            allPlayers: undefined,
            loadingGames: true,
            loadingPlayers: true
        }
        this.downloadAllGames();
        this.downloadAllPlayers();
    }

    render() {
        if (this.state.loadingGames || this.state.loadingPlayers) return <LoadingScreen/>;
        let complexityChart = this.getComplexityBarChart();
        let ratingsDifferenceChart = this.getRatingsDifferenceChart();
        return <div>{ratingsDifferenceChart}

            {complexityChart}</div>;
    }

    getPieChart() {
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
        const COLORS = pieColors;

        return <PieChart width={800} height={400}>
            <Pie
                data={data}
                dataKey={"plays"}
                innerRadius={60}
                outerRadius={80}
                fill="#8884d8"
                labelLine={false}
                label={(dataPoint) => {
                    return dataPoint.name
                }}
            >
                {
                    data.map((entry, index) => <Cell fill={COLORS[index % COLORS.length]}/>)
                }
            </Pie>
        </PieChart>
    }

    getComplexityBarChart() {
        let data = [
            {
                range: "1-2",
                Games: 0
            },
            {
                range: "2-3",
                Games: 0
            },
            {
                range: "3-4",
                Games: 0
            },
            {
                range: "4-5",
                Games: 0
            },
        ];

        this.state.allGames.forEach(
            (game) => {
                let complexity = game.complexity;
                if (complexity < 2) {
                    data[0].Games++;
                }
                else if (complexity < 3) {
                    data[1].Games++;
                }
                else if (complexity < 4) {
                    data[2].Games++;
                }
                else {
                    data[3].Games++;
                }
            }
        )
        return <BarChart width={600} height={300} data={data}
                         margin={{top: 5, right: 30, left: 20, bottom: 5}}>
            <XAxis dataKey="range"/>
            <YAxis/>
            <Bar dataKey="Games" fill="#8884d8"/>
            <Legend/>
        </BarChart>
    }

    getRatingsDifferenceChart() {
        let data = [];

        this.state.allGames.forEach(
            (game) => {

                let playersAverageRatings = 0;
                let playersHavingPlayedTheGame = 0;
                this.state.allPlayers.forEach(
                    (player) => {
                        let playerRating = player.gameRatingsMap[game.name];
                        if (playerRating) {
                            playersAverageRatings += playerRating;
                            playersHavingPlayedTheGame++;
                        }
                    }
                );
                playersAverageRatings = playersAverageRatings !== 0 ? playersAverageRatings / playersHavingPlayedTheGame : 0;
                playersAverageRatings = parseFloat(playersAverageRatings).toFixed(2);

                if (game.averageRating && game.personalRating && playersAverageRatings) {
                    data.push(
                        {
                            name: game.name,
                            publicRating: parseFloat(game.averageRating),
                            yourRating: parseFloat(game.personalRating),
                            friendsAverageRating: parseFloat(playersAverageRatings)
                        }
                    )
                }
            }
        );

        return <LineChart width={600} height={250} data={data}
                          margin={{top: 50, right: 30, left: 20, bottom: 5}}>
            <XAxis dataKey="name"/>
            <YAxis domain={[5, 10]}/>
            <Tooltip/>
            <Legend/>
            <Line type="monotone" dataKey="publicRating" stroke={pieColors[0]}/>
            <Line type="monotone" dataKey="yourRating" stroke={pieColors[1]}/>
            <Line type="monotone" dataKey="friendsAverageRating" stroke={pieColors[2]}/>
        </LineChart>
    }

    downloadAllGames() {
        var request = new XMLHttpRequest();
        request.timeout = 60000;
        request.open('GET', 'http://localhost:8080/getGames?expansions=' + false, true);
        request.send(null);
        request.onreadystatechange = function () {
            if (request.readyState === 4 && request.status === 200) {
                var type = request.getResponseHeader('Content-Type');
                if (type.indexOf("text") !== 1) {
                    let result = request.responseText;
                    let jsonObj = JSON.parse(result);
                    this.setState({loadingGames: false, allGames: jsonObj});
                }
            }
        }.bind(this);
    }

    downloadAllPlayers() {
        var request = new XMLHttpRequest();
        request.timeout = 60000;
        request.open('GET', 'http://localhost:8080/getAllPlayers', true);
        request.send(null);
        request.onreadystatechange = function () {
            if (request.readyState === 4 && request.status === 200) {
                var type = request.getResponseHeader('Content-Type');
                if (type.indexOf("text") !== 1) {
                    let result = request.responseText;
                    let jsonObj = JSON.parse(result);
                    this.setState({loadingPlayers: false, allPlayers: jsonObj});
                }
            }
        }.bind(this);
    }
}

export default StatisticsView;
