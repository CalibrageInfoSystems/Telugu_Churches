package in.calibrage.teluguchurches.views.Activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import in.calibrage.teluguchurches.R;
import in.calibrage.teluguchurches.services.APIConstants;
import in.calibrage.teluguchurches.services.ChurchService;
import in.calibrage.teluguchurches.services.ServiceFactory;
import in.calibrage.teluguchurches.util.BaseActivity;
import in.calibrage.teluguchurches.util.CommonUtil;
import in.calibrage.teluguchurches.util.Constants;
import in.calibrage.teluguchurches.util.localData.SharedPrefsData;
import in.calibrage.teluguchurches.views.custom_controls.CommonTextView;
import in.calibrage.teluguchurches.views.model.EditProfileResponseModel;
import in.calibrage.teluguchurches.views.model.GetUserInfoResponseModel;
import in.calibrage.teluguchurches.views.model.UpdateUserInfoRequestModel;
import io.fabric.sdk.android.Fabric;
import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseSequence;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView;
import uk.co.deanwild.materialshowcaseview.ShowcaseConfig;

/**
 * Created by Admin on 5/2/2018.
 */

public class ProfileActivity extends BaseActivity implements View.OnClickListener, CommonUtil.OnCartChangedListener {

    private EditText firstnameEdt, middle_nameEdt, lastnameEdt, usernameEdt, emailEdt, mobileNumberEdt, dobEdt,fullnameEdt;
    private Button saveBtn;
    private ImageView toolbar_image,profileImage, edit_button;
    CommonTextView genderlayout, editimage;
    private TextInputLayout firstname_lay, lastname_lay, name_lay, email_lay, mobile_lay, dob_lay, middlename_lay,fullname_lay;
    private LinearLayout changePhotoLay;
    private RadioGroup genderGroup;
    private RadioButton maleRB, femaleRB;
    private Context mContext;
    private Toolbar toolbar;
    public static final int PICK_IMAGE = 2;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private Subscription mSubscription;
    private Uri uri;
    private DatePickerDialog.OnDateSetListener onDateSetListener;
    private String imageString, extension, path,emailStr = "", fileName, fileLocation,dateandtime,authorizationToken;
    private static final String SHOWCASE_ID = "sequence home";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getApplicationContext();
        // added crash report's to mail
        Fabric.with(getApplicationContext(), new Crashlytics());
        //assining layout
        setContentView(R.layout.activity_profile);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.edit_profile_str);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
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
                startActivity(new Intent(ProfileActivity.this, HomeActivity.class));
            }
        });
        if (isOnline()) {
            getUserInfo();
        } else {
            showToastLong(getString(R.string.no_internet));
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                // Should we show an explanation?
                if (shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {

                    // Explain to the user why we need to read the contacts
                }
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_IMAGE_CAPTURE);

            }
            //getUserInfo();
        }
        // to start the Progress Dialog
        showProgressDialog();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        // to get current date and time
        dateAndtime();

        /* intializing and assigning ID's */
        initViews();

        /* Navigation's and using the views */
        setViews();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ProfileActivity.this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    /* intializing and assigning ID's */
    private void initViews() {
        firstnameEdt = findViewById(R.id.firstnameEdt);
        fullnameEdt = findViewById(R.id.fullnameEdt);
        dobEdt = findViewById(R.id.dobEdt);
        middle_nameEdt = findViewById(R.id.middle_nameEdt);
        lastnameEdt =findViewById(R.id.lastnameEdt);
        usernameEdt =findViewById(R.id.usernameEdt);
        emailEdt = findViewById(R.id.emailEdt);
        mobileNumberEdt =findViewById(R.id.mobileNumberEdt);
        saveBtn = findViewById(R.id.saveBtn);
        fullname_lay =  findViewById(R.id.fullname_lay);
        firstname_lay = findViewById(R.id.firstname_lay);
        lastname_lay =  findViewById(R.id.lastname_lay);
        name_lay = findViewById(R.id.name_lay);
        email_lay = findViewById(R.id.email_lay);
        mobile_lay =  findViewById(R.id.mobile_lay);
        changePhotoLay =  findViewById(R.id.changePhotoLay);
        profileImage =  findViewById(R.id.profileImage);
        genderGroup =  findViewById(R.id.genderGroup);
        maleRB = findViewById(R.id.maleRB);
        femaleRB = findViewById(R.id.femaleRB);
        middlename_lay = findViewById(R.id.middle_name_lay);
        genderlayout = findViewById(R.id.genderlayout);
        edit_button = findViewById(R.id.edit_button);
        editimage = findViewById(R.id.editimage);
        dob_lay = findViewById(R.id.inp_dob);
        dob_lay.setVisibility(View.GONE);
        genderlayout.setVisibility(View.GONE);
        genderGroup.setVisibility(View.GONE);

    }

    /* Navigation's and using the views */
    private void setViews() {
        firstname_lay.setVisibility(View.GONE);
        middlename_lay.setVisibility(View.GONE);
        lastname_lay.setVisibility(View.GONE);
        fullname_lay.setVisibility(View.VISIBLE);
        firstnameEdt.setEnabled(false);
        firstname_lay.setHintTextAppearance(R.style.user_profile_firstname);
        firstnameEdt.setTextAppearance(this, R.style.user_profile_firstname_edt);

        fullnameEdt.setEnabled(false);
        fullname_lay.setHintTextAppearance(R.style.user_profile_firstname);
        fullnameEdt.setTextAppearance(this, R.style.user_profile_firstname_edt);


        middle_nameEdt.setEnabled(false);
        middlename_lay.setHintTextAppearance(R.style.user_profile_firstname);
        middle_nameEdt.setTextAppearance(this, R.style.user_profile_firstname_edt);
        lastnameEdt.setEnabled(false);
        lastname_lay.setHintTextAppearance(R.style.user_profile_firstname);
        lastnameEdt.setTextAppearance(this, R.style.user_profile_firstname_edt);
        usernameEdt.setEnabled(false);
        name_lay.setHintTextAppearance(R.style.user_profile_firstname);
        usernameEdt.setTextAppearance(this, R.style.user_profile_firstname_edt);
        emailEdt.setEnabled(false);
        email_lay.setHintTextAppearance(R.style.user_profile_firstname);
        emailEdt.setTextAppearance(this, R.style.user_profile_firstname_edt);
        mobileNumberEdt.setEnabled(false);
        mobile_lay.setHintTextAppearance(R.style.user_profile_firstname);
        mobileNumberEdt.setTextAppearance(this, R.style.user_profile_firstname_edt);
        dobEdt.setEnabled(false);
        dob_lay.setHintTextAppearance(R.style.user_profile_firstname);
        dobEdt.setTextAppearance(this, R.style.user_profile_firstname_edt);
        genderlayout.setTextAppearance(this, R.style.user_profile_firstname_edt);
        changePhotoLay.setEnabled(false);
        maleRB.setEnabled(false);
        femaleRB.setEnabled(false);
        saveBtn.setVisibility(View.GONE);
        /**
         * @param OnClickListner
         */
        saveBtn.setOnClickListener(this);
        /**
         * @param OnClickListner
         */
        edit_button.setOnClickListener(this);
        /**
         * @param OnClickListner
         */
        changePhotoLay.setOnClickListener(this);
        /**
         * @param OnClickListner
         */
        dobEdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(ProfileActivity.this, R.style.datepicker
                        , onDateSetListener, year, month, day);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                datePickerDialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());
                datePickerDialog.show();
            }
        });
        onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                month = month + 1;
                String date = year + "-" + month + "-" + day;
                dobEdt.setText(date);

            }
        };

        usernameEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    name_lay.setErrorEnabled(false);
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
                if (charSequence.toString().length() > 0) {
                    email_lay.setErrorEnabled(false);
                }


            }

            @Override
            public void afterTextChanged(Editable editable) {
                String x = " ";
                x = editable.toString();
                if (x.startsWith(".")) {
                    emailEdt.setText("");
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

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.saveBtn:

                /**
                 * Validations of profile
                 *
                 * @return
                 */
                if (profileValidations()) {

                    // to start the Progress Dialog
                    showProgressDialog();

                    /**
                     * update user information API
                     */
                    updateUserInfo();
                }

                break;

            case R.id.changePhotoLay:

                /**
                 * Dialog for select camera or gallery options
                 */
                openDialog();
                break;
            case R.id.edit_button:
                fullname_lay.setVisibility(View.GONE);
                firstname_lay.setVisibility(View.VISIBLE);
                lastname_lay.setVisibility(View.VISIBLE);
                firstnameEdt.setEnabled(true);
                firstnameEdt.setFocusable(true);
                middlename_lay.setVisibility(View.VISIBLE);
                editimage.setVisibility(View.VISIBLE);
                middle_nameEdt.setEnabled(true);
                lastnameEdt.setEnabled(true);
                emailEdt.setEnabled(true);
                dob_lay.setVisibility(View.VISIBLE);
                genderlayout.setVisibility(View.VISIBLE);
                genderGroup.setVisibility(View.VISIBLE);
                dobEdt.setEnabled(true);
                maleRB.setEnabled(true);
                femaleRB.setEnabled(true);
                changePhotoLay.setEnabled(true);
                saveBtn.setVisibility(View.VISIBLE);
                break;
        }

    }

    /**
     * Dialog for select camera or gallery options
     */
    public void openDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Select From");
        alertDialogBuilder.setPositiveButton("camera",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        dispatchTakePictureIntent();
                    }
                });

        alertDialogBuilder.setNegativeButton("gallery", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                openGallery();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    /**
     * Take image camera or gallery
     */
    private void dispatchTakePictureIntent() {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(this.getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    /**
     * open gallery
     */
    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(galleryIntent, PICK_IMAGE);


    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_IMAGE && null != data) {
            // Get the url from data
            Uri selectedImageUri = data.getData();
            if (selectedImageUri != null) {
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(selectedImageUri, filePathColumn, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);
                cursor.close();
                // Set the image in ImageView
                profileImage.setImageURI(selectedImageUri);
                if (picturePath != null) {
                    extension = picturePath.substring(picturePath.lastIndexOf("."));
                }
                final InputStream imageStream;
                try {
                    imageStream = getContentResolver().openInputStream(selectedImageUri);
                    final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                    imageString = encodeImage(selectedImage);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        } else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {

            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");

            Uri tempUri = getImageUri(getApplicationContext(), imageBitmap);
            // CALL THIS METHOD TO GET THE ACTUAL PATH
            File finalFile = new File(getRealPathFromURI(tempUri));
            extension = finalFile.getAbsolutePath().substring(finalFile.getAbsolutePath().lastIndexOf("."));
//            //encode image to base64 string
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            imageBitmap = (Bitmap) extras.get("data");
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] imageBytes = baos.toByteArray();
            imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);
            profileImage.setImageBitmap(imageBitmap);


        }
    }

    /**
     * @param inContext
     * @param inImage
     * @return
     */
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        byte[] imageBytes = bytes.toByteArray();
        imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    /**
     * getting image real path
     *
     * @param uri
     * @return
     */
    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    /**
     * @param bm
     * @return
     */
    private String encodeImage(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        String encImage = Base64.encodeToString(b, Base64.DEFAULT);

        return encImage;
    }


    private static final int MY_CAMERA_REQUEST_CODE = 100;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, getString(R.string.toast_camera), Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, R.string.toast_camera_denied, Toast.LENGTH_LONG).show();
            }
        }
    }

    /**
     * Validations of profile
     *
     * @return
     */
    private boolean profileValidations() {
        if (firstnameEdt.getText().toString().equalsIgnoreCase("")) {
            firstname_lay.setError(getString(R.string.err_please_enter_first_name));
            firstname_lay.requestFocus();
            firstname_lay.setErrorEnabled(true);
            return false;
        } else if (lastnameEdt.getText().toString().equalsIgnoreCase("")) {
            lastname_lay.setError(getString(R.string.err_please_enter_last_name));
            lastname_lay.requestFocus();
            lastname_lay.setErrorEnabled(true);
            return false;
        } else if (usernameEdt.getText().toString().equalsIgnoreCase("")) {
            name_lay.setError(getString(R.string.err_please_enter_username));
            name_lay.requestFocus();
            name_lay.setErrorEnabled(true);
            return false;
        } else if (emailEdt.getText().toString().equalsIgnoreCase("")) {
            email_lay.setError(getString(R.string.err_please_enter_email));
            email_lay.requestFocus();
            email_lay.setErrorEnabled(true);
            return false;
        } else if (!isValidEmail(emailEdt.getText().toString())) {
            email_lay.setError(getString(R.string.err_please_enter_valid_email));
            email_lay.requestFocus();
            email_lay.setErrorEnabled(true);
            return false;
        } else if (mobileNumberEdt.getText().toString().equalsIgnoreCase("")) {
            mobile_lay.setError(getString(R.string.err_please_enter_mobile_number));
            mobile_lay.requestFocus();
            mobile_lay.setErrorEnabled(true);
            return false;
        } else if (!isValidPhone() || mobileNumberEdt.getText().toString().startsWith("0")) {
            mobile_lay.setError(getString(R.string.err_please_enter_valid_mobile_number));
            mobile_lay.requestFocus();
            mobile_lay.setErrorEnabled(true);
            return false;
        }
        return true;
    }

    /**
     * isValid mobile number or not
     *
     * @return
     */
    private boolean isValidPhone() {
        String target = mobileNumberEdt.getText().toString().trim();
        if (target.length() != 10) {
            return false;
        } else {
            return android.util.Patterns.PHONE.matcher(target).matches();
        }
    }

    /**
     * is valid email or not
     *
     * @param email
     * @return
     */

    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    /**
     * get user information data
     */
    private void getUserInfo() {
        ChurchService service = ServiceFactory.createRetrofitService(this, ChurchService.class);
        String url = APIConstants.Get_UserInfo + SharedPrefsData.getInt(getApplicationContext(), Constants.IDProfile, Constants.PREF_NAME);
        mSubscription = service.getUserInfo(url)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<GetUserInfoResponseModel>() {
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

                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onNext(GetUserInfoResponseModel userInfoResponseModel) {
                        // to hide the Progress Dialog
                        hideProgressDialog();
                        if (userInfoResponseModel.getIsSuccess()) {
                            extension = userInfoResponseModel.getListResult().get(0).getFileExtention();
                            fileLocation = userInfoResponseModel.getListResult().get(0).getFileLocation();
                            fileName = userInfoResponseModel.getListResult().get(0).getFileName();
                            fullnameEdt.setText(userInfoResponseModel.getListResult().get(0).getFirstName()+" "
                                    +userInfoResponseModel.getListResult().get(0).getMiddleName()+" "
                                    +userInfoResponseModel.getListResult().get(0).getLastName());
                            firstnameEdt.setText(userInfoResponseModel.getListResult().get(0).getFirstName());
                            middle_nameEdt.setText(userInfoResponseModel.getListResult().get(0).getMiddleName());
                            lastnameEdt.setText(userInfoResponseModel.getListResult().get(0).getLastName());
                            usernameEdt.setText(userInfoResponseModel.getListResult().get(0).getUserName());
                            emailEdt.setText(userInfoResponseModel.getListResult().get(0).getEmail());


                            mobileNumberEdt.setText(userInfoResponseModel.getListResult().get(0).getMobileNumber());
                            Picasso.with(getApplicationContext()).load(userInfoResponseModel.getListResult().get(0).getUserImage()).fit().centerCrop()
                                    .placeholder(R.drawable.profile_pic)
                                    .into(profileImage);
                            if(userInfoResponseModel.getListResult().get(0).getDob()==null||userInfoResponseModel.getListResult().get(0).getDob().isEmpty()){
                                dob_lay.setVisibility(View.GONE);
                            }else {
                                dob_lay.setVisibility(View.VISIBLE);
                                dobEdt.setText(CommonUtil.dateFormat(userInfoResponseModel.getListResult().get(0).getDob()));
                            }

                            if (userInfoResponseModel.getListResult().get(0).getGenderTypeId() != null && !userInfoResponseModel.getListResult().get(0).getGenderTypeId().equals("")) {
                                genderlayout.setVisibility(View.VISIBLE);
                                genderGroup.setVisibility(View.VISIBLE);
                                if (userInfoResponseModel.getListResult().get(0).getGenderTypeId().equals(27)) {
                                    maleRB.setChecked(true);
                                } else {
                                    femaleRB.setChecked(true);
                                }
                            }else {
                                genderlayout.setVisibility(View.GONE);
                                genderGroup.setVisibility(View.GONE);
                            }
                            /**
                             * Showcase view for suggestions
                             */
                            presentShowcaseSequence();
                        }


                    }
                });
    }

    /**
     * update user information API
     */
    private void updateUserInfo() {
        if (authorizationToken != "null" || authorizationToken != null || authorizationToken.isEmpty()) {
            authorizationToken = SharedPrefsData.getInstance(mContext).getStringFromSharedPrefs(Constants.Auth_Token);
        }
        JsonObject object = postUpdateUserInfoObject();
        ChurchService service = ServiceFactory.createRetrofitService(this, ChurchService.class);
        mSubscription = service.EditProfile(object, authorizationToken)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<EditProfileResponseModel>() {
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
                    public void onNext(EditProfileResponseModel registerResponse) {
                        // to hide the Progress Dialog
                        hideProgressDialog();
                        if (registerResponse.getIsSuccess()) {
                            startActivity(new Intent(ProfileActivity.this, HomeActivity.class));

                        } else {
                            Toast.makeText(getApplicationContext(), "" + registerResponse.getEndUserMessage(), Toast.LENGTH_SHORT).show();
                        }
                        CommonUtil.customToast(registerResponse.getEndUserMessage(), ProfileActivity.this);

                    }
                });
    }
    // to get current date and time
    private void dateAndtime() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        String formattedDate = df.format(c.getTime());
        dateandtime = formattedDate;
        SharedPrefsData.getInstance(ProfileActivity.this).updateStringValue(ProfileActivity.this, "datetime", dateandtime);
    }

    /**
     * json object of update user information
     *
     * @return
     */
    private JsonObject postUpdateUserInfoObject() {
        UpdateUserInfoRequestModel mRequest = new UpdateUserInfoRequestModel();
        if (imageString != null) {
            mRequest.setImageString(imageString);
        }
        mRequest.setFileExtention(extension);
        mRequest.setFileName(fileName);
        mRequest.setFileLocation(fileLocation);
        mRequest.setId(SharedPrefsData.getInt(ProfileActivity.this, Constants.ID, Constants.PREF_NAME));
        mRequest.setUserId("" + SharedPrefsData.getString(ProfileActivity.this, Constants.UserId, Constants.PREF_NAME));
        mRequest.setFirstName(firstnameEdt.getText().toString().trim());
        mRequest.setMiddleName(middle_nameEdt.getText().toString().trim());
        mRequest.setLastname(lastnameEdt.getText().toString().trim());
        mRequest.setMobileNumber(mobileNumberEdt.getText().toString());
        mRequest.setUserName(usernameEdt.getText().toString().trim());
        mRequest.setPassword("");
        mRequest.setRoleId(3);
        mRequest.setEmail(emailEdt.getText().toString());
        mRequest.setIsActive(true);
        if(!dobEdt.getText().toString().isEmpty()){
            mRequest.setDob(CommonUtil.dateFormat(dobEdt.getText().toString()));

        }
        if(genderGroup.getCheckedRadioButtonId() != -1){
            mRequest.setGenderTypeId(maleRB.isChecked() ? 27 : 30);
        }

        mRequest.setCreatedByUserId(SharedPrefsData.getInt(ProfileActivity.this, Constants.ID, Constants.PREF_NAME));
        mRequest.setCreatedDate(dateandtime);
        mRequest.setUpdatedByUserId(SharedPrefsData.getInt(ProfileActivity.this, Constants.ID, Constants.PREF_NAME));
        mRequest.setUpdatedDate(dateandtime);
        mRequest.setDescription("PortalAdmin");
        return new Gson().toJsonTree(mRequest).getAsJsonObject();
    }


    @Override
    public void setCartClickListener(String status) {
        if (status.equalsIgnoreCase("ok")) {
            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
        }
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
                        .setTarget(edit_button)
                        .setDismissText("GOT IT")
                        .setContentText("Click Here To Edit Profile")
                        .setContentTextColor(getResources().getColor(R.color.mediumTurquoise))
                        .setDismissTextColor(getResources().getColor(R.color.white))
                        .withCircleShape()
                        .setShapePadding(100)
                        .build()
        );




        sequence.start();

    }
}
