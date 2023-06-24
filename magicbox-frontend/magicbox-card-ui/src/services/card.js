import serverConfig from '../configs/ServerConfig';
import apiUrlConfig from '../configs/ApiUrlConfig';
import requestCommon from '../commons/RequestCommon';

export function generate(params, callback){
  let reqObj = requestCommon.getPostReqObj(serverConfig.cardServer, apiUrlConfig.cardApiUrl.generate, params);
  requestCommon.processRequest(reqObj, callback);
}

export function add(params, callback){
  let reqObj = requestCommon.getPostReqObj(serverConfig.cardServer, apiUrlConfig.cardApiUrl.add, params);
  requestCommon.processRequest(reqObj, callback);
}


export function remove(params, callback){
  let reqObj = requestCommon.getPostReqObj(serverConfig.cardServer, apiUrlConfig.cardApiUrl.remove, params);
  requestCommon.processRequest(reqObj, callback);
}

export function update(params, callback){
  let reqObj = requestCommon.getPostReqObj(serverConfig.cardServer, apiUrlConfig.cardApiUrl.update, params);
  requestCommon.processRequest(reqObj, callback);
}

export function listPage(params, callback){
  let reqObj = requestCommon.getPostReqObj(serverConfig.cardServer, apiUrlConfig.cardApiUrl.listPage, params);
  requestCommon.processRequest(reqObj, callback);
}
