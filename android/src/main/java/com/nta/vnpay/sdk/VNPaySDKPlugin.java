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
        openSdk();
        JSObject ret = new JSObject();
        ret.put("value", implementation.echo(value));
        call.resolve(ret);
    }
    public void openSdk() {
        Intent intent = new Intent(this.getContext(), VNP_AuthenticationActivity.class);
        intent.putExtra("url", "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html?vnp_Amount=10000000&vnp_Command=pay&vnp_CreateDate=20230217155719&vnp_CurrCode=VND&vnp_ExpireDate=20230217162719&vnp_IpAddr=115.79.38.101&vnp_Locale=vn&vnp_OrderInfo=Thanh+toan+don+hang%3A30308338&vnp_OrderType=250000&vnp_ReturnUrl=http%3A%2F%2Flocalhost%3A8080%2Fvnpay_jsp%2Fvnpay_return.jsp&vnp_TmnCode=CATHAYAP&vnp_TxnRef=30308338&vnp_Version=2.1.0&vnp_SecureHash=49854653586054edc69ad592f3b0b4dbaa1cdd6511a75ef8d54d4fe27c8203a733f9159bcd68c8b573b3dfd00561cb272f3c4d0086c51d37156e81fce7af2370"); //bắt buộc, VNPAY cung cấp
        intent.putExtra("tmn_code", "FAHASA03"); //bắt buộc, VNPAY cung cấp
        intent.putExtra("scheme", "resultactivity"); //bắt buộc, scheme để mở lại app khi có kết quả thanh toán từ mobile banking
        intent.putExtra("is_sandbox", false); //bắt buộc, true <=> môi trường test, true <=> môi trường live
        VNP_AuthenticationActivity.setSdkCompletedCallback(new VNP_SdkCompletedCallback() {
            @Override
            public void sdkAction(String action) {
                Log.wtf("SplashActivity", "action: " + action);
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
