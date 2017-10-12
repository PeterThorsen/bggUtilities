export function toTitleCase(str)
{
    return str.replace(/\w\S*/g, function(txt){return txt.charAt(0).toUpperCase() + txt.substr(1).toLowerCase();});
}

export function deepSlice(items) {
    return JSON.parse(JSON.stringify(items))
}

export function getPlayersString(playerNames) {
    let players = "";
    playerNames.forEach(
        (otherPlayer) => {
            players += otherPlayer + ", ";
        }
    );
    players = players.substring(0, players.length - 2);
    return players;
}