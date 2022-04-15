package in.calibrage.teluguchurches.util;

import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;

import in.calibrage.teluguchurches.services.ChurchService;
import in.calibrage.teluguchurches.services.ServiceFactory;
import in.calibrage.teluguchurches.util.localData.SharedPrefsData;
import in.calibrage.teluguchurches.views.Activities.HomeActivity;
import in.calibrage.teluguchurches.views.Activities.LoginActivity;
import in.calibrage.teluguchurches.views.Activities.YoutubePlayerActivity;
import in.calibrage.teluguchurches.views.model.GetLoginPageResponseModel;
import in.calibrage.teluguchurches.views.model.GetRefreshTokenResponseModel;
import in.calibrage.teluguchurches.views.model.LoginPageRequestModel;
import in.calibrage.teluguchurches.views.model.RefreshTokenRequestModel;
import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static in.calibrage.teluguchurches.util.Constants.PREF_NAME;



/*
* class is used to maintain the count down for refresh token
* */
public class MyCounter extends CountDownTimer {

    Context mContext;
    String refreshToken;
    private Subscription mSubscription;


    public MyCounter(long millisInFuture, long countDownInterval, Context mCon) {
        super(millisInFuture, countDownInterval);
        mContext = mCon;

    }

    @Override
    public void onFinish() {
        Intent intent = new Intent();
        intent.setAction("com.TeluguChurches.CUSTOM_INTENT");
        mContext.sendBroadcast(intent);


    }

    @Override
    public void onTick(long millisUntilFinished) {
     // TODO: use this method to update timer
    }

}
