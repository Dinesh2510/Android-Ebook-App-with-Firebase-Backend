package com.pixeldev.firepdf.activities;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.cloud.firepdf.R;
import com.pixeldev.firepdf.utils.AdsManager;
import com.pixeldev.firepdf.utils.Methods;
import com.pixeldev.firepdf.utils.SharedPrefs;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnErrorListener;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class PdfViewActivity extends AppCompatActivity {
    ProgressBar ProgressBar;
    Button Retrybtn;
    FrameLayout adContainerView;
    AdsManager adsManager;
    File appDirectory;
    String file_url;
    String filename;
    InputStream input = null;
    RelativeLayout no_internet;
    OutputStream output = null;
    PDFView pdfView;
    boolean sucessDownload = false;
    File tempFile;
    TextView txtNoConnection;
    TextView txtcheck;

    
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_pdf_view);
       adsManager = AdsManager.getInstance();
       adContainerView = (FrameLayout) findViewById(R.id.adContainerView);
       no_internet = (RelativeLayout) findViewById(R.id.no_internet);
       Retrybtn = (Button) findViewById(R.id.Retrybtn);
       txtNoConnection = (TextView) findViewById(R.id.txtNoConnection);
       txtcheck = (TextView) findViewById(R.id.txtcheck);
       pdfView = (PDFView) findViewById(R.id.pdfView);
       ProgressBar = (ProgressBar) findViewById(R.id.progressBar);
       Retrybtn.setOnClickListener(view -> checkConnection());
       file_url = getIntent().getStringExtra("file_url");
        File externalFilesDir = getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
       appDirectory = externalFilesDir;
        if (!externalFilesDir.isDirectory() && !this.appDirectory.exists()) {
           appDirectory.mkdirs();
        }
        String str =file_url;
        if (str != null) {
            String substring = str.substring(str.lastIndexOf("/") + 1);
           filename = substring;
            if (substring.contains("?")) {
               filename =filename.split("\\?")[0];
            }
           tempFile = new File(this.appDirectory,filename);
           tempFile = new File(getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS),filename);
            checkConnection();
            return;
        }
       no_internet.setVisibility(View.VISIBLE);
       ProgressBar.setVisibility(View.GONE);
    }



    private void checkConnection() {
        if (Methods.isNetworkAvailable(this)) {
           no_internet.setVisibility(View.GONE);
            if (this.tempFile.exists()) {
               sucessDownload = true;
                initOpenPDF();
                return;
            }
            new DownloadFileFromURL().execute(new String[]{this.file_url});
            return;
        }
       no_internet.setVisibility(View.VISIBLE);
       ProgressBar.setVisibility(View.GONE);
    }

    
    public void initOpenPDF() {
        openPDF(Uri.fromFile(this.tempFile), SharedPrefs.getPreferenceOfPDF(this, SharedPrefs.SAVED_PDF_PAGES,filename, 0));
        updateAds();
    }

    public final void openPDF(Uri localUri, int page) {
       pdfView.fromUri(localUri).enableAntialiasing(true).defaultPage(page).fitEachPage(false).scrollHandle(new DefaultScrollHandle(this)).onError(new OnErrorListener() {
            public void onError(Throwable t) {
                if (PdfViewActivity.this.tempFile.exists()) {
                    PdfViewActivity.this.tempFile.delete();
                }
                PdfViewActivity.this.no_internet.setVisibility(View.VISIBLE);
                PdfViewActivity.this.ProgressBar.setVisibility(View.GONE);
            }
        }).onLoad(new OnLoadCompleteListener() {
            public void loadComplete(int nbPages) {
                PdfViewActivity.this.ProgressBar.setVisibility(View.GONE);
            }
        }).onPageChange(new OnPageChangeListener() {
            public void onPageChanged(int page, int pageCount) {
                PdfViewActivity pdfViewActivity = PdfViewActivity.this;
                SharedPrefs.setPreferenceOfPDF(pdfViewActivity, SharedPrefs.SAVED_PDF_PAGES, pdfViewActivity.filename, page);
            }
        }).load();
    }

    class DownloadFileFromURL extends AsyncTask<String, String, String> {
        DownloadFileFromURL() {
        }

        
        public void onPreExecute() {
            super.onPreExecute();
            Methods.showDownloadDialog(PdfViewActivity.this);
        }

        
        public String doInBackground(String... f_url) {
            try {
                URL url = new URL(f_url[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setConnectTimeout(5000);
                connection.setRequestProperty("Accept-Encoding", "identity");
                connection.connect();
                int lenghtOfFile = connection.getContentLength();
                PdfViewActivity.this.input = new BufferedInputStream(url.openStream(), 1024);
                PdfViewActivity.this.output = new FileOutputStream(PdfViewActivity.this.tempFile);
                byte[] data = new byte[1024];
                long total = 0;
                while (true) {
                    int read = PdfViewActivity.this.input.read(data);
                    int count = read;
                    if (read != -1) {
                        total += (long) count;
                        publishProgress(new String[]{"" + ((int) ((100 * total) / ((long) lenghtOfFile)))});
                        PdfViewActivity.this.output.write(data, 0, count);
                    } else {
                        PdfViewActivity.this.output.flush();
                        PdfViewActivity.this.output.close();
                        PdfViewActivity.this.input.close();
                        return null;
                    }
                }
            } catch (Exception e) {
                Methods.dismissDownloadDialog();
                PdfViewActivity.this.sucessDownload = false;
                PdfViewActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        PdfViewActivity.this.no_internet.setVisibility(View.VISIBLE);
                        PdfViewActivity.this.ProgressBar.setVisibility(View.GONE);
                        PdfViewActivity.this.txtcheck.setText(PdfViewActivity.this.getString(R.string.no_data_msg));
                    }
                });
                return null;
            }
        }

        
        public void onProgressUpdate(String... progress) {
            Methods.updateDownloadDialog(Integer.valueOf(Integer.parseInt(progress[0])), Integer.parseInt(progress[0]) + "%");
        }

        
        public void onPostExecute(String file_url) {
            if (PdfViewActivity.this.tempFile.exists()) {
                PdfViewActivity.this.sucessDownload = true;
                Methods.dismissDownloadDialog();
                PdfViewActivity.this.initOpenPDF();
                return;
            }
            Methods.dismissDownloadDialog();
            PdfViewActivity.this.ProgressBar.setVisibility(View.GONE);
            PdfViewActivity.this.sucessDownload = false;
            if (PdfViewActivity.this.tempFile.exists()) {
                PdfViewActivity.this.tempFile.delete();
            }
            PdfViewActivity.this.txtcheck.setText(PdfViewActivity.this.getString(R.string.no_data_msg));
            PdfViewActivity.this.no_internet.setVisibility(View.VISIBLE);
        }
    }

    public void onBackPressed() {
        super.onBackPressed();
    }

    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    
    public void onDestroy() {
        if (!this.sucessDownload &&tempFile.exists()) {
           tempFile.delete();
        }
        super.onDestroy();
    }

    private void updateAds() {
       adsManager.showBannerAd(this,adContainerView);
    }

    
    public void onPause() {
        super.onPause();
       adsManager.destroyBannerAds();
    }

    
    public void onResume() {
        super.onResume();
        updateAds();
    }
}
