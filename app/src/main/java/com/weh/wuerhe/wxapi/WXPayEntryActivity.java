package com.weh.wuerhe.wxapi;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.weh.wuerhe.R;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

    private final String TAG = "WXPayEntryActivity";

    private IWXAPI api;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.pay_result);

        api = WXAPIFactory.createWXAPI(this, "");
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }
    @Override
    public void onReq(BaseReq req) {
    }

    @Override
    public void onResp(BaseResp baseResp) {
        Log.i(TAG,"errCode = " + baseResp.errCode);
        if (baseResp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            if (baseResp.errCode == 0){ //支付成功
                Toast.makeText(this,"微信支付成功",Toast.LENGTH_SHORT).show();

            }else {
                Log.d(TAG, String.valueOf(baseResp.errCode));
                Log.d(TAG, String.valueOf(baseResp.errStr));
                Log.d(TAG, String.valueOf(baseResp.openId));
                Log.d(TAG, String.valueOf(baseResp.transaction));

                Toast.makeText(this,"支付失败，请重试+ ",Toast.LENGTH_SHORT).show();
            }
            finish();

        }
    }
}