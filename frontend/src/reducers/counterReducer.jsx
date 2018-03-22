export default function reducer(state = {
	counter: 0
}, action) {

	switch (action.type) {
		case "FETCH_COUNTER_FULFILLED": {
			return {
				...state,
				counter: action.payload
			};
		}
    case "INCREMENT_COUNTER_FULFILLED": {
      return {
        ...state
      };
    }
    case "AUTHENTICATION_FULFILLED": {
      localStorage.setItem("jwtToken", action.payload);

      return {
        ...state
      };
    }
    case "REQUEST_REJECTED": {
      return {
        ...state
      };
    }
	}

	return state;
}
