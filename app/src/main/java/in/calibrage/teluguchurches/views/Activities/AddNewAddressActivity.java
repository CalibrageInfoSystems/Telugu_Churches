package in.calibrage.teluguchurches.views.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import in.calibrage.teluguchurches.R;
import in.calibrage.teluguchurches.services.APIConstants;
import in.calibrage.teluguchurches.services.ChurchService;
import in.calibrage.teluguchurches.services.ServiceFactory;
import in.calibrage.teluguchurches.util.BaseActivity;
import in.calibrage.teluguchurches.util.CommonUtil;
import in.calibrage.teluguchurches.util.Constants;
import in.calibrage.teluguchurches.util.localData.SharedPrefsData;
import in.calibrage.teluguchurches.views.model.AddressResponseModel;
import in.calibrage.teluguchurches.views.model.StateResponseModel;
import in.calibrage.teluguchurches.views.model.UpdateUserInfoRequestModel;
import in.calibrage.teluguchurches.views.model.UserRegisterResponseModel;
import io.fabric.sdk.android.Fabric;
import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class AddNewAddressActivity extends BaseActivity {
    private EditText nameEdt, address1_edt, address2_edt,
            pinCode_edt, landMarkEdt, mobileNumEdt;
    private TextInputLayout name_lay, address1_lay,
            address2_lay, pinCode_lay, landMark_lay, mobile_lay;
    private Spinner stateSpin, countrySpin;
    private Button saveBtn;
    private Toolbar toolbar;
    private Intent intent;
    private Subscription mSubscription;
    private String comingFrom, addressID, authorizationToken, dateandtime;
    private int stateSpinStr, countrySpinStr;
    private StateResponseModel mStatesModel;
    private StateResponseModel countryModel;
    private AddressResponseModel addressResponseModel;
    private Context mContext;
    ArrayAdapter<String> adapter_state, adapter_country;
    private ImageView toolbar_image;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = getApplicationContext();
        // added crash report's to mail
        Fabric.with(getApplicationContext(), new Crashlytics());

        //assining layout
        setContentView(R.layout.activity_add_new_address);

        // to get current date and time
        dateAndtime();

        /* intializing and assigning ID's */
        initViews();

        /* Navigation's and using the views */
        setViews();

    }

    /* intializing and assigning ID's */
    private void initViews() {
        intent = getIntent();
        if (intent != null) {
            /*
             * to check the intent value
             * @param  comingFrom
             *@param  addressID
            */
            comingFrom = getIntent().getStringExtra("comingFrom");
            addressID = getIntent().getStringExtra("addressID");
        }
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle(getString(R.string.address));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AddNewAddressActivity.this, AddressActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
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
                startActivity(new Intent(AddNewAddressActivity.this, HomeActivity.class));
            }
        });
        nameEdt = findViewById(R.id.nameEdt);
        address1_edt = findViewById(R.id.address1_edt);
        address2_edt = findViewById(R.id.address2_edt);
        pinCode_edt = findViewById(R.id.pinCode_edt);
        landMarkEdt = findViewById(R.id.landMarkEdt);
        mobileNumEdt = findViewById(R.id.mobileNumEdt);
        stateSpin = findViewById(R.id.stateSpin);
        countrySpin = findViewById(R.id.countrySpin);
        saveBtn = findViewById(R.id.saveBtn);
        name_lay = findViewById(R.id.name_lay);
        address1_lay = findViewById(R.id.address1_lay);
        address2_lay = findViewById(R.id.address2_lay);
        pinCode_lay = findViewById(R.id.pinCode_lay);
        landMark_lay = findViewById(R.id.landMark_lay);
        mobile_lay = findViewById(R.id.mobile_lay);

        // edit address
        if (comingFrom.equalsIgnoreCase("edit")) {
            // API for adding data to edit address
            editAddress();
        }

        // API to get state's list
        getAllStates();

        // API to get countries list
        getAllCountries();


    }
    /* Navigation's and using the views */
    private void setViews() {

        /**
         * @param OnClickListner
         */
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //validating all fields
                if (validations())
                    // API to add Address
                    addNewAddress();

            }
        });

        nameEdt.addTextChangedListener(new TextWatcher() {
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
        address1_edt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    address1_lay.setErrorEnabled(false);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        address2_edt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    address2_lay.setErrorEnabled(false);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        pinCode_edt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    pinCode_lay.setErrorEnabled(false);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        landMarkEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    landMark_lay.setErrorEnabled(false);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        mobileNumEdt.addTextChangedListener(new TextWatcher() {
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

    //validations for all fields
    private boolean validations() {
        if (nameEdt.getText().toString().equalsIgnoreCase("")) {
            name_lay.setError(getString(R.string.err_please_enter_full_name));
            name_lay.requestFocus();
            name_lay.setErrorEnabled(true);
            return false;
        } else if (address1_edt.getText().toString().equalsIgnoreCase("")) {
            address1_lay.setError(getString(R.string.err_please_enter_address1));
            address1_lay.requestFocus();
            address1_lay.setErrorEnabled(true);
            return false;
        } else if (address2_edt.getText().toString().equalsIgnoreCase("")) {
            address2_lay.setError(getString(R.string.err_please_enter_address2));
            address2_lay.requestFocus();
            address2_lay.setErrorEnabled(true);
            return false;
        } else if (pinCode_edt.getText().toString().equalsIgnoreCase("")) {
            pinCode_lay.setError(getString(R.string.err_please_enter_pincode));
            pinCode_lay.requestFocus();
            pinCode_lay.setErrorEnabled(true);
            return false;
        } else if (countrySpin.getSelectedItem().toString().trim().equals("Select")) {
            Toast.makeText(this, R.string.err_please_select_country, Toast.LENGTH_SHORT).show();
            return false;
        } else if (stateSpin.getSelectedItem().toString().trim().equals("Select")) {
            Toast.makeText(this, R.string.err_please_select_state, Toast.LENGTH_SHORT).show();
            return false;
        } else if (mobileNumEdt.getText().toString().equalsIgnoreCase("")) {
            mobile_lay.setError(getString(R.string.err_please_enter_mobile_number));
            mobile_lay.setErrorEnabled(true);
            mobile_lay.requestFocus();
            return false;
        } else if (!isValidPhone() || mobileNumEdt.getText().toString().startsWith("0")) {
            mobile_lay.setError(getString(R.string.err_please_enter_valid_mobile_number));
            mobile_lay.requestFocus();
            mobile_lay.setErrorEnabled(true);
            return false;
        }
        return true;
    }

    // to validate phone number
    private boolean isValidPhone() {
        String target = mobileNumEdt.getText().toString().trim();
        if (target.length() != 10) {
            return false;
        } else {
            return android.util.Patterns.PHONE.matcher(target).matches();
        }
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(AddNewAddressActivity.this, AddressActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        finish();
    }

    private void editAddress() {
        ChurchService service = ServiceFactory.createRetrofitService(getApplicationContext(), ChurchService.class);
        String url = APIConstants.GetDeliveryAddressById + addressID;
        mSubscription = service.getAddressDeliveryById(url)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<AddressResponseModel>() {
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
                    public void onNext(AddressResponseModel mResponse) {
                        addressResponseModel = mResponse;
                        nameEdt.setText(mResponse.getListResult().get(0).getFullName());
                        address1_edt.setText(mResponse.getListResult().get(0).getAddressLine1());
                        address2_edt.setText(mResponse.getListResult().get(0).getAddressLine2());
                        pinCode_edt.setText("" + mResponse.getListResult().get(0).getPinCode());
                        landMarkEdt.setText("" + mResponse.getListResult().get(0).getLandmark());
                        mobileNumEdt.setText("" + mResponse.getListResult().get(0).getMobileNumber());
                        stateSpinStr = mResponse.getListResult().get(0).getStateId();
                        countrySpinStr = mResponse.getListResult().get(0).getCountryId();

                        // API to get state's list
                        getAllStates();

                        // API to get countries list
                        getAllCountries();
                    }

                });
    }

    // API to get state's list
    private void getAllStates() {
        ChurchService service = ServiceFactory.createRetrofitService(getApplicationContext(), ChurchService.class);
        String url = APIConstants.GetAllStates;
        mSubscription = service.getAllStates(url)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<StateResponseModel>() {
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
                    public void onNext(StateResponseModel mResponse) {
                        ArrayList statesList = new ArrayList();
                        mStatesModel = mResponse;
                        statesList.add(getString(R.string.select));
                        for (int i = 0; i < mStatesModel.getListResult().size(); i++)
                            statesList.add(mStatesModel.getListResult().get(i).getName());
                        adapter_state = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, statesList);
                        adapter_state.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        stateSpin.setAdapter(adapter_state);
                        if (addressResponseModel != null && addressResponseModel.getListResult().get(0).getStateId() != null &&
                                !addressResponseModel.getListResult().get(0).getStateId().equals(""))
                            for (int j = 0; j < mStatesModel.getListResult().size(); j++)
                                if (mStatesModel.getListResult().get(j).getId().equals(stateSpinStr))
                                    stateSpin.setSelection(j + 1);
                    }

                });
    }

    // API to get countries list
    private void getAllCountries() {
        ChurchService service = ServiceFactory.createRetrofitService(getApplicationContext(), ChurchService.class);
        String url = APIConstants.GetAllCountries;
        mSubscription = service.getAllCountries(url)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<StateResponseModel>() {
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
                    public void onNext(StateResponseModel mResponse) {
                        ArrayList countryList = new ArrayList();
                        countryModel = mResponse;
                        countryList.add(getString(R.string.select));
                        for (int i = 0; i < countryModel.getListResult().size(); i++)
                            countryList.add(countryModel.getListResult().get(i).getName());
                        adapter_country = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_text_black, countryList);
                        countrySpin.setAdapter(adapter_country);

                        if (addressResponseModel != null && addressResponseModel.getListResult().get(0).getCountryId() != null &&
                                !addressResponseModel.getListResult().get(0).getCountryId().equals(""))
                            for (int j = 0; j < countryModel.getListResult().size(); j++)
                                if (countryModel.getListResult().get(j).getId().equals(countrySpinStr))
                                    countrySpin.setSelection(j + 1);
                    }

                });
    }


    // API to add Address
    private void addNewAddress() {
        saveBtn.setEnabled(false);
        if (authorizationToken != "null" || authorizationToken != null || authorizationToken.isEmpty()) {
            authorizationToken = SharedPrefsData.getInstance(mContext).getStringFromSharedPrefs(Constants.Auth_Token);
        }
        JsonObject object = addAddressRequest();
        ChurchService service = ServiceFactory.createRetrofitService(getApplicationContext(), ChurchService.class);
        mSubscription = service.addUpdateDeliveryAddress(object, authorizationToken)
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
                    public void onNext(UserRegisterResponseModel mResponse) {
                        saveBtn.setEnabled(true);
                        CommonUtil.customToast(mResponse.getEndUserMessage(), AddNewAddressActivity.this);
                        startActivity(new Intent(getApplicationContext(), AddressActivity.class));
                    }

                });
    }

    // to get current date and time
    private void dateAndtime() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        String formattedDate = df.format(c.getTime());
        dateandtime = formattedDate;
        SharedPrefsData.getInstance(AddNewAddressActivity.this).updateStringValue(AddNewAddressActivity.this, "datetime", dateandtime);
    }


    /**
     * Json Object of addAddressRequest
     *
     * @return
     */
    private JsonObject addAddressRequest() {
        UpdateUserInfoRequestModel model = new UpdateUserInfoRequestModel();
        model.setId(comingFrom.equalsIgnoreCase("edit") ? Integer.valueOf(addressID) : 0);
        model.setFullName(nameEdt.getText().toString());
        model.setAddressLine1(address1_edt.getText().toString());
        model.setAddressLine2(address2_edt.getText().toString());
        model.setLandmark(landMarkEdt.getText().toString());
        model.setCity(landMarkEdt.getText().toString());
        model.setPinCode(pinCode_edt.getText().toString());
        model.setStateId(mStatesModel.getListResult().get(stateSpin.getSelectedItemPosition() - 1).getId());
        model.setCountryId(countryModel.getListResult().get(countrySpin.getSelectedItemPosition() - 1).getId());
        model.setMobileNumber(mobileNumEdt.getText().toString());
        model.setUserId("" + SharedPrefsData.getInt(getApplicationContext(), Constants.ID, Constants.PREF_NAME));
        model.setIsActive(true);
        model.setCreatedByUserId(SharedPrefsData.getInt(getApplicationContext(), Constants.ID, Constants.PREF_NAME));
        model.setCreatedDate(dateandtime);
        model.setUpdatedByUserId(SharedPrefsData.getInt(getApplicationContext(), Constants.ID, Constants.PREF_NAME));
        model.setUpdatedDate(dateandtime);
        return new Gson().toJsonTree(model).getAsJsonObject();

    }
}
