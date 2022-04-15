package in.calibrage.teluguchurches.views.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import in.calibrage.teluguchurches.util.BaseActivity;
import in.calibrage.teluguchurches.util.Constants;
import in.calibrage.teluguchurches.util.localData.SharedPrefsData;
import in.calibrage.teluguchurches.views.model.AllItemsResponseModel;
import in.calibrage.teluguchurches.views.model.CartResponseModel;
import in.calibrage.teluguchurches.views.model.GetCategoryRequestModel;
import io.fabric.sdk.android.Fabric;
import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static in.calibrage.teluguchurches.util.CommonUtil.customToast;

public class OnlineshoppingItemDetailActivity extends BaseActivity implements View.OnClickListener {
    private EditText qtyEdt;
    private TextView descriptionText, priceText, authorText, inStockText, itemDetailsLabel;
    private ImageView itemImg, toolbar_image;
    private Button Btn_add_cart;
    private Context mContext;
    private Toolbar toolbar;
    private Intent intent;
    private int perquantity,newquantityvalue, id;
    private Subscription mSubscription;
    AllItemsResponseModel mResponse;
    private String authorizationToken, name, quantity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getApplicationContext();

        // added crash report's to mail
        Fabric.with(getApplicationContext(), new Crashlytics());
        //assining layout
        setContentView(R.layout.activity_item_detail);

        intent = getIntent();
        if (intent != null) {
            /*
             * to check the intent value
             * @param  id
             *@param  name
             */
            id = intent.getIntExtra("id", 0);
            name = intent.getStringExtra("name");
        }
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle(name);
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
                startActivity(new Intent(OnlineshoppingItemDetailActivity.this, HomeActivity.class));
            }
        });


        /* intializing and assigning ID's */
        initViews();

        /* Navigation's and using the views */
        setViews();
    }

    /* intializing and assigning ID's */
    private void initViews() {
        descriptionText = findViewById(R.id.descriptionText);
        priceText =findViewById(R.id.priceText);
        itemDetailsLabel = findViewById(R.id.itemDetailsLabel);
        authorText = findViewById(R.id.authorText);
        inStockText = findViewById(R.id.inStockText);
        itemImg = findViewById(R.id.itemImg);
        Btn_add_cart =findViewById(R.id.Btn_add_cart);
        qtyEdt =  findViewById(R.id.qtyEdt);

        // to start the Progress Dialog
        showProgressDialog();

        //API to get  particular shopping item details
        getItemById();

    }

    /* Navigation's and using the views */
    private void setViews() {


        qtyEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


            }

            @Override
            public void afterTextChanged(Editable editable) {

                String x = "";
                x = editable.toString();
                if (x.startsWith("0")) {
                    qtyEdt.setText("");
                } else if (x.startsWith(".")) {
                    qtyEdt.setText("");
                }


            }
        });

/**
 * @param OnClickListner
 */

        Btn_add_cart.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.Btn_add_cart:
                if (!qtyEdt.getText().toString().equalsIgnoreCase("")) {

                    // to start the Progress Dialog
                    showProgressDialog();

                    // API to get adding item to cart
                    addToCart();


                } else {
                    Toast.makeText(this, "Please enter quantity", Toast.LENGTH_SHORT).show();
                }

                break;

        }

    }


    //API to get  particular shopping item details
    private void getItemById() {
        ChurchService service = ServiceFactory.createRetrofitService(getApplicationContext(), ChurchService.class);
        String url = APIConstants.GetItemById + id + "/" + SharedPrefsData.getInt(getApplicationContext(), Constants.ID, Constants.PREF_NAME);
        mSubscription = service.getItemById(url)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<AllItemsResponseModel>() {
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
                    public void onNext(AllItemsResponseModel mResponse) {
                        // to hide the Progress Dialog
                        hideProgressDialog();
                        itemDetailsLabel.setText(mResponse.getListResult().get(0).getName());
                        descriptionText.setText("" + mResponse.getListResult().get(0).getDesc());
                    //    priceText.setText("₹" + mResponse.getListResult().get(0).getPrice());
                         priceText.setText("₹ " + "---");
                        authorText.setText("" + mResponse.getListResult().get(0).getAuthor());
                        if (mResponse.getListResult().get(0).getIsActive() == true) {
                            inStockText.setText("In stock");
                        } else {
                            inStockText.setText("Out of stock");
                        }
                        Glide.with(getApplicationContext()).load(mResponse.getListResult().get(0).getItemImage())
                                .fitCenter()
                                .error(R.drawable.loginimage)
                                .into(itemImg);
                        if (mResponse.getListResult().get(0).getQuantity() != null) {
                            perquantity = mResponse.getListResult().get(0).getQuantity();
                        }


                    }
                });
    }


    // API to get adding item to cart
    private void addToCart() {

        quantity = qtyEdt.getText().toString();
        newquantityvalue = perquantity + Integer.parseInt("" + quantity);


        if (newquantityvalue <= 99) {

            if (authorizationToken != "null" || authorizationToken != null || authorizationToken.isEmpty()) {
                authorizationToken = SharedPrefsData.getInstance(mContext).getStringFromSharedPrefs(Constants.Auth_Token);
            }
            JsonObject object = addToCartObject();
            ChurchService service = ServiceFactory.createRetrofitService(getApplicationContext(), ChurchService.class);
            mSubscription = service.AddToCart(object, authorizationToken)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<CartResponseModel>() {
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
                        public void onNext(CartResponseModel mResponseModel) {
                            // to hide the Progress Dialog
                            hideProgressDialog();
                            customToast("" + mResponseModel.getEndUserMessage(), OnlineshoppingItemDetailActivity.this);
                            Intent intent = new Intent(OnlineshoppingItemDetailActivity.this, CartActivity.class)
                                    .putExtra("quantity", mResponseModel.getListResult().get(0).getQuantity());
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);

                            //API to get  particular shopping item details
                            getItemById();

                        }
                    });
        } else {
            // to hide the Progress Dialog
            hideProgressDialog();
            Toast.makeText(mContext, "Item Quantity Should be less than 99", Toast.LENGTH_SHORT).show();
        }
    }
    /**
     * Json Object of addToCartObject
     *
     * @return
     */
    private JsonObject addToCartObject() {
        GetCategoryRequestModel mRequest = new GetCategoryRequestModel();
        mRequest.setUserId(SharedPrefsData.getInt(getApplicationContext(), Constants.ID, Constants.PREF_NAME));
        mRequest.setId(0);
        mRequest.setItemId(id);
        mRequest.setQuantity(Integer.parseInt(quantity));
        return new Gson().toJsonTree(mRequest).getAsJsonObject();
    }
}
