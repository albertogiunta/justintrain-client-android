package com.jaus.albertogiunta.justintrain_oraritreni.aboutAndSettings;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Button;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.jaus.albertogiunta.justintrain_oraritreni.R;
import com.jaus.albertogiunta.justintrain_oraritreni.utils.helpers.AnalyticsHelper;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import trikita.log.Log;

public class LicenseUpgradeActivity extends AppCompatActivity implements PurchasesUpdatedListener {

    AnalyticsHelper analyticsHelper;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.btn_iap_first)
    Button  buyFirst;
    @BindView(R.id.btn_iap_second)
    Button  buySecond;

    BillingClient mBillingClient;


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

        mBillingClient = new BillingClient.Builder(LicenseUpgradeActivity.this)
                .setListener(this)
                .build();

//        analyticsHelper.logScreenEvent(SCREEN_UPGRADE_LICENSE, IAP_TOP);
//        analyticsHelper.logScreenEvent(SCREEN_UPGRADE_LICENSE, IAP_BOTTOM);

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
//            mBillingClient.startConnection(new BillingClientStateListener() {
//                @Override
//                public void onBillingSetupFinished(@BillingResponse int billingResponse) {
//                    if (billingResponse == BillingResponse.OK) {
//                        // The billing client is ready. You can query purchases here.
//                        List<String> skuList = new ArrayList<>();
//                        skuList.add("premium_upgrade_mp");
//                        mBillingClient.querySkuDetailsAsync(BillingClient.SkuType.INAPP, skuList,
//                                result -> {
//                                    if (result.getResponseCode() == BillingResponse.OK
//                                            && result.getSkuDetailsList() != null) {
//                                        // only 1 item so far
//                                        String                    skuId        = result.getSkuDetailsList().get(0).getSku();
//                                        BillingFlowParams.Builder builder      = new BillingFlowParams.Builder().setSku(skuId).setType(BillingClient.SkuType.INAPP);
//                                        int                       responseCode = mBillingClient.launchBillingFlow(FavouriteJourneysActivity.this, builder.build());
//
//                                    }
//
//                                });
//
//                    }
//                }
//
//                @Override
//                public void onBillingServiceDisconnected() {
//                    // Try to restart the connection on the next request to the
//                    // In-app Billing service by calling the startConnection() method.
//                }
//            });

    }

    @Override
    public void onPurchasesUpdated(@BillingClient.BillingResponse int responseCode, List<Purchase> purchases) {
        if (responseCode == BillingClient.BillingResponse.OK
                && purchases != null) {
            for (Purchase purchase : purchases) {
                Log.d("onPurchasesUpdated: ", purchase.toString());
                mBillingClient.consumeAsync(purchase.getPurchaseToken(), (purchaseToken, resultCode) -> Log.d("onConsumeResponse: ", resultCode));
            }
        } else if (responseCode == BillingClient.BillingResponse.USER_CANCELED) {
            // Handle an error caused by a user cancelling the purchase flow.
        } else {
            // Handle any other error codes.
        }
    }

}
