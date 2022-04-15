package in.calibrage.teluguchurches.views.Activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.ArrayList;

import in.calibrage.teluguchurches.R;
import in.calibrage.teluguchurches.services.ChurchService;
import in.calibrage.teluguchurches.services.ServiceFactory;
import in.calibrage.teluguchurches.util.BaseActivity;
import in.calibrage.teluguchurches.util.CommonUtil;
import in.calibrage.teluguchurches.util.Constants;
import in.calibrage.teluguchurches.util.localData.SharedPrefsData;
import in.calibrage.teluguchurches.views.adapter.AddressAdapter;
import in.calibrage.teluguchurches.views.model.AddressResponseModel;
import in.calibrage.teluguchurches.views.model.GetAllChurchesRequestModel;
import in.calibrage.teluguchurches.views.model.LikeRequestModel;
import in.calibrage.teluguchurches.views.model.UserRegisterResponseModel;
import io.fabric.sdk.android.Fabric;
import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class AddressActivity extends BaseActivity implements View.OnClickListener, AddressAdapter.OnCartChangedListener {
    private TextView dialogMessage, no_items_text;
    private Button ok_btn, cancel_btn,addAddressBtn, continueBtn;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private AddressAdapter adapter;
    private Toolbar toolbar;
    private Integer pageIndex = 1, pageSize = 10, listPosition = -1,listAddressPosition = -1;
    private int addressId;
    private Subscription mSubscription;
    private ArrayList<AddressResponseModel.ListResult> BindDataListResults = new ArrayList<>();
    private ArrayList<AddressResponseModel.ListResult> listResults = new ArrayList<>();
    AddressResponseModel addressResponseModel;
    private Handler handler;
    private boolean loading = true;
    private Dialog alertDialog;
    View viewLineAddress;
    private Context mContext;
    private ImageView toolbar_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext=getApplicationContext();

        // added crash report's to mail
        Fabric.with(getApplicationContext(), new Crashlytics());

        //assining layout
        setContentView(R.layout.activity_address);

        /* intializing and assigning ID's */
        initViews();

        /* Navigation's and using the views */
        setViews();
    }
    /* intializing and assigning ID's */
    private void initViews() {

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle(getString(R.string.address));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AddressActivity.this, CartActivity.class));
            }
        });
        toolbar_image = findViewById(R.id.toolbar_image);

        /**
         * @param OnClickListner
         */
        toolbar_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AddressActivity.this, HomeActivity.class));
            }
        });
        addAddressBtn = findViewById(R.id.addAddressBtn);
        continueBtn = findViewById(R.id.continueBtn);
        recyclerView =findViewById(R.id.recyclerView);
        no_items_text = findViewById(R.id.no_items_text);
        viewLineAddress = findViewById(R.id.viewLineAddress);


        // API to get Address list
        getAllDeliveryAddress();


    }
    /* Navigation's and using the views */
    private void setViews() {
        /**
         * @param OnClickListner
         */
        addAddressBtn.setOnClickListener(this);

        /**
         * @param OnClickListner
         */
        continueBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.addAddressBtn:

                /*
                 * intent value passing to AddnewAdress
                 * @param  comingFrom
                 *@param  Add
                 */
                Intent i = new Intent(AddressActivity.this, AddNewAddressActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.putExtra("comingFrom", "Add");
                startActivity(i);


                break;
            case R.id.continueBtn:
                // PopUpDailog to show message
                popUpDailog();
                break;
        }

    }

    // PopUpDailog to show message
    private void popUpDailog() {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View dialogRootView = layoutInflater.inflate(R.layout.add_dialog_online_shopping, null);
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        ok_btn = dialogRootView.findViewById(R.id.ok_btn);

        /**
         * @param OnClickListner
         */
        ok_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        alertDialogBuilder.setView(dialogRootView);
        alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    // API to get Address list
    private void getAllDeliveryAddress() {

        JsonObject object = listAddressRequest();
        ChurchService service = ServiceFactory.createRetrofitService(getApplicationContext(), ChurchService.class);
        mSubscription = service.getAllDeliveryAddress(object)
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
                        hideProgressDialog();
                        addressResponseModel = mResponse;
                        BindDataListResults = new ArrayList<>();
                        if (mResponse.getIsSuccess()) {
                            if (mResponse.getListResult()==null) {
                                no_items_text.setVisibility(View.VISIBLE);
                                no_items_text.setText(R.string.no_address_found);
                                continueBtn.setVisibility(View.GONE);
                                viewLineAddress.setVisibility(View.GONE);

                            } else {
                                no_items_text.setVisibility(View.GONE);
                                continueBtn.setVisibility(View.VISIBLE);
                                viewLineAddress.setVisibility(View.VISIBLE);
                            }
                            BindDataListResults = (ArrayList<AddressResponseModel.ListResult>) mResponse.getListResult();

                            listResults.addAll(BindDataListResults);
                            layoutManager = new LinearLayoutManager(getApplicationContext());
                            recyclerView.setLayoutManager(layoutManager);
                            recyclerView.setHasFixedSize(true);
                            adapter = new AddressAdapter<>(getApplicationContext(), listResults, recyclerView);
                            recyclerView.setAdapter(adapter);
                            int Possition = listResults.size() - pageSize;
                            recyclerView.smoothScrollToPosition(Possition);

                            adapter.setOnCartChangedListener(AddressActivity.this);
                            handler = new Handler();
                            adapter.setOnLoadMoreListener(new AddressAdapter.OnLoadMoreListener() {
                                @Override
                                public void onLoadMore() {
                                    //add progress item
                                    listResults.add(null);
                                    adapter.notifyItemInserted(listResults.size() - 1);

                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            loading = false;
                                            //remove progress item
                                            if (BindDataListResults.size() >= pageSize) {
                                                listResults.remove(listResults.size() - 1);
                                                adapter.notifyItemRemoved(listResults.size());
                                                pageIndex = pageIndex + 1;
                                                showProgressDialog();

                                                // API to get Address list
                                                getAllDeliveryAddress();
                                                adapter.notifyDataSetChanged();
                                                loading = false;
                                            }

                                            //or you can add all at once but do not forget to call mAdapter.notifyDataSetChanged();
                                        }
                                    }, 1000);

                                }
                            });


                        } else {
                            CommonUtil.customToast(mResponse.getEndUserMessage(),AddressActivity.this);

                        }
                    }

                });
    }

    /**
     * Json Object of listAddressRequest
     *
     * @return
     */
    private JsonObject listAddressRequest() {
        GetAllChurchesRequestModel mRequest = new GetAllChurchesRequestModel();
        mRequest.setUserId(SharedPrefsData.getInt(getApplicationContext(), Constants.ID, Constants.PREF_NAME));
        mRequest.setPageIndex(pageIndex);
        mRequest.setPageSize(pageSize);
        mRequest.setSortbyColumnName("updatedDate");
        mRequest.setSortDirection("desc");
        mRequest.setSearchName("");
        return new Gson().toJsonTree(mRequest).getAsJsonObject();
    }

    // API to Delete Address
    private void deleteAddress() {
        JsonObject object = deleteObject();
        ChurchService service = ServiceFactory.createRetrofitService(getApplicationContext(), ChurchService.class);
        mSubscription = service.deleteDeliveryAddress(object)
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
                        if (mResponse.getIsSuccess()) {
                            alertDialog.dismiss();
                            listResults.remove(listPosition);

                            adapter.notifyDataSetChanged();
                            Intent intent = new Intent(AddressActivity.this, AddressActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                            CommonUtil.customToast(mResponse.getEndUserMessage(),AddressActivity.this);
                        }

                    }

                });
    }

    /**
     * Json Object of deleteObject
     *
     * @return
     */
    private JsonObject deleteObject() {
        LikeRequestModel mRequest = new LikeRequestModel();
        mRequest.setId(addressId);
        mRequest.setUserId("" + SharedPrefsData.getInt(getApplicationContext(), Constants.ID, Constants.PREF_NAME));
        return new Gson().toJsonTree(mRequest).getAsJsonObject();
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(AddressActivity.this, CartActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        finish();
    }

    // Interface between adapter and class
    @Override
    public void setCartClickListener(String status, int position) {
        listPosition = position;
        listAddressPosition = position;
        if (status.equals("itemView")) {

            /*
             * intent value passing to ChurchDetails
             * @param  comingFrom
             *@param  addressID
          */
        } else if (status.equalsIgnoreCase("edit")) {
            Intent i = new Intent(AddressActivity.this, AddNewAddressActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.putExtra("comingFrom", "edit").putExtra("addressID", "" + addressResponseModel.getListResult().get(position).getId());
            startActivity(i);


             // popupdialog to show message Are you sure want to Delete Address
        } else if (status.equalsIgnoreCase("delete")) {

            addressId = addressResponseModel.getListResult().get(position).getId();
            LayoutInflater layoutInflater = LayoutInflater.from(AddressActivity.this);

            // PopUpDailog to show message
            View dialogRootView = layoutInflater.inflate(R.layout.dialog_like, null);
            final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AddressActivity.this);
            dialogMessage = dialogRootView.findViewById(R.id.dialogMessage);
            dialogMessage.setText(R.string.are_you_want_to_delete);
            ok_btn = dialogRootView.findViewById(R.id.ok_btn);
            cancel_btn = dialogRootView.findViewById(R.id.cancel_btn);

            alertDialogBuilder.setView(dialogRootView);

            /**
             * @param OnClickListner
             */
            ok_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    // API to Delete Address
                    deleteAddress();
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

    }
}
