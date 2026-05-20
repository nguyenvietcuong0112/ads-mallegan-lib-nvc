package com.mallegan.ads.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import com.mallegan.ads.R;

public class LoadingAdsDialogNoti  extends Dialog {
    public LoadingAdsDialogNoti(Context context) {
        super(context, R.style.TransparentDialog);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_loading_ads_noti);
    }
}
