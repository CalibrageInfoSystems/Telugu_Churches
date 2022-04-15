package in.calibrage.teluguchurches.views.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.crashlytics.android.Crashlytics;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;

import in.calibrage.teluguchurches.R;
import in.calibrage.teluguchurches.services.APIConstants;
import in.calibrage.teluguchurches.services.ChurchService;
import in.calibrage.teluguchurches.services.ServiceFactory;
import in.calibrage.teluguchurches.util.CommonUtil;
import in.calibrage.teluguchurches.util.Constants;
import in.calibrage.teluguchurches.util.localData.SharedPrefsData;
import in.calibrage.teluguchurches.views.Activities.LoginActivity;
import in.calibrage.teluguchurches.views.model.GetAuthorsResponse;
import in.calibrage.teluguchurches.views.model.SubscribeRequestModel;
import in.calibrage.teluguchurches.views.model.SubscribeResponseModel;
import io.fabric.sdk.android.Fabric;
import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseSequence;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView;
import uk.co.deanwild.materialshowcaseview.ShowcaseConfig;


public class DetailsFragment extends Fragment {
    View rootView;
    ImageView userImage;
    TextView regNoText, nameText, stateText, disText,
            mandalText, villageText, pincodeText, firstText,
            middleText, lastText, pastorText, pastorEmailText,
            mobileText, dobText, genderText, countryNameText, authorvillageText,
            authormandalText, authordisText, authorstateText, authorpincodeText, authorcountryNameText;
    private Subscription mSubscription;
    Context mContext;
    Button subscribeBtn, ok_btn, cancel_btn;
    int authorId, subscibeval, userId, value;
    private AlertDialog alert;
    private String authorizationToken;
    private static final String SHOWCASE_ID = "sequence category";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext = getActivity();

        // added crash report's to mail
        Fabric.with(getActivity(), new Crashlytics());

        //assining layout
        rootView = inflater.inflate(R.layout.church_details, null, false);

        /* intializing and assigning ID's */
        initView();


        subscribeBtn = rootView.findViewById(R.id.subscribeBtn);

        //storing value's from shared preference
        userId = SharedPrefsData.getInt(mContext, Constants.ID, Constants.PREF_NAME);
        value = SharedPrefsData.getInt(mContext, Constants.ISLOGIN, Constants.PREF_NAME);


        /**
         * @param OnClickListner
         */
        subscribeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (value == 0) {
                    LayoutInflater layoutInflater = LayoutInflater.from(mContext);
                    View dialogRootView = layoutInflater.inflate(R.layout.dialog_subscribe, null);
                    final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
                    ok_btn = (Button) dialogRootView.findViewById(R.id.ok_btn);
                    cancel_btn = (Button) dialogRootView.findViewById(R.id.cancel_btn);

                    alertDialogBuilder.setView(dialogRootView);

                    /**
                     * @param OnClickListner
                     */
                    ok_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(mContext, LoginActivity.class);
                            startActivity(intent);
                        }
                    });

                    /**
                     * @param OnClickListner
                     */
                    cancel_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            alert.dismiss();
                        }
                    });

                    alert = alertDialogBuilder.create();
                    alert.show();
                } else {
                    // API to subscribe the pastor
                    getSubscribe();
                }

            }
        });
        return rootView;
    }
    /* intializing and assigning ID's */
    private void initView() {
        regNoText = rootView.findViewById(R.id.regNoText);
        nameText = rootView.findViewById(R.id.nameText);
        stateText = rootView.findViewById(R.id.stateText);
        authorstateText = rootView.findViewById(R.id.authorstateText);
        disText = rootView.findViewById(R.id.disText);
        authordisText = rootView.findViewById(R.id.authordisText);
        mandalText = rootView.findViewById(R.id.mandalText);
        villageText = rootView.findViewById(R.id.villageText);
        authorvillageText = rootView.findViewById(R.id.authorvillageText);
        authormandalText = rootView.findViewById(R.id.authormandalText);
        pincodeText = rootView.findViewById(R.id.pincodeText);
        authorpincodeText = rootView.findViewById(R.id.authorpincodeText);
        firstText = rootView.findViewById(R.id.firstText);
        middleText = rootView.findViewById(R.id.middleText);
        lastText = rootView.findViewById(R.id.lastText);
        pastorText = rootView.findViewById(R.id.pastorText);
        pastorEmailText = rootView.findViewById(R.id.pastorEmailText);
        mobileText = rootView.findViewById(R.id.mobileText);
        dobText = rootView.findViewById(R.id.dobText);
        genderText = rootView.findViewById(R.id.genderText);
        countryNameText = rootView.findViewById(R.id.countryNameText);
        authorcountryNameText = rootView.findViewById(R.id.authorcountryNameText);

        // API to get pastor details
        getAuthorsId();
    }

    // API to subscribe the pastor
    private void getSubscribe() {
        if (authorizationToken != "null" || authorizationToken != null || authorizationToken.isEmpty())
            authorizationToken = SharedPrefsData.getInstance(mContext).getStringFromSharedPrefs(Constants.Auth_Token);
        JsonObject object = subscribeObject();
        ChurchService service = ServiceFactory.createRetrofitService(mContext, ChurchService.class);
        mSubscription = service.getSubscribe(object, authorizationToken)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<SubscribeResponseModel>() {
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
                    public void onNext(final SubscribeResponseModel subscribeResponseModel) {
                        subscibeval = subscribeResponseModel.getResult().getIsSubscribed();
                        Toast.makeText(mContext, subscribeResponseModel.getEndUserMessage(), Toast.LENGTH_SHORT).show();
                        if (subscribeResponseModel.getResult().getIsSubscribed() == 0) {
                            subscribeBtn.setText(R.string.subscribe);
                        } else if (subscribeResponseModel.getResult().getIsSubscribed() == 1) {
                            subscribeBtn.setText(R.string.un_subscribe);
                        } else {
                            subscribeBtn.setText(R.string.subscribe);
                        }


                    }
                });
    }

    /**
     * Json Object of subscribeObject
     *
     * @return
     */
    private JsonObject subscribeObject() {
        SubscribeRequestModel mRequest = new SubscribeRequestModel();
        mRequest.setIsSubscribed(subscibeval);
        mRequest.setAuthorId(authorId);
        mRequest.setChurchId(null);
        mRequest.setUserId(userId);
        return new Gson().toJsonTree(mRequest).getAsJsonObject();
    }

    // API to get pastor details
    private void getAuthorsId() {
        ChurchService service = ServiceFactory.createRetrofitService(mContext, ChurchService.class);
        String url = APIConstants.GetAuthorId + SharedPrefsData.getInt(mContext, Constants.AdminId, Constants.PREF_NAME) + "/" + SharedPrefsData.getInt(mContext, Constants.ID, Constants.PREF_NAME);
        mSubscription = service.GetAuthorId(url)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<GetAuthorsResponse>() {
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
                    public void onNext(GetAuthorsResponse mResponse) {
                        if (mResponse.getIsSuccess()) {
                            regNoText.setText(mResponse.getListResult().get(0).getRegistrationNumber());
                            nameText.setText(mResponse.getListResult().get(0).getChurchName());
                            stateText.setText(mResponse.getListResult().get(0).getStateName());
                            authorstateText.setText(mResponse.getListResult().get(0).getStateName());
                            disText.setText(mResponse.getListResult().get(0).getDistrictName());
                            authordisText.setText(mResponse.getListResult().get(0).getDistrictName());
                            mandalText.setText(mResponse.getListResult().get(0).getMandalName());
                            authormandalText.setText(mResponse.getListResult().get(0).getMandalName());
                            villageText.setText(mResponse.getListResult().get(0).getChurchAddress());
                            authorvillageText.setText(mResponse.getListResult().get(0).getVillageName());
                            countryNameText.setText(mResponse.getListResult().get(0).getCountryName());
                            authorcountryNameText.setText(mResponse.getListResult().get(0).getCountryName());
                            firstText.setText(mResponse.getListResult().get(0).getFirstName() + " "
                                    + mResponse.getListResult().get(0).getMiddleName() + " " + mResponse.getListResult().get(0).getLastName());
                            middleText.setText(mResponse.getListResult().get(0).getMiddleName());
                            lastText.setText(mResponse.getListResult().get(0).getLastName());
                            pastorText.setText(mResponse.getListResult().get(0).getUserName());
                            pastorEmailText.setText(mResponse.getListResult().get(0).getAuthorEmail());
                            mobileText.setText(mResponse.getListResult().get(0).getAuthorContactNumbar());
                            if (mResponse.getListResult().get(0).getPinCode() != null) {
                                authorpincodeText.setText(mResponse.getListResult().get(0).getPinCode().toString());
                                pincodeText.setText(mResponse.getListResult().get(0).getPinCode().toString());
                            } else {
                                pincodeText.setText(" ");
                                authorpincodeText.setText(" ");
                            }
                            dobText.setText(CommonUtil.dateFormat(mResponse.getListResult().get(0).getDob()));
                            genderText.setText(mResponse.getListResult().get(0).getGender());
                            authorId = mResponse.getListResult().get(0).getId();

                            subscibeval = mResponse.getListResult().get(0).getIsSubscribed();
                            if (subscibeval == 0) {
                                subscribeBtn.setText(R.string.subscribe);
                            } else if (subscibeval == 1) {
                                subscribeBtn.setText(R.string.un_subscribe);
                            } else {
                                subscribeBtn.setText(R.string.subscribe);
                            }
                            //authorId = mResponse.getListResult().get(0).getUserId();
                            Glide.with(mContext).load(mResponse.getListResult().get(0).getChurchImage())
                                    .fitCenter()
                                    .error(R.drawable.pastor)
                                    .into(userImage);
                        }
                        /*
                         * presenting the show case view
                         * */
                        presentShowcaseSequence();

                    }
                });

    }

    /*
     * presenting the show case view
     * */
    private void presentShowcaseSequence() {

        ShowcaseConfig config = new ShowcaseConfig();
        config.setDelay(100); // half second between each showcase view

        MaterialShowcaseSequence sequence = new MaterialShowcaseSequence(getActivity(), SHOWCASE_ID);

        sequence.setOnItemShownListener(new MaterialShowcaseSequence.OnSequenceItemShownListener() {

            @Override
            public void onShow(MaterialShowcaseView itemView, int position) {
                //   Toast.makeText(itemView.getContext(), "Item #" + position, Toast.LENGTH_SHORT).show();
            }
        });

        sequence.setConfig(config);
        sequence.addSequenceItem(
                new MaterialShowcaseView.Builder(getActivity())
                        .setTarget(subscribeBtn)
                        .setDismissText("GOT IT")
                        .setContentText("Click Here To Subscribe Author")
                        .setContentTextColor(getResources().getColor(R.color.mediumTurquoise))
                        .setDismissTextColor(getResources().getColor(R.color.white))
                        .withCircleShape()
                        .build()
        );


        sequence.start();

    }
}
