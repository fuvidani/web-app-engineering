import {connect} from "react-redux";
import {
  auth
} from "../actions/counterActions";
import Login from "../components/Login";

const mapStateToProps = (state) => {
  return {
    loggedIn: state.counter.loggedIn
  };
};

const mapDispatchToProps = (dispatch) => {
  return {
    auth: (username, password) => dispatch(auth(username, password))
  };
};

const LoginContainer = connect(mapStateToProps, mapDispatchToProps)(
    Login);

export default LoginContainer;
