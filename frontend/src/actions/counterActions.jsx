import axios from "axios";
import {serverConfig} from "../config/serverConfig";

export function auth(username, password) {
  return function (dispatch) {
    axios.post(serverConfig.url + "/auth", {
      username: username,
      password: password
    })
    .then((response) => {
      dispatch({type: "AUTH_FULFILLED", payload: response.data.token});
    })
    .catch((err) => {
      dispatch({type: "AUTH_REJECTED", payload: err});
    });
  };
}

export function logout() {
  return function (dispatch) {
    dispatch({type: "LOGOUT"});
  };
}

export function fetchCounter() {
	return function (dispatch) {
    axios.defaults.headers.common['Authorization'] = 'Bearer ' + localStorage.getItem('jwtToken');

    axios.get(serverConfig.url + "/counter",)
    .then((response) => {
      dispatch({type: "FETCH_COUNTER_FULFILLED", payload: response.data});
    })
    .catch((err) => {
      dispatch({type: "REQUEST_REJECTED", payload: err});
    });
	};
}

export function incrementCounter() {
  return function (dispatch) {
    axios.defaults.headers.common['Authorization'] = 'Bearer ' + localStorage.getItem('jwtToken');

    axios.post(serverConfig.url + "/counter")
    .then(() => {
      dispatch({type: "INCREMENT_COUNTER_FULFILLED"});
    })
    .catch((err) => {
      dispatch({type: "REQUEST_REJECTED", payload: err});
    });
  };
}

export function resetCounter() {
  return function (dispatch) {
    axios.defaults.headers.common['Authorization'] = 'Bearer ' + localStorage.getItem('jwtToken');

    axios.post(serverConfig.url + "/reset")
    .then(() => {
      dispatch({type: "RESET_COUNTER_FULFILLED"});
    })
    .catch((err) => {
      dispatch({type: "REQUEST_REJECTED", payload: err});
    });
  };
}
