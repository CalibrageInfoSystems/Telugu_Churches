package in.calibrage.teluguchurches.views.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import in.calibrage.teluguchurches.R;
import in.calibrage.teluguchurches.services.ChurchService;
import in.calibrage.teluguchurches.services.ServiceFactory;
import in.calibrage.teluguchurches.util.BaseActivity;
import in.calibrage.teluguchurches.util.CommonUtil;
import in.calibrage.teluguchurches.util.Constants;
import in.calibrage.teluguchurches.util.MyCounter;
import in.calibrage.teluguchurches.util.localData.SharedPrefsData;
import in.calibrage.teluguchurches.views.model.ChangePasswordRequestModel;
import in.calibrage.teluguchurches.views.model.ChangePasswordResponseModel;
import in.calibrage.teluguchurches.views.model.GetLoginPageResponseModel;
import in.calibrage.teluguchurches.views.model.LoginPageRequestModel;
import io.fabric.sdk.android.Fabric;
import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class LoginActivity extends BaseActivity implements View.OnClickListener, CommonUtil.OnCartChangedListener {
    private EditText userNameEdt, passwordEdt, numberEdt, oldPasswordEdt, newPasswordEdt, confirmPasswordEdt;
    private TextView registerText, dialogMessage, forgotPasswordText, changePasswordText;
    private TextInputLayout username, password_edit_layout, mobile_number_lay, oldPasswordEdtLay, newPasswordInput, confirmPasswordInput;
    private Button ok_btn, cancel_btn, loginBtn, confirmBtn;
    private Context mContext;
    private Toolbar toolbar;
    ImageView toolbar_image;
    int value;
    private Subscription mSubscription;
    private ChangePasswordResponseModel mForgotResponse;
    private AlertDialog alertDialog, alert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getApplicationContext();

        // added crash report's to mail
        Fabric.with(getApplicationContext(), new Crashlytics());

        //assining layout
        setContentView(R.layout.activity_login);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.login));
        toolbar.setNavigationIcon(R.drawable.ic_home_tool);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
        toolbar_image = findViewById(R.id.toolbar_image);
        toolbar_image.setVisibility(View.GONE);

        /* intializing and assigning ID's */
        initViews();
        /* Navigation's and using the views */
        setViews();

    }

    /* intializing and assigning ID's */
    private void initViews() {
        loginBtn =  findViewById(R.id.loginBtn);
        userNameEdt =  findViewById(R.id.emailEdt);
        passwordEdt = findViewById(R.id.passwordEdt);
        registerText = findViewById(R.id.registerText);
        String register = "<b><font color='#D92B4A'>" + getString(R.string.don_t_have_an_account_register) + " </font>  <b><font color='#68aed3'>" + getString(R.string.register) + "</font>";
        // String register = getString(R.string.don_t_have_an_account_register) + "<b>" + getString(R.string.register) + "</b> ";
        registerText.setText(Html.fromHtml(register));
        username =  findViewById(R.id.username);
        password_edit_layout =  findViewById(R.id.password_edit_layout);
        forgotPasswordText = findViewById(R.id.forgotPasswordText);
        changePasswordText = findViewById(R.id.changePasswordText);

    }

    /* Navigation's and using the views */
    private void setViews() {
        /**
         * @param OnClickListner
         */
        loginBtn.setOnClickListener(this);
        /**
         * @param OnClickListner
         */
        registerText.setOnClickListener(this);
        /**
         * @param OnClickListner
         */
        forgotPasswordText.setOnClickListener(this);
        /**
         * @param OnClickListner
         */
        changePasswordText.setOnClickListener(this);
        userNameEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    username.setErrorEnabled(false);
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
                    password_edit_layout.setErrorEnabled(false);
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
            case R.id.loginBtn:

                /**
                 * Validations for user name and password
                 *
                 * @return
                 */
                if (validations()) {
                    // to start the Progress Dialog
                    showProgressDialog();

                 //    getLogin();
                    getLoginPage();
                }
                break;

            case R.id.registerText:
                Intent intent = new Intent(this, SignUpActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                break;

            case R.id.forgotPasswordText:

                /**
                 * custom forgotPasswordDialog for forgot password
                 */
                forgotPasswordDialog();
                break;

            case R.id.changePasswordText:
                if (SharedPrefsData.getString(getApplicationContext(), Constants.UserId, Constants.PREF_NAME) != null && !SharedPrefsData.getString(getApplicationContext(), Constants.UserId, Constants.PREF_NAME).equalsIgnoreCase("")) {


                    /**
                     * custom dialog for change password
                     */
                    changePasswordDialog();
                } else {
                    Toast.makeText(getApplicationContext(), R.string.please_login, Toast.LENGTH_SHORT).show();
                    Intent loginIntent = new Intent(this, LoginActivity.class);
                    loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(loginIntent);
                }
                break;
        }

    }

    /**
     * Validations for user name and password
     *
     * @return
     */
    private boolean validations() {
        if (TextUtils.isEmpty(userNameEdt.getText().toString())) {
            username.setError(getString(R.string.err_please_enter_username));
            username.requestFocus();
            username.setErrorEnabled(true);
            return false;
        } else if (TextUtils.isEmpty(passwordEdt.getText().toString().trim())) {
            password_edit_layout.setError(getString(R.string.err_please_enter_password));
            password_edit_layout.setErrorEnabled(true);
            password_edit_layout.requestFocusFromTouch();
            password_edit_layout.requestFocus();
            return false;
        }
        return true;
    }

    /**
     * Valid password or not
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
     * custom forgotPasswordDialog for forgot password
     */
    public void forgotPasswordDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_forgot_password, null);
        dialogBuilder.setView(dialogView);
        Button button =  dialogView.findViewById(R.id.ok_btn);
        numberEdt =  dialogView.findViewById(R.id.numberEdt);
        mobile_number_lay =  dialogView.findViewById(R.id.mobile_number_lay);
        alertDialog = dialogBuilder.create();
        /**
         * @param OnClickListner
         */
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isValidEmail(numberEdt.getText().toString())) {
                    mobile_number_lay.setError(getString(R.string.err_please_enter_valid_email));
                    mobile_number_lay.requestFocus();
                    mobile_number_lay.setErrorEnabled(true);
                } else {

                    //API to get forgot Password
                    forgotPassWord();
                    Toast.makeText(LoginActivity.this, "valid", Toast.LENGTH_SHORT).show();
                }


            }
        });
        numberEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    mobile_number_lay.setErrorEnabled(false);
                }


            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        alertDialog.show();
    }

    /**
     * email validations
     *
     * @return
     */
    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }





    /**
     * custom dialog for change password
     */
    public void changePasswordDialog() {
        LayoutInflater layoutInflater = LayoutInflater.from(LoginActivity.this);
        View dialogRootView = layoutInflater.inflate(R.layout.dialog_change_password, null);
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(LoginActivity.this);

        oldPasswordEdt = dialogRootView.findViewById(R.id.oldPasswordEdt);
        newPasswordEdt = dialogRootView.findViewById(R.id.newPasswordEdt);
        confirmPasswordEdt = dialogRootView.findViewById(R.id.confirmPasswordEdt);
        confirmBtn = dialogRootView.findViewById(R.id.confirmBtn);
        oldPasswordEdtLay = dialogRootView.findViewById(R.id.oldPasswordEdtLay);
        newPasswordInput =  dialogRootView.findViewById(R.id.newPasswordInput);
        confirmPasswordInput =dialogRootView.findViewById(R.id.confirmPasswordInput);
        oldPasswordEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    oldPasswordEdtLay.setErrorEnabled(false);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        newPasswordEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    newPasswordInput.setErrorEnabled(false);
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
                    confirmPasswordInput.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        alertDialogBuilder.setView(dialogRootView);

        /**
         * @param OnClickListner
         */
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /**
                 * validations for change password
                 *
                 * @return
                 */
                if (validatePassword()) {
                    /**
                     * Change password API
                     */
                    changePassword();
                }


            }
        });

        alert = alertDialogBuilder.create();
        alert.show();
    }

    /**
     * validations for change password
     *
     * @return
     */
    private boolean validatePassword() {
        if (oldPasswordEdt.getText().toString().equalsIgnoreCase("")) {
            oldPasswordEdtLay.setError(getString(R.string.err_please_enter_old_password));
            oldPasswordEdtLay.setErrorEnabled(true);
            oldPasswordEdtLay.requestFocusFromTouch();
            oldPasswordEdtLay.requestFocus();
            return false;
        } else if (newPasswordEdt.getText().toString().equalsIgnoreCase("")) {
            newPasswordInput.setError(getString(R.string.err_please_enter_new_password));
            newPasswordInput.setErrorEnabled(true);
            newPasswordInput.requestFocusFromTouch();
            newPasswordInput.requestFocus();
            return false;
        } else if (!isValidPassword(newPasswordEdt.getText().toString())) {
            newPasswordInput.setError(getString(R.string.password_must_contain_onecharacter));
            newPasswordInput.requestFocusFromTouch();
            newPasswordInput.requestFocus();
            return false;
        } else if (oldPasswordEdt.getText().toString().equalsIgnoreCase(newPasswordEdt.getText().toString())) {
            newPasswordInput.setError(getString(R.string.err_please_enter_new_password));
            newPasswordInput.setErrorEnabled(true);
            newPasswordInput.requestFocusFromTouch();
            newPasswordInput.requestFocus();
            return false;
        } else if (confirmPasswordEdt.getText().toString().equalsIgnoreCase("")) {
            confirmPasswordInput.setError(getString(R.string.err_please_enter_confirm_password));
            confirmPasswordInput.setErrorEnabled(true);
            confirmPasswordInput.requestFocusFromTouch();
            confirmPasswordInput.requestFocus();
            return false;
        } else if (!(newPasswordEdt.getText().toString().toLowerCase().equalsIgnoreCase(confirmPasswordEdt.getText().toString().toLowerCase()))) {
            confirmPasswordInput.setError(getString(R.string.err_password_do_not_match));
            confirmPasswordInput.setErrorEnabled(true);
            confirmPasswordInput.requestFocusFromTouch();
            confirmPasswordInput.requestFocus();
            return false;
        } else if (!isValidPassword(confirmPasswordEdt.getText().toString())) {
            confirmPasswordInput.setError(getString(R.string.password_must_contain_onecharacter));
            confirmPasswordInput.requestFocusFromTouch();
            confirmPasswordInput.requestFocus();
            return false;
        }
        return true;
    }
    //  API to get Login();
    private void getLoginPage() {

        JsonObject object = loginPageObject();
        ChurchService service = ServiceFactory.createRetrofitService(this, ChurchService.class);
        mSubscription = service.getLoginPage(object)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<GetLoginPageResponseModel>() {
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
                    public void onNext(GetLoginPageResponseModel loginResponseModel) {

                        // to hide the Progress Dialog
                        hideProgressDialog();
                        if (loginResponseModel.getIsSuccess()) {


                            value = SharedPrefsData.getInt(LoginActivity.this, Constants.ISFROM, Constants.PREF_NAME);
                            if (value == 1) {
                                CommonUtil.setOnCartChangedListener(LoginActivity.this);
                                SharedPrefsData.putInt(getApplicationContext(), Constants.ISLOGIN, 1, Constants.PREF_NAME);
                                SharedPrefsData.putString(getApplicationContext(), Constants.UserId, loginResponseModel.getResult().getUserDetails().getUserId(), Constants.PREF_NAME);
                                SharedPrefsData.putInt(getApplicationContext(), Constants.ID, Integer.parseInt("" + loginResponseModel.getResult().getUserDetails().getId()), Constants.PREF_NAME);
                                SharedPrefsData.putInt(getApplicationContext(), Constants.IDProfile, Integer.parseInt("" + loginResponseModel.getResult().getUserDetails().getId()), Constants.PREF_NAME);
                                SharedPrefsData.putString(getApplicationContext(), Constants.Token_Type,  loginResponseModel.getResult().getTokenType(), Constants.PREF_NAME);
                                SharedPrefsData.putString(getApplicationContext(), Constants.Refresh_Token,  loginResponseModel.getResult().getRefreshToken(), Constants.PREF_NAME);
                                SharedPrefsData.putString(getApplicationContext(), Constants.Access_Token, loginResponseModel.getResult().getAccessToken(), Constants.PREF_NAME);
                                SharedPrefsData.getInstance(getApplicationContext()).updateStringValue(getApplicationContext(),
                                        Constants.Auth_Token, loginResponseModel.getResult().getTokenType() + " " + loginResponseModel.getResult().getAccessToken());
                                if (value == 1) {
                                    SharedPrefsData.putInt(getApplicationContext(), Constants.TOYOUTUBE, 1, Constants.PREF_NAME);
                                    Intent i = new Intent(LoginActivity.this, YoutubePlayerActivity.class);
                                    startActivity(i);
                                    finish();
                                } else {
                                    startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                                }


                                Toast.makeText(mContext, "Login Successful", Toast.LENGTH_SHORT).show();



                            } else {
                                CommonUtil.setOnCartChangedListener(LoginActivity.this);
                                SharedPrefsData.putInt(getApplicationContext(), Constants.ISLOGIN, 1, Constants.PREF_NAME);
                                SharedPrefsData.putString(getApplicationContext(), Constants.UserId, loginResponseModel.getResult().getUserDetails().getUserId(), Constants.PREF_NAME);
                                SharedPrefsData.putInt(getApplicationContext(), Constants.ID, Integer.parseInt("" + loginResponseModel.getResult().getUserDetails().getId()), Constants.PREF_NAME);
                                SharedPrefsData.putInt(getApplicationContext(), Constants.IDProfile, Integer.parseInt("" + loginResponseModel.getResult().getUserDetails().getId()), Constants.PREF_NAME);

                                SharedPrefsData.getInstance(getApplicationContext()).updateStringValue(getApplicationContext(),  Constants.Token_Type,  loginResponseModel.getResult().getTokenType());
                                SharedPrefsData.getInstance(getApplicationContext()).updateStringValue(getApplicationContext(),
                                        Constants.Refresh_Token,  loginResponseModel.getResult().getRefreshToken());
                                SharedPrefsData.getInstance(getApplicationContext()).updateStringValue(getApplicationContext(),  Constants.Access_Token, loginResponseModel.getResult().getAccessToken());

                               SharedPrefsData.getInstance(getApplicationContext()).updateStringValue(getApplicationContext(),
                                       Constants.Auth_Token, loginResponseModel.getResult().getTokenType() + " " + loginResponseModel.getResult().getAccessToken());
                              String token = SharedPrefsData.getInstance(LoginActivity.this).getStringFromSharedPrefs(Constants.Auth_Token); ;

                                /* SharedPrefsData.putString(getApplicationContext(), Constants.Token_Type,  loginResponseModel.getResult().getTokenType(), Constants.PREF_NAME);
                                SharedPrefsData.putString(getApplicationContext(), Constants.Refresh_Token,  loginResponseModel.getResult().getRefreshToken(), Constants.PREF_NAME);
                                SharedPrefsData.putString(getApplicationContext(), Constants.Access_Token, loginResponseModel.getResult().getAccessToken(), Constants.PREF_NAME);*/
                                SharedPrefsData.putInt(getApplicationContext(),"expires_in",loginResponseModel.getResult().getExpiresIn(),Constants.PREF_NAME);


                                if (value == 1) {
                                    SharedPrefsData.putInt(getApplicationContext(), Constants.TOYOUTUBE, 1, Constants.PREF_NAME);
                                    Intent i = new Intent(LoginActivity.this, YoutubePlayerActivity.class);
                                    startActivity(i);
                                    finish();
                                } else {
                                    startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                                }

                                CommonUtil.customToast(loginResponseModel.getEndUserMessage(), LoginActivity.this);


                            }
                            if (CommonUtil.timer != null)
                                CommonUtil.timer.cancel();

                            CommonUtil.timer = new MyCounter(loginResponseModel.getResult().getExpiresIn()*1000, 1000, mContext);

                            CommonUtil.timer.start();

                            Intent i = new Intent(getApplicationContext(), HomeActivity.class);
                            startActivity(i);
                        } else {

                            Toast.makeText(getApplicationContext(), " " + loginResponseModel.getEndUserMessage(), Toast.LENGTH_SHORT).show();
                        }


                    }
                });

    }

    /**
     * Json object of loginPageObject
     *
     * @return
     */
    private JsonObject loginPageObject() {
        LoginPageRequestModel requestModel = new LoginPageRequestModel();
        requestModel.setUserName(userNameEdt.getText().toString().trim());
        requestModel.setPassword(passwordEdt.getText().toString().trim());
        requestModel.setDeviceId("" + SharedPrefsData.getString(LoginActivity.this, Constants.gcmRegId, Constants.PREF_NAME));
        requestModel.setClientId("ConsoleApp");
        requestModel.setClientSecret("abc@123");
        return new Gson().toJsonTree(requestModel).getAsJsonObject();
    }



    /**
     * Change password API
     */
    private void changePassword() {
        JsonObject object = changePasswordObject();
        ChurchService service = ServiceFactory.createRetrofitService(this, ChurchService.class);
        mSubscription = service.ChangePassword(object)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ChangePasswordResponseModel>() {
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
                    public void onNext(ChangePasswordResponseModel registerResponse) {
                        if (registerResponse.getIsSuccess()) {
                            CommonUtil.customToast(registerResponse.getEndUserMessage(), LoginActivity.this);


                        } else {
                            Toast.makeText(getApplicationContext(), "" + registerResponse.getEndUserMessage(), Toast.LENGTH_SHORT).show();
                        }


                    }
                });
    }

    /**
     * Json object of change password
     *
     * @return
     */
    private JsonObject changePasswordObject() {
        ChangePasswordRequestModel mRequest = new ChangePasswordRequestModel();
        mRequest.setUserId("" + SharedPrefsData.getInt(LoginActivity.this, Constants.ID, Constants.PREF_NAME));
        mRequest.setOldPassword(oldPasswordEdt.getText().toString());
        mRequest.setNewPassword(newPasswordEdt.getText().toString());
        mRequest.setConfirmPassword(confirmPasswordEdt.getText().toString());
        return new Gson().toJsonTree(mRequest).getAsJsonObject();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    @Override
    public void setCartClickListener(String status) {
        if (status.equalsIgnoreCase("ok")) {
            if (value == 1) {
                SharedPrefsData.putInt(getApplicationContext(), Constants.TOYOUTUBE, 1, Constants.PREF_NAME);
                Intent i = new Intent(LoginActivity.this, YoutubePlayerActivity.class);
                startActivity(i);
                finish();
            } else {
                startActivity(new Intent(LoginActivity.this, HomeActivity.class));
            }
        }

    }

    //API to get forgot Password
    private void forgotPassWord() {
        JsonObject object = forgotPasswordObject();
        ChurchService service = ServiceFactory.createRetrofitService(getApplicationContext(), ChurchService.class);
        mSubscription = service.ForgotPassword(object)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ChangePasswordResponseModel>() {
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
                    public void onNext(ChangePasswordResponseModel mResponse) {
                        // to hide the Progress Dialog
                        hideProgressDialog();
                        mForgotResponse = mResponse;
                        CommonUtil.customToast(mResponse.getEndUserMessage(), LoginActivity.this);
                        Toast.makeText(LoginActivity.this, "" + mResponse.getEndUserMessage(), Toast.LENGTH_SHORT).show();

                    }

                });
    }

    /**
     * Json object of forgotPasswordObject()
     *
     * @return
     */
    private JsonObject forgotPasswordObject() {
        ChangePasswordRequestModel mRequest = new ChangePasswordRequestModel();
        mRequest.setEmail(numberEdt.getText().toString());
        return new Gson().toJsonTree(mRequest).getAsJsonObject();
    }
}
