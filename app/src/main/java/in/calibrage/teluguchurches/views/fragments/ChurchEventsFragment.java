package in.calibrage.teluguchurches.views.fragments;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import in.calibrage.teluguchurches.R;
import in.calibrage.teluguchurches.services.APIConstants;
import in.calibrage.teluguchurches.services.ChurchService;
import in.calibrage.teluguchurches.services.ServiceFactory;
import in.calibrage.teluguchurches.util.BaseFragment;
import in.calibrage.teluguchurches.util.Constants;
import in.calibrage.teluguchurches.util.calender.EventDecoratorwithCount;
import in.calibrage.teluguchurches.util.localData.SharedPrefsData;
import in.calibrage.teluguchurches.views.adapter.ChurchEventDialogAdapter;
import in.calibrage.teluguchurches.views.adapter.ChurchEventMonthDialogAdapter;
import in.calibrage.teluguchurches.views.adapter.EventDetailsInfoByChurchIdMonthYearAdapter;
import in.calibrage.teluguchurches.views.model.GetEventByDateAndChurchIdResponseModel;
import in.calibrage.teluguchurches.views.model.GetEventDetailsInfoByChurchIdMonthYear;
import in.calibrage.teluguchurches.views.model.GetEventsForTodayResponseModel;
import in.calibrage.teluguchurches.views.model.GetUpComingEvents;
import io.fabric.sdk.android.Fabric;
import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseSequence;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView;
import uk.co.deanwild.materialshowcaseview.ShowcaseConfig;



public class ChurchEventsFragment extends BaseFragment {
    private Context mContext;
    private View rootView, dialogRootView;
    MaterialCalendarView calendarView;
    private Subscription mSubscription;
    Calendar calendar = Calendar.getInstance();
    private AlertDialog alert;
    private ArrayList<GetEventsForTodayResponseModel.ListResult> mResponse = new ArrayList<>();
    private ArrayList<GetUpComingEvents.ListResult> listEvents = new ArrayList<>();
    private RecyclerView recyclerView;
    private RecyclerView rcv_allevent;
    LinearLayoutManager layoutManager;
    private ChurchEventDialogAdapter adapter;
    private ChurchEventMonthDialogAdapter madapter;
    private String dateStr, position;
    TextView no_records;
    int churchid, authorId;
    private TabLayout tabhost;
    private LinearLayout calendarlyt;
    private static final String SHOWCASE_ID = "sequence category";


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext = getActivity();

        // added crash report's to mail
        Fabric.with(getActivity(), new Crashlytics());

        //assining layout
        rootView = inflater.inflate(R.layout.activity_events2, null, false);

        //storing value's from shared preference
        churchid = SharedPrefsData.getInt(mContext, String.valueOf(Constants.Churchid), Constants.PREF_NAME);
        authorId = SharedPrefsData.getInt(mContext, Constants.AdminId, Constants.PREF_NAME);

        position = getArguments().getString("notiChurchEventId");
        if (position != null && !position.equalsIgnoreCase("0")) {
            tabhost = (TabLayout) getActivity().findViewById(R.id.tabs);
            tabhost.getTabAt(1).select();
        }
        /* intializing and assigning ID's */
        initViews();

        /* Navigation's and using the views */
        setViews();

        //API to get Events for month
        getEventsForToday(churchid, calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.YEAR));

        //API to get Events information for month
        getEventsInfoForToday(churchid, calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.YEAR));

        return rootView;
    }

    private void initViews() {
        /* intializing and assigning ID's */
        calendarView = rootView.findViewById(R.id.calendarView);
        rcv_allevent = rootView.findViewById(R.id.rcv_allevent);

        Calendar instance = Calendar.getInstance();
        calendarView.setSelectedDate(instance.getTime());
        no_records = (TextView) rootView.findViewById(R.id.no_records);
        calendarlyt = rootView.findViewById(R.id.calendarlyt);
    }

    private void setViews() {
        /* Navigation's and using the views */

        calendarView.setOnMonthChangedListener(new OnMonthChangedListener() {
            @Override
            public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {

                //API to get Events for month
                getEventsForToday(churchid, date.getMonth() + 1, date.getYear());

                //API to get Events information for month
                getEventsInfoForToday(churchid, date.getMonth() + 1, date.getYear());

            }
        });

        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                dateStr = "" + date.getYear() + "-" + (date.getMonth() + 1) + "-" + date.getDay();

                //API to get Events by date and userId
                getEventByDateAndUserId();

            }
        });

    }


    private void getEventsForToday(int id, int month, int year) {
        //API to get Events for month

        ChurchService service = ServiceFactory.createRetrofitService(mContext, ChurchService.class);
        mSubscription = service.GetEventByChurchIdMonthYear(APIConstants.GetEventByChurchIdMonthYear + id + "/" + month + "/" + year)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<GetEventsForTodayResponseModel>() {
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
                    public void onNext(GetEventsForTodayResponseModel Events) {

                        if ((Events.getIsSuccess()) & (Events.getListResult().size() >= 1)) {

                            /*
                             * decarating the calendar view
                             * */
                            decarateEachEventDay(Events.getListResult());

                        }

                    }
                });
    }


    //API to get Events information for month
    private void getEventsInfoForToday(int id, int month, int year) {
        /* get default date events */
        ChurchService service = ServiceFactory.createRetrofitService(mContext, ChurchService.class);

        mSubscription = service.GetEventDetailsInfoByChurchIdMonthYear(APIConstants.GetEventDetailsInfoByChurchIdMonthYear + id + "/" + month + "/" + year)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<GetEventDetailsInfoByChurchIdMonthYear>() {
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
                    public void onNext(GetEventDetailsInfoByChurchIdMonthYear mResponse) {

                        if (mResponse.getTotalRecords() != 0) {
                            no_records.setVisibility(View.GONE);
                            rcv_allevent.setVisibility(View.VISIBLE);
                            EventDetailsInfoByChurchIdMonthYearAdapter eventsByMonthAdapter = new EventDetailsInfoByChurchIdMonthYearAdapter(mContext, mResponse);
                            rcv_allevent.setAdapter(eventsByMonthAdapter);
                            rcv_allevent.setHasFixedSize(true);
                            layoutManager = new LinearLayoutManager(mContext);
                            rcv_allevent.setLayoutManager(layoutManager);
                        } else {
                            no_records.setVisibility(View.VISIBLE);
                            rcv_allevent.setVisibility(View.GONE);
                        }
                    }
                });
    }


    //API to get Events by date and userId
    private void getEventByDateAndUserId() {
        ChurchService service = ServiceFactory.createRetrofitService(mContext, ChurchService.class);
        String url = APIConstants.GetEventByDateAndChurchId + "/" + dateStr + "/" + churchid;
        mSubscription = service.GetEventDateAndChurchId(url)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<GetEventByDateAndChurchIdResponseModel>() {
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
                    public void onNext(GetEventByDateAndChurchIdResponseModel mResponse) {
                        if (mResponse.getIsSuccess() && mResponse.getTotalRecords() > 0) {
                            /*
                             * Dialog box to show particular event details on selcting date in calendar view
                             * */
                            dialog();
                            madapter = new ChurchEventMonthDialogAdapter(mContext, mResponse);
                            recyclerView.setAdapter(madapter);

                        }
                    }
                });
    }

    /*
     * Dialog box to show particular event details on selcting date in calendar view
     * */
    private void dialog() {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        dialogRootView = layoutInflater.inflate(R.layout.dialog_events, null);
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
        alertDialogBuilder.setView(dialogRootView);
        recyclerView = dialogRootView.findViewById(R.id.recyclerView);
        TextView okText = dialogRootView.findViewById(R.id.okText);

        /*
         * @param OnClickListner for OK button
         * */
        okText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                alert.dismiss();
            }
        });

        alert = alertDialogBuilder.create();
        alert.show();

    }

    @TargetApi(Build.VERSION_CODES.M)

    /*
     * decarating the calendar view
     * */
    private void decarateEachEventDay(List<GetEventsForTodayResponseModel.ListResult> listResult) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        ArrayList<CalendarDay> dates = new ArrayList<>();

        for (GetEventsForTodayResponseModel.ListResult events : listResult) {

            Date d = null;
            try {
                d = sdf.parse(events.getEventDate());
                CalendarDay day = new CalendarDay(d);
                EventDecoratorwithCount eventDecoratorwithCount = new EventDecoratorwithCount(mContext.getResources().getColor(R.color.red), day, getActivity(), events.getEventsCount());
                calendarView.addDecorators(eventDecoratorwithCount);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            CalendarDay day = new CalendarDay(d);
            dates.add(day);
        }
    }

    /*
     * Calling the showcase when the view is visible to the user
     * */

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            /*
             * presenting the show case view
             * */
            presentShowcaseSequence();
        }

    }

    /*
     * presenting the show case view
     * */
    private void presentShowcaseSequence() {

        ShowcaseConfig config = new ShowcaseConfig();
        config.setDelay(200); // half second between each showcase view

        MaterialShowcaseSequence sequence = new MaterialShowcaseSequence(getActivity(), SHOWCASE_ID);

        sequence.setOnItemShownListener(new MaterialShowcaseSequence.OnSequenceItemShownListener() {

            @Override
            public void onShow(MaterialShowcaseView itemView, int position) {
                //   Toast.makeText(itemView.getContext(), "Item #" + position, Toast.LENGTH_SHORT).show();
            }
        });

        sequence.setConfig(config);
        sequence.addSequenceItem(
                new MaterialShowcaseView.Builder(getActivity())
                        .setTarget(calendarView)
                        .setDismissText("GOT IT")
                        .setContentText("Click On A Particular Date For Events Details")
                        .setContentTextColor(getResources().getColor(R.color.mediumTurquoise))
                        .setDismissTextColor(getResources().getColor(R.color.white))
                        .withCircleShape()
                        .setShapePadding(50)
                        .build()
        );


        sequence.start();

    }
}
