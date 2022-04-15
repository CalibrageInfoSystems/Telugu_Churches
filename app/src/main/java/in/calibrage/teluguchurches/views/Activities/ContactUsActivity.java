package in.calibrage.teluguchurches.views.Activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;

import java.io.IOException;

import in.calibrage.teluguchurches.R;
import in.calibrage.teluguchurches.services.APIConstants;
import in.calibrage.teluguchurches.services.ChurchService;
import in.calibrage.teluguchurches.services.ServiceFactory;
import in.calibrage.teluguchurches.util.BaseActivity;
import in.calibrage.teluguchurches.util.CommonUtil;
import in.calibrage.teluguchurches.views.model.GetContactDetails;
import io.fabric.sdk.android.Fabric;
import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ContactUsActivity  extends BaseActivity {

    private TextView contactnumberText,emailidText,addressText;
    private ImageView headerImg;
    private Context mContext;
    private Subscription mSubscription;
    private String contactNumberStr,emailStr;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = ContactUsActivity.this;

        // added crash report's to mail
        Fabric.with(this, new Crashlytics());
        //assining layout
        setContentView(R.layout.activity_contact_us);

        // checking permission
        checkPermission();

        /* intializing and assigning ID's */
        initViews();

        // to check the phone is connected to Internet or Not
        if (isOnline()) {

            // API to get contact US
           getContactDeatils();
        } else {
            // when no Internet connection
            CommonUtil.customToast(getString(R.string.no_internet), getApplicationContext());
        }
    }



    /* intializing and assigning ID's */
    private void  initViews() {
        Toolbar toolbar =findViewById(R.id.toolbar);
        toolbar.setVisibility(View.VISIBLE);
        toolbar.setTitle(getString(R.string.contact));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        contactnumberText = findViewById(R.id.contactnumberText);

        emailidText = findViewById(R.id.emailidText);
        addressText = findViewById(R.id.addressText);
        headerImg = findViewById(R.id.headerImg);
        headerImg.setColorFilter(ContextCompat.getColor(ContactUsActivity.this, R.color.primaryTransColor), android.graphics.PorterDuff.Mode.MULTIPLY);

    }

    /* Navigation's and using the views */
    private void setViews() {

        /**
         * @param OnClickListner
         */
        emailidText.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("IntentReset")
            @Override
            public void onClick(View v) {
                emailStr = emailidText.getText().toString().trim();

                String[] TO = {emailStr};

                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.setData(Uri.parse("mailto:"));
                emailIntent.setType("text/plain");
                emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
                //emailIntent.putExtra(Intent.EXTRA_CC, CC);
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Contact Us");

                try {
                    startActivity(Intent.createChooser(emailIntent, "Send mail..."));
                    finish();
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(ContactUsActivity.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        /**
         * @param OnClickListner
         */
        contactnumberText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contactNumberStr =  contactnumberText.getText().toString().trim();
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + contactNumberStr));
                startActivity(intent);;
            }
        });
    }

    public static final int MY_PERMISSIONS_REQUEST_PHONE = 99;

    // checking permission
    public boolean checkPermission() {
        if (ContextCompat.checkSelfPermission(mContext,
                Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {

            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.CALL_PHONE)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CALL_PHONE},
                        MY_PERMISSIONS_REQUEST_PHONE);


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CALL_PHONE},
                        MY_PERMISSIONS_REQUEST_PHONE);
            }
            return false;
        } else {
            return true;
        }
    }

    // API to get contact US
    private void getContactDeatils() {

        ChurchService service = ServiceFactory.createRetrofitService(getApplicationContext(), ChurchService.class);
        String url = APIConstants.GetContactDetails ;
        mSubscription = service.getContactus(url)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<GetContactDetails>() {
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
                    public void onNext(GetContactDetails mResponse) {
                        if(!mResponse.equals(null)||!mResponse.equals("")){
                           // emailidText.setText(mResponse.getEmail()+"@gmail.com");
                            emailidText.setText(mResponse.getEmail());
                            contactnumberText.setText(mResponse.getContactNo());
                            addressText.setText(mResponse.getAddress()+mResponse.getCompanyName()+mResponse.getLandmark()
                                    +mResponse.getVillage()+mResponse.getMandal()+mResponse.getDistrict()+mResponse.getState());

                            /* Navigation's and using the views */
                            setViews();
                        }

                    }
                });

    }

}
