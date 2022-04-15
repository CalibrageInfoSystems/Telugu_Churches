package in.calibrage.teluguchurches.views.Activities;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.TextInputLayout;
import android.support.v4.util.Pair;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.crashlytics.android.Crashlytics;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import in.calibrage.teluguchurches.R;
import in.calibrage.teluguchurches.interfaces.DeleteImageListiner;
import in.calibrage.teluguchurches.services.ChurchService;
import in.calibrage.teluguchurches.services.ServiceFactory;
import in.calibrage.teluguchurches.util.BaseActivity;
import in.calibrage.teluguchurches.util.CommonUtil;
import in.calibrage.teluguchurches.util.Constants;
import in.calibrage.teluguchurches.util.localData.SharedPrefsData;
import in.calibrage.teluguchurches.views.adapter.ImageDocAdapter;
import in.calibrage.teluguchurches.views.custom_controls.CommonButton;
import in.calibrage.teluguchurches.views.custom_controls.CommonEditText;
import in.calibrage.teluguchurches.views.model.GetAddUpdateApplicantRequest;
import in.calibrage.teluguchurches.views.model.GetAddUpdateApplicantResponseModel;
import io.fabric.sdk.android.Fabric;
import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseSequence;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView;
import uk.co.deanwild.materialshowcaseview.ShowcaseConfig;


public class ApplyActivity extends BaseActivity implements View.OnClickListener, DeleteImageListiner {

    private TextView dialogMessage, docText;
    private Button cancel_btn, ok_btn;
    private TextInputLayout jobtitle_lay, firstname_lay, middle_name_lay, lastname_lay, email_lay, mobile_lay,
            qualification_lay, currentOrganization_lay, currentsalary_lay, expectedSalary_lay;
    private CommonEditText qualificationEdt, mobileNumberEdt, emailEdt, lastnameEdt, middle_nameEdt,
            firstnameEdt, jobtitleEdt, currentOrganizationEdt, currentsalaryEdt, expectedSalaryEdt;
    private CommonButton uploadResumeBtn, apply;
    private ImageView toolbar_image, imageView, deleteIcon;
    private Spinner yearSp, monthSp;
    LinearLayout main_lty, maindoc;
    private RecyclerView imagesRecylerView;
    ImageDocAdapter imageAdapter;
    ArrayAdapter yeararray, montharray;
    private Context mContext;
    private Toolbar toolbar;
    int jobId, pos;
    Intent intent;
    private Subscription mSubscription;
    private ArrayList<Pair<String, String>> filePathArray;
    ArrayList<String> extensions;
    String docString, extension, filePath, authorizationToken, yearTxt, monthTxt, dateandtime, jobTitle;
    private static long MAX_FILE_SIZE = 5;
    private AlertDialog alertDialog;
    Bitmap bitmap;
    private static final String SHOWCASE_ID = "sequence category";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getApplicationContext();

        // added crash report's to mail
        Fabric.with(getApplicationContext(), new Crashlytics());

        //assining layout
        setContentView(R.layout.activity_apply);


        intent = getIntent();
        if (intent != null) {
            /*
             * to check the intent value
             * @param  jobId
             *@param  jobTitle
            */
            jobId = intent.getIntExtra("jobId", 0);
            jobTitle = intent.getStringExtra("jobTitle");

        }

        // to get current date and time
        dateAndtime();

        /* intializing and assigning ID's */
        initView();

        /* Navigation's and using the views */
        setViews();

        /**
         * Showcase view for suggestions
         */
        presentShowcaseSequence();
    }

    /* intializing and assigning ID's */
    private void initView() {

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle(getString(R.string.apply));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        toolbar_image = findViewById(R.id.toolbar_image);

        /**
         * @param OnClickListner
         */
        toolbar_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ApplyActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });
        jobtitle_lay = findViewById(R.id.jobtitle_lay);
        firstname_lay = findViewById(R.id.firstname_lay);
        middle_name_lay =findViewById(R.id.middle_name_lay);
        lastname_lay = findViewById(R.id.lastname_lay);
        email_lay = findViewById(R.id.email_lay);
        mobile_lay = findViewById(R.id.mobile_lay);
        qualification_lay =findViewById(R.id.qualification_lay);
        currentOrganization_lay = findViewById(R.id.currentOrganization_lay);
        currentsalary_lay = findViewById(R.id.currentsalary_lay);
        expectedSalary_lay = findViewById(R.id.expectedSalary_lay);
        jobtitleEdt = findViewById(R.id.jobtitleEdt);
        firstnameEdt =findViewById(R.id.firstnameEdt);
        middle_nameEdt = findViewById(R.id.middle_nameEdt);
        lastnameEdt =findViewById(R.id.lastnameEdt);
        emailEdt = findViewById(R.id.emailEdt);
        mobileNumberEdt = findViewById(R.id.mobileNumberEdt);
        qualificationEdt =findViewById(R.id.qualificationEdt);
        uploadResumeBtn =findViewById(R.id.uploadResumeBtn);
        yearSp = findViewById(R.id.yearSp);
        apply = findViewById(R.id.apply);
        monthSp = findViewById(R.id.monthSp);
        main_lty =findViewById(R.id.main_lty);
        currentOrganizationEdt = findViewById(R.id.currentOrganizationEdt);
        currentsalaryEdt = findViewById(R.id.currentsalaryEdt);
        expectedSalaryEdt =findViewById(R.id.expectedSalaryEdt);
        imagesRecylerView = findViewById(R.id.imagesRecylerView);
        imageView = findViewById(R.id.imageView);
        maindoc = findViewById(R.id.maindoc);
        deleteIcon = findViewById(R.id.deleteIcon);
        docText = findViewById(R.id.docText);


        filePathArray = new ArrayList<>();
        jobtitleEdt.setText(jobTitle);

        /**
         * @param OnClickListner
         */
        deleteIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // selected file to be remove
                showConformationDialog(pos);
            }
        });
    }

    /* Navigation's and using the views */
    private void setViews() {

        /**
         * @param OnClickListner
         */
        uploadResumeBtn.setOnClickListener(this);

        /**
         * @param OnClickListner
         */
        apply.setOnClickListener(this);

        jobtitleEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    jobtitle_lay.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        firstnameEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    firstname_lay.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        middle_nameEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    middle_name_lay.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        lastnameEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    lastname_lay.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        emailEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!isValidEmail(charSequence.toString())) {
                    email_lay.setError(getString(R.string.err_please_enter_valid_email));
                    email_lay.requestFocus();
                    email_lay.setErrorEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String x = " ";
                x = editable.toString();
                if (x.startsWith(".")) {
                    emailEdt.setText("");
                } else if (x.isEmpty()) {
                    email_lay.setErrorEnabled(false);
                } else if (isValidEmail(x)) {
                    email_lay.setErrorEnabled(false);
                }
            }
        });

        mobileNumberEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    mobile_lay.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        qualificationEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    qualification_lay.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        yearSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                yearTxt = yearSp.getSelectedItem().toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        monthSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                monthTxt = monthSp.getSelectedItem().toString();


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        String[] year = getResources().getStringArray(R.array.year);
        yeararray = new ArrayAdapter(this, android.R.layout.simple_list_item_1, year);
        yearSp.setAdapter(yeararray);

        String[] month = getResources().getStringArray(R.array.month);
        montharray = new ArrayAdapter(this, android.R.layout.simple_list_item_1, month);
        monthSp.setAdapter(montharray);

    }

    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }


      //validations for all fields
    private boolean applyValidation() {
        boolean status = true;
        if (jobtitleEdt.getText().toString().equalsIgnoreCase("")) {
            status = false;
            jobtitle_lay.setError(getString(R.string.err_please_enter_job_title));
            jobtitleEdt.requestFocusFromTouch();


        } else if (firstnameEdt.getText().toString().equalsIgnoreCase("")) {
            status = false;
            firstname_lay.setError(getString(R.string.err_please_enter_first_name));
            firstname_lay.requestFocusFromTouch();
            firstname_lay.setErrorEnabled(true);

        } else if (lastnameEdt.getText().toString().equalsIgnoreCase("")) {
            status = false;
            lastname_lay.setError(getString(R.string.err_please_enter_last_name));
            lastname_lay.setErrorEnabled(true);
            lastname_lay.requestFocusFromTouch();

        } else if (emailEdt.getText().toString().equalsIgnoreCase("")) {
            status = false;
            email_lay.requestFocusFromTouch();
            email_lay.setErrorEnabled(true);
            email_lay.setError(getString(R.string.err_please_enter_email));

        } else if (!isValidEmail(emailEdt.getText().toString())) {
            status = false;
            email_lay.setError(getString(R.string.err_please_enter_valid_email));
            email_lay.requestFocusFromTouch();
            email_lay.setErrorEnabled(true);

        } else if (mobileNumberEdt.getText().toString().equalsIgnoreCase("")) {
            status = false;
            mobile_lay.setError(getString(R.string.err_please_enter_mobile_number));
            mobile_lay.setErrorEnabled(true);
            mobile_lay.requestFocusFromTouch();
        } else if (!isValidPhone() || mobileNumberEdt.getText().toString().startsWith("0")) {
            status = false;
            mobile_lay.setError(getString(R.string.err_please_enter_valid_mobile_number));
            mobile_lay.requestFocusFromTouch();
            mobile_lay.setErrorEnabled(true);

        } else if (qualificationEdt.getText().toString().equalsIgnoreCase("")) {
            status = false;
            qualification_lay.requestFocusFromTouch();
            qualification_lay.setErrorEnabled(true);
            qualification_lay.setError(getString(R.string.err_please_enter_qualification));
        } else if (isEmptySpinner(yearSp)) {
            status = false;
            InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(yearSp.getWindowToken(), 0);
            showToast(getResources().getString(R.string.err_enter_year));
            yearSp.requestFocusFromTouch();

        } else if (isEmptySpinner(monthSp)) {
            status = false;
            InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(monthSp.getWindowToken(), 0);
            showToast(getResources().getString(R.string.err_enter_month));
            monthSp.requestFocusFromTouch();

        } else if (filePath == null) {
            status = false;
            Toast.makeText(mContext, getString(R.string.err_please_upload_resume), Toast.LENGTH_SHORT).show();
        }
        return status;

    }

    // to validate phone number
    private boolean isValidPhone() {
        String target = mobileNumberEdt.getText().toString().trim();
        if (target.length() != 10) {
            return false;
        } else {
            return android.util.Patterns.PHONE.matcher(target).matches();
        }
    }

    // to validate EmptySpinner
    public static boolean isEmptySpinner(final Spinner inputSpinner) {
        if (null == inputSpinner) return true;
        if (inputSpinner.getSelectedItemPosition() == -1 || inputSpinner.getSelectedItemPosition() == 0) {
            return true;
        }
        return false;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.uploadResumeBtn:


                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        // Should we show an explanation?
                        if (shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {

                            // Explain to the user why we need to read the contacts
                        }
                        requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                1);
                    }

                    // PopUpDailog to Select Resume
                    startDialog();
                }


                break;
            case R.id.apply:

                //validations for all fields
                if (applyValidation()) {

                    // API to Apply for job
                    getAddUpdateApplicant();
                    Intent intent = new Intent(ApplyActivity.this, CareersActivity.class);
                    startActivity(intent);
                    finish();
                }
                break;
        }
    }

    private void startDialog() {
        LayoutInflater layoutInflater = LayoutInflater.from(ApplyActivity.this);
        View dialogRootView = layoutInflater.inflate(R.layout.dialog_addresume, null);
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ApplyActivity.this);
        cancel_btn =dialogRootView.findViewById(R.id.cancel_btn);
        dialogMessage = dialogRootView.findViewById(R.id.dialogMessage);

        /**
         * @param OnClickListner
         */
        dialogMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), FileChooser.class);
                extensions = new ArrayList<String>();
                extensions.add(".pdf");
                extensions.add(".doc");
                extensions.add(".docx");
                extensions.add(".txt");
                extensions.add(".rtf");
                intent.putStringArrayListExtra("filterFileExtension", extensions);
                startActivityForResult(intent, 1);
                alertDialog.dismiss();
            }
        });

        alertDialogBuilder.setView(dialogRootView);


        /**
         * @param OnClickListner
         */
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }

      //  To select file
    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                filePath = data.getStringExtra("fileSelected");
                File file = new File(filePath);

                long fileSizeInBytes = file.length();
                long fileSizeInKB = fileSizeInBytes / 1024;
                long fileSizeInMB = fileSizeInKB / 1024;

                // selected file is converting into base64 conversion
                convertFileToByteArray(filePath);

                if (fileSizeInMB < MAX_FILE_SIZE) {

                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.fromFile(file));
                        imageView.setImageBitmap(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "file_size_is_must_be_less_than_5MB", Toast.LENGTH_SHORT).show();
                }

                String filename = filePath.substring(filePath.lastIndexOf("/") + 1);
                docText.setText(filename);
            }
            Glide.with(ApplyActivity.this).load("")
                    .fitCenter()
                    .error(R.drawable.doc_sbi)
                    .into(imageView);
            maindoc.setVisibility(View.VISIBLE);
            deleteIcon.setVisibility(View.VISIBLE);


        }
    }
    // selected file is converting into base64 conversion
    private String convertFileToByteArray(String filePath) {
        String val = null;

        byte[] byteArray = null;
        try {
            File f = new File(filePath);
            if (f.exists()) {
                extension = f.getAbsolutePath().substring(f.getAbsolutePath().lastIndexOf("."));

                long fileSizeInBytes = f.length();
                long fileSizeInKB = fileSizeInBytes / 1024;
                long fileSizeInMB = fileSizeInKB / 1024;

                if (fileSizeInMB < 5) {
                    InputStream inputStream = new FileInputStream(f);
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    byte[] b = new byte[1024 * 10000];
                    int bytesRead = 0;

                    while ((bytesRead = inputStream.read(b)) != -1) {
                        bos.write(b, 0, bytesRead);
                    }

                    byteArray = bos.toByteArray();
                    val = Base64.encodeToString(byteArray, Base64.NO_WRAP);
                    docString = val.toString();

                } else {
                    Toast.makeText(mContext, "file_size_is_must_be_less_than_5MB", Toast.LENGTH_SHORT).show();
                }


            }

            return val;

        } catch (IOException e) {
            e.printStackTrace();
            return val;
        }


    }


    @Override
    public void onAdapterClickListiner(int pos, boolean isPopUp) {

    }

    // selected file to be remove
    private void showConformationDialog(final int pos) {
        android.app.AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new android.app.AlertDialog.Builder(ApplyActivity.this, android.R.style.Theme_DeviceDefault_Dialog_Alert);
        } else {
            builder = new android.app.AlertDialog.Builder(ApplyActivity.this);
        }


        builder.setTitle("delete_entry")
                .setMessage("are_you_sure_you_want_to_delete_this_entry")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        imageView.setImageBitmap(null);
                        docText.setText("");
                        filePath = null;
                        deleteIcon.setVisibility(View.GONE);
                        maindoc.setVisibility(View.GONE);

                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    // API to Apply for job
    private void getAddUpdateApplicant() {
        if (authorizationToken != "null" || authorizationToken != null || authorizationToken.isEmpty()) {
            authorizationToken = SharedPrefsData.getInstance(mContext).getStringFromSharedPrefs(Constants.Auth_Token);
        }
        JsonObject object = ApplyApplicationObject();
        ChurchService service = ServiceFactory.createRetrofitService(getApplicationContext(), ChurchService.class);
        mSubscription = service.getAddUpdateApplicant(object, authorizationToken)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<GetAddUpdateApplicantResponseModel>() {
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
                    public void onNext(final GetAddUpdateApplicantResponseModel mResponseModel) {
                        CommonUtil.customToast(mResponseModel.getEndUserMessage(), ApplyActivity.this);

                        // to hide the Progress Dialog
                        hideProgressDialog();


                    }
                });
    }

    // to get current date and time
    private void dateAndtime() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        String formattedDate = df.format(c.getTime());
        dateandtime = formattedDate;
        SharedPrefsData.getInstance(ApplyActivity.this).updateStringValue(ApplyActivity.this, "datetime", dateandtime);
    }

    /**
     * Json Object of ApplyApplicationObject
     *
     * @return
     */
    private JsonObject ApplyApplicationObject() {
        GetAddUpdateApplicantRequest mRequest = new GetAddUpdateApplicantRequest();
        mRequest.setId(0);
        mRequest.setJobId(jobId);
        mRequest.setFileName(null);
        mRequest.setFileLocation(null);
        mRequest.setIsActive(true);
        mRequest.setCreatedByUserId(SharedPrefsData.getInt(getApplicationContext(), Constants.ID, Constants.PREF_NAME));
        mRequest.setCreatedDate(dateandtime);
        mRequest.setUpdatedByUserId(SharedPrefsData.getInt(getApplicationContext(), Constants.ID, Constants.PREF_NAME));
        mRequest.setUpdatedDate(dateandtime);
        mRequest.setApplyingFor(jobtitleEdt.getText().toString());
        mRequest.setFirstName(firstnameEdt.getText().toString().trim());
        mRequest.setMiddleName(middle_nameEdt.getText().toString().trim());
        mRequest.setLastName(lastnameEdt.getText().toString().trim());
        mRequest.setEmail(emailEdt.getText().toString());
        mRequest.setMobileNumber(mobileNumberEdt.getText().toString());
        mRequest.setQualification(qualificationEdt.getText().toString());
        mRequest.setYearsofExp(yearTxt + monthTxt);
        mRequest.setCurrentOrganization(currentOrganizationEdt.getText().toString());
        mRequest.setCurrentSalary(ParseDouble(currentsalaryEdt.getText().toString()));
        mRequest.setExpectedSalary(ParseDouble(expectedSalaryEdt.getText().toString()));
        mRequest.setFileExtention(extension.toString());
        mRequest.setDocString(docString);
        return new Gson().toJsonTree(mRequest).getAsJsonObject();
    }

    double ParseDouble(String strNumber) {
        if (strNumber != null && strNumber.length() > 0) {
            try {
                return Double.parseDouble(strNumber);
            } catch (Exception e) {
                return -1;   // or some value to mark this field is wrong. or make a function validates field first ...
            }
        } else return 0;
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
                        .setTarget(uploadResumeBtn)
                        .setDismissText("GOT IT")
                        .setContentText("Click Here To Upload Resume")
                        .setContentTextColor(getResources().getColor(R.color.mediumTurquoise))
                        .setDismissTextColor(getResources().getColor(R.color.white))
                        .withCircleShape()
                        .setShapePadding(100)
                        .build()
        );


        sequence.start();

    }
}
