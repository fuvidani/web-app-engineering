import React from 'react';
import 'normalize.css';
import 'styles/index.scss';
import {MuiThemeProvider} from "material-ui";
import store from "./store/store";
import Provider from "react-redux/es/components/Provider";
import Routes from "./routes";

const App = () => (
	<Provider store={store}>
		<MuiThemeProvider>
      <Routes />
		</MuiThemeProvider>
	</Provider>
);

export default App;
