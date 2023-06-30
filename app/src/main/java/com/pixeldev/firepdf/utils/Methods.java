package com.pixeldev.firepdf.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;
import com.cloud.firepdf.R;
import com.pixeldev.firepdf.activities.MainActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

public class Methods {
    public static Dialog downloadDialog;

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null) {
            return false;
        }
        for (NetworkInfo networkInfo : connectivity.getAllNetworkInfo()) {
            if (networkInfo.getState() == NetworkInfo.State.CONNECTED) {
                return true;
            }
        }
        return false;
    }

    public static void loadImage(ImageView imageView, String url, final ProgressBar progressBar) {
        Glide.with(imageView.getContext()).load(url).listener(new RequestListener<Drawable>() {
            public boolean onLoadFailed(GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                progressBar.setVisibility(View.GONE);
                return false;
            }

            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                progressBar.setVisibility(View.GONE);
                return false;
            }
        }).into(imageView);
    }

    public static ActionBar getActionBar(Context context) {
        return ((MainActivity) context).getSupportActionBar();
    }

    public static void displaySelectedFragment(Fragment fragment) {
        MainActivity.fragmentManager.beginTransaction().replace((int) R.id.fragment_container, fragment, (String) null).addToBackStack((String) null).commit();
    }

    public static void showDownloadDialog(Activity activity) {
        Dialog dialog = new Dialog(activity);
        downloadDialog = dialog;
        dialog.setContentView(R.layout.download_dialog);
        //downloadDialog.getWindow().setBackgroundDrawable(activity.getDrawable(R.drawable.custom_dialog_background));
        downloadDialog.setCanceledOnTouchOutside(false);
        downloadDialog.setCancelable(false);
      //  downloadDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        downloadDialog.show();
        downloadDialog.getWindow().getAttributes().width = -1;
        downloadDialog.getWindow().getAttributes().height = -2;
    }

    public static void dismissDownloadDialog() {
        Dialog dialog = downloadDialog;
        if (dialog != null && dialog.isShowing()) {
            downloadDialog.dismiss();
        }
    }

    public static void updateDownloadDialog(Integer ipercentage, String percentage) {
        Dialog dialog = downloadDialog;
        if (dialog != null && dialog.isShowing()) {
            ProgressBar progressBar = (ProgressBar) downloadDialog.findViewById(R.id.download_progressBar);
            progressBar.setProgress(ipercentage.intValue());
            progressBar.setMax(100);
            ((TextView) downloadDialog.findViewById(R.id.progress_percentage)).setText(percentage);
        }
    }
}
