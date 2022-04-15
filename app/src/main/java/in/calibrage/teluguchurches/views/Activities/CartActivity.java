package in.calibrage.teluguchurches.views.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;

import in.calibrage.teluguchurches.R;
import in.calibrage.teluguchurches.services.APIConstants;
import in.calibrage.teluguchurches.services.ChurchService;
import in.calibrage.teluguchurches.services.ServiceFactory;
import in.calibrage.teluguchurches.util.BaseActivity;
import in.calibrage.teluguchurches.util.CommonUtil;
import in.calibrage.teluguchurches.util.Constants;
import in.calibrage.teluguchurches.util.localData.SharedPrefsData;
import in.calibrage.teluguchurches.views.adapter.CartAdapter;
import in.calibrage.teluguchurches.views.model.CartResponseModel;
import in.calibrage.teluguchurches.views.model.UpdateQuantityRequestModel;
import io.fabric.sdk.android.Fabric;
import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class CartActivity extends BaseActivity implements CartAdapter.OnChangedListener, View.OnClickListener {
    private TextView dialogMessage, no_items;
    private ImageView toolbar_image;
    private Button checkOutBtn, ok_btn, cancel_btn, continueShoppingBtn;
    private LinearLayout btnLay;
    private RecyclerView notificationRecyclerView;
    CartAdapter cartAdapter;
    private Context mContext;
    private Toolbar toolbar;
    private Integer id, itemid;
    private int listPos = 0;
    Intent intent;
    private Subscription mSubscription;
    private CartResponseModel mCartResponse;
    private AlertDialog alertDialog;
    private String quantityText, authorizationToken;
    View viewLine;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getApplicationContext();

        // added crash report's to mail
        Fabric.with(getApplicationContext(), new Crashlytics());
        //assining layout
        setContentView(R.layout.activity_cart);

        /* intializing and assigning ID's */
        initViews();

        /* Navigation's and using the views */
        setViews();

        /**
         * Hides the soft keyboard
         */
        hideKeyBoard();
    }

    /* intializing and assigning ID's */
    private void initViews() {

        toolbar =findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle(getString(R.string.cart));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CartActivity.this, HomeActivity.class));
            }
        });
        toolbar_image =  findViewById(R.id.toolbar_image);

        /**
         * @param OnClickListner
         */
        toolbar_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CartActivity.this, HomeActivity.class));
            }
        });
        checkOutBtn = findViewById(R.id.checkOutBtn);
        continueShoppingBtn = findViewById(R.id.continueShoppingBtn);
        notificationRecyclerView =findViewById(R.id.notificationRecyclerView);
        btnLay = findViewById(R.id.btnLay);
        //btnLay.setVisibility(View.VISIBLE);
        no_items = findViewById(R.id.no_items);
        viewLine = findViewById(R.id.viewLine);

        // to start the Progress Dialog
        showProgressDialog();

        // API to get cart items list
        getCartInfo();
    }

    /* Navigation's and using the views */
    private void setViews() {
        /**
         * @param OnClickListner
         */
        continueShoppingBtn.setOnClickListener(this);
        /**
         * @param OnClickListner
         */
        checkOutBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.continueShoppingBtn:

                startActivity(new Intent(getApplicationContext(), OnlineShoppingActivity.class));
                break;
            case R.id.checkOutBtn:
                Intent address = new Intent(getApplicationContext(), AddressActivity.class);
                address.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(address);

                break;
        }

    }

    // API to get cart items list
    private void getCartInfo() {
        if (authorizationToken != "null" || authorizationToken != null || authorizationToken.isEmpty())
            authorizationToken = SharedPrefsData.getInstance(mContext).getStringFromSharedPrefs(Constants.Auth_Token);
        ChurchService service = ServiceFactory.createRetrofitService(getApplicationContext(), ChurchService.class);
        String url = APIConstants.GetCartInfo + "" + SharedPrefsData.getInt(getApplicationContext(), Constants.ID, Constants.PREF_NAME);
        mSubscription = service.CartItemsCount(url, authorizationToken)
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
                    public void onNext(CartResponseModel mResponse) {
                        hideProgressDialog();
                        mCartResponse = mResponse;

                        if (mResponse.getListResult().isEmpty()) {
                            no_items.setVisibility(View.VISIBLE);
                            checkOutBtn.setVisibility(View.GONE);
                            viewLine.setVisibility(View.GONE);
                            //  no_items.setText(mContext.getString(R.string.no_records_items));
                        } else {
                            no_items.setVisibility(View.GONE);
                            checkOutBtn.setVisibility(View.VISIBLE);
                            viewLine.setVisibility(View.VISIBLE);
                        }

                        //   itemid=mCartResponse.getListResult().get(listPos).getItemId();
                        id = mCartResponse.getListResult().get(listPos).getId();
                        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                        notificationRecyclerView.setLayoutManager(layoutManager);
                        cartAdapter = new CartAdapter(getApplicationContext(), mCartResponse, "cart");
                        notificationRecyclerView.setAdapter(cartAdapter);
                        cartAdapter.setOnChangedListener(CartActivity.this);

                    }

                });
    }
    // API ot get delete item from cart
    private void deleteFromCart() {
        ChurchService service = ServiceFactory.createRetrofitService(getApplicationContext(), ChurchService.class);
        String url = APIConstants.DeleteFromCart + id +
                "/" + SharedPrefsData.getInt(getApplicationContext(), Constants.ID, Constants.PREF_NAME);
        mSubscription = service.DeleteFromCart(url)
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
                    public void onNext(CartResponseModel mResponse) {
                        mCartResponse = mResponse;
                        //id = mCartResponse.getListResult().get(listPos).getId();

                        // to hide the Progress Dialog
                        hideProgressDialog();
                        if (mResponse.getListResult().isEmpty()) {
                            no_items.setVisibility(View.VISIBLE);
                            checkOutBtn.setVisibility(View.GONE);
                            viewLine.setVisibility(View.GONE);
                            //no_items.setText(mContext.getString(R.string.no_records_items));
                        } else {
                            no_items.setVisibility(View.GONE);
                            checkOutBtn.setVisibility(View.VISIBLE);
                            viewLine.setVisibility(View.VISIBLE);
                        }

                        CommonUtil.customToast(mResponse.getEndUserMessage(), CartActivity.this);


                        cartAdapter = new CartAdapter(getApplicationContext(), mResponse, "delete");
                        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                        notificationRecyclerView.setLayoutManager(layoutManager);
                        notificationRecyclerView.setAdapter(cartAdapter);
                        cartAdapter.notifyDataSetChanged();
                        cartAdapter.setOnChangedListener(CartActivity.this);


                    }

                });
    }

    // delete item from cart
    private void itemDeleteFromCart() {

           // popupdialog to show message Are you sure want to remove item from your cart
        LayoutInflater layoutInflater = LayoutInflater.from(CartActivity.this);
        View dialogRootView = layoutInflater.inflate(R.layout.dialog_logout, null);
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(CartActivity.this);
        ok_btn = dialogRootView.findViewById(R.id.ok_btn);
        cancel_btn =dialogRootView.findViewById(R.id.cancel_btn);
        dialogMessage = dialogRootView.findViewById(R.id.dialogMessage);
        dialogMessage.setText(R.string.are_you_remove_from_cart);

        alertDialogBuilder.setView(dialogRootView);

        /**
         * @param OnClickListner
         */
        ok_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // API ot get delete item from cart
                deleteFromCart();
                alertDialog.dismiss();
            }
        });
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

    // Interface between adapter and class
    /*
     * getting values from adapter
     * @param  itemid
     *@param  listPos
     *@param  quantityText
     *@param  quantityText
     *@param  id
     */

    @Override
    public void setClickListener(String status, int position, String quantity, int itemId) {
        itemid = itemId;
        listPos = position;
        quantityText = quantity;
        id = mCartResponse.getListResult().get(listPos).getId();
        if (status.equalsIgnoreCase("delete")) {
            // delete item from cart
            itemDeleteFromCart();
        } else if (status.equalsIgnoreCase("updatequantity")) {

            // API to get update online shopping item quantity in cart
            updateQuantity();

            /**
             * Hides the soft keyboard
             */
            hideKeyBoard();
        }

    }

    // API to get update online shopping item quantity in cart
    private void updateQuantity() {

        if (authorizationToken != "null" || authorizationToken != null || authorizationToken.isEmpty())
            authorizationToken = SharedPrefsData.getInstance(mContext).getStringFromSharedPrefs(Constants.Auth_Token);
        JsonObject object = updatequantityRequestObject();
        ChurchService service = ServiceFactory.createRetrofitService(getApplicationContext(), ChurchService.class);
        mSubscription = service.UpdateToCart(object, authorizationToken)
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
                    public void onNext(CartResponseModel mResponse) {

                        // to hide the Progress Dialog
                        hideProgressDialog();
                        //  alertDialog.dismiss();

                        /**
                         * Hides the soft keyboard
                         */
                        hideKeyBoard();
                        if (mResponse.getIsSuccess()) {
                            LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                            notificationRecyclerView.setLayoutManager(layoutManager);
                            cartAdapter = new CartAdapter(getApplicationContext(), mResponse, "updatequantity");
                            notificationRecyclerView.setAdapter(cartAdapter);
                            cartAdapter.setOnChangedListener(CartActivity.this);
                            CommonUtil.customToast(mResponse.getEndUserMessage(), CartActivity.this);
                        } else {
                            CommonUtil.customToast(mResponse.getEndUserMessage(), CartActivity.this);

                        }


                    }

                });
    }

    /**
     * Json Object of updatequantityRequestObject
     *
     * @return
     */
    private JsonObject updatequantityRequestObject() {

        UpdateQuantityRequestModel mRequest = new UpdateQuantityRequestModel();
        mRequest.setUserId(SharedPrefsData.getInt(getApplicationContext(), Constants.ID, Constants.PREF_NAME));
        mRequest.setId(0);
        mRequest.setItemId(itemid);
        mRequest.setQuantity(quantityText);

        return new Gson().toJsonTree(mRequest).getAsJsonObject();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(CartActivity.this, HomeActivity.class));
    }
}
