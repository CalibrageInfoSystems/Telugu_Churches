package in.calibrage.teluguchurches.views.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import in.calibrage.teluguchurches.R;
import in.calibrage.teluguchurches.services.ChurchService;
import in.calibrage.teluguchurches.services.ServiceFactory;
import in.calibrage.teluguchurches.util.BaseFragment;
import in.calibrage.teluguchurches.util.CommonUtil;
import in.calibrage.teluguchurches.util.Constants;
import in.calibrage.teluguchurches.util.localData.SharedPrefsData;
import in.calibrage.teluguchurches.views.Activities.AdminDetailsActivity;
import in.calibrage.teluguchurches.views.Activities.ChurchDetailsActivity;
import in.calibrage.teluguchurches.views.adapter.NotificationScreenAdapter;
import in.calibrage.teluguchurches.views.model.AddUpdateNotificationsRequestModel;
import in.calibrage.teluguchurches.views.model.AddUpdateNotificationsResponeModel;
import in.calibrage.teluguchurches.views.model.GetNotificationResponseModel;
import in.calibrage.teluguchurches.views.model.GetUpComingEventsModel;
import in.calibrage.teluguchurches.views.model.NotiifcationRequestModel;
import io.fabric.sdk.android.Fabric;
import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ViewNotificationFragment extends BaseFragment implements NotificationScreenAdapter.OnCartChangedListener {
    public static final String TAG = ViewNotificationFragment.class.getSimpleName();
    private Subscription mSubscription;
    private RecyclerView notificationRecyclerView;
    private Toolbar toolbar;
    GetNotificationResponseModel mNotificationResponse;
    private GetUpComingEventsModel commentResponse;
    private TextView no_items_text, titleText, descriptionText, generatedOnText, generatedByText;
    private Context mContext;
    private String authorizationToken;
    private Integer NotificationId;
    private View rootView;
    ArrayList<GetNotificationResponseModel> getnotificationDataList;
    GetNotificationResponseModel getmNotificationResponse;
    Boolean isRead;
    NotificationScreenAdapter adapter;
    private boolean loading = true;
    private Handler handler;
    private Integer pageIndex = 1, listPosition = 0, pazeSize = 100, Position;
    private ArrayList<GetNotificationResponseModel.NotificationsList> listResults = new ArrayList<>();
    private ArrayList<GetNotificationResponseModel.NotificationsList> BIndDatalistResults = new ArrayList<>();
    private AlertDialog alertDialog;
    private Button cancel_btn;
    private String generatedOnDate,dateandtime;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext = getActivity();

        // added crash report's to mail
        Fabric.with(getActivity(), new Crashlytics());

        //assining layout
        rootView = inflater.inflate(R.layout.activity_notification_view, null, false);

        getnotificationDataList = new ArrayList<>();
        notificationRecyclerView =  rootView.findViewById(R.id.notificationRecyclerView);
        no_items_text = rootView.findViewById(R.id.no_items_text);

        // to start the Progress Dialog
        activity.showProgressDialog();

        // Method to get current date and time
        dateAndtime();

        // API to get Notifications list
        getNotification();


        return rootView;
    }

    // API to get Notifications list
    private void getNotification() {
        if (authorizationToken != "null" || authorizationToken != null || authorizationToken.isEmpty())
            authorizationToken = SharedPrefsData.getInstance(mContext).getStringFromSharedPrefs(Constants.Auth_Token);
        JsonObject object = notificationObject();
        ChurchService service = ServiceFactory.createRetrofitService(mContext, ChurchService.class);
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
                        activity.hideProgressDialog();
                        if (mResponse.getIsSuccess()) {

                            if (mResponse.getResult().getNotificationsList().isEmpty()) {
                                no_items_text.setVisibility(View.VISIBLE);
                              //  no_items_text.setText("No Records found");
                            } else {
                                no_items_text.setVisibility(View.GONE);
                            }
                            mNotificationResponse = mResponse;


                            BIndDatalistResults = (ArrayList<GetNotificationResponseModel.NotificationsList>) mResponse.getResult().getNotificationsList();
                            listResults =  new ArrayList<>();
                            listResults.addAll(BIndDatalistResults);
                            notificationRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
                            notificationRecyclerView.setHasFixedSize(true);
                            adapter = new NotificationScreenAdapter(mContext, listResults, notificationRecyclerView);
                            notificationRecyclerView.setAdapter(adapter);
                            notificationRecyclerView.scrollToPosition(listResults.size() - 10);
                            adapter.setOnCartChangedListener(ViewNotificationFragment.this);

                            handler = new Handler();
                            adapter.setOnLoadMoreListener(new NotificationScreenAdapter.OnLoadMoreListener() {
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
                                            if (BIndDatalistResults.size() >= pazeSize) {
                                                listResults.remove(listResults.size() - 1);
                                                adapter.notifyItemRemoved(listResults.size());
                                                pageIndex = pageIndex + 1;

                                                // to start the Progress Dialog
                                                activity.showProgressDialog();
                                                getNotification();
                                                adapter.notifyDataSetChanged();
                                                loading = false;
//                                              adapter.setLoaded();
                                            }

                                            //or you can add all at once but do not forget to call mAdapter.notifyDataSetChanged();
                                        }
                                    }, 1000);

                                }
                            });
                        }


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
        mRequest.setUserId(SharedPrefsData.getInt(mContext, Constants.ID, Constants.PREF_NAME));
        mRequest.setSortbyColumnName("UpdatedDate");
        mRequest.setSortDirection("desc");
        mRequest.setPageIndex(pageIndex);
        mRequest.setPageSize(pazeSize);
        return new Gson().toJsonTree(mRequest).getAsJsonObject();
    }


    @Override
    public void setCartClickListener(String status, int position) {
        if (status.equalsIgnoreCase("itemPosition")) {


            NotificationId = listResults.get(position).getId();
            // API to get the notifications count
            getNotificationCount();

            if (listResults.get(position).getChurchId() != null)
                SharedPrefsData.putInt(mContext, Constants.Churchid, listResults.get(position).getChurchId(), Constants.PREF_NAME);
            if (listResults.get(position).getAuthorId() != null)
                SharedPrefsData.putInt(mContext, Constants.AdminId, listResults.get(position).getAuthorId(), Constants.PREF_NAME);
            if (listResults.get(position).getEventId() != null)
                SharedPrefsData.putInt(mContext, Constants.eventId, listResults.get(position).getEventId(), Constants.PREF_NAME);
            if (listResults.get(position).getPostId() != null)
                SharedPrefsData.putInt(mContext, Constants.PostId, listResults.get(position).getPostId(), Constants.PREF_NAME);
           if (listResults.get(position).getChurchId() != null) {
                if (listResults.get(position).getEventId() != null) {
                    /*
                     * intent value passing to ChurchDetailsActivity
                     * @param  id
                     * @param  notiChurchEventId
                     * @param  name
                     */
                    SharedPrefsData.putInt(mContext, Constants.ISCHURCH, 1, Constants.PREF_NAME);
                    SharedPrefsData.putInt(mContext, "" + Constants.eventId, listResults.get(position).getEventId(), Constants.PREF_NAME);
                    Intent intent = new Intent(mContext, ChurchDetailsActivity.class);
                    intent.putExtra("id", listResults.get(position).getChurchId())
                            .putExtra("notiChurchEventId", listResults.get(position).getEventId())
                            .putExtra("name", listResults.get(position).getName());
                    startActivity(intent);
                }else if (listResults.get(position).getPostId() != null) {
                    /*
                     * intent value passing to ChurchDetailsActivity
                     * @param  id
                     * @param  notiChurchEventId
                     * @param  name
                     */
                    SharedPrefsData.putInt(mContext, Constants.ISCHURCH, 1, Constants.PREF_NAME);
                    Intent intent = new Intent(mContext, ChurchDetailsActivity.class);
                    intent.putExtra("id", listResults.get(position).getChurchId())
                            .putExtra("notiChurchPostId", listResults.get(position).getPostId())
                            .putExtra("name", listResults.get(position).getName());
                    startActivity(intent);
                } else {
                    LayoutInflater layoutInflater = LayoutInflater.from(mContext);
                    View dialogRootView = layoutInflater.inflate(R.layout.add_dialog_notification, null);
                    final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
                    titleText =  dialogRootView.findViewById(R.id.titleText);
                    descriptionText =  dialogRootView.findViewById(R.id.descriptionText);
                    generatedOnText =  dialogRootView.findViewById(R.id.generatedOnText);
                    generatedByText =  dialogRootView.findViewById(R.id.generatedByText);
                    cancel_btn = (Button) dialogRootView.findViewById(R.id.cancel_btn);
                    titleText.setText(listResults.get(position).getName());
                    descriptionText.setText(listResults.get(position).getDesc());
                    generatedOnDate = listResults.get(position).getCreatedDate();
                    String from = CommonUtil.formatDateTimeUi(generatedOnDate);
                    generatedOnText.setText(Html.fromHtml(from));

                    generatedByText.setText(listResults.get(position).getCreatedBy());

                    /**
                     * @param OnClickListner
                     */
                    cancel_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            alertDialog.dismiss();
                        }
                        });
                    alertDialogBuilder.setView(dialogRootView);
                    alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }

            } else {
                if (listResults.get(position).getEventId() != null) {
                    /*
                     * intent value passing to AdminDetailsActivity
                     * @param  id
                     * @param  notiAuthorEventId
                     * @param church name
                     */
                    SharedPrefsData.putInt(mContext, Constants.ISCHURCH, 1, Constants.PREF_NAME);
                    Intent intent = new Intent(mContext, AdminDetailsActivity.class);
                    intent.putExtra("id", listResults.get(position).getAuthorId())
                            .putExtra("notiAuthorEventId", listResults.get(position).getEventId())
                            .putExtra("church name", listResults.get(position).getName());
                    startActivity(intent);
                }else if (listResults.get(position).getPostId() != null) {
                    /*
                     * intent value passing to AdminDetailsActivity
                     * @param  id
                     * @param  notiAuthorEventId
                     * @param church name
                     */
                    Intent intent = new Intent(mContext, AdminDetailsActivity.class);
                    SharedPrefsData.putInt(mContext, Constants.ISCHURCH, 1, Constants.PREF_NAME);
                    intent.putExtra("id", listResults.get(position).getAuthorId())
                            .putExtra("notiAuthorPostId", listResults.get(position).getPostId())
                            .putExtra("church name", listResults.get(position).getName());
                    startActivity(intent);
                } else {
                    LayoutInflater layoutInflater = LayoutInflater.from(mContext);
                    View dialogRootView = layoutInflater.inflate(R.layout.add_dialog_notification, null);
                    final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
                    titleText =  dialogRootView.findViewById(R.id.titleText);
                    descriptionText =  dialogRootView.findViewById(R.id.descriptionText);
                    generatedOnText =  dialogRootView.findViewById(R.id.generatedOnText);
                    generatedByText =  dialogRootView.findViewById(R.id.generatedByText);
                    cancel_btn =  dialogRootView.findViewById(R.id.cancel_btn);
                    titleText.setText(listResults.get(position).getName());
                    descriptionText.setText(listResults.get(position).getDesc());
                    generatedOnDate = listResults.get(position).getCreatedDate();
                    String from = CommonUtil.formatDateTimeUi(generatedOnDate);
                    generatedOnText.setText(Html.fromHtml(from));

                    generatedByText.setText(listResults.get(position).getCreatedBy());

                    /**
                     * @param OnClickListner
                     */
                    cancel_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            alertDialog.dismiss();
                        }
                    });
                    alertDialogBuilder.setView(dialogRootView);
                    alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }
            }
        }

    }
// API to get the notifications count
    private void getNotificationCount() {
        JsonObject object = addupdatenotificationObject();
        ChurchService service = ServiceFactory.createRetrofitService(mContext, ChurchService.class);
        mSubscription = service.getAddUpdateNotifications(object)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<AddUpdateNotificationsResponeModel>() {
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
                    public void onNext(AddUpdateNotificationsResponeModel mResponse) {
                        listResults.removeAll(BIndDatalistResults);
                        getNotification();
                    }

                });
    }

    // Method to get current date and time
    private void dateAndtime() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        String formattedDate = df.format(c.getTime());
        dateandtime = formattedDate;
        SharedPrefsData.getInstance(mContext).updateStringValue(mContext, "datetime", dateandtime);
    }
    /**
     * Json Object of addupdatenotificationObject
     *
     * @return
     */
    private JsonObject addupdatenotificationObject() {
        AddUpdateNotificationsRequestModel mRequest = new AddUpdateNotificationsRequestModel();
        mRequest.setRoleIds(null);
        mRequest.setUserId(SharedPrefsData.getInt(mContext, Constants.ID, Constants.PREF_NAME));
        mRequest.setId(NotificationId);
        mRequest.setName(null);
        mRequest.setDesc(null);
        mRequest.setHtmlDesc(null);
        mRequest.setEventId(0);
        mRequest.setPostId(0);
        mRequest.setNotificationTypeId(0);
        mRequest.setCreatedByUserId(SharedPrefsData.getInt(mContext, Constants.ID, Constants.PREF_NAME));
        mRequest.setCreatedDate(dateandtime);
        mRequest.setUpdatedByUserId(SharedPrefsData.getInt(mContext, Constants.ID, Constants.PREF_NAME));
        mRequest.setUpdatedDate(dateandtime);
        mRequest.setServerUpdatedStatus(true);
        return new Gson().toJsonTree(mRequest).getAsJsonObject();
    }


}
