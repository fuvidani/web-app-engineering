import { serverConf } from "./prodServerConfig";

export const serverConfig = {
    url: serverConf.url + ":" + serverConf.port,
    logging: serverConf.logging,
};
