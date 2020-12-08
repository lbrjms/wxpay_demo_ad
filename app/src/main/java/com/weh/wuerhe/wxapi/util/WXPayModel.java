package com.weh.wuerhe.wxapi.util;

/**
 * Created by  on 2020/12/4..
 */
public class WXPayModel {
    // appid
    private  String appid;
    // 商户号
    private  String mch_id;
    // 预支付交易会话ID
    private  String prepay_id;

    // 扩展字段 暂填写固定值Sign=WXPay
    private  String packageValue;
    // 随机字符串 随机字符串，不长于32位
    private  String nonce_str;
    // 时间戳
    private  String timestamp;
    // 签名
    private  String sign;
    public void setAppid(String appid) {
        this.appid = appid;
    }
    public String getAppid() {
        return appid;
    }
    public void setMch_id(String mch_id) {
        this.mch_id = mch_id;
    }

    public void setPrepay_id(String prepay_id) {
        this.prepay_id = prepay_id;
    }

    public void setPackageValue(String packageValue) {
        this.packageValue = packageValue;
    }

    public void setNonce_str(String nonce_str) {
        this.nonce_str = nonce_str;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getMch_id() {
        return mch_id;
    }

    public String getPrepay_id() {
        return prepay_id;
    }

    public String getPackageValue() {
        return packageValue;
    }

    public String getNonce_str() {
        return nonce_str;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getSign() {
        return sign;
    }



}
