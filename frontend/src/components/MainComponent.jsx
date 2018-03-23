import React from "react";
import "styles/css/bootstrap.css";
import {
  Card, CardActions, CardText, CardTitle, FlatButton
} from "material-ui";
import "styles/css/bootstrap.css";
import Redirect from "react-router-dom/es/Redirect";

export default class MainComponent extends React.Component {
  render() {
    function zeroPad(num, places) {
      let zero = places - num.toString().length + 1;
      return Array(+(zero > 0 && zero)).join("0") + num;
    }

    return <div>
      <div className="container">
        <div className="row container-row">
          <div className="col-md-2"/>
          <div className="col-md-6">
            <Card>
              <CardTitle title="Counter" subtitle="Manipulate counter"/>
              <CardText className="text-center">
                <div className="counter-number"
                     style={this.props.fresh ? {color: "darkgreen"}
                         : {color: "darkred"}}>{this.props.counter}</div>
              </CardText>
              <CardActions>
                <FlatButton label="Fetch"
                            primary={true}
                            onClick={() => this.props.fetchCounter()}/>
                <FlatButton label="Increment"
                            primary={true}
                            onClick={() => this.props.incrementCounter()}/>
                <FlatButton label="Reset"
                            primary={true}
                            onClick={() => this.props.resetCounter()}/>
                <FlatButton label="Logout"
                            secondary={true}
                            onClick={() => this.props.logout()}/>
              </CardActions>
            </Card>
          </div>
          <div className="col-md-2"/>
        </div>
        <div className="row container-row">
          <div className="col-md-2"/>
          <div className="col-md-6">
            <Card>
              <CardTitle title="Logs" subtitle="Request history"/>
              <CardText>
                {this.props.logs.map((log, i) =>
                    <p key={i}>
                      <svg height="10" width="10">
                        <circle cx="5" cy="5" r="4"
                                fill={log.sign === 0 ? "red" : "green"}/>
                      </svg>
                      {" " + log.timestamp.toLocaleDateString() + " " + zeroPad(log.timestamp.getHours(), 2) + ":" + zeroPad(log.timestamp.getMinutes(), 2) + ":" + zeroPad(log.timestamp.getSeconds(), 2) + " - "}
                      <b>{log.msg}</b>
                    </p>
                )}

              </CardText>
            </Card>
          </div>
          <div className="col-md-2"/>
        </div>
      </div>
      {this.props.loggedIn ? null : <Redirect to="/" push/>}
    </div>;
  }
}
