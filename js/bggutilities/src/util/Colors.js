export const pieColors = ['#FF8042', '#0088FE', '#00C49F', '#FFBB28'];

export const greenColors = (i) => {
    let colors = ['#99cc00', '#ace600', '#bfff00', '#c6ff1a', '#ccff33', '#d2ff4d', '#d9ff66', '#dfff80','#e6ff99'];
    if(i>colors.length-1) {
        return colors[colors.length-1];
    }
    return colors[i];
};

export const redColors = (i) => {
    let colors = ['#ff8080', '#ff6666', '#ff4d4d', '#ff3333', '#ff1a1a', '#ff0000', '#e60000'];
    if(i>colors.length-1) {
        return colors[colors.length-1];
    }
    return colors[i];
};