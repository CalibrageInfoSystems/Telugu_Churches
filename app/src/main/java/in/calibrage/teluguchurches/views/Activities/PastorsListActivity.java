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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.ArrayList;

import in.calibrage.teluguchurches.R;
import in.calibrage.teluguchurches.services.ChurchService;
import in.calibrage.teluguchurches.services.ServiceFactory;
import in.calibrage.teluguchurches.util.BaseActivity;
import in.calibrage.teluguchurches.util.Constants;
import in.calibrage.teluguchurches.util.localData.SharedPrefsData;
import in.calibrage.teluguchurches.views.adapter.AdminAdapter;
import in.calibrage.teluguchurches.views.adapter.AdminSearchAdapter;
import in.calibrage.teluguchurches.views.model.GetAdminRequestModel;
import in.calibrage.teluguchurches.views.model.GetAllAdminResponseModel;
import io.fabric.sdk.android.Fabric;
import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class PastorsListActivity extends BaseActivity implements AdminAdapter.OnCartChangedListener, AdminSearchAdapter.OnChangedListener {
    public static final String TAG = PastorsListActivity.class.getSimpleName();
    TextView noRecords;
    private ImageView toolbar_image;
    private RecyclerView eventRecyclerView, eventSearchRecyclerView;
    private AdminAdapter adapter;
    AdminSearchAdapter adminSearchAdapter;
    private LinearLayoutManager layoutManager;
    private Context mContext;
    private Toolbar toolbar;
    private Integer pageIndex = 1, pageSize = 10, listPosition = 0;
    private int intValue, subscibeval;
    private Subscription mSubscription;
    private ArrayList<GetAllAdminResponseModel.ListResult> BindDataListResults = new ArrayList<>();
    private ArrayList<GetAllAdminResponseModel.ListResult> listResults = new ArrayList<>();
    private Handler handler;
    private boolean loading = true;
    SearchView searchView;
    private String searchStr = "";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getApplicationContext();

        // added crash report's to mail
        Fabric.with(getApplicationContext(), new Crashlytics());

        //assining layout
        setContentView(R.layout.activity_authors);

        /* intializing and assigning ID's */
        initViews();

    }

    /* intializing and assigning ID's */
    private void initViews() {

        toolbar = findViewById(R.id.toolbar);
        noRecords = findViewById(R.id.no_records);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle(getString(R.string.pastors_title));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        toolbar_image =  findViewById(R.id.toolbar_image);

        /**
         * @param OnClickListner
         */
        toolbar_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PastorsListActivity.this, HomeActivity.class));
            }
        });
        eventRecyclerView = findViewById(R.id.eventRecyclerView);
        eventSearchRecyclerView =findViewById(R.id.eventsearchRecyclerView);
        eventSearchRecyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getApplicationContext());
        eventSearchRecyclerView.setLayoutManager(layoutManager);
        eventSearchRecyclerView.setVisibility(View.GONE);
        noRecords.setVisibility(View.GONE);

        // to check the phone is connected to Internet or Not
        if (isOnline()) {

            // API to get Pastor's List
            getAllAdmins();

            // to start the Progress Dialog
            showProgressDialog();
        } else {
            // when no internet connection
            showToastLong(getString(R.string.no_internet));
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_all_churches_search, menu);
        MenuItem search = menu.findItem(R.id.action_search);

        searchView = (SearchView) MenuItemCompat.getActionView(search);
        EditText searchEditText = searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchView.setQueryHint(getString(R.string.search));
        // searchview pastor list
        search(searchView);
        searchView.setIconifiedByDefault(false);
        // Get the search close button image view
        ImageView closeButton = searchView.findViewById(R.id.search_close_btn);


        /**
         * @param OnClickListner
         */
        closeButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(), PastorsListActivity.class));
                finish();

            }
        });


        search.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {

            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {


                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {

                searchView.onActionViewCollapsed();

                startActivity(new Intent(getApplicationContext(), PastorsListActivity.class));
                finish();
                return true;
            }
        });

        return true;
    }

    // searchview pastor list
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

                    // API to get Pastor's List
                    getAllAdmins();
                }
                return false;
            }


            @Override
            public boolean onQueryTextChange(String newText) {

                if(!searchStr.isEmpty()){
                    pageIndex = 1;
                    searchStr = "";
                    // API to get Pastor's List
                    getAllAdmins();
                }
                return true;
            }
        });
    }


    // API to get Pastor's List
    private void getAllAdmins() {
        JsonObject object = adminObject();
        ChurchService service = ServiceFactory.createRetrofitService(getApplicationContext(), ChurchService.class);
        mSubscription = service.getAllAdmins(object)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<GetAllAdminResponseModel>() {
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
                    public void onNext(final GetAllAdminResponseModel adminResponseModel) {

                        // to hide the Progress Dialog
                        hideProgressDialog();
                        if (adminResponseModel.getIsSuccess()) {
                            if (adminResponseModel.getListResult().isEmpty()) {
                                noRecords.setVisibility(View.VISIBLE);

                            } else {
                                noRecords.setVisibility(View.GONE);
                            }
                            BindDataListResults = (ArrayList<GetAllAdminResponseModel.ListResult>) adminResponseModel.getListResult();
                            listResults.addAll(BindDataListResults);
                            layoutManager = new LinearLayoutManager(getApplicationContext());
                            eventRecyclerView.setLayoutManager(layoutManager);
                            eventRecyclerView.setHasFixedSize(true);
                            adapter = new AdminAdapter(mContext, listResults, eventRecyclerView);
                            eventRecyclerView.setAdapter(adapter);
                            int Possition = listResults.size() - pageSize;
                            adapter.setOnCartChangedListener(PastorsListActivity.this);
                            eventRecyclerView.smoothScrollToPosition(Possition);


                            handler = new Handler();
                            adapter.setOnLoadMoreListener(new AdminAdapter.OnLoadMoreListener() {
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
                                                // to start the Progress Dialog
                                                showProgressDialog();

                                                // API to get Pastor's List
                                                getAllAdmins();
                                                adapter.notifyDataSetChanged();
                                                loading = false;
                                            }

                                            //or you can add all at once but do not forget to call mAdapter.notifyDataSetChanged();
                                        }
                                    }, 1000);

                                }
                            });


                        } else {
                            Toast.makeText(getApplicationContext(), "" + adminResponseModel.getEndUserMessage(), Toast.LENGTH_SHORT).show();
                        }


                    }
                });
    }

    /**
     * Json object of adminObject
     *
     * @return
     */

    private JsonObject adminObject() {
        GetAdminRequestModel mRequest = new GetAdminRequestModel();
        mRequest.setUserId(SharedPrefsData.getInt(PastorsListActivity.this, Constants.ID, Constants.PREF_NAME));
        mRequest.setPageIndex(pageIndex);
        mRequest.setPageSize(pageSize);
        mRequest.setUid(0);
        mRequest.setSortbyColumnName("UpdatedDate");
        mRequest.setSortDirection("desc");
        mRequest.setSearchName(searchStr);
        return new Gson().toJsonTree(mRequest).getAsJsonObject();
    }

    // Interface between adapter and class

    /*
     * passing intent value to AdminDetailsActivity
     * @param  id
     * @param  subId
     * @param  image
     * @param  church name
     * @param  author name
     */
    @Override
    public void setCartClickListener(String status, int position) {
        listPosition = position;
        if (status.equalsIgnoreCase("MainClick")) {
            if (listResults.get(position).getChurchAdmin() != null && !listResults.get(position).getChurchAdmin().equals("")) {
                SharedPrefsData.putInt(PastorsListActivity.this,  Constants.AdminId, listResults.get(position).getId(), Constants.PREF_NAME);
                startActivity(new Intent(getApplicationContext(), AdminDetailsActivity.class)
                        .putExtra("id", listResults.get(position).getId())
                        .putExtra("subId", listResults.get(position).getId())
                        .putExtra("image", listResults.get(position).getUserImage())
                        .putExtra("church name", listResults.get(position).getChurchName())
                        .putExtra("author name",listResults.get(position).getChurchAdmin())
                );
            }


        }
        subscibeval = listResults.get(position).getIsSubscribed();

        SharedPrefsData.putInt(getApplicationContext(), Constants.ISSUBSCRIBED, subscibeval, Constants.PREF_NAME);

    }

    // Interface between adapter and class

    /*
     * passing intent value to AdminDetailsActivity
     * @param  id
     * @param  image
     * @param  church name
     * @param  author name
     */
    @Override
    public void setClickListener(String status, int position) {
        listPosition = position;

        if (status.equalsIgnoreCase("MainSearch")) {
            if (listResults.get(position).getChurchAdmin() != null && !listResults.get(position).getChurchAdmin().equals("")) {
                SharedPrefsData.putInt(PastorsListActivity.this, Constants.AdminId, listResults.get(position).getId(), Constants.PREF_NAME);
                startActivity(new Intent(getApplicationContext(), AdminDetailsActivity.class)
                        .putExtra("id", listResults.get(position).getId())
                        .putExtra("image", listResults.get(position).getChurchImage())
                        .putExtra("church name", listResults.get(position).getChurchName())
                        .putExtra("author name",listResults.get(position).getChurchAdmin())
                );
            }


        }

    }
}




