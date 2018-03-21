import { serverConf } from "./localServerConfig";

export const serverConfig = {
    url: serverConf.url + ":" + serverConf.port,
    webSocketUrl: serverConf.url + ":" + serverConf.port + serverConf.webSocketPath,
    withCredentials: serverConf.withCredentials,
    localStorage: serverConf.localStorage,
    logging: serverConf.logging,
};
