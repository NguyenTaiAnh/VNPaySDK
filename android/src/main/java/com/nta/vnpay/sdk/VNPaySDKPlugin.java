package com.nta.vnpay.sdk;

import android.content.Intent;
import android.util.Log;

import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.NativePlugin;
import com.vnpay.authentication.VNP_AuthenticationActivity;
import com.vnpay.authentication.VNP_SdkCompletedCallback;

@NativePlugin(
        requestCodes = {123123}
)
public class VNPaySDKPlugin extends Plugin {

    private VNPaySDK implementation = new VNPaySDK();

    @PluginMethod
    public void echo(PluginCall call) {
        String value = call.getString("value");
        openSdk(value, call);
        JSObject ret = new JSObject();
        ret.put("value", implementation.echo(value));
//        call.resolve(ret);
    }
    public void openSdk(String value,PluginCall call) {
        Log.wtf("check: ", "value: " + value);
        Intent intent = new Intent(this.getContext(), VNP_AuthenticationActivity.class);
        intent.putExtra("url", value); //bắt buộc, VNPAY cung cấp
        intent.putExtra("tmn_code", "CATHAYAP"); //bắt buộc, VNPAY cung cấp
        intent.putExtra("scheme", "resultactivity"); //bắt buộc, scheme để mở lại app khi có kết quả thanh toán từ mobile banking
        intent.putExtra("is_sandbox", true); //bắt buộc, true <=> môi trường test, true <=> môi trường live
        VNP_AuthenticationActivity.setSdkCompletedCallback(new VNP_SdkCompletedCallback() {
            @Override
            public void sdkAction(String action) {
                Log.wtf("SplashActivity", "action: " + action);
                call.resolve();
                //action == AppBackAction
                //Người dùng nhấn back từ sdk để quay lại

                //action == CallMobileBankingApp
                //Người dùng nhấn chọn thanh toán qua app thanh toán (Mobile Banking, Ví...)
                //lúc này app tích hợp sẽ cần lưu lại cái PNR, khi nào người dùng mở lại app tích hợp thì sẽ gọi kiểm tra trạng thái thanh toán của PNR Đó xem đã thanh toán hay chưa.

                //action == WebBackAction
                //Người dùng nhấn back từ trang thanh toán thành công khi thanh toán qua thẻ khi url có chứa: cancel.sdk.merchantbackapp

                //action == FaildBackAction
                //giao dịch thanh toán bị failed

                //action == SuccessBackAction
                //thanh toán thành công trên webview
            }
        });
        this.getContext().startActivity(intent);
    }
}
