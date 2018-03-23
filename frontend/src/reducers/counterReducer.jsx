export default function reducer(state = {
	counter: 0,
  fresh: false,
  loggedIn: localStorage.getItem("jwtToken"),
  logs: []
}, action) {

	switch (action.type) {
		case "FETCH_COUNTER_FULFILLED": {
      let newLogs = state.logs.slice(0);
      newLogs.push({msg: "FETCH COUNTER SUCCEEDED", sign: 1, timestamp: new Date()});

			return {
				...state,
        logs: newLogs,
        counter: action.payload,
        fresh: true
			};
		}
    case "INCREMENT_COUNTER_FULFILLED": {
      let newLogs = state.logs.slice(0);
      newLogs.push({msg: "INCREMENT COUNTER SUCCEEDED", sign: 1, timestamp: new Date()});

      return {
        ...state,
        logs: newLogs,
        fresh: false
      };
    }
    case "RESET_COUNTER_FULFILLED": {
      let newLogs = state.logs.slice(0);
      newLogs.push({msg: "RESET COUNTER SUCCEEDED", sign: 1, timestamp: new Date()});

      return {
        ...state,
        logs: newLogs,
        fresh: false
      };
    }
    case "AUTH_FULFILLED": {
      let newLogs = state.logs.slice(0);
      newLogs.push({msg: "AUTHENTICATION SUCCEEDED", sign: 1, timestamp: new Date()});

      localStorage.setItem("jwtToken", action.payload);

      return {
        ...state,
        logs: newLogs,
        loggedIn: true,
        fresh: false
      };
    }
    case "REQUEST_REJECTED": {
      let newLogs = state.logs.slice(0);
      newLogs.push({msg: "REQUEST FAILED", sign: 0, timestamp: new Date()});

      return {
        ...state,
        logs: newLogs,
        fresh: false
      };
    }
    case "AUTH_REJECTED": {
      let newLogs = state.logs.slice(0);
      newLogs.push({msg: "AUTHENTICATION FAILED", sign: 0, timestamp: new Date()});

      alert(action.payload);

      return {
        ...state,
        logs: newLogs,
        fresh: false
      };
    }
    case "LOGOUT": {
      let newLogs = state.logs.slice(0);
      newLogs.push({msg: "LOGOUT SUCCEEDED", sign: 1, timestamp: new Date()});

      localStorage.removeItem("jwtToken");

      return {
        ...state,
        logs: newLogs,
        loggedIn: false
      };
    }
	}

	return state;
}
