package in.calibrage.teluguchurches.views.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.GridLayoutManager;
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

import com.crashlytics.android.Crashlytics;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;

import in.calibrage.teluguchurches.R;
import in.calibrage.teluguchurches.services.ChurchService;
import in.calibrage.teluguchurches.services.ServiceFactory;
import in.calibrage.teluguchurches.util.BaseActivity;
import in.calibrage.teluguchurches.util.Constants;
import in.calibrage.teluguchurches.util.localData.SharedPrefsData;
import in.calibrage.teluguchurches.views.adapter.ImageAdapter;
import in.calibrage.teluguchurches.views.adapter.SearchImageAdapter;
import in.calibrage.teluguchurches.views.model.GetCategory;
import in.calibrage.teluguchurches.views.model.GetCategoryRequestModel;
import io.fabric.sdk.android.Fabric;
import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseSequence;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView;
import uk.co.deanwild.materialshowcaseview.ShowcaseConfig;

public class CategoriesActivity extends BaseActivity implements ImageAdapter.OnCartChangedListener, SearchImageAdapter.OnCartChangedListener {
    TextView noRecords;
    private ImageView toolbar_image;
    RecyclerView recyclerView, categorysearchRecyclerView;
    LinearLayoutManager layoutManager;
    private Context mContext;
    private Subscription mSubscription;
    private GetCategory listResult;
    private int listPosition;
    SearchView searchView;
    private String searchStr = "";
    MenuItem search;
    private static final String SHOWCASE_ID = "sequence category";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext=getApplicationContext();

        // added crash report's to mail
        Fabric.with(getApplicationContext(), new Crashlytics());

        //assining layout
        setContentView(R.layout.categories_mainlay);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("All Categories");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(getApplicationContext(), HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

        recyclerView = findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(getApplicationContext());
        categorysearchRecyclerView = findViewById(R.id.categorysearchRecyclerView);
        noRecords = findViewById(R.id.no_records);
        categorysearchRecyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getApplicationContext());
        categorysearchRecyclerView.setLayoutManager(layoutManager);
        categorysearchRecyclerView.setVisibility(View.GONE);
        noRecords.setVisibility(View.GONE);

        toolbar_image = findViewById(R.id.toolbar_image);
        /**
         * @param OnClickListner
         */
        toolbar_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CategoriesActivity.this, HomeActivity.class));
            }
        });


        // to check the phone is connected to Internet or Not
        if (isOnline()) {

            // ApI to get Categories List
            getCategory();
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
        search = menu.findItem(R.id.action_search);

        searchView = (SearchView) MenuItemCompat.getActionView(search);
        EditText searchEditText = searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);

        // SearchView
        search(searchView);
        searchView.setIconifiedByDefault(false);
        searchView.setQueryHint(getString(R.string.search));
        // Get the search close button image view
        ImageView closeButton = searchView.findViewById(R.id.search_close_btn);

        /**
         * @param OnClickListner
         */
        closeButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Collapse the action view
                searchView.onActionViewCollapsed();
                startActivity(new Intent(getApplicationContext(), CategoriesActivity.class));
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
                startActivity(new Intent(getApplicationContext(), CategoriesActivity.class));
                finish();
                return true;
            }
        });

        /**
         * Showcase view for suggestions
         */
        presentShowcaseSequence();
        return true;

    }

    private void search(SearchView searchView) {
        //presentShowcaseSequence();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchStr = query;
                if (!searchStr.isEmpty()) {
                    listResult = new GetCategory();
                    // API to get Search Categories List
                    getCategorySearch();
                }

                return false;
            }


            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });
    }
    // API to get Search Categories List
    private void getCategorySearch() {
        JsonObject object = categoryObjectSearch();
        ChurchService service = ServiceFactory.createRetrofitService(getApplicationContext(), ChurchService.class);
        mSubscription = service.GetCategoryInfo(object)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<GetCategory>() {
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
                    public void onNext(GetCategory mResponse) {

                        // to hide the Progress Dialog
                        hideProgressDialog();
                        listResult = mResponse;
                        recyclerView.removeAllViews();
                        recyclerView.setVisibility(View.GONE);
                        categorysearchRecyclerView.setVisibility(View.VISIBLE);
                        categorysearchRecyclerView.setHasFixedSize(true);
                        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 3);
                        categorysearchRecyclerView.setLayoutManager(layoutManager);
                        SearchImageAdapter adapter = new SearchImageAdapter(getApplicationContext(), mResponse);
                        categorysearchRecyclerView.setAdapter(adapter);
                        adapter.setOnCartChangedListener(CategoriesActivity.this);
                        if (mResponse.getListResult().isEmpty()) {
                            noRecords.setVisibility(View.VISIBLE);

                        } else {
                            noRecords.setVisibility(View.GONE);
                        }

                    }


                });
    }

    // ApI to get Categories List
    private void getCategory() {

        JsonObject object = categoryObject();
        ChurchService service = ServiceFactory.createRetrofitService(getApplicationContext(), ChurchService.class);
        mSubscription = service.GetCategoryInfo(object)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<GetCategory>() {
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
                    public void onNext(GetCategory mResponse) {

                        // to hide the Progress Dialog
                        hideProgressDialog();

                        if (mResponse.getIsSuccess()){
                            listResult = mResponse;
                            recyclerView.setHasFixedSize(true);
                            RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 3);
                            recyclerView.setLayoutManager(layoutManager);
                            ImageAdapter adapter = new ImageAdapter(getApplicationContext(), mResponse);
                            recyclerView.setAdapter(adapter);
                            adapter.setOnCartChangedListener(CategoriesActivity.this);

                        }

                    }

                });
    }

    /**
     * Json object of categoryObjectSearch
     *
     * @return
     */
    private JsonObject categoryObjectSearch() {
        GetCategoryRequestModel mRequest = new GetCategoryRequestModel();
        mRequest.setActive(1);
        mRequest.setPageSize(50);
        mRequest.setPageIndex(1);
        mRequest.setSortbyColumnName("updatedDate");
        mRequest.setSortDirection("desc");
        mRequest.setSearchName(searchStr);
        return new Gson().toJsonTree(mRequest).getAsJsonObject();
    }

    /**
     * Json object of categoryObject
     *
     * @return
     */
    private JsonObject categoryObject() {
        GetCategoryRequestModel mRequest = new GetCategoryRequestModel();
        mRequest.setActive(1);
        mRequest.setPageSize(50);
        mRequest.setPageIndex(1);
        mRequest.setSortbyColumnName("updatedDate");
        mRequest.setSortDirection("desc");
        mRequest.setSearchName("");
        return new Gson().toJsonTree(mRequest).getAsJsonObject();
    }

    // Interface between adapter and class

    /*
     * passing intent value to CategoriesDetailActivity
     * @param  name
     * @param  image
     */
    @Override
    public void setCartClickListener(String status, int position) {
        listPosition = position;
        SharedPrefsData.putInt(getApplicationContext(), "" + Constants.CategoryId, listResult.getListResult().get(position).getId(), Constants.PREF_NAME);
        SharedPrefsData.putString(getApplicationContext(), "" + Constants.CategoryName, listResult.getListResult().get(position).getCategoryName(), Constants.PREF_NAME);
        if (status.equalsIgnoreCase("position")) {
            startActivity(new Intent(getApplicationContext(), CategoriesDetailActivity.class)
                    .putExtra("name", listResult.getListResult().get(position).getCategoryName())
                    .putExtra("image", listResult.getListResult().get(position).getCategoryImage()));

        }
    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(CategoriesActivity.this,HomeActivity.class);
        startActivity(intent);
        finish();
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
                        .setTarget(toolbar_image)
                        .setDismissText("GOT IT")
                        .setContentText("Click Here For Home Screen")
                        .setContentTextColor(getResources().getColor(R.color.mediumTurquoise))
                        .setDismissTextColor(getResources().getColor(R.color.white))
                        .withCircleShape()
                        .setShapePadding(100)
                        .build()
        );

        sequence.start();

    }
}
