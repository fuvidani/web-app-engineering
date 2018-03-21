export default function reducer(state = {
	taxis: {},
	numberOfActiveTaxis: 0,
	overallDistance: 0,
	speedingIncidents: [],
	maxNumberOfTaxis: 100
}, action) {

	switch (action.type) {
		case "WEBSOCKET_CONNECT_FULFILLED": {
			console.log(action.type);

			return {
				...state
			};
		}
		case "WEBSOCKET_DISCONNECT_FULFILLED": {
			console.log(action.type);

			return {
				...state
			};
		}
		case "location_information": {
			let newTaxis = Object.assign({}, state.taxis);
			newTaxis[action.payload.id] = {id: action.payload.id, position: action.payload.position, violation: action.payload.violation};

			return {
				...state,
				taxis: newTaxis
			};
		}
		case "taxi_stopped": {
			let newItems = Object.assign({}, state.taxis);
			delete newItems[action.payload.id];

			return {
				...state,
				taxis: newItems
			};
		}
		case "taxi_speeding": {
			state.speedingIncidents.push({id: action.payload.id, speed: action.payload.speed});

			return {
				...state
			};
		}
		case "general_information": {
			state.numberOfActiveTaxis = action.payload.active_taxis;
			state.overallDistance = action.payload.overall_distance;

			return {
				...state
			};
		}
		case "setMaxNumberOfTaxis": {
			state.maxNumberOfTaxis = action.payload;

			return {
				...state
			};
		}
	}

	return state;
}
