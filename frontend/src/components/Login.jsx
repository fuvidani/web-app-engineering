import React from "react";
import "styles/css/bootstrap.css";
import {
  Card, CardActions, CardText, CardTitle, FlatButton, TextField
} from "material-ui";
import "styles/css/bootstrap.css";
import Redirect from "react-router-dom/es/Redirect";

require("../../favicon.ico");

export default class Login extends React.Component {

  render() {
    return <div>
      <div className="container">
        <div className="row container-row">
          <div className="col-md-4"/>
          <div className="col-md-4">
            <Card>
              <CardTitle title="WAE Frontend" subtitle="username: user - password: password" />
              <CardText>
                <TextField
                    ref="username"
                    hintText="Username"
                    floatingLabelText="Username"
                    fullWidth={true}
                /><br />
                <TextField
                    ref="password"
                    hintText="Password"
                    floatingLabelText="Password"
                    type="password"
                    onKeyDown={(e) => e.keyCode === 13 ? this.props.auth(this.refs.username.getValue(), this.refs.password.getValue()) : {}}
                    fullWidth={true}
                /><br />
              </CardText>
              <CardActions>
                <FlatButton label="Login"
                            fullWidth={true}
                            primary={true}
                            onClick={() => this.props.auth(this.refs.username.getValue(), this.refs.password.getValue())}/>
              </CardActions>
            </Card>
          </div>
          <div className="col-md-4"/>
        </div>
      </div>
      {this.props.loggedIn ? <Redirect to="/counter" push /> : null}
    </div>;
  }
}
