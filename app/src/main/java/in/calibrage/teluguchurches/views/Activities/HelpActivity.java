package in.calibrage.teluguchurches.views.Activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.crashlytics.android.Crashlytics;
import com.github.barteksc.pdfviewer.PDFView;

import org.apache.commons.io.IOUtils;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import in.calibrage.teluguchurches.R;
import in.calibrage.teluguchurches.services.APIConstants;
import in.calibrage.teluguchurches.services.ChurchService;
import in.calibrage.teluguchurches.services.ServiceFactory;
import in.calibrage.teluguchurches.util.BaseActivity;
import in.calibrage.teluguchurches.util.CommonUtil;
import in.calibrage.teluguchurches.views.model.GetUserManualPdfResponeModel;
import io.fabric.sdk.android.Fabric;
import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

//import org.apache.commons.io.IOUtils;

public class HelpActivity extends BaseActivity {

    private ImageView toolbar_image;
    PDFView pdfView;
    Context mContext;
    Toolbar toolbar;
    ViewPager viewPager;
    int[] helpImage;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    private Subscription mSubscription;
    GetUserManualPdfResponeModel mResponse;
    private ArrayList<Integer> ImagesArray = new ArrayList<Integer>();
    String pdfString;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getApplicationContext();
        // added crash report's to mail
        Fabric.with(getApplicationContext(), new Crashlytics());

        //assining layout
        setContentView(R.layout.activity_help);


        pdfView = findViewById(R.id.pdfView);

        /* intializing and assigning ID's */
        initViews();
        // to check the phone is connected to Internet or Not
        if (isOnline()) {
            // API to get User manual
            getPdfFile();
        } else {
            // when no internet connection
            CommonUtil.customToast(getString(R.string.no_internet), getApplicationContext());
        }

    }

    /* intializing and assigning ID's */
    private void initViews() {

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle(getString(R.string.help));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        toolbar_image = findViewById(R.id.toolbar_image);
        toolbar_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HelpActivity.this, HomeActivity.class));
            }
        });


    }


    // API to get User manual
    private void getPdfFile() {

        ChurchService service = ServiceFactory.createRetrofitService(getApplicationContext(), ChurchService.class);
        String url = APIConstants.GetUserManual + 1;
        mSubscription = service.getUserManualPdf(url)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<GetUserManualPdfResponeModel>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e instanceof HttpException) {
                            ((HttpException) e).code();
                            ((HttpException) e).message();
                            ((HttpException) e).response().errorBody();
                            try {
                                ((HttpException) e).response().errorBody().string();
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }
                            e.printStackTrace();
                        }
                    }


                    @SuppressLint("SetJavaScriptEnabled")
                    @Override
                    public void onNext(GetUserManualPdfResponeModel mResponse) {
                        pdfString = mResponse.getResult();

                        // Async Task Class
                        new PdfFile().execute(pdfString);

                    }
                });

    }

    // Async Task Class
    class PdfFile extends AsyncTask<String, Void, byte[]> {

        // This is run in a background thread
        @Override
        protected byte[] doInBackground(String... strings) {
            // get the string from params
            InputStream inputStream = null;
            try {
                URL url = new URL(strings[0]);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                if (httpURLConnection.getResponseCode() == 200) {
                    inputStream = new BufferedInputStream(httpURLConnection.getInputStream());

                }
            } catch (IOException e) {
                return null;
            }
            try {
                return IOUtils.toByteArray(inputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(byte[] bytes) {
            //pdf file is loading
            pdfView.fromBytes(bytes).load();
        }
    }

}
