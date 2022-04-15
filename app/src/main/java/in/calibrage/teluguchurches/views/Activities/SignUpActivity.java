package in.calibrage.teluguchurches.views.Activities;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import in.calibrage.teluguchurches.R;
import in.calibrage.teluguchurches.services.ChurchService;
import in.calibrage.teluguchurches.services.ServiceFactory;
import in.calibrage.teluguchurches.util.BaseActivity;
import in.calibrage.teluguchurches.util.CommonUtil;
import in.calibrage.teluguchurches.util.localData.SharedPrefsData;
import in.calibrage.teluguchurches.views.model.UpdateUserInfoRequestModel;
import in.calibrage.teluguchurches.views.model.UserRegisterResponseModel;
import io.fabric.sdk.android.Fabric;
import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SignUpActivity extends BaseActivity implements View.OnClickListener, CommonUtil.OnCartChangedListener {
    private EditText usernameEdt, emailEdt, mobileNumberEdt, passwordEdt, confirmPasswordEdt, firstnameEdt, middle_nameEdt, lastnameEdt, dobEdt;
    private Button registerBtn;
    private TextView loginText;
    private Toolbar toolbar;
    private TextInputLayout name_lay, mobile_lay, email_lay, password_lay, confirm_password_lay, firstname_lay, lastname_lay, dob_lay;
    private RadioButton maleRb, femaleRb;
    private RadioGroup genderGroup;
    private ImageView toolbar_image;
    private Context mContext;
    private Subscription mRegisterSubscription;
    private DatePickerDialog.OnDateSetListener onDateSetListener;
    private String dateandtime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext=getApplicationContext();

        // added crash report's to mail
        Fabric.with(getApplicationContext(), new Crashlytics());

        //assining layout
        setContentView(R.layout.activity_signup);

        toolbar =findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.registration);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
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
                startActivity(new Intent(SignUpActivity.this, HomeActivity.class));
            }
        });

        // to get current date and time
        dateAndtime();
        /* intializing and assigning ID's */
        initViews();

        /* Navigation's and using the views */
        setViews();
    }

    /* intializing and assigning ID's */
    private void initViews() {
        firstnameEdt = findViewById(R.id.firstnameEdt);
        middle_nameEdt =findViewById(R.id.middle_nameEdt);
        lastnameEdt = findViewById(R.id.lastnameEdt);
        usernameEdt =  findViewById(R.id.usernameEdt);
        emailEdt = findViewById(R.id.emailEdt);
        mobileNumberEdt = findViewById(R.id.mobileNumberEdt);
        passwordEdt =  findViewById(R.id.passwordEdt);
        confirmPasswordEdt =  findViewById(R.id.confirmPasswordEdt);
        registerBtn = findViewById(R.id.registerBtn);
        loginText = findViewById(R.id.loginText);
        String register = "<b><font color='#D92B4A'>" + getString(R.string.already_have_an_account_login) + " </font>  <b><font color='#68aed3'>" + getString(R.string.login) + "</font>";
        //String register = getString(R.string.already_have_an_account_login) + "<b>" + getString(R.string.login) + "</b> ";
        loginText.setText(Html.fromHtml(register));
        name_lay =findViewById(R.id.name_lay);
        mobile_lay = findViewById(R.id.mobile_lay);
        email_lay =findViewById(R.id.email_lay);
        password_lay = findViewById(R.id.password_lay);
        confirm_password_lay = findViewById(R.id.confirm_password_lay);
        firstname_lay = findViewById(R.id.firstname_lay);
        lastname_lay = findViewById(R.id.lastname_lay);
        dob_lay = findViewById(R.id.dob_lay);
        maleRb = findViewById(R.id.maleRb);
        femaleRb = findViewById(R.id.femaleRb);
        dobEdt = findViewById(R.id.dobEdt);
        genderGroup = findViewById(R.id.genderGroup);
    }

    /* Navigation's and using the views */
    private void setViews() {
        /**
         * @param OnClickListner
         */
        loginText.setOnClickListener(this);
        /**
         * @param OnClickListner
         */
        registerBtn.setOnClickListener(this);
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
                DatePickerDialog datePickerDialog = new DatePickerDialog(SignUpActivity.this, R.style.datepicker
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
        passwordEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    password_lay.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        confirmPasswordEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    confirm_password_lay.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        dobEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    dob_lay.setErrorEnabled(false);
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
            case R.id.loginText:
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                break;

            case R.id.registerBtn:

                /**
                 * Validations of signUp
                 *
                 * @return
                 */
                if (signUpValidations()) {

                    // to start the Progress Dialog
                    showProgressDialog();

                    /**
                     * Update user info API
                     */
                    updateUserInfo();
                }
                break;

        }
    }

    /**
     * Validations of sign up
     *
     * @return
     */
    private boolean signUpValidations() {
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
        }  else if (passwordEdt.getText().toString().equalsIgnoreCase("")) {
            password_lay.setError(getString(R.string.err_please_enter_password));
            password_lay.requestFocus();
            password_lay.setErrorEnabled(true);
            return false;
        } else if (!isValidPassword(passwordEdt.getText().toString())||passwordEdt.getText().toString().length()<8) {
            password_lay.setError(getString(R.string.password_must_contain_onecharacter));
            password_lay.requestFocusFromTouch();
            password_lay.requestFocus();
            return false;
        } else if (confirmPasswordEdt.getText().toString().equalsIgnoreCase("")) {
            confirm_password_lay.setError(getString(R.string.err_please_enter_confirm_password));
            confirm_password_lay.requestFocus();
            confirm_password_lay.setErrorEnabled(true);
            return false;
        } else if (!passwordEdt.getText().toString().equals(confirmPasswordEdt.getText().toString())) {
            confirm_password_lay.setError(getString(R.string.err_password_do_not_match));
            confirm_password_lay.requestFocus();
            confirm_password_lay.setErrorEnabled(true);
            return false;
        }
        return true;
    }

    /**
     * is valid password or not
     *
     * @param password
     * @return
     */
    public static boolean isValidPassword(final String password) {
        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);
        return matcher.matches();

    }

    /**
     * Valid mobile number or not
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
     * valid email or not
     *
     * @param email
     * @return
     */
    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    /**
     * Update user info API
     */
    private void updateUserInfo() {
        final JsonObject object = postUpdateUserInfoObject();
        ChurchService service = ServiceFactory.createRetrofitService(this, ChurchService.class);
        mRegisterSubscription = service.UpdateUserInfo(object)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<UserRegisterResponseModel>() {
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
                    public void onNext(UserRegisterResponseModel registerResponse) {

                        // to hide the Progress Dialog
                        hideProgressDialog();
                        if (registerResponse.getIsSuccess()) {
                            startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                            CommonUtil.setOnCartChangedListener(SignUpActivity.this);
                            CommonUtil.customToast(registerResponse.getEndUserMessage(), SignUpActivity.this);
                        } else {
                            CommonUtil.customToast(registerResponse.getEndUserMessage(), SignUpActivity.this);
                        }

                    }
                });
    }

    // to get current date and time
    private void dateAndtime() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        String formattedDate = df.format(c.getTime());
        dateandtime = formattedDate;
        SharedPrefsData.getInstance(SignUpActivity.this).updateStringValue(SignUpActivity.this, "datetime", dateandtime);
    }


    /**
     * Json Object of update user info details
     *
     * @return
     */
    private JsonObject postUpdateUserInfoObject() {
        UpdateUserInfoRequestModel mRequest = new UpdateUserInfoRequestModel();
        mRequest.setId(0);
        mRequest.setUserId("");
        mRequest.setFirstName(firstnameEdt.getText().toString().trim());
        mRequest.setMiddleName(middle_nameEdt.getText().toString().trim());
        mRequest.setLastname(lastnameEdt.getText().toString().trim());
        mRequest.setMobileNumber(mobileNumberEdt.getText().toString());
        mRequest.setUserName(usernameEdt.getText().toString().trim());
        mRequest.setPassword(passwordEdt.getText().toString().trim());
        mRequest.setRoleId(3);
        mRequest.setEmail(emailEdt.getText().toString());
        mRequest.setIsActive(true);
        mRequest.setCreatedByUserId(null);
        mRequest.setFileExtention(null);
        mRequest.setFileLocation(null);
        mRequest.setFileName(null);
        mRequest.setCreatedDate(dateandtime);
        mRequest.setUpdatedByUserId(null);
        mRequest.setUpdatedDate(dateandtime);
        return new Gson().toJsonTree(mRequest).getAsJsonObject();
    }


    @Override
    public void setCartClickListener(String status) {
        if (status.equalsIgnoreCase("ok")) {
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
