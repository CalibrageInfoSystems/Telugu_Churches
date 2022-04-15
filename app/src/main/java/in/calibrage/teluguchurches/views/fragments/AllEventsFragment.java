package in.calibrage.teluguchurches.views.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import in.calibrage.teluguchurches.R;
import in.calibrage.teluguchurches.services.ChurchService;
import in.calibrage.teluguchurches.services.ServiceFactory;
import in.calibrage.teluguchurches.util.BaseFragment;
import in.calibrage.teluguchurches.util.Constants;
import in.calibrage.teluguchurches.util.localData.SharedPrefsData;
import in.calibrage.teluguchurches.views.Activities.EventDetailTabActivity;
import in.calibrage.teluguchurches.views.adapter.EventAdapterYear;
import in.calibrage.teluguchurches.views.model.GetAllChurchesRequestModel;
import in.calibrage.teluguchurches.views.model.GetAllEventsResponeModel;
import io.fabric.sdk.android.Fabric;
import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class AllEventsFragment extends BaseFragment implements View.OnClickListener, EventAdapterYear.OnCartChangedListener {
    private View rootView;
    private Context mContext;
    private RecyclerView eventRecyclerView;
    private SearchView search;
    Calendar calendar = Calendar.getInstance();
    private ArrayList<GetAllEventsResponeModel.ListResult> BindDataListResults = new ArrayList<>();
    private ArrayList<GetAllEventsResponeModel.ListResult> listResults = new ArrayList<>();
    private Subscription mSubscription;
    private EventAdapterYear adapter;
    private String eventStr = "";
    int pageIndex = 1, listPosition = 0, pazeSize = 10;
    private boolean loading = true;
    private Handler handler;
    private ImageView mCloseButton, clearImg, iv_clear_text;
    TextView noRecords;
    private EditText searchEdt;
    private LinearLayout searchLay;
    private GetAllEventsResponeModel eventResponse;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext = getActivity();

        // added crash report's to mail
        Fabric.with(mContext, new Crashlytics());

        //assining layout
        rootView = inflater.inflate(R.layout.fragment_recyclerview, null, false);

        /* intializing and assigning ID's */
        initViews();

        /* Navigation's and using the views */
        setViews();
        return rootView;
    }

    private void initViews() {
        /*
         intializing and assigning ID's
         */
        eventRecyclerView = rootView.findViewById(R.id.eventRecyclerView);
        mCloseButton = rootView.findViewById(android.support.v7.appcompat.R.id.search_close_btn);
        search = rootView.findViewById(R.id.search);
        clearImg = rootView.findViewById(R.id.clearImg);
        iv_clear_text = rootView.findViewById(R.id.iv_clear_text);
        noRecords = rootView.findViewById(R.id.no_records);
        searchEdt = rootView.findViewById(R.id.searchEdt);
        searchLay = rootView.findViewById(R.id.searchLay);
        noRecords.setVisibility(View.GONE);
        search.setVisibility(View.GONE);
        searchLay.setVisibility(View.VISIBLE);

        // to start the Progress Dialog
        activity.showProgressDialog();

        // API to get events list
        eventSearch();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_clear_text:
                if (eventStr != null && !eventStr.equalsIgnoreCase(""))
                    if (!eventStr.isEmpty()) {
                        searchEdt.setText("");
                        pageIndex = 1;
                        eventStr = "";
                        BindDataListResults = new ArrayList<>();
                        listResults = new ArrayList<>();

                        // to start the Progress Dialog
                        activity.showProgressDialog();
                        /**
                         * API for events
                         */
                        eventSearch();
                    }
                break;
        }

    }

    private void setViews() {
        /*
         * Navigation's and using the views
         * */
        /**
         * @param OnClickListner
         */
        iv_clear_text.setOnClickListener(this);
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                eventStr = query;
                if (isOnline(mContext)) {
                    if (!eventStr.isEmpty()) {
                        pageIndex = 1;
                        BindDataListResults = new ArrayList<>();
                        listResults = new ArrayList<>();

                        // to start the Progress Dialog
                        activity.showProgressDialog();

                        /**
                         * API for events
                         */
                        eventSearch();
                    }
                } else {
                    showToast(getActivity(), getString(R.string.no_internet));
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });

        /**
         * @param onCloseListener
         */
        search.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                if (eventStr != null && !eventStr.equalsIgnoreCase(""))
                    if (!eventStr.isEmpty()) {
                        pageIndex = 1;
                        eventStr = "";
                        BindDataListResults = new ArrayList<>();
                        listResults = new ArrayList<>();

                        // to start the Progress Dialog
                        activity.showProgressDialog();
                        /**
                         * API for events
                         */
                        eventSearch();
                    }
                return false;
            }
        });

        /**
         * @param OnEditorActionListener
         */
        searchEdt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    performSearch();
                    return true;
                }
                return false;
            }
        });

        /**
         * @param TextChangedListener
         */
        searchEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }


            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                eventStr = charSequence.toString();
                if (charSequence.toString().trim().length() == 0) {
                    iv_clear_text.setVisibility(View.GONE);
                } else {
                    iv_clear_text.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    /*
     * API for search
     * */
    private void performSearch() {
        if (eventStr != null && !eventStr.equalsIgnoreCase(""))
            if (!eventStr.isEmpty()) {
                pageIndex = 1;
                BindDataListResults = new ArrayList<>();
                listResults = new ArrayList<>();

                // to start the Progress Dialog
                activity.showProgressDialog();

                /**
                 * API for events
                 */
                eventSearch();
            }
    }

    /**
     * API for events
     */
    private void eventSearch() {
        JsonObject object = eventSearchObject();
        ChurchService service = ServiceFactory.createRetrofitService(mContext, ChurchService.class);
        mSubscription = service.GetEventInfoByMonthYear(object)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<GetAllEventsResponeModel>() {
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
                    public void onNext(final GetAllEventsResponeModel mResponse) {
                        // to hide the Progress Dialog
                        activity.hideProgressDialog();
                        if (mResponse.getIsSuccess()) {
                            eventResponse = mResponse;
                            BindDataListResults = (ArrayList<GetAllEventsResponeModel.ListResult>) mResponse.getListResult();
                            listResults.addAll(BindDataListResults);
                            eventRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
                            eventRecyclerView.setHasFixedSize(true);
                            adapter = new EventAdapterYear(mContext, listResults, eventRecyclerView);
                            eventRecyclerView.setAdapter(adapter);
                            eventRecyclerView.scrollToPosition(listResults.size() - 10);
                            adapter.setOnCartChangedListener(AllEventsFragment.this);
                            handler = new Handler();

                            //interface for view more functionality
                            adapter.setOnLoadMoreListener(new EventAdapterYear.OnLoadMoreListener() {
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
                                            if (BindDataListResults.size() >= pazeSize) {
                                                listResults.remove(listResults.size() - 1);
                                                adapter.notifyItemRemoved(listResults.size());
                                                pageIndex = pageIndex + 1;

                                                // to start the Progress Dialog
                                                activity.showProgressDialog();
                                                eventSearch();
                                                adapter.notifyDataSetChanged();
                                                loading = false;
//                                            adapter.setLoaded();
                                            }

                                            //or you can add all at once but do not forget to call mAdapter.notifyDataSetChanged();
                                        }
                                    }, 1000);
                                }
                            });
                            if (listResults.isEmpty()) {
                                noRecords.setVisibility(View.VISIBLE);
                            } else {
                                noRecords.setVisibility(View.GONE);
                            }
                        }
                    }


                });
    }

    /**
     * Json Object of eventSearchObject
     *
     * @return
     */
    private JsonObject eventSearchObject() {
        GetAllChurchesRequestModel mRequest = new GetAllChurchesRequestModel();
        mRequest.setMonth(calendar.get(Calendar.MONTH) + 1);
        mRequest.setYear(calendar.get(Calendar.YEAR));
        mRequest.setPageIndex(pageIndex);
        mRequest.setPageSize(pazeSize);
        mRequest.setSortbyColumnName("UpdatedDate");
        mRequest.setSortDirection("desc");
        mRequest.setSearchName(eventStr);
        return new Gson().toJsonTree(mRequest).getAsJsonObject();
    }

    /**
     * ClickListner for adapter
     */
    /*
     * intent value passing to ChurchDetails
     * @param  eventId
     *@param  eventName
     *@param  image
     */
    @Override
    public void setCartClickListener(String status, int position) {
        if (listPosition <= listResults.size() - 1) {
            listPosition = position;
            if (status.equalsIgnoreCase("allevent")) {
                if (listResults.get(position).getId() != null && !listResults.get(position).getId().equals("")
                        || listResults.get(position).getAuthorName() != null && !listResults.get(position).getAuthorName().equals("")) {
                    SharedPrefsData.putInt(mContext, "" + Constants.eventId, listResults.get(position).getId(), Constants.PREF_NAME);
                    startActivity(new Intent(mContext, EventDetailTabActivity.class)
                            .putExtra("eventId", listResults.get(listPosition).getId())
                            .putExtra("eventName", listResults.get(listPosition).getTitle())
                            .putExtra("image", listResults.get(listPosition).getEventImage()));
                }
            }

        }

    }
}