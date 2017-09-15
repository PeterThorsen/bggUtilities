import React, { Component } from 'react';
import './App.css';
import {$,jQuery} from 'jquery';

class App extends Component {
  render() {

      $.getJSON('localhost:8080', function(data) {
          console.log(data);
      });
    return (
      <div className="App">
        Hi
      </div>
    );
  }
}

export default App;
