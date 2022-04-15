package in.calibrage.teluguchurches.views.Activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TextInputLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.viewpagerindicator.CirclePageIndicator;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import in.calibrage.teluguchurches.R;
import in.calibrage.teluguchurches.services.APIConstants;
import in.calibrage.teluguchurches.services.ChurchService;
import in.calibrage.teluguchurches.services.ServiceFactory;
import in.calibrage.teluguchurches.util.BaseActivity;
import in.calibrage.teluguchurches.util.CommonUtil;
import in.calibrage.teluguchurches.util.Constants;
import in.calibrage.teluguchurches.util.MyCounter;
import in.calibrage.teluguchurches.util.RefreshToken;
import in.calibrage.teluguchurches.util.localData.SharedPrefsData;
import in.calibrage.teluguchurches.views.adapter.EventPagerAdapter;
import in.calibrage.teluguchurches.views.adapter.RecyclerViewDataAdapter;
import in.calibrage.teluguchurches.views.adapter.SlidingAdapter;
import in.calibrage.teluguchurches.views.adapter.churchHomepagerAdapter;
import in.calibrage.teluguchurches.views.model.CartResponseModel;
import in.calibrage.teluguchurches.views.model.ChangePasswordRequestModel;
import in.calibrage.teluguchurches.views.model.ChangePasswordResponseModel;
import in.calibrage.teluguchurches.views.model.GetAllBannersId;
import in.calibrage.teluguchurches.views.model.GetAllChurchesRequestModel;
import in.calibrage.teluguchurches.views.model.GetAllChurchesResponseModel;
import in.calibrage.teluguchurches.views.model.GetCategory;
import in.calibrage.teluguchurches.views.model.GetCategoryRequestModel;
import in.calibrage.teluguchurches.views.model.GetChurchesRequestModel;
import in.calibrage.teluguchurches.views.model.GetNotificationResponseModel;
import in.calibrage.teluguchurches.views.model.GetRefreshTokenResponseModel;
import in.calibrage.teluguchurches.views.model.GetUpComingEvents;
import in.calibrage.teluguchurches.views.model.NotiifcationRequestModel;
import in.calibrage.teluguchurches.views.model.RefreshTokenRequestModel;
import io.fabric.sdk.android.Fabric;
import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseSequence;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView;
import uk.co.deanwild.materialshowcaseview.ShowcaseConfig;

import static in.calibrage.teluguchurches.util.CommonUtil.updateResources;
import static in.calibrage.teluguchurches.util.Constants.PREF_NAME;

public class HomeActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener, RecyclerViewDataAdapter.OnCartChangedListener,
        EventPagerAdapter.OnCartChangedListener, churchHomepagerAdapter.OnCartChangedListener {

    private EditText oldPasswordEdt, newPasswordEdt, confirmPasswordEdt;
    private TextView itemTitle, itemTitleEvents, count_text, noti_count_text, dialogMessage;
    private Button ok_btn, cancel_btn, languageBtn, btnMore, btnMoreChurch;
    private ImageView cart_img, notification_img;
    private TextInputLayout oldPasswordEdtLay, newPasswordInput, confirmPasswordInput;
    private RelativeLayout categorytitleLay, churchtitleLay, content_main_lyt;
    private LinearLayout pagerLay, pagerHomeLay;
    private SwipeRefreshLayout swipeToRefresh;
    private NavigationView navigationView;
    private RecyclerView recyclerViewPager;
    RecyclerViewDataAdapter adapter;
    private EventPagerAdapter eventPagerAdapter;
    churchHomepagerAdapter adapter_churches_home;
    LinearLayoutManager layoutManager;
    ViewPager pager, eventPager;
    private Configuration config;
    private Context mContext;
    private int value, position = 0;
    Integer islogin;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    private static int currentBannerPage = 0;
    private static int NUM_BANNERPAGES = 0;
    final int duration = 10;
    final int pixelsToMove = 30;
    final int durationchurch = 5000;
    final int pixelsToMovechurch = 30;
    private Integer pageIndex = 1, listPosition = 0, pazeSize = 100, Position, listPositionchurch = 0;
    private Subscription mSubscription;
    GetCategory listResponse;
    GetAllChurchesResponseModel listResponsechurch;
    GetUpComingEvents eventResponse;
    private CartResponseModel mCartResponseModel;
    private GetNotificationResponseModel mNotificationResponseModel;
    private DrawerLayout drawer;
    private AlertDialog alert, alertDialog;
    ArrayList<String> imageArray = new ArrayList<String>();
    private ArrayList<Integer> imagesArrayView;
    private ArrayList<GetNotificationResponseModel.NotificationsList> listResults = new ArrayList<>();
    private ArrayList<GetNotificationResponseModel.NotificationsList> BIndDatalistResults = new ArrayList<>();
    private ArrayList<GetAllChurchesResponseModel.ListResult> listResultschurch = new ArrayList<>();
    private ArrayList<GetAllChurchesResponseModel.ListResult> BIndDatalistResultschurch = new ArrayList<>();
    private MenuItem cart;
    private Menu menu;
    RefreshToken broadCastReceiver = new RefreshToken();
    private IntentFilter mIntentFilter;
    private boolean loading = true;
    private Handler handler;
    private static final String SHOWCASE_ID = "sequence home";
    private String currentLanguage, date, sevenDays, authorizationToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getApplicationContext();

        // added crash report's to mail
        Fabric.with(HomeActivity.this, new Crashlytics());

        //assining layout
        setContentView(R.layout.activity_home2);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.app_name));
        setSupportActionBar(toolbar);
        date = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, 7);
        Date todate1 = cal.getTime();
        sevenDays = dateFormat.format(todate1);

        value = SharedPrefsData.getInt(HomeActivity.this, Constants.ISLOGIN, Constants.PREF_NAME);

        mIntentFilter = new IntentFilter("action_name");
        this.registerReceiver(broadCastReceiver, mIntentFilter);
        if (value != 0) {
            if (SharedPrefsData.getInstance(this).getStringFromSharedPrefs("RefreshToken") != null) {

                // API to get RefreshToken
                getRefreshToken();
            }
        }

        pager = findViewById(R.id.pager);
        eventPager = findViewById(R.id.eventPager);
        languageBtn = findViewById(R.id.languageBtn);
        pagerLay = findViewById(R.id.pagerLay);
        navigationView = findViewById(R.id.nav_view);
        pagerLay.setVisibility(View.VISIBLE);
        categorytitleLay = findViewById(R.id.categorytitleLay);
        churchtitleLay = findViewById(R.id.churchtitleLay);
        pagerHomeLay = findViewById(R.id.pagerHomeLay);
        pagerHomeLay.setVisibility(View.GONE);
        content_main_lyt = findViewById(R.id.content_main_lyt);
        btnMore = findViewById(R.id.btnMore);
        btnMoreChurch = findViewById(R.id.btnMoreChurch);
        itemTitle = findViewById(R.id.itemTitle);
        itemTitleEvents = findViewById(R.id.itemTitleEvents);
        itemTitleEvents.setVisibility(View.GONE);
        itemTitle.setVisibility(View.VISIBLE);
        btnMore.setVisibility(View.VISIBLE);


/**
 * @param OnClickListner
 */
        btnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //  mTourGuide.cleanUp();
                Intent intent = new Intent(HomeActivity.this, CategoriesActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);


            }
        });


/**
 * @param OnClickListner
 */
        btnMoreChurch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //  mTourGuide.cleanUp();
                Intent intent = new Intent(HomeActivity.this, AllChurchesActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);


            }
        });

        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        Menu menuNav = navigationView.getMenu();
        MenuItem nav_item1 = menuNav.findItem(R.id.empty_one);
        nav_item1.setEnabled(false);
        MenuItem nav_item2 = menuNav.findItem(R.id.empty_two);
        nav_item2.setEnabled(false);

        // API to get  Unread notifications count
        getNotification();
        swipeToRefresh = findViewById(R.id.swipeToRefresh);
        swipeToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                // to check the phone is connected to Internet or Not
                if (isOnline()) {
                    itemTitle.setVisibility(View.VISIBLE);
                    btnMore.setVisibility(View.VISIBLE);
                    content_main_lyt.setVisibility(View.VISIBLE);

                    // API to Get Banners
                    getBanners();
                    // API to get Upcoming Events list
                    getUpcomingEventsInfo();

                    // API to get Categories List
                    getCategory();

                    // API to get Churche's list
                    getAllChurches();

                    // API to get  Unread notifications count
                    getNotification();
//            getCartItemsCount();
                } else {
                    CommonUtil.customToast(getString(R.string.no_internet), getApplicationContext());
                    itemTitle.setVisibility(View.GONE);
                    btnMore.setVisibility(View.GONE);
                    churchtitleLay.setVisibility(View.GONE);
                    categorytitleLay.setVisibility(View.GONE);
                    content_main_lyt.setVisibility(View.GONE);
                    swipeToRefresh.setRefreshing(false);

                }
            }


        });
        // to check the phone is connected to Internet or Not
        if (isOnline()) {
            itemTitle.setVisibility(View.VISIBLE);
            btnMore.setVisibility(View.VISIBLE);

            // API to Get Banners
            getBanners();

            // API to get Upcoming Events list
            getUpcomingEventsInfo();

            // API to get Categories List
            getCategory();

            // API to get Churche's list
            getAllChurches();

            // API to get  Unread notifications count
            getNotification();
//            getCartItemsCount();
        } else {

            itemTitle.setVisibility(View.GONE);
            btnMore.setVisibility(View.GONE);
            churchtitleLay.setVisibility(View.GONE);
            categorytitleLay.setVisibility(View.GONE);
            CommonUtil.customToast(getString(R.string.no_internet), getApplicationContext());
            // showToastLong(getString(R.string.no_internet));
        }


        islogin = SharedPrefsData.getInt(getApplicationContext(), Constants.ISLOGIN, PREF_NAME);
        if (islogin != 1) {
            // if user Alredy login
            navigationView.getMenu().findItem(R.id.nav_profile).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_logout).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_login).setVisible(true);
            //    navigationView.getMenu().findItem(R.id.nav_signUp).setVisible(true);
            navigationView.getMenu().findItem(R.id.nav_change_password).setVisible(false);
        } else {
            // User Not yet Login OR pref Null
            navigationView.getMenu().findItem(R.id.nav_profile).setVisible(true);
            navigationView.getMenu().findItem(R.id.nav_logout).setVisible(true);
            navigationView.getMenu().findItem(R.id.nav_login).setVisible(false);
            //      navigationView.getMenu().findItem(R.id.nav_signUp).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_change_password).setVisible(true);
        }


/**
 * @param OnClickListner
 */
        languageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // popupdialog to show message to select choose language
                selectLanguage();
            }
        });

    }


    // API to Get Banners
    private void getBanners() {


        ChurchService service = ServiceFactory.createRetrofitService(getApplicationContext(), ChurchService.class);
        String url = APIConstants.GetAllBannersById;
        mSubscription = service.GetAllBannerId(url)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<GetAllBannersId>() {
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
                    public void onNext(GetAllBannersId mResponse) {

                        for (int i = 0; i < mResponse.getListResult().size(); i++)
                            imageArray.add(mResponse.getListResult().get(i).getBannerImage());
                        pager.setAdapter(new SlidingAdapter(getApplicationContext(), mResponse));

                        final CirclePageIndicator indicator = findViewById(R.id.indicator);
                        indicator.setViewPager(pager);
                        final float density = getResources().getDisplayMetrics().density;
                        indicator.setRadius(5 * density);
                        NUM_BANNERPAGES = mResponse.getListResult().size();

                        final Handler handler = new Handler();
                        final Runnable runnable = new Runnable() {
                            @Override
                            public void run() {

                                if (currentBannerPage == NUM_BANNERPAGES) {
                                    currentBannerPage = 0;
                                }
                                pager.setCurrentItem(currentBannerPage++, true);
                            }
                        };
                        Timer swipeTimer = new Timer();
                        swipeTimer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                handler.post(runnable);
                            }
                        }, 5000, 6000);


                        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

                            @Override
                            public void onPageSelected(int position) {
                                currentBannerPage = position;
                            }

                            @Override
                            public void onPageScrolled(int pos, float arg1, int arg2) {
                            }

                            @Override
                            public void onPageScrollStateChanged(int pos) {
                            }
                        });

                    }
                });
    }

    // API to get Upcoming Events list
    private void getUpcomingEventsInfo() {
        JsonObject object = upComingObject();
        ChurchService service = ServiceFactory.createRetrofitService(getApplicationContext(), ChurchService.class);
        mSubscription = service.GetUpcomingEventsInfo(object)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<GetUpComingEvents>() {
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
                    public void onNext(GetUpComingEvents mResponse) {
                        if (mResponse.getListResult().isEmpty()) {
                            eventPager.setVisibility(View.GONE);
                        } else {
                            itemTitleEvents.setVisibility(View.VISIBLE);
                            eventPager.setVisibility(View.VISIBLE);
                        }
                        if (mResponse.getIsSuccess()) {
                            eventResponse = mResponse;
                            for (int i = 0; i < mResponse.getListResult().size(); i++)
                                eventPagerAdapter = new EventPagerAdapter(getApplicationContext(), mResponse);
                            eventPager.setAdapter(eventPagerAdapter);
                            eventPagerAdapter.setOnCartChangedListener(HomeActivity.this);


                            NUM_PAGES = mResponse.getListResult().size();

                            final Handler handler = new Handler();
                            final Runnable runnable = new Runnable() {
                                @Override
                                public void run() {

                                    if (currentPage == NUM_PAGES) {
                                        currentPage = 0;
                                    }
                                    eventPager.setCurrentItem(currentPage++, true);
                                }
                            };
                            Timer swipeTimer = new Timer();
                            swipeTimer.schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    handler.post(runnable);
                                }
                            }, 5000, 4000);

                        }

                    }

                    private final Handler mHandler = new Handler(Looper.getMainLooper());
                    private final Runnable SCROLLING_RUNNABLE = new Runnable() {

                        @Override
                        public void run() {

                            recyclerViewPager.smoothScrollBy(pixelsToMove, 0);
                            mHandler.postDelayed(this, duration);
                        }
                    };

                });
    }

    /**
     * Json Object of upComingObject
     *
     * @return
     */
    private JsonObject upComingObject() {
        GetAllChurchesRequestModel mRequest = new GetAllChurchesRequestModel();
        mRequest.setFromDate(date);
        mRequest.setToDate(sevenDays);
        return new Gson().toJsonTree(mRequest).getAsJsonObject();
    }

    // API to get Categories List
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
                        if (mResponse.getIsSuccess()) {
                            if (mResponse.getListResult().isEmpty()) {
                                categorytitleLay.setVisibility(View.GONE);
                            } else {
                                categorytitleLay.setVisibility(View.VISIBLE);
                            }
                            listResponse = mResponse;
                            RecyclerView my_recycler_view = findViewById(R.id.recyclerView);
                            my_recycler_view.setHasFixedSize(true);
                            adapter = new RecyclerViewDataAdapter(getApplicationContext(), mResponse);
                            my_recycler_view.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
                            my_recycler_view.setAdapter(adapter);
                            adapter.setOnCartChangedListener(HomeActivity.this);
                            swipeToRefresh.setRefreshing(false);


                        }

                        /**
                         * Showcase view for suggestions
                         */
                        presentShowcaseSequence();
                    }

                });

    }
    /**
     * Json Object of categoryObject
     *
     * @return
     */
    private JsonObject categoryObject() {
        GetCategoryRequestModel mRequest = new GetCategoryRequestModel();
        mRequest.setActive(1);
        mRequest.setPageSize(50);
        mRequest.setPageIndex(1);
        mRequest.setSortbyColumnName("");
        mRequest.setSortDirection("desc");
        mRequest.setSearchName("");
        return new Gson().toJsonTree(mRequest).getAsJsonObject();
    }

    // API to get Churche's list
    private void getAllChurches() {
        JsonObject object = churchesObject();
        ChurchService service = ServiceFactory.createRetrofitService(getApplicationContext(), ChurchService.class);
        mSubscription = service.getAllChurches(object)
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
                    public void onNext(GetAllChurchesResponseModel churchesResponseModel) {
                        // to hide the Progress Dialog
                        hideProgressDialog();
                        if (churchesResponseModel.getIsSuccess()) {

                            if (churchesResponseModel.getListResult().isEmpty()) {
                                churchtitleLay.setVisibility(View.GONE);

                            } else {
                                churchtitleLay.setVisibility(View.VISIBLE);
                            }
                            BIndDatalistResultschurch = (ArrayList<GetAllChurchesResponseModel.ListResult>) churchesResponseModel.getListResult();
                            listResultschurch.addAll(BIndDatalistResultschurch);
                            layoutManager = new LinearLayoutManager(getApplicationContext());
                            final RecyclerView recycler_view_church = findViewById(R.id.recyclerViewChurch);
                            recycler_view_church.setHasFixedSize(true);
                            adapter_churches_home = new churchHomepagerAdapter(mContext, listResultschurch, recycler_view_church);
                            recycler_view_church.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
                            recycler_view_church.setAdapter(adapter_churches_home);
                            adapter_churches_home.setCartClickListenerchurh(HomeActivity.this);
                            swipeToRefresh.setRefreshing(false);
                            if (churchesResponseModel.getListResult().size() >= 1) {
                                final Handler mHandler = new Handler(Looper.getMainLooper());
                                final Runnable SCROLLING_RUNNABLE = new Runnable() {

                                    @Override
                                    public void run() {
                                        if (position == listResultschurch.size()) {
                                            position = 0;
                                            position = 0;
                                        } else {
                                            position++;
                                        }
                                        recycler_view_church.getLayoutManager().scrollToPosition(position);
                                        recycler_view_church.smoothScrollBy(pixelsToMovechurch, 0);
                                        mHandler.postDelayed(this, durationchurch);
                                    }
                                };

                                mHandler.postDelayed(SCROLLING_RUNNABLE, durationchurch);
                            }


                        } else {
                            Toast.makeText(mContext, "" + churchesResponseModel.getEndUserMessage(), Toast.LENGTH_SHORT).show();
                        }


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
        mRequest.setUserId(SharedPrefsData.getInt(HomeActivity.this, Constants.ID, Constants.PREF_NAME));
        mRequest.setPageIndex(1);
        mRequest.setPageSize(50);
        mRequest.setUid(0);
        mRequest.setSortbyColumnName(" ");
        mRequest.setSortDirection("desc");
        mRequest.setSearchName(" ");
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
    public void setCartClickListenerchurh(String status, int position) {
        listPositionchurch = position;
        if (status.equalsIgnoreCase("MainClick")) {
            if (listResultschurch.get(position).getPasterUserId() != null && !listResultschurch.get(position).getPasterUserId().equals(""))
                startActivity(new Intent(getApplicationContext(), ChurchDetailsActivity.class)
                        .putExtra("id", listResultschurch.get(position).getId())
                        .putExtra("name", listResultschurch.get(position).getName())
                        .putExtra("regNo", listResultschurch.get(position).getRegistrationNumber())
                        .putExtra("address1", listResultschurch.get(position).getAddress1())
                        .putExtra("address2", listResultschurch.get(position).getAddress2())
                        .putExtra("landmark", listResultschurch.get(position).getLandMark())
                        .putExtra("mobileNo", listResultschurch.get(position).getContactNumber())
                        .putExtra("email", listResultschurch.get(position).getEmail())
                        .putExtra("image", listResultschurch.get(position).getChurchImage())
                        .putExtra("pastor", listResultschurch.get(position).getPasterUser()));

            // SharedPrefsData.putInt(getApplicationContext(), Constants.ID, Integer.parseInt("" + listResults.get(position).getId()), Constants.PREF_NAME);
            SharedPrefsData.putInt(getApplicationContext(), "" + Constants.Churchid, listResultschurch.get(position).getId(), Constants.PREF_NAME);
        }
    }

    // popupdialog to show message to select choose language
    private void selectLanguage() {
        final Dialog dialog = new Dialog(HomeActivity.this);
        dialog.setContentView(R.layout.dialog_select_language);
        dialog.setTitle("");
        // set the custom forgotPasswordDialog components - text, image and button
        final TextView rbEng = dialog.findViewById(R.id.rbEng);
        final TextView rbTelugu = dialog.findViewById(R.id.rbTelugu);


/**
 * @param OnClickListner
 */
        rbEng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * "en" is the localization code for our default English language.
                 */
                updateResources(HomeActivity.this, "en-US");
                SharedPrefsData.getInstance(HomeActivity.this).updateIntValue(HomeActivity.this, "lang", 1);
                Intent refresh = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(refresh);
                finish();
                dialog.dismiss();
            }
        });

/**
 * @param OnClickListner
 */
        rbTelugu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * "te" is the localization code for our default Telugu language.
                 */
                updateResources(HomeActivity.this, "te");
                SharedPrefsData.getInstance(HomeActivity.this).updateIntValue(HomeActivity.this, "lang", 2);
                Intent refresh = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(refresh);
                finish();
                dialog.dismiss();
            }
        });


        dialog.show();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            moveTaskToBack(true);
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.categories) {
            startActivity(new Intent(this, CategoriesActivity.class));
            // Handle the camera action
        } else if (id == R.id.all_churches) {
            startActivity(new Intent(this, AllChurchesActivity.class));
        } else if (id == R.id.event_title) {
            startActivity(new Intent(this, EventsTabActivity.class));
        } else if (id == R.id.author_title) {
            startActivity(new Intent(this, PastorsListActivity.class));
        } else if (id == R.id.bible_study_telugu) {
            startActivity(new Intent(this, BibleStudyActivity.class));
        } else if (id == R.id.bible_study_test) {
            startActivity(new Intent(this, BibleStudyEnglish.class));
        } else if (id == R.id.nav_login) {
            Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_logout) {

            //popupdialog to show message to logout the application
            logOutDialog();
        } else if (id == R.id.nav_profile) {
            Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_change_password) {
            if (SharedPrefsData.getString(getApplicationContext(), Constants.UserId, PREF_NAME) != null && !SharedPrefsData.getString(getApplicationContext(), Constants.UserId, PREF_NAME).equalsIgnoreCase("")) {

                // popupdialog to show message to change password
                showDialog();
            }
        } else if (id == R.id.notification_menu) {
            if (SharedPrefsData.getString(getApplicationContext(), Constants.UserId, PREF_NAME) != null && !SharedPrefsData.getString(getApplicationContext(), Constants.UserId, PREF_NAME).equalsIgnoreCase("")) {
                startActivity(new Intent(this, NotificationScreenActivity.class));
            } else {

                Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        } else if (id == R.id.careers) {
            if (SharedPrefsData.getString(getApplicationContext(), Constants.UserId, PREF_NAME) != null && !SharedPrefsData.getString(getApplicationContext(), Constants.UserId, PREF_NAME).equalsIgnoreCase("")) {
                Intent intent = new Intent(this, CareersActivity.class);
                startActivity(intent);
                finish();
            } else {

                Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }

        } else if (id == R.id.onlinedshopping) {
            if (SharedPrefsData.getString(getApplicationContext(), Constants.UserId, PREF_NAME) != null && !SharedPrefsData.getString(getApplicationContext(), Constants.UserId, PREF_NAME).equalsIgnoreCase("")) {
                Intent intent = new Intent(this, OnlineShoppingActivity.class);
                startActivity(intent);
            } else {

                Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }

        } else if (id == R.id.help) {
            Intent intent = new Intent(HomeActivity.this, HelpActivity.class);
            startActivity(intent);

        } else if (id == R.id.contact) {
            Intent intent = new Intent(HomeActivity.this, ContactUsActivity.class);
            startActivity(intent);
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //popupdialog to show message to logout the application
    private void logOutDialog() {
        LayoutInflater layoutInflater = LayoutInflater.from(HomeActivity.this);
        View dialogRootView = layoutInflater.inflate(R.layout.dialog_logout, null);
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(HomeActivity.this);
        ok_btn =dialogRootView.findViewById(R.id.ok_btn);
        cancel_btn = dialogRootView.findViewById(R.id.cancel_btn);
        dialogMessage =dialogRootView.findViewById(R.id.dialogMessage);
        dialogMessage.setText(getString(R.string.alert_logout));

        alertDialogBuilder.setView(dialogRootView);


/**
 * @param OnClickListner
 */
        ok_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getApplicationContext().getSharedPreferences(PREF_NAME, 0).edit().clear().commit();
                updateResources(getApplicationContext(), "en-US");
                SharedPrefsData.putInt(getApplicationContext(), Constants.ISLOGIN, 0, PREF_NAME);
                SharedPrefsData.getInstance(getApplicationContext()).ClearData(getApplicationContext());
                Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                // startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                navigationView.getMenu().findItem(R.id.nav_profile).setVisible(false);
                navigationView.getMenu().findItem(R.id.nav_login).setVisible(true);
                navigationView.getMenu().findItem(R.id.nav_logout).setVisible(false);
                //navigationView.getMenu().findItem(R.id.nav_signUp).setVisible(true);
                navigationView.getMenu().findItem(R.id.nav_change_password).setVisible(false);
                finish();
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


    // popupdialog to show message to change password
    public void showDialog() {
        LayoutInflater layoutInflater = LayoutInflater.from(HomeActivity.this);
        View dialogRootView = layoutInflater.inflate(R.layout.dialog_change_password, null);
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(HomeActivity.this);

        oldPasswordEdt = dialogRootView.findViewById(R.id.oldPasswordEdt);
        newPasswordEdt = dialogRootView.findViewById(R.id.newPasswordEdt);
        confirmPasswordEdt = dialogRootView.findViewById(R.id.confirmPasswordEdt);
        Button confirmBtn = dialogRootView.findViewById(R.id.confirmBtn);
        Button cancelBtn = dialogRootView.findViewById(R.id.cancelBtn);
        oldPasswordEdtLay = dialogRootView.findViewById(R.id.oldPasswordEdtLay);
        newPasswordInput = dialogRootView.findViewById(R.id.newPasswordInput);
        confirmPasswordInput = dialogRootView.findViewById(R.id.confirmPasswordInput);
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

                //validations for all fields
                if (validatePassword()) {

                    // to start the Progress Dialog
                    showProgressDialog();
                    //API to get change Password
                    changePassword();

                }


            }
        });

/**
 * @param OnClickListner
 */
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alert.dismiss();
            }
        });

        alert = alertDialogBuilder.create();
        alert.show();
    }

    //validations for all fields
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
    /**
     * isValid Password or not
     *
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

    //API to get change Password
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
                        // to hide the Progress Dialog
                        hideProgressDialog();
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
                            // to hide the Progress Dialog
                            hideProgressDialog();
                            CommonUtil.customToast(registerResponse.getEndUserMessage(), HomeActivity.this);

                            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                        } else {
                            // to hide the Progress Dialog
                            hideProgressDialog();
                            CommonUtil.customToast("" + registerResponse.getEndUserMessage(), HomeActivity.this);

                        }

                    }
                });
    }


    /**
     * Json Object of changePasswordObject
     *
     * @return
     */
    private JsonObject changePasswordObject() {
        ChangePasswordRequestModel mRequest = new ChangePasswordRequestModel();
        mRequest.setUserId("" + SharedPrefsData.getString(HomeActivity.this, Constants.UserId, PREF_NAME));
        mRequest.setOldPassword(oldPasswordEdt.getText().toString());
        mRequest.setNewPassword(newPasswordEdt.getText().toString());
        mRequest.setConfirmPassword(confirmPasswordEdt.getText().toString());
        return new Gson().toJsonTree(mRequest).getAsJsonObject();
    }

    // creating option menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_cart, menu);
        // Create your menu...
        this.menu = menu;
        MenuItem item = menu.findItem(R.id.cart_toolbar);
        MenuItemCompat.setActionView(item, R.layout.feed_update_count);
        RelativeLayout cartCount = (RelativeLayout) MenuItemCompat.getActionView(item);
        count_text = cartCount.findViewById(R.id.count_text);
        cart_img = cartCount.findViewById(R.id.cart_img);
        if (mCartResponseModel != null)
            count_text.setText("" + mCartResponseModel.getCount());

        MenuItem notifyItem = menu.findItem(R.id.notification_toolbar);
        MenuItemCompat.setActionView(notifyItem, R.layout.notification_count);
        RelativeLayout notifCount = (RelativeLayout) MenuItemCompat.getActionView(notifyItem);
        noti_count_text = notifCount.findViewById(R.id.noti_count_text);
        notification_img = notifCount.findViewById(R.id.notification_img);
        if (mNotificationResponseModel != null) {
            noti_count_text.setText("" + mNotificationResponseModel.getResult().getUnreadCount());
        }


/**
 * @param OnClickListner
 */
        cart_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (SharedPrefsData.getString(getApplicationContext(), Constants.UserId, PREF_NAME) != null && !SharedPrefsData.getString(getApplicationContext(), Constants.UserId, PREF_NAME).equalsIgnoreCase("")) {
                    Intent intent = new Intent(HomeActivity.this, CartActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                } else {
                    // CommonUtil.customToast("Please login", HomeActivity.this);
                    Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }
            }
        });

/**
 * @param OnClickListner
 */
        notification_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SharedPrefsData.getString(getApplicationContext(), Constants.UserId, PREF_NAME) != null && !SharedPrefsData.getString(getApplicationContext(), Constants.UserId, PREF_NAME).equalsIgnoreCase("")) {
                    Intent intent = new Intent(HomeActivity.this, NotificationScreenActivity.class);
                    startActivity(intent);
                } else {
                    // CommonUtil.customToast("Please login", HomeActivity.this);
                    Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // call ActionBarDrawerToggle.onOptionsItemSelected(), if it returns true
        switch (item.getItemId()) {

            // Respond to the action bar's cart toolbarbutton
            case R.id.cart_toolbar:
                if (SharedPrefsData.getString(getApplicationContext(), Constants.UserId, PREF_NAME) != null && !SharedPrefsData.getString(getApplicationContext(), Constants.UserId, PREF_NAME).equalsIgnoreCase("")) {
                    Intent intent = new Intent(this, CartActivity.class);
                    startActivity(intent);
                } else {
                    CommonUtil.customToast("Please login", HomeActivity.this);
                    Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }
                break;

            default:
                return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }

    // API to get added cart items count
    private void getCartItemsCount() {
        if (authorizationToken != "null" || authorizationToken != null || authorizationToken.isEmpty())
            authorizationToken = SharedPrefsData.getInstance(mContext).getStringFromSharedPrefs(Constants.Auth_Token);
        ChurchService service = ServiceFactory.createRetrofitService(getApplicationContext(), ChurchService.class);
        String url = APIConstants.CartItemsCount + "" + SharedPrefsData.getInt(getApplicationContext(), Constants.ID, Constants.PREF_NAME);
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
                        mCartResponseModel = mResponse;
                        count_text.setText("" + mCartResponseModel.getCount());

                    }

                });
    }

    // API to get  Unread notifications count
    private void getNotification() {
        if (authorizationToken != "null" || authorizationToken != null || authorizationToken.isEmpty())
            authorizationToken = SharedPrefsData.getInstance(mContext).getStringFromSharedPrefs(Constants.Auth_Token);
        JsonObject object = notificationObject();
        ChurchService service = ServiceFactory.createRetrofitService(getApplicationContext(), ChurchService.class);
        mSubscription = service.GetUnReadNotification(object, authorizationToken)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<GetNotificationResponseModel>() {
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
                    public void onNext(GetNotificationResponseModel mResponse) {

                        // to hide the Progress Dialog
                        hideProgressDialog();
                        mNotificationResponseModel = mResponse;
                        noti_count_text.setText(" " + mNotificationResponseModel.getResult().getUnreadCount());



                    }

                });
    }

    /**
     * Json Object of notificationObject
     *
     * @return
     */
    private JsonObject notificationObject() {
        NotiifcationRequestModel mRequest = new NotiifcationRequestModel();
        mRequest.setUserId(SharedPrefsData.getInt(getApplicationContext(), Constants.ID, Constants.PREF_NAME));
        mRequest.setSortbyColumnName("UpdatedDate");
        mRequest.setSortDirection("desc");
        mRequest.setPageIndex(pageIndex);
        mRequest.setPageSize(pazeSize);
        return new Gson().toJsonTree(mRequest).getAsJsonObject();
    }

    // Interface between adapter and class

    /*
     * SharedPref value
        * @param  CategoryId
        *@param  CategoryName
     * Intent value are passing to CategoriesDetailActivity
       * @param name
       * @param image
   */

    /*
     * SharedPref value
        * @param  eventId
     * Intent value are passing to EventDetailTabActivity
       * @param eventId
       * @param eventName
       * @param image
     */
    @Override
    public void setCartClickListener(String status, int position) {

        if (status.equalsIgnoreCase("position")) {

            SharedPrefsData.putInt(getApplicationContext(), "" + Constants.CategoryId, listResponse.getListResult().get(position).getId(), PREF_NAME);
            SharedPrefsData.putString(getApplicationContext(), "" + Constants.CategoryName, listResponse.getListResult().get(position).getCategoryName(), PREF_NAME);
            startActivity(new Intent(getApplicationContext(), CategoriesDetailActivity.class)
                    .putExtra("name", listResponse.getListResult().get(position).getCategoryName())
                    .putExtra("image", listResponse.getListResult().get(position).getCategoryImage()));
        } else if (status.equalsIgnoreCase("event")) {
            if (eventResponse.getListResult().get(position).getId() != null && !eventResponse.getListResult().get(position).getId().equals("")) {

                SharedPrefsData.putInt(HomeActivity.this, "" + Constants.eventId, eventResponse.getListResult().get(position).getId(), PREF_NAME);
                startActivity(new Intent(HomeActivity.this, EventDetailTabActivity.class)
                        .putExtra("eventId", eventResponse.getListResult().get(position).getId())
                        .putExtra("eventName", eventResponse.getListResult().get(position).getTitle())
                        .putExtra("image", eventResponse.getListResult().get(position).getEventImage()));
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        // API to get added cart items count
        getCartItemsCount();

        // API to get  Unread notifications count
        getNotification();

        // API to Get Banners
        getBanners();

    }

    // API to get RefreshToken
    private void getRefreshToken() {
        if (CommonUtil.isNetworkAvailable(mContext)) {
            JsonObject object = refreshObject();
            ChurchService service = ServiceFactory.createRetrofitService(mContext, ChurchService.class);
            mSubscription = service.getRefreshToken(object)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<GetRefreshTokenResponseModel>() {
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
                        public void onNext(GetRefreshTokenResponseModel refreshResponseModel) {
                            //  hideProgressDialog();
                            if (refreshResponseModel.getIsSuccess()) {
                                SharedPrefsData.getInstance(getApplicationContext()).updateStringValue(getApplicationContext(), Constants.Token_Type, refreshResponseModel.getResult().getTokenType());
                                SharedPrefsData.getInstance(getApplicationContext()).updateStringValue(getApplicationContext(), Constants.Refresh_Token, refreshResponseModel.getResult().getRefreshToken());
                                SharedPrefsData.getInstance(getApplicationContext()).updateStringValue(getApplicationContext(), Constants.Access_Token, refreshResponseModel.getResult().getAccessToken());
                                SharedPrefsData.getInstance(getApplicationContext()).updateStringValue(getApplicationContext(), Constants.Auth_Token,
                                        refreshResponseModel.getResult().getTokenType() + " " + refreshResponseModel.getResult().getAccessToken());
                                SharedPrefsData.putInt(mContext, "expires_in", refreshResponseModel.getResult().getExpiresIn(), Constants.PREF_NAME);
                                if (refreshResponseModel.getEndUserMessage().equalsIgnoreCase("Invalid Token")) {
                                    CommonUtil.customToast(refreshResponseModel.getEndUserMessage(), mContext);
                                }

                                if (CommonUtil.timer != null)
                                    CommonUtil.timer.cancel();

                                CommonUtil.timer = new MyCounter(refreshResponseModel.getResult().getExpiresIn() * 1000, 1000, HomeActivity.this);


                                CommonUtil.timer.start();

                            }
                        }
                    });
        }
    }

    /**
     * Json Object of refreshObject
     *
     * @return
     */
    private JsonObject refreshObject() {
        RefreshTokenRequestModel requestModel = new RefreshTokenRequestModel();

        requestModel.setClientId(Constants.ClientId);
        requestModel.setClientSecret(Constants.ClientSecret);
        requestModel.setRefreshToken(SharedPrefsData.getInstance(HomeActivity.this).getStringFromSharedPrefs("RefreshToken"));
        return new Gson().toJsonTree(requestModel).getAsJsonObject();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (broadCastReceiver != null) {
            unregisterReceiver(broadCastReceiver);
            broadCastReceiver = null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mSubscription != null) {
            mSubscription.unsubscribe();
        }
    }

    /**
     * Showcase view for suggestions
     */
    private void presentShowcaseSequence() {

        ShowcaseConfig config = new ShowcaseConfig();
        config.setDelay(200);

        MaterialShowcaseSequence sequence = new MaterialShowcaseSequence(this, SHOWCASE_ID);

        sequence.setOnItemShownListener(new MaterialShowcaseSequence.OnSequenceItemShownListener() {

            @Override
            public void onShow(MaterialShowcaseView itemView, int position) {

            }
        });

        sequence.setConfig(config);

        sequence.addSequenceItem(
                new MaterialShowcaseView.Builder(this)
                        .setTarget(cart_img)
                        .setDismissText("GOT IT")
                        .setContentText("Click here for cart items")
                        .setContentTextColor(getResources().getColor(R.color.mediumTurquoise))
                        .setDismissTextColor(getResources().getColor(R.color.white))
                        .withCircleShape()
                        .setShapePadding(80)
                        .build());

        sequence.addSequenceItem(
                new MaterialShowcaseView.Builder(this)
                        .setTarget(notification_img)
                        .setDismissText("GOT IT")
                        .setContentText("Click here for Notifications")
                        .withCircleShape()
                        .setContentTextColor(getResources().getColor(R.color.mediumTurquoise))
                        .setDismissTextColor(getResources().getColor(R.color.white))
                        .useFadeAnimation()
                        .setShapePadding(60)
                        .build());


        sequence.addSequenceItem(
                new MaterialShowcaseView.Builder(this)
                        .setTarget(btnMoreChurch)
                        .setDismissText("GOT IT")
                        .setContentText("Click here for all Churches")
                        .setContentTextColor(getResources().getColor(R.color.mediumTurquoise))
                        .setDismissTextColor(getResources().getColor(R.color.white))
                        .withCircleShape()
                        .setShapePadding(50)
                        .build()
        );

        sequence.start();

    }

}
