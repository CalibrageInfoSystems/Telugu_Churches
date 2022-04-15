package in.calibrage.teluguchurches.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;


import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;

import in.calibrage.teluguchurches.services.ChurchService;
import in.calibrage.teluguchurches.services.ServiceFactory;
import in.calibrage.teluguchurches.util.localData.SharedPrefsData;
import in.calibrage.teluguchurches.views.Activities.HomeActivity;
import in.calibrage.teluguchurches.views.model.GetRefreshTokenResponseModel;
import in.calibrage.teluguchurches.views.model.RefreshTokenRequestModel;
import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


import static in.calibrage.teluguchurches.util.Constants.PREF_NAME;

/**
 * Created class to call refresh token
 * it will extend the broadcast receiver to get token
 */

public class RefreshToken extends BroadcastReceiver {
    private Context context;
    private Subscription  refrshSubscription;
    String refreshToken;
    private Subscription mSubscription;
    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        refreshToken = SharedPrefsData.getString(context, Constants.Refresh_Token, PREF_NAME);

        //API call to get the refresh token
        getRefreshToken();
    }


    private void getRefreshToken() {
        if (CommonUtil.isNetworkAvailable(context)) {
            JsonObject object = refreshObject();
            ChurchService service = ServiceFactory.createRetrofitService(context, ChurchService.class);
            mSubscription = service.getRefreshToken(object)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<GetRefreshTokenResponseModel>() {
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
                        public void onNext(GetRefreshTokenResponseModel refreshResponseModel) {
                            if (refreshResponseModel.getIsSuccess()) {
                                SharedPrefsData.getInstance(context).updateStringValue(context,  Constants.Token_Type,  refreshResponseModel.getResult().getTokenType());
                                SharedPrefsData.getInstance(context).updateStringValue(context, Constants.Refresh_Token,  refreshResponseModel.getResult().getRefreshToken());
                                SharedPrefsData.getInstance(context).updateStringValue(context,  Constants.Access_Token, refreshResponseModel.getResult().getAccessToken());
                                SharedPrefsData.getInstance(context).updateStringValue(context, Constants.Auth_Token,
                                        refreshResponseModel.getResult().getTokenType() + " " + refreshResponseModel.getResult().getAccessToken());

                                Intent i = new Intent(context, HomeActivity.class);
                                context.startActivity(i);
                                if (refreshResponseModel.getEndUserMessage().equalsIgnoreCase("Invalid Token")){
                                    CommonUtil.customToast(refreshResponseModel.getEndUserMessage(), context);
                                }

                                CommonUtil.timer.cancel();

                                CommonUtil.timer = new MyCounter(refreshResponseModel.getResult().getExpiresIn()*1000,1000,context);

                                CommonUtil.timer.start();

                            }
                        }
                    });
        }
    }

    private JsonObject refreshObject() {
        RefreshTokenRequestModel requestModel = new RefreshTokenRequestModel();
        requestModel.setClientId(Constants.ClientId);
        requestModel.setClientSecret(Constants.ClientSecret);
        requestModel.setRefreshToken(refreshToken);
        return new Gson().toJsonTree(requestModel).getAsJsonObject();
    }
}
