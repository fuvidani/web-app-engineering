import {connect} from "react-redux";
import Dashboard from "../components/MainComponent";

const mapStateToProps = (state) => {
	return {
		counter: state.counter
	};
};

const mapDispatchToProps = (dispatch) => {
	return {

	};
};

const MainContainer = connect(mapStateToProps, mapDispatchToProps)(Dashboard);

export default MainContainer;
