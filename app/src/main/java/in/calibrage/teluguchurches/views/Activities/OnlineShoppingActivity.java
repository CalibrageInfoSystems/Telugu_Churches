package in.calibrage.teluguchurches.views.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import in.calibrage.teluguchurches.util.Constants;
import in.calibrage.teluguchurches.util.localData.SharedPrefsData;
import in.calibrage.teluguchurches.views.adapter.ShoppingAdapter;
import in.calibrage.teluguchurches.views.model.AllItemsResponseModel;
import in.calibrage.teluguchurches.views.model.JobDetailsRequestModel;
import io.fabric.sdk.android.Fabric;
import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class OnlineShoppingActivity extends BaseActivity implements ShoppingAdapter.OnChangedListener {
    private TextView no_items_text;
    private ImageView toolbar_image;
    LinearLayout btnLay;
    private RecyclerView notificationRecyclerView;
    private Context mContext;
    private Toolbar toolbar;
    private Integer pageIndex = 1, pageSize = 10, listPosition = 0;
    int position;
    private boolean loading = true;
    private Subscription mSubscription;
    private ShoppingAdapter shoppingAdapter;
    private ArrayList<AllItemsResponseModel.ListResult> listResults = new ArrayList<>();
    private ArrayList<AllItemsResponseModel.ListResult> BindDataListResults = new ArrayList<>();
    private Handler handler;
    SearchView searchView;
    private String searchStr = "", authorizationToken;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getApplicationContext();

        // added crash report's to mail
        Fabric.with(getApplicationContext(), new Crashlytics());

        //assining layout
        setContentView(R.layout.activity_cart);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle(getString(R.string.online_shopping));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        toolbar_image = findViewById(R.id.toolbar_image);
        toolbar_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(OnlineShoppingActivity.this, HomeActivity.class));
            }
        });
        no_items_text = findViewById(R.id.no_items_text);
        no_items_text.setVisibility(View.GONE);

        /* intializing and assigning ID's */
        initViews();
        /* Navigation's and using the views */
        setViews();
    }

    /* intializing and assigning ID's */
    private void initViews() {
        notificationRecyclerView = findViewById(R.id.notificationRecyclerView);
        btnLay = findViewById(R.id.btnLay);
        btnLay.setVisibility(View.GONE);

        // to start the Progress Dialog
        showProgressDialog();

        // API to get shopping items list
        getAllItems();
    }

    /* Navigation's and using the views */
    private void setViews() {

    }

    // API to get shopping items list
    private void getAllItems() {
        if (authorizationToken != "null" || authorizationToken != null || authorizationToken.isEmpty())
            authorizationToken = SharedPrefsData.getInstance(mContext).getStringFromSharedPrefs(Constants.Auth_Token);
        JsonObject object = getAllItemsObject();
        ChurchService service = ServiceFactory.createRetrofitService(getApplicationContext(), ChurchService.class);
        mSubscription = service.getAllItems(object, authorizationToken)
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
                    public void onNext(final AllItemsResponseModel mResponseModel) {

                        // to hide the Progress Dialog
                        hideProgressDialog();
                        if (mResponseModel.getIsSuccess()) {
                            BindDataListResults = (ArrayList<AllItemsResponseModel.ListResult>) mResponseModel.getListResult();
                            listResults.addAll(BindDataListResults);
                            LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                            notificationRecyclerView.setLayoutManager(layoutManager);
                            shoppingAdapter = new ShoppingAdapter(getApplicationContext(), listResults, notificationRecyclerView);
                            notificationRecyclerView.setAdapter(shoppingAdapter);
                            position = listResults.size() - pageSize;
                            notificationRecyclerView.smoothScrollToPosition(position);
                            shoppingAdapter.setOnChangedListener(OnlineShoppingActivity.this);
                            handler = new Handler();
                            shoppingAdapter.setOnLoadMoreListener(new ShoppingAdapter.OnLoadMoreListener() {
                                @Override
                                public void onLoadMore() {
                                    //add progress item
                                    listResults.add(null);
                                    shoppingAdapter.notifyItemInserted(listResults.size() - 1);

                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            loading = false;
                                            //remove progress item
                                            if (BindDataListResults.size() >= pageSize) {
                                                listResults.remove(listResults.size() - 1);
                                                shoppingAdapter.notifyItemRemoved(listResults.size());
                                                pageIndex = pageIndex + 1;

                                                // to start the Progress Dialog
                                                showProgressDialog();

                                                // API to get shopping items list
                                                getAllItems();
                                                shoppingAdapter.notifyDataSetChanged();
                                                loading = false;
                                            }

                                            //or you can add all at once but do not forget to call mAdapter.notifyDataSetChanged();
                                        }
                                    }, 1000);

                                }
                            });
                            if (listResults.isEmpty()) {
                                no_items_text.setVisibility(View.VISIBLE);
                                //  no_items_text.setText("");
                            } else {
                                no_items_text.setVisibility(View.GONE);
                            }
                        }

                    }
                });

    }

    /**
     * Json object of getAllItemsObject
     *
     * @return
     */
    private JsonObject getAllItemsObject() {
        JobDetailsRequestModel mRequest = new JobDetailsRequestModel();
        mRequest.setUserId(SharedPrefsData.getInt(getApplicationContext(), Constants.ID, Constants.PREF_NAME));
        mRequest.setPageIndex(pageIndex);
        mRequest.setPageSize(pageSize);
        mRequest.setSortbyColumnName("updatedDate");
        mRequest.setSortDirection("desc");
        mRequest.setSearchName(searchStr);
        return new Gson().toJsonTree(mRequest).getAsJsonObject();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main_all_churches_search, menu);
        MenuItem search = menu.findItem(R.id.action_search);

        searchView = (SearchView) MenuItemCompat.getActionView(search);
        EditText searchEditText = searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchView.setQueryHint(getString(R.string.search));

        // searchView shopping items list
        search(searchView);
        searchView.setIconifiedByDefault(false);
        searchView.setIconified(true);

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
                if (searchStr != null && !searchStr.equalsIgnoreCase("")) {
                    if (!searchStr.isEmpty()) {
                        pageIndex = 1;
                        searchStr = "";
                        BindDataListResults = new ArrayList<>();
                        listResults = new ArrayList<>();

                        // to start the Progress Dialog
                        showProgressDialog();

                        // API to get shopping items list
                        getAllItems();
                        loading = false;
                    }
                }

                return true;
            }
        });
        return true;
    }

    // searchView shopping items list
    private void search(SearchView searchView) {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

                searchStr = query;
                if (!searchStr.isEmpty()) {
                    pageIndex = 1;
                    BindDataListResults = new ArrayList<>();
                    listResults = new ArrayList<>();

                    // to start the Progress Dialog
                    showProgressDialog();

                    // API to get shopping items list
                    getAllItems();
                }
                return false;
            }


            @Override
            public boolean onQueryTextChange(String newText) {

                return true;
            }
        });
    }

    // menu option's selection
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                break;

            default:
                return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }

   // Interface between adapter and class
    /*
     * passing value to OnlineshoppingItemDetailActivity
     * @param  id
     */

    @Override
    public void setClickListener(String status, int position) {
        if (status.equalsIgnoreCase("itemClick")) {
            startActivity(new Intent(getApplicationContext(), OnlineshoppingItemDetailActivity.class)
                    .putExtra("id", listResults.get(position).getId()).putExtra("name", listResults.get(position).getName()));
        }
    }
}
