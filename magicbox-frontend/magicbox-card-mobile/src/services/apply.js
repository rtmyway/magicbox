import serverConfig from '../configs/ServerConfig';
import apiUrlConfig from '../configs/ApiUrlConfig';
import requestCommon from '../commons/RequestCommon';

export function add(params, callback){
  let reqObj = requestCommon.getPostReqObj(serverConfig.cardServer, apiUrlConfig.applyApiUrl.add, params);
  requestCommon.processRequest(reqObj, callback);
}

export function process(params, callback){
  let reqObj = requestCommon.getPostReqObj(serverConfig.cardServer, apiUrlConfig.applyApiUrl.process, params);
  requestCommon.processRequest(reqObj, callback);
}

export function load(params, callback){
  let reqObj = requestCommon.getPostReqObj(serverConfig.cardServer, apiUrlConfig.applyApiUrl.load, params);
  requestCommon.processRequest(reqObj, callback);
}
