package com.jaus.albertogiunta.justintrain_oraritreni.utils.helpers;

import android.content.Context;

import com.jaus.albertogiunta.justintrain_oraritreni.utils.helpers.IAB.IabHelper;
import com.jaus.albertogiunta.justintrain_oraritreni.utils.helpers.IAB.IabResult;
import com.jaus.albertogiunta.justintrain_oraritreni.utils.helpers.IAB.Inventory;

import trikita.log.Log;

public class CustomIABHelper implements IabHelper.QueryInventoryFinishedListener {

    private IabHelper billingHelper;

    private static CustomIABHelper instance = null;

    private boolean isSetupDone = false;
    private boolean isPro       = false;

    private CustomIABHelper(Context context, IabHelper.QueryInventoryFinishedListener listener) {
        billingHelper = new IabHelper(context.getApplicationContext(), "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAnl+N1zhaAmBhLF54yJo7T+ku8m5c/XZEsZ3+zBb7HyKCxpGuQL4+oPTVSNRtNdXNCl2R0eqojv+f3sT6tWL2XHt1DK6F7rYVtvs8rmQR1himMDEV0Tgljn5uBQuMag9nvNeyGIAi1wQeRz9Hk8tGF+laZcD3phR6NFnrwebD8FkG0lc6XHKpjAzCsdu+YbyPH7JuFAUoi+Yrbvs50r0JzeUWqz+v427V4W4kQR9zAmN3evVmou1R+2WXPtD1wGMMSQAs2DOuxfgh85o4sRS21an8xQk/d70EX3E78O3iXzl76CKZNM54O2g8iE3tkNgGfeCkqs0m2v/2E6VDubVf3QIDAQAB");

        try {
            if (!isSetupDone) {
                billingHelper.startSetup(result -> {
                    isSetupDone = true;
                    userIsPro(listener);
                });
            }
        } catch (IllegalStateException e) {
            Log.d("onResume: setup already done");
        }

    }

    public static CustomIABHelper getInstance(Context context, IabHelper.QueryInventoryFinishedListener listener) {
        if (instance == null) {
            instance = new CustomIABHelper(context, listener);
        } else {
            instance.userIsPro(listener);
        }
        return CustomIABHelper.instance;
    }

    public void userIsPro(IabHelper.QueryInventoryFinishedListener listener) {
        if (isSetupDone) {
            Log.d("isPro: 1", isPro);
            billingHelper.queryInventoryAsync(false, listener);
        }
        Log.d("isPro: 2", isPro);
    }

    @Override
    public void onQueryInventoryFinished(IabResult result, Inventory inv) {
        isPro = (inv.hasPurchase("premium_upgrade_mp"));
    }
}
