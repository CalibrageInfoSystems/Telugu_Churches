package in.calibrage.teluguchurches.views.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.crashlytics.android.Crashlytics;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.ArrayList;

import in.calibrage.teluguchurches.R;
import in.calibrage.teluguchurches.services.APIConstants;
import in.calibrage.teluguchurches.services.ChurchService;
import in.calibrage.teluguchurches.services.ServiceFactory;
import in.calibrage.teluguchurches.util.BaseFragment;
import in.calibrage.teluguchurches.util.CommonUtil;
import in.calibrage.teluguchurches.util.Constants;
import in.calibrage.teluguchurches.util.localData.SharedPrefsData;
import in.calibrage.teluguchurches.views.Activities.AudioActivity;
import in.calibrage.teluguchurches.views.Activities.LoginActivity;
import in.calibrage.teluguchurches.views.Activities.PdfReaderActivity;
import in.calibrage.teluguchurches.views.adapter.EventDocumentAdapter;
import in.calibrage.teluguchurches.views.adapter.EventImageAdapter;
import in.calibrage.teluguchurches.views.adapter.EventsCommentAdapter;
import in.calibrage.teluguchurches.views.adapter.EventsViewAllRepliesAdapter;
import in.calibrage.teluguchurches.views.model.EventCommentDeleteResponseModel;
import in.calibrage.teluguchurches.views.model.EventCommentRequestModel;
import in.calibrage.teluguchurches.views.model.EventDeleteRequestModel;
import in.calibrage.teluguchurches.views.model.EventViewAllRepliesResponseModel;
import in.calibrage.teluguchurches.views.model.EventsCommentResponseModel;
import in.calibrage.teluguchurches.views.model.GetPostbyEventIdResponseModel;
import in.calibrage.teluguchurches.views.model.GetUpComingEventsModel;
import in.calibrage.teluguchurches.views.model.LikeDisLikeRequestModel;
import in.calibrage.teluguchurches.views.model.LikeDislikeResponeModel;
import io.fabric.sdk.android.Fabric;
import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseSequence;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView;
import uk.co.deanwild.materialshowcaseview.ShowcaseConfig;


public class EventDetailsFragment extends BaseFragment implements EventDocumentAdapter.OnCartChangedListener, EventImageAdapter.OnCartChangedListener,
        View.OnClickListener, EventsCommentAdapter.OnCartChangedListener, EventsViewAllRepliesAdapter.OnCartChangedListener {
    public static final String TAG = EventDetailsFragment.class.getSimpleName();
    private View rootView;
    private String eventName, image, categoryName, imgUrl, comingFrom;
    private Intent intent;
    private Subscription mSubscription;
    private Toolbar toolbar;
    private int eventId, value, userId, commentID, listPosition, parentCommentId;
    private TextView regNoText, nameText, churchNameText,
            authorNameText, contactNoText, startDtText, endDtText,
            likeText, disLikeText, addCommentText, viewCount,
            dialogMessage, commentTitleText, titleText, editText, deleteText,
            descriText, contactNumberTitle;
    private ImageView headerImg, likeImg, disLikeImg, clearDialog;
    private RecyclerView docRecyclerView, videoRecyclerView, audioRecyclerView, imgRecyclerView,
            commentRecyclerView, allReplyRecyclerView;
    private RelativeLayout likeLay, diLikeLay, shareLay;
    private Context mContext;
    private LinearLayout docCardView, videoLay, imageCardView, audioCardView,
            regNoText_lay, nameText_lay, churchNameText_lay, contactNoText_lay,
            authorNameText_lay, startDtText_lay, endDtText_lay, descriText_lay;
    private ArrayList<String> documentList = new ArrayList<>();
    private ArrayList<String> videoList = new ArrayList<>();
    private ArrayList<String> audioList = new ArrayList<>();
    private ArrayList<String> imageList = new ArrayList<>();
    private EventImageAdapter cAdapter;
    private ArrayList<String> linkArray = new ArrayList<>();
    private Button ok_btn, cancel_btn;
    private AlertDialog alertDialog, alertDialogTest;
    private EditText input, addCommentTxt;
    private boolean like = false, disLike = false;
    GetPostbyEventIdResponseModel listResponse;
    private LikeDislikeResponeModel mModel;
    private GetUpComingEventsModel commentResponse;
    private EventsCommentAdapter eventsCommentAdapter;
    private EventsViewAllRepliesAdapter eventsViewAllRepliesAdapter;
    private ProgressDialog mDialog;
    private BottomSheetDialog mBottomSheetDialog;
    private View dialogView;
    BottomSheetBehavior bottomSheetBehavior;
    private String authorizationToken, shareUrl;
    private static final String SHOWCASE_ID = "sequence home";


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext = getActivity();

        // added crash report's to mail
        Fabric.with(mContext, new Crashlytics());

        //assining layout
        rootView = inflater.inflate(R.layout.activity_event_details, null, false);
        intent = getActivity().getIntent();
        if (intent != null) {
            /*
             * getting data from intent
             * */
            eventId = intent.getIntExtra("eventId", 0);
            eventName = intent.getStringExtra("eventName");
            categoryName = intent.getStringExtra("name");
            image = intent.getStringExtra("image");
        }


        headerImg = (ImageView) rootView.findViewById(R.id.headerImg);

        Glide.with(mContext).load(image)
                .fitCenter()
                .error(R.drawable.eventsdetails)
                .into(headerImg);


        // to start the Progress Dialog
        activity.showProgressDialog();


        //API to get event details by EventID
        getEventById();

        //storing value's from shared preference
        userId = SharedPrefsData.getInt(mContext, Constants.ID, Constants.PREF_NAME);
        value = SharedPrefsData.getInt(mContext, Constants.ISLOGIN, Constants.PREF_NAME);

        /* intializing and assigning ID's */
        initView();

        /* Navigation's and using the views */
        setViews();


        return rootView;
    }

    private void initView() {
        /* intializing and assigning ID's */

        regNoText = rootView.findViewById(R.id.regNoText);
        nameText = rootView.findViewById(R.id.nameText);
        churchNameText = rootView.findViewById(R.id.churchNameText);
        authorNameText = rootView.findViewById(R.id.authorNameText);
        descriText = rootView.findViewById(R.id.descriText);
        contactNoText = rootView.findViewById(R.id.contactNoText);
        startDtText = rootView.findViewById(R.id.startDtText);
        endDtText = rootView.findViewById(R.id.endDtText);
        docRecyclerView = rootView.findViewById(R.id.docRecyclerView);
        videoRecyclerView = rootView.findViewById(R.id.videoRecyclerView);
        audioRecyclerView = rootView.findViewById(R.id.audioRecyclerView);
        imgRecyclerView = rootView.findViewById(R.id.imgRecyclerView);
        docCardView = rootView.findViewById(R.id.docCardView);
        videoLay = rootView.findViewById(R.id.videoLay);
        audioCardView = rootView.findViewById(R.id.audioCardView);
        imageCardView = rootView.findViewById(R.id.imageCardView);
        likeText = rootView.findViewById(R.id.likeText);
        likeImg = rootView.findViewById(R.id.likeImg);
        likeLay = rootView.findViewById(R.id.likeLay);
        disLikeText = rootView.findViewById(R.id.disLikeText);
        contactNumberTitle = rootView.findViewById(R.id.contactNumberTitle);
        disLikeImg = rootView.findViewById(R.id.disLikeImg);
        diLikeLay = rootView.findViewById(R.id.diLikeLay);
        addCommentText = rootView.findViewById(R.id.addCommentText);
        commentRecyclerView = rootView.findViewById(R.id.commentRecyclerView);
        shareLay = rootView.findViewById(R.id.shareLay);
        titleText = rootView.findViewById(R.id.titleText);
        viewCount = rootView.findViewById(R.id.viewCount);
        commentTitleText = rootView.findViewById(R.id.commentTitleText);
        regNoText_lay = rootView.findViewById(R.id.regNoText_lay);
        nameText_lay = rootView.findViewById(R.id.nameText_lay);
        descriText_lay = rootView.findViewById(R.id.descriText_lay);
        churchNameText_lay = rootView.findViewById(R.id.churchNameText_lay);
        authorNameText_lay = rootView.findViewById(R.id.authorNameText_lay);
        contactNoText_lay = rootView.findViewById(R.id.contactNoText_lay);
        startDtText_lay = rootView.findViewById(R.id.startDtText_lay);
        endDtText_lay = rootView.findViewById(R.id.endDtText_lay);
        mDialog = new ProgressDialog(mContext);
        mBottomSheetDialog = new BottomSheetDialog(mContext);
        dialogView = getLayoutInflater().inflate(R.layout.view_all_replies, null);
        mBottomSheetDialog.setContentView(dialogView);
        mBottomSheetDialog.setCancelable(false);
        bottomSheetBehavior = BottomSheetBehavior.from((View) dialogView.getParent());
        clearDialog = dialogView.findViewById(R.id.clearDialog);
        allReplyRecyclerView = dialogView.findViewById(R.id.allReplyRecyclerView);
    }

    private void setViews() {
        /**
         * @param OnClickListner
         */
        addCommentText.setOnClickListener(this);
        /**
         * @param OnClickListner
         */
        likeLay.setOnClickListener(this);
        /**
         * @param OnClickListner
         */
        diLikeLay.setOnClickListener(this);
        /**
         * @param OnClickListner
         */
        shareLay.setOnClickListener(this);
        /**
         * @param OnClickListner
         */
        clearDialog.setOnClickListener(this);

    }

    //API to get event details by EventID
    private void getEventById() {
        ChurchService service = ServiceFactory.createRetrofitService(mContext, ChurchService.class);
        String url = APIConstants.GetUpComingEventsId + SharedPrefsData.getInt(mContext, "" + Constants.eventId, Constants.PREF_NAME) + "/" + SharedPrefsData.getInt(mContext, Constants.ID, Constants.PREF_NAME);
        mSubscription = service.GetUpComingEventsId(url)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<GetUpComingEventsModel>() {
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
                    public void onNext(GetUpComingEventsModel mResponse) {

                        // to hide the Progress Dialog
                        activity.hideProgressDialog();
                        commentResponse = mResponse;
                        if (mResponse.getIsSuccess()) {
                            shareUrl = Constants.ShareUrlStaicEvent + mResponse.getResult().getEventDetails().get(0).getEventShortTitle() + "/" + mResponse.getResult().getEventDetails().get(0).getId();

                            if (mResponse.getResult().getEventDetails().get(0).getRegistrationNumber() != null) {
                                regNoText.setText(mResponse.getResult().getEventDetails().get(0).getRegistrationNumber());
                            } else {
                                regNoText_lay.setVisibility(View.GONE);
                            }
                            if (mResponse.getResult().getEventDetails().get(0).getTitle() != null) {
                                nameText.setText(mResponse.getResult().getEventDetails().get(0).getTitle());
                            } else {
                                nameText_lay.setVisibility(View.GONE);
                            }
                            if (mResponse.getResult().getEventDetails().get(0).getChurchName() != null) {
                                churchNameText.setText(mResponse.getResult().getEventDetails().get(0).getChurchName());
                            } else {
                                churchNameText_lay.setVisibility(View.GONE);
                            }
                            if (mResponse.getResult().getEventDetails().get(0).getAuthorName() != null) {
                                authorNameText.setText(mResponse.getResult().getEventDetails().get(0).getAuthorName());
                            } else {
                                authorNameText_lay.setVisibility(View.GONE);
                            }
                            if (mResponse.getResult().getEventDetails().get(0).getMobileNumber() != null) {
                                contactNumberTitle.setText(getString(R.string.mobile_number_event));
                                contactNoText.setText(mResponse.getResult().getEventDetails().get(0).getMobileNumber());
                            } else {
                                if (mResponse.getResult().getEventDetails().get(0).getContactNumber() != null) {
                                    contactNumberTitle.setText(getString(R.string.contact_number));
                                    contactNoText.setText(mResponse.getResult().getEventDetails().get(0).getContactNumber());
                                } else {
                                    contactNoText_lay.setVisibility(View.GONE);
                                }

                            }
                            if (mResponse.getResult().getEventDetails().get(0).getDescription() != null) {
                                descriText.setText(mResponse.getResult().getEventDetails().get(0).getDescription());
                            } else {
                                descriText_lay.setVisibility(View.GONE);
                            }

                            startDtText.setText(CommonUtil.formatDateTimeUi(mResponse.getResult().getEventDetails().get(0).getStartDate()));
                            endDtText.setText(CommonUtil.formatDateTimeUi(mResponse.getResult().getEventDetails().get(0).getEndDate()));

                            likeText.setText("" + mResponse.getResult().getEventDetails().get(0).getLikeCount());
                            disLikeText.setText("" + mResponse.getResult().getEventDetails().get(0).getDisLikeCount());

                            if (mResponse.getResult().getEventDetails().get(0).getIsLike().equals(0)) {
                                likeImg.setImageResource(R.drawable.ic_thumb_up_black_24dp);
                            } else {
                                likeImg.setImageResource(R.drawable.ic_thumb_up_red);
                            }
                            if (mResponse.getResult().getEventDetails().get(0).getIsDisLike().equals(0)) {
                                disLikeImg.setImageResource(R.drawable.ic_thumb_down_black_24dp);
                            } else {
                                disLikeImg.setImageResource(R.drawable.ic_thumb_down_red);
                            }
                            LinearLayoutManager mLayoutManager = new LinearLayoutManager(mContext);
                            commentRecyclerView.setHasFixedSize(true);
                            commentRecyclerView.setLayoutManager(mLayoutManager);
                            eventsCommentAdapter = new EventsCommentAdapter(mContext, mResponse, "Post", null);
                            commentRecyclerView.setAdapter(eventsCommentAdapter);
                            eventsCommentAdapter.notifyDataSetChanged();
                            eventsCommentAdapter.setOnCartChangedListener(EventDetailsFragment.this);
                            commentTitleText.setText(mResponse.getResult().getCommentDetails().size() + " " + getString(R.string.comments));
                            imgUrl = "" + mResponse.getResult().getEventDetails().get(0).getEventImage();
                            titleText.setText(mResponse.getResult().getEventDetails().get(0).getTitle());
                            /*
                             * presenting the show case view
                             * */
                            presentShowcaseSequence();
                        }

                    }
                });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.addCommentText:
                if (value == 0) {
                    //dialog box to show login
                    LayoutInflater layoutInflater = LayoutInflater.from(mContext);
                    View dialogRootView = layoutInflater.inflate(R.layout.dialog_like, null);
                    final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
                    dialogMessage = dialogRootView.findViewById(R.id.dialogMessage);
                    dialogMessage.setText(getString(R.string.please_login_to_comment));
                    ok_btn = dialogRootView.findViewById(R.id.ok_btn);
                    cancel_btn = dialogRootView.findViewById(R.id.cancel_btn);

                    alertDialogBuilder.setView(dialogRootView);

                    /**
                     * @param OnClickListner
                     */
                    ok_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(mContext, LoginActivity.class);
                            startActivity(intent);
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
                } else {

                    LayoutInflater layoutInflater = LayoutInflater.from(mContext);
                    View dialogRootView = layoutInflater.inflate(R.layout.add_dialog_comment, null);
                    final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
                    ok_btn = dialogRootView.findViewById(R.id.ok_btn);
                    cancel_btn = dialogRootView.findViewById(R.id.cancel_btn);
                    addCommentTxt = dialogRootView.findViewById(R.id.addCommentTxt);
                    alertDialogBuilder.setView(dialogRootView);

                    /**
                     * @param OnClickListner
                     */
                    ok_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (addCommentTxt.getText().length() == 0) {
                                Toast.makeText(mContext, "Please Add Comments", Toast.LENGTH_SHORT).show();
                            } else {
                                comingFrom = "add";

                                // to start the Progress Dialog
                                activity.showProgressDialog();

                                //API to ADD/UPDATE comments post
                                addUpdateComments();

                            }
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
                break;


            case R.id.likeLay:

                if (value == 0) {
                    // PopUpDailog to show message please login to like for events
                    LayoutInflater layoutInflater = LayoutInflater.from(mContext);
                    View dialogRootView = layoutInflater.inflate(R.layout.dialog_like, null);
                    final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
                    ok_btn = (Button) dialogRootView.findViewById(R.id.ok_btn);
                    cancel_btn = (Button) dialogRootView.findViewById(R.id.cancel_btn);

                    alertDialogBuilder.setView(dialogRootView);

                    /**
                     * @param OnClickListner
                     */
                    ok_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(mContext, LoginActivity.class);
                            startActivity(intent);
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
                } else {
                    like = true;
                    disLike = false;

                    // to start the Progress Dialog
                    activity.showProgressDialog();


                    //API to LIKE/DISLIKE post
                    likeOrDislikePost();


                    if (like = true) {
                        likeImg.setImageResource(R.drawable.ic_thumb_up_red);
                        disLikeImg.setImageResource(R.drawable.ic_thumb_down_black_24dp);
                    } else {
                        likeImg.setImageResource(R.drawable.ic_thumb_up_black_24dp);
                        disLikeImg.setImageResource(R.drawable.ic_thumb_down_red);
                    }

                }


                break;

            case R.id.diLikeLay:
                if (value == 0) {
                    // PopUpDailog to show message please login to dislike for event
                    LayoutInflater layoutInflater = LayoutInflater.from(mContext);
                    View dialogRootView = layoutInflater.inflate(R.layout.dialog_like, null);
                    final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
                    dialogMessage = dialogRootView.findViewById(R.id.dialogMessage);
                    dialogMessage.setText(getString(R.string.please_login_to_dislike));
                    ok_btn = (Button) dialogRootView.findViewById(R.id.ok_btn);
                    cancel_btn = (Button) dialogRootView.findViewById(R.id.cancel_btn);
                    alertDialogBuilder.setView(dialogRootView);

                    /**
                     * @param OnClickListner
                     */
                    ok_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(mContext, LoginActivity.class);
                            startActivity(intent);
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
                } else {
                    like = false;
                    disLike = true;

                    // to start the Progress Dialog
                    activity.showProgressDialog();

                    //API to LIKE/DISLIKE post
                    likeOrDislikePost();


                    if (disLike = true) {
                        disLikeImg.setImageResource(R.drawable.ic_thumb_down_red);
                        likeImg.setImageResource(R.drawable.ic_thumb_up_black_24dp);
                    } else {
                        disLikeImg.setImageResource(R.drawable.ic_thumb_down_black_24dp);
                        likeImg.setImageResource(R.drawable.ic_thumb_up_red);
                    }
                }

                break;

            case R.id.shareLay:

                // we can share post
                Intent share = new Intent(android.content.Intent.ACTION_SEND);
                share.setType("text/plain");
                share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                // Add data to the intent, the receiving app will decide
                // what to do with it.
                share.putExtra(Intent.EXTRA_SUBJECT, "Title Of The Post");
                share.putExtra(Intent.EXTRA_TEXT, shareUrl);
                startActivity(Intent.createChooser(share, "Share link!"));
                break;

            case R.id.clearDialog:
                mBottomSheetDialog.dismiss();
                break;
        }
    }

    @Override
    public void setCartClickListener(String status, int position) {
        listPosition = position;
        if (status.equalsIgnoreCase("position")) {
            /*
             * intent value passing to YoutubePlayerActivity
             * @param  document
             */
            startActivity(new Intent(mContext, PdfReaderActivity.class).putExtra("document", documentList.get(position)));
        } else if (status.equalsIgnoreCase("images")) {
            /*
             * intent value passing to YoutubePlayerActivity
             * @param  audio
             */
            startActivity(new Intent(mContext, AudioActivity.class).putExtra("audio", audioList.get(position)));
        }
        if (status.equalsIgnoreCase("edit")) {
            if ("" + userId != null && userId != 0) {
                // PopUpDailog to show message edit comment
                if (commentResponse.getResult().getCommentDetails().get(listPosition).getUserId().equals(userId)) {
                    commentID = commentResponse.getResult().getCommentDetails().get(listPosition).getId();
                    if (" " + parentCommentId != null && parentCommentId != 0)
                        parentCommentId = commentResponse.getResult().getCommentDetails().get(listPosition).getParentCommentId();
                    LayoutInflater layoutInflater = LayoutInflater.from(mContext);
                    View dialogRootView = layoutInflater.inflate(R.layout.add_dialog_comment, null);
                    final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
                    ok_btn = (Button) dialogRootView.findViewById(R.id.ok_btn);
                    cancel_btn = (Button) dialogRootView.findViewById(R.id.cancel_btn);
                    addCommentTxt = (EditText) dialogRootView.findViewById(R.id.addCommentTxt);
                    addCommentTxt.setText(commentResponse.getResult().getCommentDetails().get(listPosition).getComment());
                    alertDialogBuilder.setView(dialogRootView);
                    /**
                     * @param OnClickListner
                     */
                    ok_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (addCommentTxt.getText().length() == 0) {
                                Toast.makeText(mContext, "Please Add Comments", Toast.LENGTH_SHORT).show();
                            } else {
                                comingFrom = "edit";

                                // to start the Progress Dialog
                                activity.showProgressDialog();

                                //API to ADD/UPDATE comments post
                                addUpdateComments();

                            }
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

            } else {
                // PopUpDailog to show message please login to eidt comment
                LayoutInflater layoutInflater = LayoutInflater.from(mContext);
                View dialogRootView = layoutInflater.inflate(R.layout.dialog_like, null);
                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
                dialogMessage = dialogRootView.findViewById(R.id.dialogMessage);
                dialogMessage.setText(getString(R.string.please_login_to_edit));
                ok_btn = (Button) dialogRootView.findViewById(R.id.ok_btn);
                cancel_btn = (Button) dialogRootView.findViewById(R.id.cancel_btn);

                alertDialogBuilder.setView(dialogRootView);

                /**
                 * @param OnClickListner
                 */
                ok_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(mContext, LoginActivity.class);
                        startActivity(intent);
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
        } else if (status.equalsIgnoreCase("delete")) {
            if ("" + userId != null && userId != 0) {

                // PopUpDailog to show message delete comment permanently

                if (commentResponse.getResult().getCommentDetails().get(listPosition).getUserId().equals(userId)) {
                    commentID = commentResponse.getResult().getCommentDetails().get(listPosition).getId();
                    LayoutInflater layoutInflater = LayoutInflater.from(mContext);
                    View dialogRootView = layoutInflater.inflate(R.layout.delete_dialog, null);
                    final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
                    ok_btn = (Button) dialogRootView.findViewById(R.id.ok_btn);
                    cancel_btn = (Button) dialogRootView.findViewById(R.id.cancel_btn);
                    alertDialogBuilder.setView(dialogRootView);
                    /**
                     * @param OnClickListner
                     */
                    ok_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            // to start the Progress Dialog
                            activity.showProgressDialog();

                            //API to delete comments
                            eventDeleteComments();

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
            } else {

                // PopUpDailog to show message please login to delete comment
                LayoutInflater layoutInflater = LayoutInflater.from(mContext);
                View dialogRootView = layoutInflater.inflate(R.layout.dialog_like, null);
                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
                dialogMessage = dialogRootView.findViewById(R.id.dialogMessage);
                dialogMessage.setText(getString(R.string.please_login_to_delete));
                ok_btn = (Button) dialogRootView.findViewById(R.id.ok_btn);
                cancel_btn = (Button) dialogRootView.findViewById(R.id.cancel_btn);

                alertDialogBuilder.setView(dialogRootView);

                /**
                 * @param OnClickListner
                 */
                ok_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(mContext, LoginActivity.class);
                        startActivity(intent);
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


        } else if (status.equalsIgnoreCase("viewAllReplies")) {
            if ("" + userId != null && userId != 0) {
                commentID = commentResponse.getResult().getCommentDetails().get(listPosition).getId();
                mBottomSheetDialog.show();

                // to start the Progress Dialog
                activity.showProgressDialog();


                //API to get comment relipes
                getPostCommentReplies();
            } else {

                // PopUpDailog to show message please login to view all replies
                LayoutInflater layoutInflater = LayoutInflater.from(mContext);
                View dialogRootView = layoutInflater.inflate(R.layout.dialog_like, null);
                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
                dialogMessage = dialogRootView.findViewById(R.id.dialogMessage);
                dialogMessage.setText(getString(R.string.please_login_to_viewall_replies));
                ok_btn = (Button) dialogRootView.findViewById(R.id.ok_btn);
                cancel_btn = (Button) dialogRootView.findViewById(R.id.cancel_btn);

                alertDialogBuilder.setView(dialogRootView);
                /**
                 * @param OnClickListner
                 */
                ok_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(mContext, LoginActivity.class);
                        startActivity(intent);
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


        } else if (status.equalsIgnoreCase("reply")) {
            if ("" + userId != null && userId != 0) {
                commentID = commentResponse.getResult().getCommentDetails().get(listPosition).getId();
                if (" " + parentCommentId != null && parentCommentId != 0)
                    parentCommentId = commentResponse.getResult().getCommentDetails().get(listPosition).getParentCommentId();
                if (value == 0) {

                    //popup dialog please login to comments
                    LayoutInflater layoutInflater = LayoutInflater.from(mContext);
                    View dialogRootView = layoutInflater.inflate(R.layout.dialog_like, null);
                    final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
                    dialogMessage = dialogRootView.findViewById(R.id.dialogMessage);
                    dialogMessage.setText(getString(R.string.please_login_to_comment));
                    ok_btn = (Button) dialogRootView.findViewById(R.id.ok_btn);
                    cancel_btn = (Button) dialogRootView.findViewById(R.id.cancel_btn);

                    alertDialogBuilder.setView(dialogRootView);

                    /**
                     * @param OnClickListner
                     */
                    ok_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            SharedPrefsData.putInt(mContext, Constants.ISFROM, 1, Constants.PREF_NAME);
                            Intent intent = new Intent(mContext, LoginActivity.class);
                            startActivity(intent);
                            getActivity().finish();
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
                } else {

                    //popup dialog to show message add a reply comments
                    LayoutInflater layoutInflater = LayoutInflater.from(mContext);
                    View dialogRootView = layoutInflater.inflate(R.layout.add_dialog_reply_comment, null);
                    final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
                    ok_btn = (Button) dialogRootView.findViewById(R.id.ok_btn);
                    cancel_btn = (Button) dialogRootView.findViewById(R.id.cancel_btn);
                    addCommentTxt = (EditText) dialogRootView.findViewById(R.id.addCommentTxt);
                    alertDialogBuilder.setView(dialogRootView);

                    /**
                     * @param OnClickListner
                     */
                    ok_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (addCommentTxt.getText().length() == 0) {
                                Toast.makeText(mContext, "Please Add Comments", Toast.LENGTH_SHORT).show();
                            } else {
                                comingFrom = "reply";

                                // to start the Progress Dialog
                                activity.showProgressDialog();

                                //API to ADD/UPDATE comments post
                                addUpdateComments();
                            }
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
            } else {

                // PopUpDailog to show message please login to reply for the comments
                LayoutInflater layoutInflater = LayoutInflater.from(mContext);
                View dialogRootView = layoutInflater.inflate(R.layout.dialog_like, null);
                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
                dialogMessage = dialogRootView.findViewById(R.id.dialogMessage);
                dialogMessage.setText(getString(R.string.please_login_to_reply));
                ok_btn = dialogRootView.findViewById(R.id.ok_btn);
                cancel_btn = dialogRootView.findViewById(R.id.cancel_btn);

                alertDialogBuilder.setView(dialogRootView);

                /**
                 * @param OnClickListner
                 */
                ok_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(mContext, LoginActivity.class);
                        startActivity(intent);
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

        }
    }

    private void getPostCommentReplies() {
        //API to get comment relipes

        if (authorizationToken != "null" || authorizationToken != null || authorizationToken.isEmpty())
            authorizationToken = SharedPrefsData.getInstance(mContext).getStringFromSharedPrefs(Constants.Auth_Token);
        ChurchService service = ServiceFactory.createRetrofitService(mContext, ChurchService.class);
        String url = APIConstants.GetEventCommentReplies + commentID;
        mSubscription = service.GetEventCommentReplies(url, authorizationToken)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<EventViewAllRepliesResponseModel>() {
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
                    public void onNext(EventViewAllRepliesResponseModel mResponse) {

                        // to hide the Progress Dialog
                        activity.hideProgressDialog();
                        LinearLayoutManager mLayoutManager = new LinearLayoutManager(mContext);
                        allReplyRecyclerView.setHasFixedSize(true);
                        allReplyRecyclerView.setLayoutManager(mLayoutManager);
                        eventsViewAllRepliesAdapter = new EventsViewAllRepliesAdapter(mContext, null, "ViewAllReplies", mResponse);
                        allReplyRecyclerView.setAdapter(eventsViewAllRepliesAdapter);
                        eventsViewAllRepliesAdapter.notifyDataSetChanged();
                    }
                });
    }

    //API to LIKE/DISLIKE post
    private void likeOrDislikePost() {
        if (authorizationToken != "null" || authorizationToken != null || authorizationToken.isEmpty())
            authorizationToken = SharedPrefsData.getInstance(mContext).getStringFromSharedPrefs(Constants.Auth_Token);
        JsonObject object = likePostObject();
        ChurchService service = ServiceFactory.createRetrofitService(mContext, ChurchService.class);
        mSubscription = service.likeOrDislikePostEvents(object, authorizationToken)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<LikeDislikeResponeModel>() {
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
                    public void onNext(LikeDislikeResponeModel mResponse) {

                        // to hide the Progress Dialog
                        activity.hideProgressDialog();
                        if (mResponse.getIsSuccess()) {
                            mModel = mResponse;
                            getEventById();
                            //Toast.makeText(mContext, "" + mResponse.getEndUserMessage(), Toast.LENGTH_SHORT).show();

                        }

                    }

                });
    }

    //API request object to LIKE/DISLIKE post
    private JsonObject likePostObject() {
        LikeDisLikeRequestModel mRequest = new LikeDisLikeRequestModel();
        mRequest.setUserId(userId);
        mRequest.setLike(like);
        mRequest.setDisLike(disLike);
        mRequest.setEventId(eventId);
        return new Gson().toJsonTree(mRequest).getAsJsonObject();
    }

    //API to ADD/UPDATE comments post
    private void addUpdateComments() {
        if (authorizationToken != "null" || authorizationToken != null || authorizationToken.isEmpty())
            authorizationToken = SharedPrefsData.getInstance(mContext).getStringFromSharedPrefs(Constants.Auth_Token);
        JsonObject object = addUpdateCommentObject();
        ChurchService service = ServiceFactory.createRetrofitService(mContext, ChurchService.class);
        mSubscription = service.EventAddUpdateComments(object, authorizationToken)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<EventsCommentResponseModel>() {
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
                    public void onNext(EventsCommentResponseModel mResponse) {

                        // to hide the Progress Dialog
                        activity.hideProgressDialog();
                        if (mResponse.getIsSuccess()) {

                            // to start the Progress Dialog
                            activity.showProgressDialog();
                            getEventById();
                            alertDialog.dismiss();
                            alertDialogTest.dismiss();
                            Toast.makeText(mContext, "" + mResponse.getEndUserMessage(), Toast.LENGTH_SHORT).show();

                        }

                    }

                });
    }

    //API request object to ADD/UPDATE comments post
    private JsonObject addUpdateCommentObject() {
        EventCommentRequestModel mRequest = new EventCommentRequestModel();
        if (comingFrom.equalsIgnoreCase("add")) {
            mRequest.setId(0);
            mRequest.setParentCommentId(0);
        } else if (comingFrom.equalsIgnoreCase("edit")) {
            mRequest.setId(commentID);
            mRequest.setParentCommentId(parentCommentId);
        } else {
            mRequest.setId(0);
            mRequest.setParentCommentId(commentID);
        }
        mRequest.setEventId(eventId);
        mRequest.setDescription(addCommentTxt.getText().toString());
        mRequest.setUserId(SharedPrefsData.getInt(mContext, Constants.ID, Constants.PREF_NAME));
        return new Gson().toJsonTree(mRequest).getAsJsonObject();
    }

    //API to delete comments
    private void eventDeleteComments() {
        JsonObject object = deleteObject();
        ChurchService service = ServiceFactory.createRetrofitService(mContext, ChurchService.class);
        mSubscription = service.DeleteEventComment(object)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<EventCommentDeleteResponseModel>() {
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
                    public void onNext(EventCommentDeleteResponseModel mResponse) {

                        // to hide the Progress Dialog
                        activity.hideProgressDialog();
                        if (mResponse.getIsSuccess()) {
                            alertDialog.dismiss();

                            // to start the Progress Dialog
                            activity.showProgressDialog();
                            getEventById();
                            Toast.makeText(mContext, "" + mResponse.getEndUserMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }

                });
    }

    //API request object to delete comments
    private JsonObject deleteObject() {
        EventDeleteRequestModel mRequest = new EventDeleteRequestModel();
        mRequest.setId(commentID);
        mRequest.setEventId(eventId);
        mRequest.setChurchId(null);
        mRequest.setUserId(SharedPrefsData.getInt(mContext, Constants.ID, Constants.PREF_NAME));
        return new Gson().toJsonTree(mRequest).getAsJsonObject();
    }

    /*
     * presenting the show case view
     * */
    private void presentShowcaseSequence() {

        ShowcaseConfig config = new ShowcaseConfig();
        config.setDelay(200);

        MaterialShowcaseSequence sequence = new MaterialShowcaseSequence(getActivity(), SHOWCASE_ID);

        sequence.setOnItemShownListener(new MaterialShowcaseSequence.OnSequenceItemShownListener() {

            @Override
            public void onShow(MaterialShowcaseView itemView, int position) {

            }
        });

        sequence.setConfig(config);
        sequence.addSequenceItem(
                new MaterialShowcaseView.Builder(getActivity())
                        .setTarget(likeLay)
                        .setDismissText("GOT IT")
                        .setContentText("Click Here To Like Post")
                        .setContentTextColor(getResources().getColor(R.color.mediumTurquoise))
                        .setDismissTextColor(getResources().getColor(R.color.white))
                        .withCircleShape()
                        .setShapePadding(80)
                        .build());


        sequence.addSequenceItem(
                new MaterialShowcaseView.Builder(getActivity())
                        .setTarget(diLikeLay)
                        .setDismissText("GOT IT")
                        .setContentText("Click Here To DisLike Post")
                        .withCircleShape()
                        .setContentTextColor(getResources().getColor(R.color.mediumTurquoise))
                        .setDismissTextColor(getResources().getColor(R.color.white))
                        .useFadeAnimation()
                        .setShapePadding(60)
                        .build()
        );


        sequence.addSequenceItem(
                new MaterialShowcaseView.Builder(getActivity())
                        .setTarget(shareLay)
                        .setDismissText("GOT IT")
                        .setContentText("Click Here To Share Post")
                        .setContentTextColor(getResources().getColor(R.color.mediumTurquoise))
                        .setDismissTextColor(getResources().getColor(R.color.white))
                        .withCircleShape()
                        .setShapePadding(50)
                        .build()
        );

        sequence.addSequenceItem(
                new MaterialShowcaseView.Builder(getActivity())
                        .setTarget(addCommentText)
                        .setDismissText("GOT IT")
                        .setContentText("Click Here To Add Comments For Post")
                        .setContentTextColor(getResources().getColor(R.color.mediumTurquoise))
                        .setDismissTextColor(getResources().getColor(R.color.white))
                        .withCircleShape()
                        .setShapePadding(50)
                        .build()
        );

        sequence.start();

    }
}
