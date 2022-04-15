package in.calibrage.teluguchurches.views.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import in.calibrage.teluguchurches.R;
import in.calibrage.teluguchurches.services.ChurchService;
import in.calibrage.teluguchurches.services.ServiceFactory;
import in.calibrage.teluguchurches.util.BaseFragment;
import in.calibrage.teluguchurches.util.Constants;
import in.calibrage.teluguchurches.util.localData.SharedPrefsData;
import in.calibrage.teluguchurches.views.Activities.EventDetailTabActivity;
import in.calibrage.teluguchurches.views.adapter.EventAdapter;
import in.calibrage.teluguchurches.views.model.GetAllChurchesRequestModel;
import in.calibrage.teluguchurches.views.model.GetUpComingEvents;
import io.fabric.sdk.android.Fabric;
import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;



public class UpComingEventsFragment extends BaseFragment implements EventAdapter.OnCartChangedListener {
    private View rootView;
    private Context mContext;
    private RecyclerView eventRecyclerView;
    private String date, sevenDays, eventStr;
    private Subscription mSubscription;
    private ArrayList<GetUpComingEvents.ListResult> BindDataListResults = new ArrayList<>();
    private ArrayList<GetUpComingEvents.ListResult> listResults = new ArrayList<>();
    boolean loadedApi = false;
    GetUpComingEvents eventResponse;
    TextView noRecords;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext = getActivity();

        // added crash report's to mail
        Fabric.with(getActivity(), new Crashlytics());

        //assining layout
        rootView = inflater.inflate(R.layout.fragment_recyclerview, null, false);

        /* intializing and assigning ID's */
        initViews();

        // Hiding soft key board
        hideKeyboard(mContext);

        return rootView;

    }
    /* intializing and assigning ID's */
    private void initViews() {
        date = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, 7);
        Date todate1 = cal.getTime();
        sevenDays = dateFormat.format(todate1);
        eventRecyclerView = (RecyclerView) rootView.findViewById(R.id.eventRecyclerView);

        // to start the Progress Dialog
        activity.showProgressDialog();
        //method to check that, internet is connected or not
        if (isOnline(mContext)) {
            //API to get UP-coming events list
            getUpcomingEventsInfo();
        } else {
            showToast(getActivity(), getString(R.string.no_internet));
        }
        noRecords = (TextView) rootView.findViewById(R.id.no_records);
        noRecords.setVisibility(View.GONE);

    }

    //Method to check that screen is visible to user or not

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser && !loadedApi && getActivity() != null) {
            hideKeyboard(mContext);

        }
    }

    //API to get UP-coming events list
    private void getUpcomingEventsInfo() {
        JsonObject object = upComingObject();
        ChurchService service = ServiceFactory.createRetrofitService(mContext, ChurchService.class);
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
                    public void onNext(final GetUpComingEvents mResponse) {

                        // to hide the Progress Dialog
                        activity.hideProgressDialog();
                        eventResponse = mResponse;
                        BindDataListResults = (ArrayList<GetUpComingEvents.ListResult>) mResponse.getListResult();
                        listResults.addAll(BindDataListResults);
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
                        eventRecyclerView.setLayoutManager(linearLayoutManager);
                        eventRecyclerView.setHasFixedSize(true);
                        EventAdapter adapter = new EventAdapter(mContext, BindDataListResults);
                        eventRecyclerView.setAdapter(adapter);
                        adapter.setOnCartChangedListener(UpComingEventsFragment.this);
                        if (listResults.isEmpty()) {
                            noRecords.setVisibility(View.VISIBLE);
                            //  no_items_text.setText("");
                        } else {
                            noRecords.setVisibility(View.GONE);
                        }
                    }
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

    @Override
    public void setCartClickListener(String status, int position) {
        if (status.equalsIgnoreCase("event")) {

            /*
             * intent value passing to EventDetailTabActivity
             * @param  eventId
             * @param  eventName
             * @param  image
             */
            if (listResults.get(position).getId() != null && !listResults.get(position).getId().equals("")) {
                SharedPrefsData.putInt(mContext, "" + Constants.eventId, listResults.get(position).getId(), Constants.PREF_NAME);
                startActivity(new Intent(mContext, EventDetailTabActivity.class)
                        .putExtra("eventId", eventResponse.getListResult().get(position).getId())
                        .putExtra("eventName", eventResponse.getListResult().get(position).getTitle())
                        .putExtra("image", eventResponse.getListResult().get(position).getEventImage())
                );
            }
        }

    }
}
