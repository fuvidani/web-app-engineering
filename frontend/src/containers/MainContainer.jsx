import {connect} from "react-redux";
import MainComponent from "../components/MainComponent";
import {
  auth,
  fetchCounter, incrementCounter, logout,
  resetCounter
} from "../actions/counterActions";

const mapStateToProps = (state) => {
  return {
    counter: state.counter.counter,
    fresh: state.counter.fresh,
    logs: state.counter.logs,
    loggedIn: state.counter.loggedIn
  };
};

const mapDispatchToProps = (dispatch) => {
  return {
    auth: () => dispatch(auth()),
    fetchCounter: () => dispatch(fetchCounter()),
    incrementCounter: () => dispatch(incrementCounter()),
    resetCounter: () => dispatch(resetCounter()),
    logout: () => dispatch(logout())
  };
};

const MainContainer = connect(mapStateToProps, mapDispatchToProps)(MainComponent);

export default MainContainer;
