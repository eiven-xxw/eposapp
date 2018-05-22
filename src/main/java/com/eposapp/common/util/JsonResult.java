package com.eposapp.common.util;


import com.eposapp.common.constant.JsonConstans;

/**
 *  @author eiven
 *   Json返回结果
 * */
public class JsonResult {

    /**
     * 是否成功
     */
    private boolean rel;
    /**
     * 返回信息
     */
    private String msg="";
    /**
     * 返回数据
     * */
    private Object data="";

    public JsonResult(){

    }
    public JsonResult(boolean rel, String msg, Object data, int count) {
        this.rel = rel;
        this.msg = msg;
        this.data = data;
    }
    public JsonResult setError(final JsonResult error, String info){
        this.rel = error.isRel();
        this.msg = error.getMsg()+info;
        return this;
    }
    public static JsonResult putFail(){
        JsonResult result = new JsonResult();
        result.setMsg(JsonConstans.OPERATION_FAILURE);
        result.setRel(JsonConstans.RESULT_FAIL);
        return result;
    }
    public static JsonResult putFail(String msg){
        JsonResult result = new JsonResult();
        result.setMsg(msg);
        result.setRel(JsonConstans.RESULT_FAIL);
        return result;
    }
    public static JsonResult putSuccess(Object data){
        JsonResult result = new JsonResult();
        result.setRel(JsonConstans.RESULT_SUCCESS);
        result.setMsg(JsonConstans.RESULT_SUCCESS_MSG);
        result.setData(data);
        return result;
    }
    public static JsonResult putSuccess(){
        JsonResult result = new JsonResult();
        result.setRel(JsonConstans.RESULT_SUCCESS);
        result.setMsg(JsonConstans.RESULT_SUCCESS_MSG);
        return result;
    }
    public static JsonResult putSuccess(String msg){
        JsonResult result = new JsonResult();
        result.setRel(JsonConstans.RESULT_SUCCESS);
        result.setMsg(msg);
        return result;
    }

    public static JsonResult getResult(boolean b){
        JsonResult result = new JsonResult();
        if(b){
            result.setRel(JsonConstans.RESULT_SUCCESS);
            result.setMsg(JsonConstans.RESULT_SUCCESS_MSG);
        }else{
            result.setMsg(JsonConstans.OPERATION_FAILURE);
            result.setRel(JsonConstans.RESULT_FAIL);
        }
        return result;
    }



    public boolean isRel() {
        return rel;
    }

    public void setRel(boolean rel) {
        this.rel = rel;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public static String toJson(JsonResult result) {
        return JsonContext.toJson(result);
    }







}
