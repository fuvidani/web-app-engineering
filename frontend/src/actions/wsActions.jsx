export function webSocketConnected() {
	return function (dispatch) {
		dispatch({
			type: "WEBSOCKET_CONNECT_FULFILLED"
		});
	};
}
