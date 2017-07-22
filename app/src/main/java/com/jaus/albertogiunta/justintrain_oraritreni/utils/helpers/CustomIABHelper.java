package com.jaus.albertogiunta.justintrain_oraritreni.utils.helpers;

import android.content.Context;

import com.android.billingclient.api.Purchase;
import com.jaus.albertogiunta.justintrain_oraritreni.utils.helpers.IAB.IabHelper;
import com.jaus.albertogiunta.justintrain_oraritreni.utils.helpers.IAB.IabResult;
import com.jaus.albertogiunta.justintrain_oraritreni.utils.helpers.IAB.Inventory;

import trikita.log.Log;

import static com.jaus.albertogiunta.justintrain_oraritreni.aboutAndSettings.LicenseUpgradeActivity.SKU_UPGRADE;

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
                    isUserPro(listener);
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
            instance.isUserPro(listener);
        }
        return CustomIABHelper.instance;
    }

    public void isUserPro(IabHelper.QueryInventoryFinishedListener listener) {
        if (isSetupDone) {
            billingHelper.queryInventoryAsync(false, listener);
        }
    }

    @Override
    public void onQueryInventoryFinished(IabResult result, Inventory inv) {
        isPro = (inv.hasPurchase(SKU_UPGRADE) &&
                inv.getPurchase(SKU_UPGRADE).getPurchaseState() == Purchase.PurchaseState.PURCHASED);
    }

    public static boolean isOrderOk(IabResult result, Inventory inv) {
        return result.getResponse() == IabHelper.BILLING_RESPONSE_RESULT_OK &&
                inv.hasPurchase(SKU_UPGRADE) &&
                (inv.getPurchase(SKU_UPGRADE).getPurchaseState() == Purchase.PurchaseState.PURCHASED);
    }
}
