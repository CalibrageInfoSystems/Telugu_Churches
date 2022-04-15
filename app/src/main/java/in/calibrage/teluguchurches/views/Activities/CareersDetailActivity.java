package in.calibrage.teluguchurches.views.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;

import java.io.IOException;

import in.calibrage.teluguchurches.R;
import in.calibrage.teluguchurches.services.APIConstants;
import in.calibrage.teluguchurches.services.ChurchService;
import in.calibrage.teluguchurches.services.ServiceFactory;
import in.calibrage.teluguchurches.util.BaseActivity;
import in.calibrage.teluguchurches.util.CommonUtil;
import in.calibrage.teluguchurches.util.Constants;
import in.calibrage.teluguchurches.util.localData.SharedPrefsData;
import in.calibrage.teluguchurches.views.model.GetJobResponseModel;
import io.fabric.sdk.android.Fabric;
import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseSequence;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView;
import uk.co.deanwild.materialshowcaseview.ShowcaseConfig;

public class CareersDetailActivity extends BaseActivity {

    public TextView jobTitleText, vacanciesText, qualificationText, jobDescText, contactPersonText, contactNoText, salaryText, lastdateText, adminNameText, churchNameText;
    private ImageView toolbar_image;
    private Button applyBtn;
    private LinearLayout jobTitleText_lay, vacanciesText_lay, qualificationText_lay, jobDescText_lay, contactPersonText_lay,
            contactNoText_lay, salaryText_lay, lastdateText_lay, adminNameText_lay, churchNameText_lay;
    private Context mContext;
    private Toolbar toolbar;
    Intent intent;
    private int careersid, listPosition, jobId;
    String jobTitle, churchName;
    private Subscription mSubscription;
    private static final String SHOWCASE_ID = "sequence category";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getApplicationContext();

        // added crash report's to mail
        Fabric.with(getApplicationContext(), new Crashlytics());

        //assining layout
        setContentView(R.layout.activity_careers_deatails);

        intent = getIntent();
        if (intent != null) {
            /*
             * to check the intent value
             * @param  careersid
             *@param  churchName
             */
            careersid = intent.getIntExtra("id", 0);
            churchName = intent.getStringExtra("churchName");
        }

        /* intializing and assigning ID's */
        initView();

    }
    /* intializing and assigning ID's */
    private void initView() {

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle(getString(R.string.job_details));
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
                Intent intent = new Intent(CareersDetailActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });
        applyBtn =findViewById(R.id.applyBtn);

        /*
         * passing intent value to Apply
         * @param  jobId
         *@param  jobTitle
         */
        applyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CareersDetailActivity.this, ApplyActivity.class)
                        .putExtra("jobId", jobId)
                        .putExtra("jobTitle", jobTitle);


                startActivity(intent);
            }
        });
        jobTitleText_lay =findViewById(R.id.jobTitleText_lay);
        vacanciesText_lay = findViewById(R.id.vacanciesText_lay);
        qualificationText_lay = findViewById(R.id.qualificationText_lay);
        jobDescText_lay = findViewById(R.id.jobDescText_lay);
        contactPersonText_lay = findViewById(R.id.contactPersonText_lay);
        contactNoText_lay =findViewById(R.id.contactNoText_lay);
        salaryText_lay = findViewById(R.id.salaryText_lay);
        lastdateText_lay = findViewById(R.id.lastdateText_lay);
        adminNameText_lay = findViewById(R.id.adminNameText_lay);
        churchNameText_lay =findViewById(R.id.churchNameText_lay);
        jobTitleText = findViewById(R.id.jobTitleText);
        vacanciesText = findViewById(R.id.vacanciesText);
        qualificationText =findViewById(R.id.qualificationText);
        jobDescText = findViewById(R.id.jobDescText);
        contactPersonText = findViewById(R.id.contactPersonText);
        contactNoText =findViewById(R.id.contactNoText);
        salaryText = findViewById(R.id.salaryText);
        lastdateText = findViewById(R.id.lastdateText);
        adminNameText = findViewById(R.id.adminNameText);
        churchNameText =findViewById(R.id.churchNameText);

        // API to get particular career details
        getJobById();

    }
    // API to get particular career details
    private void getJobById() {
        ChurchService service = ServiceFactory.createRetrofitService(mContext, ChurchService.class);
        String url = APIConstants.GetJobById + SharedPrefsData.getInt(mContext, Constants.CareersId, Constants.PREF_NAME);
        mSubscription = service.GetJobById(url)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<GetJobResponseModel>() {
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
                    public void onNext(GetJobResponseModel mResponse) {
                        if (mResponse.getIsSuccess()) {
                            if (mResponse.getResult().getContactNumber().toString().isEmpty()) {
                                contactNoText_lay.setVisibility(View.GONE);
                            } else {
                                contactNoText_lay.setVisibility(View.VISIBLE);
                                contactNoText.setText(mResponse.getResult().getContactNumber());
                            }
                            if (mResponse.getResult().getContactPerson().toString().isEmpty()) {
                                contactPersonText_lay.setVisibility(View.GONE);
                            } else {
                                contactPersonText_lay.setVisibility(View.VISIBLE);
                                contactPersonText.setText(mResponse.getResult().getContactPerson().toString());
                            }
                            if(mResponse.getResult().getChurchName() !=null){
                                churchNameText.setText(mResponse.getResult().getChurchName().toString());
                            }else {
                                churchNameText_lay.setVisibility(View.GONE);

                            }

                            jobTitle = mResponse.getResult().getJobTitle();
                            jobTitleText.setText(mResponse.getResult().getJobTitle().toString());
                            jobId = mResponse.getResult().getId();
                            vacanciesText.setText(mResponse.getResult().getVacencies().toString());
                            qualificationText.setText(mResponse.getResult().getQualification().toString());
                            jobDescText.setText(mResponse.getResult().getJobDesc().toString());
                            contactPersonText.setText(mResponse.getResult().getContactPerson().toString());

                            salaryText.setText(mResponse.getResult().getSalary().toString());
                            lastdateText.setText(CommonUtil.formatDateTimeUi(mResponse.getResult().getLastDateToApply().toString()));
                            adminNameText.setText(mResponse.getResult().getAdminName().toString());

                            /**
                             * Showcase view for suggestions
                             */
                            presentShowcaseSequence();
                        }
                    }

                });
    }


    /**
     * Showcase view for suggestions
     */
    private void presentShowcaseSequence() {

        ShowcaseConfig config = new ShowcaseConfig();
        config.setDelay(200); // half second between each showcase view

        MaterialShowcaseSequence sequence = new MaterialShowcaseSequence(this, SHOWCASE_ID);

        sequence.setOnItemShownListener(new MaterialShowcaseSequence.OnSequenceItemShownListener() {

            @Override
            public void onShow(MaterialShowcaseView itemView, int position) {

            }
        });

        sequence.setConfig(config);
        sequence.addSequenceItem(
                new MaterialShowcaseView.Builder(this)
                        .setTarget(applyBtn)
                        .setDismissText("GOT IT")
                        .setContentText("Click Here Apply For a Job")
                        .setContentTextColor(getResources().getColor(R.color.mediumTurquoise))
                        .setDismissTextColor(getResources().getColor(R.color.white))
                        .withCircleShape()
                        .setShapePadding(100)
                        .build()
        );




        sequence.start();

    }
}
