package com.wgd.gdcp.gdcplibrary;

public class GDImageBean {

    private GDConfig mGDConfig;

    private int code = -1 ;//-1=Not operated;0=Success;1=fail
    private String errorMsg = "" ;

    public GDImageBean(){

    }
    public GDImageBean(GDConfig mGDConfig){
        this.mGDConfig = mGDConfig;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public GDConfig getmGDConfig() {
        return null==mGDConfig?new GDConfig():mGDConfig;
    }

    public void setmGDConfig(GDConfig mGDConfig) {
        this.mGDConfig = mGDConfig;
    }
}
