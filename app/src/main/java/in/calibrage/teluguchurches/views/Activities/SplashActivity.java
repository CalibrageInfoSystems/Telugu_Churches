package in.calibrage.teluguchurches.views.Activities;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;

import java.io.IOException;

import in.calibrage.teluguchurches.R;
import in.calibrage.teluguchurches.services.APIConstants;
import in.calibrage.teluguchurches.services.ChurchService;
import in.calibrage.teluguchurches.services.ServiceFactory;
import in.calibrage.teluguchurches.util.BaseActivity;
import in.calibrage.teluguchurches.util.localData.SharedPrefsData;
import in.calibrage.teluguchurches.views.custom_controls.Cis_TextView;
import in.calibrage.teluguchurches.views.model.GetSplashMessage;
import io.fabric.sdk.android.Fabric;
import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static in.calibrage.teluguchurches.util.CommonUtil.updateResources;

public class SplashActivity extends BaseActivity {
    private SQLiteDatabase db;
    public TextView txt_splash;
    private Subscription mSubscription;
    String Title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);

        // added crash report's to mail
        Fabric.with(this, new Crashlytics());

        //assining layout
        setContentView(R.layout.activity_splash);
        ActionBar actionBar = getSupportActionBar();
        txt_splash = findViewById(R.id.txt_splash);


        // to check the phone is connected to Internet or Not
        if (isOnline()) {

            //API to get Splash Message
            getSplash();
        } else {

            // when no internet connection
            showToastLong(getString(R.string.no_internet));
        }


        if (null != actionBar) {
            actionBar.hide();
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (SharedPrefsData.getInstance(SplashActivity.this).getIntFromSharedPrefs("lang") == 1) {
                    updateResources(SplashActivity.this, "en-US");
                    Intent i = new Intent(SplashActivity.this, HomeActivity.class);
                    startActivity(i);
                    // close this activity
                    finish();
                } else if (SharedPrefsData.getInstance(SplashActivity.this).getIntFromSharedPrefs("lang") == 2) {
                    updateResources(SplashActivity.this, "te");
                    Intent i = new Intent(SplashActivity.this, HomeActivity.class);
                    startActivity(i);
                    // close this activity
                    finish();
                } else {
                    Intent i = new Intent(SplashActivity.this, HomeActivity.class);
                    startActivity(i);
                    // close this activity
                    finish();
                }

            }
        }, 8000);
    }


    //API to get Splash Message
    private void getSplash() {
        ChurchService service = ServiceFactory.createRetrofitService(this, ChurchService.class);
        String url = APIConstants.Get_Splash_message;
        mSubscription = service.getSplashMessage(url)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<GetSplashMessage>() {
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

                    @Override
                    public void onNext(GetSplashMessage splashmessage) {
                        hideProgressDialog();
                        if (splashmessage.getIsSuccess()) {

                            if (splashmessage.getResult() == null) {
                                Title = "Jesus answered, 'I am the way and the truth and the life. No one comes to the Father except through me'.";
                                Cis_TextView txt = findViewById(R.id.txt_splash);
                                txt.setCharacterDelay(50);
                                txt.animateText(Title);
                            } else {
                                Title = splashmessage.getResult().getDesc();
                                Cis_TextView txt = findViewById(R.id.txt_splash);
                                txt.setCharacterDelay(50);
                                txt.animateText(Title);
                            }


                        }


                    }
                });
    }
}
