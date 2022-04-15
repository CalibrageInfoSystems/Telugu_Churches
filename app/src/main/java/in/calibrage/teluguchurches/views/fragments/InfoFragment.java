package in.calibrage.teluguchurches.views.fragments;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
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
import in.calibrage.teluguchurches.views.model.ChurchesSubscribeResponseModel;
import in.calibrage.teluguchurches.views.model.GetChurchResponse;
import in.calibrage.teluguchurches.views.model.SubscribeRequestModel;
import io.fabric.sdk.android.Fabric;
import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseSequence;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView;
import uk.co.deanwild.materialshowcaseview.ShowcaseConfig;


public class InfoFragment extends Fragment implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {
    private Context mContext;
    private View rootView;
    private TextView idText, regNoText, nameText, addressText,
            landText, mobileText, emailText, PastorText,
            descText, visionText, missionText, webText,
            stateText, disText, mandalText, villageText,
            pincodeText, dobText, genderText, PastorEmailText,
            churchContactText, openingTimeText, closingTimeText,
            countryText, usernameText;
    GoogleMap mGoogleMap;
    SupportMapFragment mapFrag;
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Button subscribeBtn, ok_btn, cancel_btn;
    int churchId, subscibeval, userId, value, notificationChurchId;
    Marker mCurrLocationMarker;
    private Subscription mSubscription;
    private AlertDialog alert;
    private String nameChurchStr, stateChurchStr, districtChurchStr, authorizationToken;
    private Double latitudeStr, longitudeStr;
    private static final String SHOWCASE_ID = "sequence category";


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext = getActivity();

        // added crash report's to mail
        Fabric.with(getActivity(), new Crashlytics());

        //assining layout
        rootView = inflater.inflate(R.layout.church_info, null, false);

        mapFrag = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.map);

        mapFrag.getMapAsync(this);


        if (getArguments() != null) {
            notificationChurchId = getArguments().getInt("churchId", 0);
        }
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
                    ok_btn = dialogRootView.findViewById(R.id.ok_btn);
                    cancel_btn = dialogRootView.findViewById(R.id.cancel_btn);

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
                    getSubscribe();
                }

            }
        });

        /* intializing and assigning ID's */
        initViews();

        setViews();
        return rootView;
    }

    /* intializing and assigning ID's */
    private void initViews() {
        regNoText = rootView.findViewById(R.id.regNoText);
        nameText = rootView.findViewById(R.id.nameText);
        addressText = rootView.findViewById(R.id.addressText);
        landText = rootView.findViewById(R.id.landText);
        mobileText = rootView.findViewById(R.id.mobileText);
        emailText = rootView.findViewById(R.id.emailText);
        PastorText = rootView.findViewById(R.id.PastorText);
        descText = rootView.findViewById(R.id.descText);
        visionText = rootView.findViewById(R.id.visionText);
        missionText = rootView.findViewById(R.id.missionText);
        webText = rootView.findViewById(R.id.webText);
        stateText = rootView.findViewById(R.id.stateText);
        disText = rootView.findViewById(R.id.disText);
        mandalText = rootView.findViewById(R.id.mandalText);
        villageText = rootView.findViewById(R.id.villageText);
        pincodeText = rootView.findViewById(R.id.pincodeText);
        dobText = rootView.findViewById(R.id.dobText);
        genderText = rootView.findViewById(R.id.genderText);
        churchContactText = rootView.findViewById(R.id.churchContactText);
        PastorEmailText = rootView.findViewById(R.id.PastorEmailText);
        openingTimeText = rootView.findViewById(R.id.openingTimeText);
        closingTimeText = rootView.findViewById(R.id.closingTimeText);
        countryText = rootView.findViewById(R.id.countryText);
        usernameText = rootView.findViewById(R.id.usernameText);

        // API to get church details by churchID
        getChurchById();

    }

    private void setViews() {
        //method for show case View
        presentShowcaseSequence();
    }

    // API to get church details by churchID
    private void getChurchById() {
        ChurchService service = ServiceFactory.createRetrofitService(mContext, ChurchService.class);
        String url = APIConstants.GetChurchbyId + SharedPrefsData.getInt(mContext, String.valueOf(Constants.Churchid), Constants.PREF_NAME) + "/" + SharedPrefsData.getInt(mContext, Constants.ID, Constants.PREF_NAME);
        mSubscription = service.GetChurchById(url)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<GetChurchResponse>() {
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
                    public void onNext(GetChurchResponse mResponse) {
                        if (mResponse.getIsSuccess()) {
                            regNoText.setText(mResponse.getListResult().get(0).getRegistrationNumber());
                            churchContactText.setText(mResponse.getListResult().get(0).getContactNumber());
                            openingTimeText.setText(CommonUtil.formatTime(mResponse.getListResult().get(0).getOpeningTime()));
                            closingTimeText.setText(CommonUtil.formatTime(mResponse.getListResult().get(0).getClosingTime()));
                            nameChurchStr = mResponse.getListResult().get(0).getName();
                            stateChurchStr = mResponse.getListResult().get(0).getStateName();
                            districtChurchStr = mResponse.getListResult().get(0).getDistrictName();
                            latitudeStr = mResponse.getListResult().get(0).getLatitude();
                            longitudeStr = mResponse.getListResult().get(0).getLongitude();
                            nameText.setText(mResponse.getListResult().get(0).getName());
                            addressText.setText(mResponse.getListResult().get(0).getAddress1() + ", " + (mResponse.getListResult().get(0).getAddress2()));
                            landText.setText(mResponse.getListResult().get(0).getLandMark());
                            mobileText.setText(mResponse.getListResult().get(0).getUserContactNumbar());
                            emailText.setText(mResponse.getListResult().get(0).getEmail());
                            PastorText.setText(mResponse.getListResult().get(0).getPasterUser());
                            usernameText.setText(mResponse.getListResult().get(0).getUserName());
                            descText.setText(mResponse.getListResult().get(0).getDescription());
                            visionText.setText(mResponse.getListResult().get(0).getVision());
                            missionText.setText(mResponse.getListResult().get(0).getMission());
                            webText.setText(mResponse.getListResult().get(0).getWebsiteAddress());
                            stateText.setText(mResponse.getListResult().get(0).getStateName());
                            countryText.setText(mResponse.getListResult().get(0).getCountryName());
                            disText.setText(mResponse.getListResult().get(0).getDistrictName());
                            mandalText.setText(mResponse.getListResult().get(0).getMandalName());
                            villageText.setText(mResponse.getListResult().get(0).getVillageName());
                            pincodeText.setText("" + mResponse.getListResult().get(0).getPinCode());
                            PastorEmailText.setText(mResponse.getListResult().get(0).getUserEmail());
                            dobText.setText(CommonUtil.dateFormat(mResponse.getListResult().get(0).getDob()));
                            genderText.setText(mResponse.getListResult().get(0).getGender());
                            churchId = mResponse.getListResult().get(0).getId();
                            subscibeval = mResponse.getListResult().get(0).getIsSubscribed();

                            if (subscibeval == 0) {
                                subscribeBtn.setText(R.string.subscribe);
                            } else if (subscibeval == 1) {
                                subscribeBtn.setText(R.string.un_subscribe);
                            } else {
                                subscribeBtn.setText(R.string.subscribe);
                            }

                        }

                    }
                });
    }

    // API to subscribe the church
    private void getSubscribe() {
        if (authorizationToken != "null" || authorizationToken != null || authorizationToken.isEmpty())
            authorizationToken = SharedPrefsData.getInstance(mContext).getStringFromSharedPrefs(Constants.Auth_Token);
        JsonObject object = subscribeObject();
        ChurchService service = ServiceFactory.createRetrofitService(mContext, ChurchService.class);
        mSubscription = service.getchurchSubscribe(object, authorizationToken)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ChurchesSubscribeResponseModel>() {
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
                    public void onNext(ChurchesSubscribeResponseModel churchSubscribeResponseModel) {
                        subscibeval = churchSubscribeResponseModel.getResult().getIsSubscribed();
                        Toast.makeText(mContext, churchSubscribeResponseModel.getEndUserMessage(), Toast.LENGTH_SHORT).show();
                        if (churchSubscribeResponseModel.getResult().getIsSubscribed() == 0) {
                            subscribeBtn.setText(R.string.subscribe);
                        } else if (churchSubscribeResponseModel.getResult().getIsSubscribed() == 1) {
                            subscribeBtn.setText(R.string.un_subscribe);
                        } else {
                            subscribeBtn.setText(R.string.subscribe);
                        }


                    }
                });
    }

    // API request object to subscribe the church
    private JsonObject subscribeObject() {
        SubscribeRequestModel mRequest = new SubscribeRequestModel();
        mRequest.setIsSubscribed(subscibeval);
        mRequest.setAuthorId(null);
        mRequest.setChurchId(churchId);
        mRequest.setUserId(userId);
        return new Gson().toJsonTree(mRequest).getAsJsonObject();
    }


    // MAP method to show the church location in Google Map
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mGoogleMap.getUiSettings().setZoomControlsEnabled(true);

        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(mContext,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                //Location Permission already granted
                buildGoogleApiClient();
                mGoogleMap.setMyLocationEnabled(true);
            } else {
                //Request Location Permission
                checkLocationPermission();
            }
        } else {
            buildGoogleApiClient();
            mGoogleMap.setMyLocationEnabled(true);
        }

    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(mContext)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }


    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }
        Double lat = latitudeStr;
        Double longitude = longitudeStr;

        //Place current location marker
        if (lat != null && longitude != null) {
            LatLng latLng = new LatLng(lat, longitude);
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.title(nameChurchStr + "," + districtChurchStr + "," + stateChurchStr);
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
            mCurrLocationMarker = mGoogleMap.addMarker(markerOptions);

            //move map camera
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(lat, longitude)));
            mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(19));

            //stop location updates
            if (mGoogleApiClient != null) {
                LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, (com.google.android.gms.location.LocationListener) this);
            }
        }

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(mContext,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }


    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onPause() {
        super.onPause();
        //stop location updates when Activity is no longer active
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(mContext,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // Permission was granted.
                    if (ContextCompat.checkSelfPermission(getActivity(),
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mGoogleMap.setMyLocationEnabled(true);
                    }

                } else {

                    // Permission denied, Disable the functionality that depends on this permission.
                    Toast.makeText(mContext, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other permissions this app might request.
            //You can add here other case statements according to your requirement.
        }
    }
    //method for show case View
    private void presentShowcaseSequence() {

        ShowcaseConfig config = new ShowcaseConfig();
        config.setDelay(100); // half second between each showcase view

        MaterialShowcaseSequence sequence = new MaterialShowcaseSequence(getActivity(), SHOWCASE_ID);

        sequence.setOnItemShownListener(new MaterialShowcaseSequence.OnSequenceItemShownListener() {

            @Override
            public void onShow(MaterialShowcaseView itemView, int position) {

            }
        });

        sequence.setConfig(config);
        sequence.addSequenceItem(
                new MaterialShowcaseView.Builder(getActivity())
                        .setTarget(subscribeBtn)
                        .setDismissText("GOT IT")
                        .setContentText("Click Here To Subscribe Church")
                        .setContentTextColor(getResources().getColor(R.color.mediumTurquoise))
                        .setDismissTextColor(getResources().getColor(R.color.white))
                        .withCircleShape()
                        .build()
        );


        sequence.start();

    }

}
