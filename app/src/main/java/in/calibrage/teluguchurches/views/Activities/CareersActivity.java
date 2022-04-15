package in.calibrage.teluguchurches.views.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
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
import in.calibrage.teluguchurches.views.adapter.CareerAdapter;
import in.calibrage.teluguchurches.views.adapter.SearchAdapter;
import in.calibrage.teluguchurches.views.model.GetAllJobDetailsResponseModel;
import in.calibrage.teluguchurches.views.model.JobDetailsRequestModel;
import io.fabric.sdk.android.Fabric;
import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;



public class CareersActivity extends BaseActivity implements CareerAdapter.OnCartChangedListener {
    private TextView no_records;
    private ImageView toolbar_image;
    private RecyclerView recyclerViewCareers;
    CareerAdapter careersAdapter;
    LinearLayoutManager mLayoutManager;
    private Toolbar toolbar;
    private Context mContext;
    private int listPosition;
    private Integer pageIndex = 1, pageSize = 10;
    private Subscription mSubscription;
    private ArrayList<GetAllJobDetailsResponseModel.ListResult> listResults = new ArrayList<>();
    private ArrayList<GetAllJobDetailsResponseModel.ListResult> BindDataListResults = new ArrayList<>();
    GetAllJobDetailsResponseModel jobResponse;
    SearchView searchView;
    private SearchAdapter adapterSearch;
    private String searchStr = "",authorizationToken;
    private boolean loading = true;
    private Handler handler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mContext = getApplicationContext();
        super.onCreate(savedInstanceState);

        // added crash report's to mail
        Fabric.with(getApplicationContext(), new Crashlytics());

        //assining layout
        setContentView(R.layout.activity_careers);

        /* intializing and assigning ID's */
        initView();

        // to check the phone is connected to Internet or Not
        if(isOnline()){
            // API to get careers list
            getAllJobDetails();
        }else {
            // when no Internet connection
            CommonUtil.customToast(getString(R.string.no_internet),CareersActivity.this);
        }

    }

    /* intializing and assigning ID's */
    private void initView() {

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle(getString(R.string.careers));
        recyclerViewCareers =findViewById(R.id.recyclerViewCareers);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CareersActivity.this, HomeActivity.class);
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
                Intent intent = new Intent(CareersActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });
        no_records = findViewById(R.id.no_records);
        no_records.setVisibility(View.GONE);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_all_churches_search, menu);
        MenuItem search = menu.findItem(R.id.action_search);

        searchView = (SearchView) MenuItemCompat.getActionView(search);
        EditText searchEditText =  searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchView.setQueryHint(getString(R.string.search));

        // Particular careers to search
        search(searchView);
        searchView.setIconifiedByDefault(false);

        ImageView closeButton =searchView.findViewById(R.id.search_close_btn);


        /**
         * @param OnClickListner
         */
        closeButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(), CareersActivity.class));
                finish();

            }
        });


        search.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {

            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                //showToast("open");

                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                // showToast("close");
                searchView.onActionViewCollapsed();
                if (searchStr != null && !searchStr.equalsIgnoreCase(""))
                    if (!searchStr.isEmpty()) {
                        pageIndex = 1;
                        searchStr = "";
                        pageSize = 200;
                        BindDataListResults = new ArrayList<>();
                        listResults = new ArrayList<>();

                        // to start the Progress Dialog
                        showProgressDialog();

                        // API to get careers list
                        getAllJobDetails();
                        loading = false;
                    }

                return true;
            }
        });

        return true;
    }

    // Particular careers to search
    private void search(SearchView searchView) {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                searchStr = query;
                if (!searchStr.isEmpty()) {
                    pageIndex = 1;
                    BindDataListResults = new ArrayList<>();
                    listResults = new ArrayList<>();

                    // to start the Progress Dialog
                    showProgressDialog();

                    // API to get careers list
                    getAllJobDetails();
                }
                return false;
            }


            @Override
            public boolean onQueryTextChange(String newText) {

                return true;
            }
        });
    }

    // API to get careers list
    private void getAllJobDetails() {
        if (authorizationToken != "null" || authorizationToken != null || authorizationToken.isEmpty())
            authorizationToken = SharedPrefsData.getInstance(mContext).getStringFromSharedPrefs(Constants.Auth_Token);
        JsonObject object = jobDetailsObject();
        ChurchService service = ServiceFactory.createRetrofitService(getApplicationContext(), ChurchService.class);
        mSubscription = service.getAllJobDetails(object,authorizationToken)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<GetAllJobDetailsResponseModel>() {
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
                    public void onNext(final GetAllJobDetailsResponseModel mResponseModel) {

                        // to hide the Progress Dialog
                        hideProgressDialog();
                        jobResponse = mResponseModel;
                        if (mResponseModel.getIsSuccess()) {
                            if (mResponseModel.getListResult().isEmpty()) {
                                no_records.setVisibility(View.VISIBLE);

                            } else {
                                no_records.setVisibility(View.GONE);
                            }
                            BindDataListResults = (ArrayList<GetAllJobDetailsResponseModel.ListResult>) mResponseModel.getListResult();
                            listResults.addAll(BindDataListResults);
                            mLayoutManager = new LinearLayoutManager(getApplicationContext());
                            recyclerViewCareers.setLayoutManager(mLayoutManager);
                            recyclerViewCareers.setHasFixedSize(true);
                            careersAdapter = new CareerAdapter(getApplicationContext(), listResults, recyclerViewCareers);
                            recyclerViewCareers.setAdapter(careersAdapter);
                            int Possition = listResults.size() - pageSize;
                            recyclerViewCareers.smoothScrollToPosition(Possition);

                            careersAdapter.setOnCartChangedListener(CareersActivity.this);
                            handler = new Handler();
                            careersAdapter.setOnLoadMoreListener(new CareerAdapter.OnLoadMoreListener() {
                                @Override
                                public void onLoadMore() {
                                    //add progress item
                                    listResults.add(null);
                                    careersAdapter.notifyItemInserted(listResults.size() - 1);

                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            loading = false;
                                            //remove progress item
                                            if (BindDataListResults.size() >= pageSize) {
                                                listResults.remove(listResults.size() - 1);
                                                careersAdapter.notifyItemRemoved(listResults.size());
                                                pageIndex = pageIndex + 1;

                                                // to start the Progress Dialog
                                                showProgressDialog();

                                                // API to get careers list
                                                getAllJobDetails();
                                                careersAdapter.notifyDataSetChanged();
                                                loading = false;
                                            }

                                            //or you can add all at once but do not forget to call mAdapter.notifyDataSetChanged();
                                        }
                                    }, 1000);

                                }
                            });


                        } else {
                            CommonUtil.customToast(mResponseModel.getEndUserMessage(),CareersActivity.this);

                        }
                    }
                });
    }

    /**
     * Json Object of jobDetailsObject
     *
     * @return
     */
    private JsonObject jobDetailsObject() {
        JobDetailsRequestModel mRequest = new JobDetailsRequestModel();
        mRequest.setUserId(SharedPrefsData.getInt(getApplicationContext(), Constants.ID, Constants.PREF_NAME));
        mRequest.setPageIndex(pageIndex);
        mRequest.setPageSize(pageSize);
        mRequest.setSortbyColumnName("updatedDate");
        mRequest.setSortDirection("desc");
        mRequest.setSearchName(searchStr);

        return new Gson().toJsonTree(mRequest).getAsJsonObject();
    }


    // Interface between adapter and class
    /*
     * intent value passing to CareersDetailActivity
     * @param  id
     *@param  churchName
    */
    @Override
    public void setCartClickListener(String status, int position) {
        listPosition = position;
        if (status.equalsIgnoreCase("MainClick")) {
            if (listResults.get(position).getId() != null && !listResults.get(position).getId().equals("")) {
                SharedPrefsData.putInt(CareersActivity.this, Constants.CareersId, listResults.get(position).getId(), Constants.PREF_NAME);
                startActivity(new Intent(getApplicationContext(), CareersDetailActivity.class)
                        .putExtra("id", listResults.get(position).getId())
                        .putExtra("churchName", listResults.get(position).getChurchName()));
            }
        }

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(CareersActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }
}

