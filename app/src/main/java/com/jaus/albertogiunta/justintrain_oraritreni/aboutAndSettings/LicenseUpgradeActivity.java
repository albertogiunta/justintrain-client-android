package com.jaus.albertogiunta.justintrain_oraritreni.aboutAndSettings;

import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.jaus.albertogiunta.justintrain_oraritreni.BuildConfig;
import com.jaus.albertogiunta.justintrain_oraritreni.R;
import com.jaus.albertogiunta.justintrain_oraritreni.utils.helpers.AnalyticsHelper;
import com.jaus.albertogiunta.justintrain_oraritreni.utils.helpers.CustomIABHelper;
import com.jaus.albertogiunta.justintrain_oraritreni.utils.helpers.IAB.IabHelper;
import com.jaus.albertogiunta.justintrain_oraritreni.utils.helpers.IAB.IabResult;
import com.jaus.albertogiunta.justintrain_oraritreni.utils.helpers.IAB.Purchase;
import com.jaus.albertogiunta.justintrain_oraritreni.utils.sharedPreferences.ProPreferences;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import trikita.log.Log;

import static butterknife.ButterKnife.apply;
import static com.jaus.albertogiunta.justintrain_oraritreni.utils.components.ViewsUtils.GONE;
import static com.jaus.albertogiunta.justintrain_oraritreni.utils.components.ViewsUtils.VISIBLE;
import static com.jaus.albertogiunta.justintrain_oraritreni.utils.constants.CONST_ANALYTICS.IAP_BOTTOM;
import static com.jaus.albertogiunta.justintrain_oraritreni.utils.constants.CONST_ANALYTICS.IAP_PAYPAL;
import static com.jaus.albertogiunta.justintrain_oraritreni.utils.constants.CONST_ANALYTICS.IAP_TOP;
import static com.jaus.albertogiunta.justintrain_oraritreni.utils.constants.CONST_ANALYTICS.SCREEN_UPGRADE_LICENSE;
import static com.jaus.albertogiunta.justintrain_oraritreni.utils.constants.CONST_FIREBASE.FIREBASE_IAP_BTN_MESSAGE;
import static com.jaus.albertogiunta.justintrain_oraritreni.utils.constants.CONST_FIREBASE.FIREBASE_PRO_USERS_NUMBER;
import static com.jaus.albertogiunta.justintrain_oraritreni.utils.constants.CONST_FIREBASE.FIREBASE_SHOW_MONEY_IN_IAP;

//public class LicenseUpgradeActivity extends AppCompatActivity implements PurchasesUpdatedListener {
public class LicenseUpgradeActivity extends AppCompatActivity implements IabHelper.OnIabSetupFinishedListener, IabHelper.OnIabPurchaseFinishedListener {

    AnalyticsHelper analyticsHelper;

    public static final String SKU_UPGRADE  = "premium_upgrade_mp";
    public static final String SKU_DONATION = "donate";

    @BindView(R.id.toolbar)
    Toolbar  toolbar;
    @BindView(R.id.btn_iap_first)
    Button   btnBuyFirst;
    @BindView(R.id.btn_iap_second)
    Button   btnBuySecond;
    @BindView(R.id.tv_already_pro)
    TextView tvAlreadyPro;
    @BindView(R.id.btn_donate_paypal)
    Button   btnAlreadyProPaypal;
    @BindView(R.id.tv_pro_users_number)
    TextView tvProUsersNumber;

    @BindViews({R.id.tv_already_pro, R.id.ll_already_pro_for_paypal})
    List<View> alreadyProViews = new LinkedList<>();

    boolean isAlreadyPro = false;
    private IabHelper billingHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_license_upgrade);
        ButterKnife.bind(this);
        analyticsHelper = AnalyticsHelper.getInstance(LicenseUpgradeActivity.this);

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        billingHelper = new IabHelper(this, "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAnl+N1zhaAmBhLF54yJo7T+ku8m5c/XZEsZ3+zBb7HyKCxpGuQL4+oPTVSNRtNdXNCl2R0eqojv+f3sT6tWL2XHt1DK6F7rYVtvs8rmQR1himMDEV0Tgljn5uBQuMag9nvNeyGIAi1wQeRz9Hk8tGF+laZcD3phR6NFnrwebD8FkG0lc6XHKpjAzCsdu+YbyPH7JuFAUoi+Yrbvs50r0JzeUWqz+v427V4W4kQR9zAmN3evVmou1R+2WXPtD1wGMMSQAs2DOuxfgh85o4sRS21an8xQk/d70EX3E78O3iXzl76CKZNM54O2g8iE3tkNgGfeCkqs0m2v/2E6VDubVf3QIDAQAB");
        billingHelper.startSetup(this);

        btnBuyFirst.setOnClickListener(v -> {
            onIAPButtonClicked();
            analyticsHelper.logScreenEvent(SCREEN_UPGRADE_LICENSE, IAP_TOP);
        });

        btnBuySecond.setOnClickListener(v -> {
            onIAPButtonClicked();
            analyticsHelper.logScreenEvent(SCREEN_UPGRADE_LICENSE, IAP_BOTTOM);
        });

        btnAlreadyProPaypal.setOnClickListener(v -> {
            Uri    uri    = Uri.parse("https://www.paypal.me/albertogiunta");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
            analyticsHelper.logScreenEvent(SCREEN_UPGRADE_LICENSE, IAP_PAYPAL);
        });

        Resources res          = getResources();
        String    proUsrNumber = String.format(res.getString(R.string.upgrade_pro_users_number), ((int) FirebaseRemoteConfig.getInstance().getDouble(FIREBASE_PRO_USERS_NUMBER)));
        tvProUsersNumber.setText(proUsrNumber);

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onIAPButtonClicked() {
        if (isAlreadyPro) {
            purchaseItem(SKU_DONATION);
        } else {
            purchaseItem(SKU_UPGRADE);
        }
    }

    @Override
    public void onIabSetupFinished(IabResult result) {
        if (result.isSuccess()) {
            dealWithIabSetupSuccess(result);
        } else {
            Log.d("Problem setting up In-app Billing: " + result);
            dealWithIabSetupFailure();
        }
    }

    private void dealWithIabSetupSuccess(IabResult result) {
        List<String> l = new LinkedList<>();
        l.add(SKU_UPGRADE);
        l.add(SKU_DONATION);
        if (billingHelper.isSetupDone() && !billingHelper.isAsyncInProgress()) {
            billingHelper.queryInventoryAsync(true, l, (result2, inv) -> {
                if (inv != null && inv.getSkuDetails(SKU_UPGRADE) != null) {
                    if (CustomIABHelper.isOrderOk(result2, inv)) {
                        isAlreadyPro = true;
                        apply(alreadyProViews, VISIBLE);
                        String textForBtn = "SUPPORTA LO SVILUPPO";
                        if (FirebaseRemoteConfig.getInstance().getBoolean(FIREBASE_SHOW_MONEY_IN_IAP)) {
                            String money = "(" + inv.getSkuDetails(SKU_DONATION).getPrice() + ")";
                            textForBtn += " " + money;
                        }
                        btnBuyFirst.setText(textForBtn);
                        btnBuySecond.setText(textForBtn);
                    } else {
                        apply(alreadyProViews, GONE);
                        String textForBtn = FirebaseRemoteConfig.getInstance().getString(FIREBASE_IAP_BTN_MESSAGE);
                        if (FirebaseRemoteConfig.getInstance().getBoolean(FIREBASE_SHOW_MONEY_IN_IAP)) {
                            String money = "(" + inv.getSkuDetails(SKU_UPGRADE).getPrice() + ")";
                            textForBtn += " " + money;
                        }
                        btnBuyFirst.setText(textForBtn);
                        btnBuySecond.setText(textForBtn);
                    }
                } else {
                    FirebaseCrash.report(new Exception("USER TRIED TO BUY AND INVENTORY WAS NULL!"));
                }

                // TODO DEBUGGGGGG
                boolean debugPurchase = false;
                if (!BuildConfig.DEBUG) debugPurchase = false;
                if (debugPurchase && CustomIABHelper.isOrderOk(result, inv)) {
                    if (inv != null && inv.hasPurchase(SKU_UPGRADE)) {
                        billingHelper.consumeAsync(inv.getPurchase(SKU_UPGRADE), (purchase, result1) -> {
                            if (inv.hasPurchase(SKU_DONATION)) {
                                billingHelper.consumeAsync(inv.getPurchase(SKU_DONATION), null);
                            }
                        });
                        ProPreferences.disableLiveNotification(LicenseUpgradeActivity.this);
                        ProPreferences.disableInstantDelay(LicenseUpgradeActivity.this);
                    }
                }
                // TODO END DEBUGGGGGG
            });
        }
    }

    private void dealWithIabSetupFailure() {
        Log.d("dealWithIabSetupFailure: setup failed!!!");
        FirebaseCrash.report(new Exception("IAB SETUP FAILED!"));
    }

    protected void purchaseItem(String sku) {
        try {
            if (billingHelper != null) {
                billingHelper.flagEndAsync();
                billingHelper.launchPurchaseFlow(this, sku, 123, this);
            }
        } catch (IllegalStateException e) {
            Log.d("purchaseItem: exception");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (billingHelper == null) return;
        // Pass on the activity result to the helper for handling
        if (!billingHelper.handleActivityResult(requestCode, resultCode, data)) {
            // not handled, so handle it ourselves (here's where you'd
            // perform any handling of activity results not related to in-app
            // billing...
            super.onActivityResult(requestCode, resultCode, data);
        } else {
            Log.d("onActivityResult handled by IABUtil.");
        }
    }

    /**
     * Security Recommendation: When you receive the purchase response from Google Play, make sure
     * to check the returned data
     * signature, the orderId, and the developerPayload string in the Purchase object to make sure
     * that you are getting the
     * expected values. You should verify that the orderId is a unique value that you have not
     * previously processed, and the
     * developerPayload string matches the token that you sent previously with the purchase request.
     * As a further security
     * precaution, you should perform the verification on your own secure server.
     */
    @Override
    public void onIabPurchaseFinished(IabResult result, Purchase info) {
        if (result.isFailure()) {
            dealWithPurchaseFailed(result);
        } else if (SKU_UPGRADE.equals(info.getSku())) {
            dealWithPurchaseSuccessUpgrade(result, info);
        } else if (SKU_DONATION.equals(info.getSku())) {
            dealWithPurchaseSuccessDonate(result, info);
        }
    }

    protected void dealWithPurchaseFailed(IabResult result) {
        Log.d("Error purchasing: " + result);
        ProPreferences.disablePro(LicenseUpgradeActivity.this);
        FirebaseCrash.report(new Exception("Purchase failed. Code: " + result.getResponse() + " " + result.getMessage()));
    }

    protected void dealWithPurchaseSuccessUpgrade(IabResult result, Purchase info) {
        if (billingHelper.isSetupDone() && !billingHelper.isAsyncInProgress()) {
            billingHelper.queryInventoryAsync(false, (result1, inv) -> {
                ProPreferences.enablePro(LicenseUpgradeActivity.this);
                ProPreferences.enableLiveNotification(LicenseUpgradeActivity.this);
                ProPreferences.enableInstantDelay(LicenseUpgradeActivity.this);
                showDialogCustom(R.layout.dialog_gone_pro);
            });
        }
    }

    protected void dealWithPurchaseSuccessDonate(IabResult result, Purchase info) {
        if (billingHelper.isSetupDone() && !billingHelper.isAsyncInProgress()) {
            billingHelper.queryInventoryAsync(false, (result1, inv) -> {
                billingHelper.consumeAsync(inv.getPurchase(SKU_DONATION), null);
                showDialogCustom(R.layout.dialog_donation);
            });
        }
    }

    @Override
    protected void onDestroy() {
        disposeBillingHelper();
        super.onDestroy();
    }

    private void disposeBillingHelper() {
        if (billingHelper != null) {
            billingHelper.dispose();
        }
        billingHelper = null;
    }

    private void showDialogCustom(int layout) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(LicenseUpgradeActivity.this);
        View                view        = LayoutInflater.from(LicenseUpgradeActivity.this).inflate(layout, null);
        alertDialog.setView(view)
                .setPositiveButton("OK", (dialogInterface, i) -> dialogInterface.dismiss())
                .create()
                .show();
    }
}