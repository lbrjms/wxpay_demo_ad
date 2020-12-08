package com.weh.wuerhe;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.weh.wuerhe.wxapi.util.ConstantUtil;
import com.weh.wuerhe.wxapi.util.PayCommonUtil;
import com.weh.wuerhe.wxapi.util.WXOrderModel;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import fr.arnaudguyon.xmltojsonlib.XmlToJson;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final IWXAPI wxapi = WXAPIFactory.createWXAPI(this, "", false);
        final Button button = findViewById(R.id.button);
        Log.d(TAG, "onCreate");
        final TextView textView = findViewById(R.id.editTextNumber);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                button.setEnabled(false);
                Log.d(TAG, "onClick: 开始统一下单");
                // 封装下单参数
                SortedMap<String, String> signParams = new TreeMap<String, String>();
                signParams.put("appid", ConstantUtil.APP_ID);//app_id
                signParams.put("body","测试");//商品参数信息
                signParams.put("mch_id", ConstantUtil.PARTNER_ID);//微信商户账号
                signParams.put("nonce_str", "5K8264ILTKCH16CQ250");//32位不重复的编号
                signParams.put("notify_url", ConstantUtil.WEI_XIN_NOTIFY_URL);//回调页面
                //获取当前时间戳
                long timeStamp = System.currentTimeMillis();
                signParams.put("out_trade_no", String.valueOf(timeStamp));//订单编号
                signParams.put("spbill_create_ip","123.12.12.123");//请求的实际ip地址
                signParams.put("total_fee", String.valueOf(textView.getText()));//支付金额 单位为分
                signParams.put("trade_type", "APP");//付款类型为APP
                String sign = PayCommonUtil.createSign("UTF-8", signParams);//生成签名
                signParams.put("sign", sign);
                //生成Xml格式的字符串
                String requestXml = PayCommonUtil.getRequestXml(signParams);
                //统一下单请求
                Request request = new Request.Builder().url(ConstantUtil.UNIFIED_ORDER_URL).post(RequestBody.create(MediaType.parse("application/xml"), requestXml)).build();
                Log.d(TAG, "param: " + requestXml);
                OkHttpClient okHttpClient = new OkHttpClient();
                okHttpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.d(TAG, "onFailure: " + e.getMessage());
                    }
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        Log.d(TAG, response.protocol() + " " + response.code() + " " + response.message());
                        // 获取返回的json数据 xml转json
                        XmlToJson xmlToJson = new XmlToJson.Builder(response.body().string()).build();
                        Log.d(TAG, "onResponsejson: " + xmlToJson);
                        signPreOrder(xmlToJson.toString());
                    }
                });
            }
        });
    }
    // 签名预支付订单
    void signPreOrder(String xmlToJsonStr) {
        Gson gson = new GsonBuilder().create();
        try {
            JSONObject jsonObject = new JSONObject(xmlToJsonStr);
            // 获取xml部分的内容就行了
            JSONObject data = jsonObject.optJSONObject("xml");
            Log.d(TAG, "onResponsexml ：" + data.toString());
            // 订单数据转模型
            WXOrderModel orderModel = gson.fromJson(data.toString(), WXOrderModel.class);
            // 封装订单数据 签名
            SortedMap<String, String> signParams = new TreeMap<String, String>();
            signParams.put("appid", orderModel.getAppid());//app_id
            signParams.put("partnerid", orderModel.getMch_id());//微信商户账号
            signParams.put("noncestr", orderModel.getNonce_str());//32位不重复的编号
            signParams.put("prepayid", orderModel.getPrepay_id());//预付单号
            signParams.put("package","Sign=WXPay");//请求的实际ip地址
            // 获取当前时间戳
            long timeStamp = System.currentTimeMillis();
            Log.d("timeStamp", String.valueOf(timeStamp));
            orderModel.setTimestamp(String.valueOf(timeStamp));
            signParams.put("timestamp", String.valueOf(timeStamp));//支付金额 单位为分
            // 签名
            String sign = PayCommonUtil.createSign("UTF-8", signParams);//生成签名
            // 订单模型赋值sign
            orderModel.setSign(sign);
            // 预支付订单模型支付
            wxPay(orderModel);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    void wxPay(WXOrderModel model) {

        IWXAPI api = WXAPIFactory.createWXAPI(this, ConstantUtil.APP_ID);
        api.registerApp(ConstantUtil.APP_ID);
        PayReq request = new PayReq();
        // appid
        request.appId = model.getAppid();
        // 商户号
        request.partnerId = model.getMch_id();
        // 预支付交易会话ID
        request.prepayId = model.getPrepay_id();
        // 扩展字段 暂填写固定值Sign=WXPay
        request.packageValue = "Sign=WXPay";
        // 随机字符串 随机字符串，不长于32位
        request.nonceStr = model.getNonce_str();
        // 时间戳
        request.timeStamp = model.getTimestamp();
        // 签名
        request.sign = model.getSign();
        // 跳转微信支付
        api.sendReq(request);
    }
}