package com.pixeldev.firepdf.utils;

import android.app.Activity;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.widget.FrameLayout;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

public class AdsManager {
    private static AdsManager mInstance;
    AdView admobSmallBanner;
    public InterstitialAd googleFullscreen;
    public int inter_i = 0;

    public interface InterAdListener {
        void onClick(String str);
    }

    public static synchronized AdsManager getInstance() {
        AdsManager adsManager;
        synchronized (AdsManager.class) {
            if (mInstance == null) {
                mInstance = new AdsManager();
            }
            adsManager = mInstance;
        }
        return adsManager;
    }

    public void showBannerAd(Activity activity, FrameLayout adContainerView) {
        if (AdsConfig.isAdsEnabled) {
            loadAdMOBBanner(activity, adContainerView);
        } else {
            adContainerView.setVisibility(View.GONE);
        }
    }

    public void loadAdMOBBanner(Activity activity, final FrameLayout adContainerView) {
        adContainerView.setVisibility(View.GONE);
        AdView adView = new AdView(activity);
       admobSmallBanner = adView;
        adView.setAdUnitId(AdsConfig.ADMOB_SMALL_BANNER_AD_ID);
        AdRequest adRequest = new AdRequest.Builder().build();
       admobSmallBanner.setAdSize(getAdSize(activity));
       admobSmallBanner.setAdListener(new AdListener() {
            public void onAdFailedToLoad(LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
                adContainerView.setVisibility(View.GONE);
            }

            public void onAdLoaded() {
                super.onAdLoaded();
                adContainerView.removeAllViews();
                adContainerView.addView(AdsManager.this.admobSmallBanner);
                adContainerView.setVisibility(View.VISIBLE);
            }
        });
       admobSmallBanner.loadAd(adRequest);
    }

    public AdSize getAdSize(Activity activity) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(activity, (int) (((float) outMetrics.widthPixels) / outMetrics.density));
    }

    public void showInterAdOnClick(final Activity activity, final InterAdListener interAdListener, final String type) {
        if (!AdsConfig.isAdsEnabled) {
            interAdListener.onClick(type);
        } else if (this.inter_i == AdsConfig.SHOW_INTER_ON_CLICKS) {
           inter_i = 0;
            InterstitialAd interstitialAd =googleFullscreen;
            if (interstitialAd != null) {
                interstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                    public void onAdDismissedFullScreenContent() {
                        interAdListener.onClick(type);
                        AdsManager.this.loadInterAd(activity);
                        super.onAdDismissedFullScreenContent();
                    }

                    public void onAdFailedToShowFullScreenContent(AdError adError) {
                        interAdListener.onClick(type);
                        AdsManager.this.loadInterAd(activity);
                        super.onAdFailedToShowFullScreenContent(adError);
                    }
                });
               googleFullscreen.show(activity);
                return;
            }
            interAdListener.onClick(type);
            loadInterAd(activity);
        } else {
           inter_i++;
            interAdListener.onClick(type);
        }
    }

    public void loadInterAd(Activity activity) {
        if (AdsConfig.isAdsEnabled) {
            final FullScreenContentCallback fullScreenContentCallback = new FullScreenContentCallback() {
                public void onAdDismissedFullScreenContent() {
                    AdsManager.this.googleFullscreen = null;
                }

                public void onAdShowedFullScreenContent() {
                    super.onAdShowedFullScreenContent();
                }

                public void onAdFailedToShowFullScreenContent(AdError adError) {
                    super.onAdFailedToShowFullScreenContent(adError);
                }
            };
            InterstitialAd.load(activity, AdsConfig.ADMOB_INTER_AD_ID, new AdRequest.Builder().build(), new InterstitialAdLoadCallback() {
                public void onAdLoaded(InterstitialAd ad) {
                    AdsManager.this.googleFullscreen = ad;
                    AdsManager.this.googleFullscreen.setFullScreenContentCallback(fullScreenContentCallback);
                }

                public void onAdFailedToLoad(LoadAdError adError) {
                    AdsManager.this.googleFullscreen = null;
                    AdsManager.this.inter_i = 0;
                }
            });
        }
    }

    public void destroyBannerAds() {
        AdView adView;
        if (AdsConfig.isAdsEnabled && (adView =admobSmallBanner) != null) {
            adView.removeAllViews();
           admobSmallBanner.destroy();
        }
    }

    public void destroyInterAds() {
        if (AdsConfig.isAdsEnabled &&googleFullscreen != null) {
           googleFullscreen = null;
        }
    }
}
