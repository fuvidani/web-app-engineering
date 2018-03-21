import React from 'react';
import 'normalize.css';
import 'styles/index.scss';
import {MuiThemeProvider} from "material-ui";
import store from "./store/store";
import MainContainer from "./containers/MainContainer";
import Provider from "react-redux/es/components/Provider";

const App = () => (
	<Provider store={store}>
		<MuiThemeProvider>
			<MainContainer/>
		</MuiThemeProvider>
	</Provider>
);

export default App;
