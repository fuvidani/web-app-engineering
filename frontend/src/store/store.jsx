import {applyMiddleware, createStore} from "redux";
import {serverConfig} from "../config/serverConfig";

import {logger} from "redux-logger";
import thunk from "redux-thunk";
import promise from "redux-promise-middleware";

import reducer from "../reducers/reducers";

let middleware = null;
if (serverConfig.logging) {
    middleware = applyMiddleware(promise(), thunk, logger);
} else {
    middleware = applyMiddleware(promise(), thunk);
}

export default createStore(reducer, middleware);
