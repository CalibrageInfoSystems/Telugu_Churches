package in.calibrage.teluguchurches.views.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
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
import in.calibrage.teluguchurches.views.adapter.AllChurchesAdapter;
import in.calibrage.teluguchurches.views.model.GetAllChurchesResponseModel;
import in.calibrage.teluguchurches.views.model.GetChurchesRequestModel;
import io.fabric.sdk.android.Fabric;
import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseSequence;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView;
import uk.co.deanwild.materialshowcaseview.ShowcaseConfig;


public class AllChurchesActivity extends BaseActivity implements AllChurchesAdapter.OnCartChangedListener {
    public static final String TAG = AllChurchesActivity.class.getSimpleName();
    TextView noRecords;
    private RecyclerView eventRecyclerView, eventSearchRecyclerView;
    LinearLayoutManager layoutManager;
    private AllChurchesAdapter adapter;
    private Toolbar toolbar;
    private Integer pageIndex = 1, listPosition = 0, pazeSize = 10;
    int Possition;
    private Subscription getAllChurchesSubscription;
    private ArrayList<GetAllChurchesResponseModel.ListResult> listResults = new ArrayList<>();
    private ArrayList<GetAllChurchesResponseModel.ListResult> BIndDatalistResults = new ArrayList<>();
    private Context mContext;
    private String searchStr = "";
    SearchView searchView;
    private Handler handler;
    private boolean loading = true;
    private ImageView toolbar_image;
    private static final String SHOWCASE_ID = "sequence churches";

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getApplicationContext();

        // added crash report's to mail
        Fabric.with(getApplicationContext(), new Crashlytics());
        //assining layout
        setContentView(R.layout.activity_all_churches);

        toolbar = findViewById(R.id.toolbar);
        noRecords = findViewById(R.id.no_records);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle(getString(R.string.all_churches));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
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
                startActivity(new Intent(AllChurchesActivity.this, HomeActivity.class));
            }
        });

        eventRecyclerView = findViewById(R.id.eventRecyclerView);
        eventRecyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getApplicationContext());
        eventRecyclerView.setLayoutManager(layoutManager);
        eventSearchRecyclerView = findViewById(R.id.eventsearchRecyclerView);
        eventSearchRecyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getApplicationContext());
        eventSearchRecyclerView.setLayoutManager(layoutManager);
        eventSearchRecyclerView.setVisibility(View.GONE);
        noRecords.setVisibility(View.GONE);

        RecyclerView.SmoothScroller smoothScroller = new LinearSmoothScroller(mContext) {
            @Override
            protected int getVerticalSnapPreference() {
                return LinearSmoothScroller.SNAP_TO_ANY;
            }
        };


        // to check the phone is connected to Internet or Not
        if (isOnline()) {
            // API to get church list
            getAllChurches();

            // to start the Progress Dialog
            showProgressDialog();
        } else {
            //when no internet connection
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

        // Particular church to search
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

                startActivity(new Intent(getApplicationContext(), AllChurchesActivity.class));
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

                startActivity(new Intent(getApplicationContext(), AllChurchesActivity.class));
                finish();
                return true;
            }
        });

        return true;
    }

    // Particular church to search
    private void search(SearchView searchView) {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                searchStr = query;
                if (!searchStr.isEmpty()) {
                    pageIndex = 1;
                    BIndDatalistResults = new ArrayList<>();
                    listResults = new ArrayList<>();

                    // to start the Progress Dialog
                    showProgressDialog();

                    // API to get church list
                    getAllChurches();
                }
                return false;
            }


            @Override
            public boolean onQueryTextChange(String newText) {

                if (!searchStr.isEmpty()) {
                    pageIndex = 1;
                    searchStr = "";

                    // API to get church list
                    getAllChurches();
                }

                return true;
            }
        });
    }

    // API to get church list
    private void getAllChurches() {
        JsonObject object = churchesObject();
        ChurchService service = ServiceFactory.createRetrofitService(getApplicationContext(), ChurchService.class);
        getAllChurchesSubscription = service.getAllChurches(object)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<GetAllChurchesResponseModel>() {
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
                    public void onNext(final GetAllChurchesResponseModel churchesResponseModel) {
                        // to hide the Progress Dialog
                        hideProgressDialog();
                        if (churchesResponseModel.getIsSuccess()) {

                            if (churchesResponseModel.getListResult().isEmpty()) {
                                noRecords.setVisibility(View.VISIBLE);

                            } else {
                                noRecords.setVisibility(View.GONE);
                            }
                            BIndDatalistResults = (ArrayList<GetAllChurchesResponseModel.ListResult>) churchesResponseModel.getListResult();

                            listResults.addAll(BIndDatalistResults);
                            layoutManager = new LinearLayoutManager(getApplicationContext());
                            eventRecyclerView.setLayoutManager(layoutManager);
                            adapter = new AllChurchesAdapter(mContext, listResults, eventRecyclerView);
                            eventRecyclerView.setAdapter(adapter);
                            Possition = listResults.size() - pazeSize;
                            eventRecyclerView.smoothScrollToPosition(Possition);
                            adapter.setOnCartChangedListener(AllChurchesActivity.this);
                            handler = new Handler();
                            adapter.setOnLoadMoreListener(new AllChurchesAdapter.OnLoadMoreListener() {
                                @Override
                                public void onLoadMore() {
                                    listResults.add(null);
                                    adapter.notifyItemInserted(listResults.size() - 1);

                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            loading = false;
                                            if (BIndDatalistResults.size() >= pazeSize) {
                                                listResults.remove(listResults.size() - 1);
                                                adapter.notifyItemRemoved(listResults.size());
                                                pageIndex = pageIndex + 1;

                                                // to start the Progress Dialog
                                                showProgressDialog();
                                                getAllChurches();
                                                adapter.notifyDataSetChanged();
                                                loading = false;

                                            }

                                            //or you can add all at once but do not forget to call mAdapter.notifyDataSetChanged();
                                        }
                                    }, 1000);

                                }
                            });

                        } else {
                            Toast.makeText(mContext, "" + churchesResponseModel.getEndUserMessage(), Toast.LENGTH_SHORT).show();
                        }

                        /**
                         * Showcase view for suggestions
                         */
                        presentShowcaseSequence();
                    }
                });
    }

    /**
     * Json Object of churchesObject
     *
     * @return
     */
    private JsonObject churchesObject() {
        GetChurchesRequestModel mRequest = new GetChurchesRequestModel();
        mRequest.setUserId(SharedPrefsData.getInt(AllChurchesActivity.this, Constants.ID, Constants.PREF_NAME));
        mRequest.setPageIndex(pageIndex);
        mRequest.setPageSize(pazeSize);
        mRequest.setUid(0);
        mRequest.setSortbyColumnName("UpdatedDate");
        mRequest.setSortDirection("desc");
        mRequest.setSearchName(searchStr);

        return new Gson().toJsonTree(mRequest).getAsJsonObject();
    }

    // Interface between adapter and class
    /*
      * intent value passing to ChurchDetails
      * @param  id
      *@param  name
      *@param  regNo
      *@param  address1
      *@param  address2
      *@param  landmark
      *@param  mobileNo
      *@param  email
      *@param  image
      *@param  pastor
    */
    @Override
    public void setCartClickListener(String status, int position) {
        listPosition = position;
        if (status.equalsIgnoreCase("MainClick")) {
            if (listResults.get(position).getPasterUserId() != null && !listResults.get(position).getPasterUserId().equals(""))
                startActivity(new Intent(getApplicationContext(), ChurchDetailsActivity.class)
                        .putExtra("id", listResults.get(position).getId())
                        .putExtra("name", listResults.get(position).getName())
                        .putExtra("regNo", listResults.get(position).getRegistrationNumber())
                        .putExtra("address1", listResults.get(position).getAddress1())
                        .putExtra("address2", listResults.get(position).getAddress2())
                        .putExtra("landmark", listResults.get(position).getLandMark())
                        .putExtra("mobileNo", listResults.get(position).getContactNumber())
                        .putExtra("email", listResults.get(position).getEmail())
                        .putExtra("image", listResults.get(position).getChurchImage())
                        .putExtra("pastor", listResults.get(position).getPasterUser()));


            SharedPrefsData.putInt(getApplicationContext(), "" + Constants.Churchid, listResults.get(position).getId(), Constants.PREF_NAME);
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
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
