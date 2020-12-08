package com.weh.wuerhe.wxapi.util;

/**
 * Created by  on 2020/12/8..
 */
public class WXOrderModel {
    private  String appid;
    private  String nonce_str;
    private  String trade_type;
    private  String mch_id;
    private  String prepay_id;

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getTimestamp() {
        return timestamp;
    }

    private  String timestamp;
    private  String sign;

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getSign() {
        return sign;
    }


    public String getAppid() {
        return appid;
    }

    public String getNonce_str() {
        return nonce_str;
    }

    public String getTrade_type() {
        return trade_type;
    }

    public String getMch_id() {
        return mch_id;
    }

    public String getPrepay_id() {
        return prepay_id;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public void setNonce_str(String nonce_str) {
        this.nonce_str = nonce_str;
    }

    public void setTrade_type(String trade_type) {
        this.trade_type = trade_type;
    }

    public void setMch_id(String mch_id) {
        this.mch_id = mch_id;
    }

    public void setPrepay_id(String prepay_id) {
        this.prepay_id = prepay_id;
    }
}
