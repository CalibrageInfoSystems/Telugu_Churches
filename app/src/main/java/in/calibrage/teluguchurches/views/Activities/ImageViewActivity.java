package in.calibrage.teluguchurches.views.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.crashlytics.android.Crashlytics;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;

import in.calibrage.teluguchurches.R;
import in.calibrage.teluguchurches.services.APIConstants;
import in.calibrage.teluguchurches.services.ChurchService;
import in.calibrage.teluguchurches.services.ServiceFactory;
import in.calibrage.teluguchurches.util.BaseActivity;
import in.calibrage.teluguchurches.util.Constants;
import in.calibrage.teluguchurches.util.localData.SharedPrefsData;
import in.calibrage.teluguchurches.views.adapter.CommentAdapter;
import in.calibrage.teluguchurches.views.adapter.ViewAllRepliesAdapter;
import in.calibrage.teluguchurches.views.model.CommentDeleteResponseModel;
import in.calibrage.teluguchurches.views.model.CommentResponseModel;
import in.calibrage.teluguchurches.views.model.GetPostByIdResponseModel;
import in.calibrage.teluguchurches.views.model.GetUpdateViewCountByPostIdResponseModel;
import in.calibrage.teluguchurches.views.model.LikeRequestModel;
import in.calibrage.teluguchurches.views.model.LikeResponseModel;
import in.calibrage.teluguchurches.views.model.ViewAllRepliesResponseModel;
import io.fabric.sdk.android.Fabric;
import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseSequence;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView;
import uk.co.deanwild.materialshowcaseview.ShowcaseConfig;

public class ImageViewActivity extends BaseActivity implements View.OnClickListener, CommentAdapter.OnCartChangedListener {
    private EditText input, addCommentTxt;
    private TextView addCommentText, commentTitleText, likeText, disLikeText, viewCount, titleText, deleteText, editText, dialogMessage;
    private Button ok_btn, cancel_btn;
    private ImageView toolbar_image, disLikeImg, likeImg, churchImg, clearDialog;
    private RelativeLayout shareLay, likeLay, diLikeLay;
    private RecyclerView commentRecyclerView, allReplyRecyclerView;
    private CommentAdapter adapter;
    private ViewAllRepliesAdapter adapterViewAllReplies;
    private Context mContext;
    private Toolbar toolbar;
    private int postTypeId, value, userId, commentID, listPosition, longPressSelectedItemCount = 0, position, parentCommentId;
    private boolean like, disLike;
    private ActionMode mActionMode;
    private Subscription mSubscription;
    private LikeResponseModel mModel;
    private GetPostByIdResponseModel commentResponse;
    private GetUpdateViewCountByPostIdResponseModel viewCountResponse;
    private ProgressDialog mDialog;
    private BottomSheetDialog mBottomSheetDialog;
    private View dialogView;
    BottomSheetBehavior bottomSheetBehavior;
    private AlertDialog alertDialog, alertDialogTest;
    private static final String SHOWCASE_ID = "sequence home";
    private String authorizationToken, image, imgUrl, comingFrom, shareUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getApplicationContext();

        // added crash report's to mail
        Fabric.with(getApplicationContext(), new Crashlytics());

        //assining layout
        setContentView(R.layout.activity_image_view);


        if (getIntent() != null) {
            /*
             * to check the intent value
             * @param  postTypeId
             *@param  image
             */
            postTypeId = getIntent().getIntExtra("postTypeId", 0);
            image = getIntent().getStringExtra("image");
        }
        userId = SharedPrefsData.getInt(ImageViewActivity.this, Constants.ID, Constants.PREF_NAME);
        value = SharedPrefsData.getInt(ImageViewActivity.this, Constants.ISLOGIN, Constants.PREF_NAME);

        /* intializing and assigning ID's */
        initViews();

        /* Navigation's and using the views */
        setViews();
    }

    /* intializing and assigning ID's */
    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
            public void onClick(View v) {
                Intent intent = new Intent(ImageViewActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });
        commentRecyclerView = findViewById(R.id.commentRecyclerView);
        addCommentText = findViewById(R.id.addCommentText);
        commentTitleText = findViewById(R.id.commentTitleText);
        viewCount = findViewById(R.id.viewCount);
        titleText = findViewById(R.id.titleText);
        likeText = findViewById(R.id.likeText);
        disLikeText = findViewById(R.id.disLikeText);
        likeLay = findViewById(R.id.likeLay);
        diLikeLay = findViewById(R.id.diLikeLay);
        shareLay = findViewById(R.id.shareLay);
        disLikeImg = findViewById(R.id.disLikeImg);
        likeImg = findViewById(R.id.likeImg);
        churchImg = findViewById(R.id.churchImg);

        mDialog = new ProgressDialog(ImageViewActivity.this);

        Glide.with(getApplicationContext()).load(image)
                .fitCenter()
                .error(R.drawable.loginimage)
                .into(churchImg);

        mBottomSheetDialog = new BottomSheetDialog(ImageViewActivity.this);
        dialogView = getLayoutInflater().inflate(R.layout.view_all_replies, null);
        mBottomSheetDialog.setContentView(dialogView);
        mBottomSheetDialog.setCancelable(false);
        bottomSheetBehavior = BottomSheetBehavior.from((View) dialogView.getParent());
        clearDialog = dialogView.findViewById(R.id.clearDialog);
        allReplyRecyclerView = dialogView.findViewById(R.id.allReplyRecyclerView);

        // to start the Progress Dialog
        showProgressDialog();

        // API to get Particular post details
        getPostById();

        // API to get ViewCount for Particular post
        getViewCountPostId();
    }

    /* Navigation's and using the views */
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

    // API to get Particular post details
    private void getPostById() {
        ChurchService service = ServiceFactory.createRetrofitService(getApplicationContext(), ChurchService.class);
        final String url = APIConstants.GetPostById + postTypeId + "/" + SharedPrefsData.getInt(getApplicationContext(), Constants.ID, Constants.PREF_NAME);
        mSubscription = service.GetPostById(url)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<GetPostByIdResponseModel>() {
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
                    public void onNext(GetPostByIdResponseModel mResponse) {

                        // to hide the Progress Dialog
                        hideProgressDialog();
                        commentResponse = mResponse;
                        shareUrl = Constants.ShareUrlStaic + mResponse.getResult().getPostDetails().get(0).getPostShortTitle();
                        toolbar.setTitle(mResponse.getResult().getPostDetails().get(0).getTitle());
                        likeText.setText("" + mResponse.getResult().getPostDetails().get(0).getLikeCount());
                        disLikeText.setText("" + mResponse.getResult().getPostDetails().get(0).getDisLikeCount());
                        if (mResponse.getResult().getPostDetails().get(0).getIsLike().equals(0)) {
                            likeImg.setImageResource(R.drawable.ic_thumb_up_black_24dp);
                        } else {
                            likeImg.setImageResource(R.drawable.ic_thumb_up_red);
                        }
                        if (mResponse.getResult().getPostDetails().get(0).getIsDisLike().equals(0)) {
                            disLikeImg.setImageResource(R.drawable.ic_thumb_down_black_24dp);
                        } else {
                            disLikeImg.setImageResource(R.drawable.ic_thumb_down_red);
                        }
                        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                        commentRecyclerView.setHasFixedSize(true);
                        commentRecyclerView.setLayoutManager(mLayoutManager);
                        adapter = new CommentAdapter(getApplicationContext(), mResponse, "Post", null);
                        commentRecyclerView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                        adapter.setOnCartChangedListener(ImageViewActivity.this);
                        commentTitleText.setText(mResponse.getResult().getCommentDetails().size() + " " + getString(R.string.comments));
                        imgUrl = "" + mResponse.getResult().getPostDetails().get(0).getPostImage();
                        titleText.setText(mResponse.getResult().getPostDetails().get(0).getTitle());

                        /**
                         * Showcase view for suggestions
                         */
                        presentShowcaseSequence();

                    }
                });

    }


    // API to get ViewCount for Particular post
    public void getViewCountPostId() {

        ChurchService service = ServiceFactory.createRetrofitService(getApplicationContext(), ChurchService.class);
        String url = APIConstants.UpdateViewCountByPostId + postTypeId;
        mSubscription = service.GetUpdateViewCountByPostId(url)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<GetUpdateViewCountByPostIdResponseModel>() {
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
                    public void onNext(GetUpdateViewCountByPostIdResponseModel mResponse) {
                        mDialog.dismiss();
                        viewCountResponse = mResponse;
                        if (mResponse.getListResult().get(0).getViewCount() != null && mResponse.getListResult().get(0).getViewCount() > 0) {
                            viewCount.setText(mResponse.getListResult().get(0).getViewCount() + " " + getString(R.string.views));
                            viewCount.setVisibility(View.VISIBLE);
                        } else {
                            viewCount.setVisibility(View.GONE);
                        }


                    }
                });

    }

    // API to get Like or Disk-like for the Post
    private void likeOrDislikePost() {
        if (authorizationToken != "null" || authorizationToken != null || authorizationToken.isEmpty())
            authorizationToken = SharedPrefsData.getInstance(mContext).getStringFromSharedPrefs(Constants.Auth_Token);
        JsonObject object = likePostObject();
        ChurchService service = ServiceFactory.createRetrofitService(getApplicationContext(), ChurchService.class);
        mSubscription = service.likeOrDislikePost(object, authorizationToken)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<LikeResponseModel>() {
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
                    public void onNext(LikeResponseModel mResponse) {
                        if (mResponse.getIsSuccess()) {
                            mModel = mResponse;

                            // API to get Particular post details
                            getPostById();


                        }

                    }

                });
    }

    /**
     * Json Object of likePostObject
     *
     * @return
     */
    private JsonObject likePostObject() {
        LikeRequestModel mRequest = new LikeRequestModel();
        mRequest.setUserId("" + SharedPrefsData.getInt(getApplicationContext(), Constants.ID, Constants.PREF_NAME));
        mRequest.setPostId(postTypeId);
        mRequest.setLike1(like);
        mRequest.setDisLike(disLike);
        return new Gson().toJsonTree(mRequest).getAsJsonObject();
    }

    // API to get add a public comments
    private void addUpdateComments() {
        if (authorizationToken != "null" || authorizationToken != null || authorizationToken.isEmpty())
            authorizationToken = SharedPrefsData.getInstance(mContext).getStringFromSharedPrefs(Constants.Auth_Token);
        JsonObject object = addUpdateCommentObject();
        ChurchService service = ServiceFactory.createRetrofitService(getApplicationContext(), ChurchService.class);
        mSubscription = service.AddUpdateComments(object, authorizationToken)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<CommentResponseModel>() {
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
                    public void onNext(CommentResponseModel mResponse) {

                        // to hide the Progress Dialog
                        hideProgressDialog();
                        mDialog.dismiss();
                        if (mResponse.getIsSuccess()) {

                            // API to get Particular post details
                            getPostById();
                            alertDialog.dismiss();
                            alertDialogTest.dismiss();
                            Toast.makeText(ImageViewActivity.this, "" + mResponse.getEndUserMessage(), Toast.LENGTH_SHORT).show();

                        }

                    }

                });
    }

    /**
     * Json Object of addUpdateCommentObject
     *
     * @return
     */
    private JsonObject addUpdateCommentObject() {
        LikeRequestModel mRequest = new LikeRequestModel();
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
        mRequest.setPostId(postTypeId);
        mRequest.setDescription(addCommentTxt.getText().toString());
        mRequest.setUserId("" + SharedPrefsData.getInt(getApplicationContext(), Constants.ID, Constants.PREF_NAME));
        return new Gson().toJsonTree(mRequest).getAsJsonObject();
    }

    // API to get delete comments
    private void deleteComments() {
        JsonObject object = deleteObject();
        ChurchService service = ServiceFactory.createRetrofitService(getApplicationContext(), ChurchService.class);
        mSubscription = service.DeleteComments(object)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<CommentDeleteResponseModel>() {
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
                    public void onNext(CommentDeleteResponseModel mResponse) {

                        // to hide the Progress Dialog
                        hideProgressDialog();
                        if (mResponse.getIsSuccess()) {
                            alertDialog.dismiss();

                            // to start the Progress Dialog
                            showProgressDialog();

                            // API to get Particular post details
                            getPostById();
                            Toast.makeText(ImageViewActivity.this, "" + mResponse.getEndUserMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }

                });
    }

    /**
     * Json Object of deleteObject
     *
     * @return
     */
    private JsonObject deleteObject() {
        LikeRequestModel mRequest = new LikeRequestModel();
        mRequest.setId(commentID);
        mRequest.setPostId(postTypeId);
        mRequest.setChurchId(null);
        mRequest.setUserId("" + SharedPrefsData.getInt(getApplicationContext(), Constants.ID, Constants.PREF_NAME));
        return new Gson().toJsonTree(mRequest).getAsJsonObject();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.addCommentText:
                if (value == 0) {
                    // PopUpDailog to show message please login to comment
                    LayoutInflater layoutInflater = LayoutInflater.from(ImageViewActivity.this);
                    View dialogRootView = layoutInflater.inflate(R.layout.dialog_like, null);
                    final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ImageViewActivity.this);
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
                            Intent intent = new Intent(ImageViewActivity.this, LoginActivity.class);
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

                    // PopUpDailog to show message add a public comment
                    LayoutInflater layoutInflater = LayoutInflater.from(ImageViewActivity.this);
                    View dialogRootView = layoutInflater.inflate(R.layout.add_dialog_comment, null);
                    final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ImageViewActivity.this);
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
                                Toast.makeText(ImageViewActivity.this, "Please Add Comments", Toast.LENGTH_SHORT).show();
                            } else {
                                comingFrom = "add";
                                // to start the Progress Dialog
                                showProgressDialog();

                                // API to get add a public comments
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

                // PopUpDailog to show message please login to like for image
                if (value == 0) {
                    LayoutInflater layoutInflater = LayoutInflater.from(ImageViewActivity.this);
                    View dialogRootView = layoutInflater.inflate(R.layout.dialog_like, null);
                    final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ImageViewActivity.this);
                    ok_btn = dialogRootView.findViewById(R.id.ok_btn);
                    cancel_btn = dialogRootView.findViewById(R.id.cancel_btn);

                    alertDialogBuilder.setView(dialogRootView);


/**
 * @param OnClickListner
 */
                    ok_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(ImageViewActivity.this, LoginActivity.class);
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
                    // API to get Like or Disk-like for the Post
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
                    // PopUpDailog to show message please login to dislike for image
                    LayoutInflater layoutInflater = LayoutInflater.from(ImageViewActivity.this);
                    View dialogRootView = layoutInflater.inflate(R.layout.dialog_like, null);
                    final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ImageViewActivity.this);
                    dialogMessage = dialogRootView.findViewById(R.id.dialogMessage);
                    dialogMessage.setText(getString(R.string.please_login_to_dislike));
                    ok_btn = dialogRootView.findViewById(R.id.ok_btn);
                    cancel_btn = dialogRootView.findViewById(R.id.cancel_btn);

                    alertDialogBuilder.setView(dialogRootView);

/**
 * @param OnClickListner
 */
                    ok_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(ImageViewActivity.this, LoginActivity.class);
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

                    // API to get Like or Disk-like for the Post
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

                // we can share the post
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

    // API to get add a reply comments
    private void getPostCommentReplies() {
        if (authorizationToken != "null" || authorizationToken != null || authorizationToken.isEmpty())
            authorizationToken = SharedPrefsData.getInstance(mContext).getStringFromSharedPrefs(Constants.Auth_Token);
        ChurchService service = ServiceFactory.createRetrofitService(getApplicationContext(), ChurchService.class);
        String url = APIConstants.GetPostCommentReplies + commentID;
        mSubscription = service.GetPostCommentReplies(url, authorizationToken)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ViewAllRepliesResponseModel>() {
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
                    public void onNext(ViewAllRepliesResponseModel mResponse) {
                        mDialog.dismiss();
                        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                        allReplyRecyclerView.setHasFixedSize(true);
                        allReplyRecyclerView.setLayoutManager(mLayoutManager);
                        adapterViewAllReplies = new ViewAllRepliesAdapter(getApplicationContext(), null, "ViewAllReplies", mResponse);
                        allReplyRecyclerView.setAdapter(adapterViewAllReplies);
                        adapterViewAllReplies.notifyDataSetChanged();


                    }
                });
    }

    // Interface between adapter and class
    @Override
    public void setCartClickListener(String status, final int position) {
        listPosition = position;
        if (status.equalsIgnoreCase("edit")) {
            if ("" + userId != null && userId != 0) {

                // PopUpDailog to show message edit comment
                if (commentResponse.getResult().getCommentDetails().get(listPosition).getUserId().equals(userId)) {
                    commentID = commentResponse.getResult().getCommentDetails().get(listPosition).getId();
                    LayoutInflater layoutInflater = LayoutInflater.from(ImageViewActivity.this);
                    View dialogRootView = layoutInflater.inflate(R.layout.add_dialog_comment, null);
                    final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ImageViewActivity.this);
                    ok_btn = dialogRootView.findViewById(R.id.ok_btn);
                    cancel_btn = dialogRootView.findViewById(R.id.cancel_btn);
                    addCommentTxt = dialogRootView.findViewById(R.id.addCommentTxt);
                    addCommentTxt.setText(commentResponse.getResult().getCommentDetails().get(listPosition).getComment());
                    alertDialogBuilder.setView(dialogRootView);

/**
 * @param OnClickListner
 */
                    ok_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (addCommentTxt.getText().length() == 0) {
                                Toast.makeText(ImageViewActivity.this, "Please Add Comments", Toast.LENGTH_SHORT).show();
                            } else {
                                comingFrom = "edit";
                                mDialog.setMessage("Please wait...");
                                mDialog.show();

                                // API to get add a public comments
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
                LayoutInflater layoutInflater = LayoutInflater.from(ImageViewActivity.this);
                View dialogRootView = layoutInflater.inflate(R.layout.dialog_like, null);
                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ImageViewActivity.this);
                dialogMessage = dialogRootView.findViewById(R.id.dialogMessage);
                dialogMessage.setText(getString(R.string.please_login_to_edit));
                ok_btn = dialogRootView.findViewById(R.id.ok_btn);
                cancel_btn = dialogRootView.findViewById(R.id.cancel_btn);

                alertDialogBuilder.setView(dialogRootView);


/**
 * @param OnClickListner
 */
                ok_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(ImageViewActivity.this, LoginActivity.class);
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
                    LayoutInflater layoutInflater = LayoutInflater.from(ImageViewActivity.this);
                    View dialogRootView = layoutInflater.inflate(R.layout.delete_dialog, null);
                    final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ImageViewActivity.this);
                    ok_btn = dialogRootView.findViewById(R.id.ok_btn);
                    cancel_btn = dialogRootView.findViewById(R.id.cancel_btn);
                    alertDialogBuilder.setView(dialogRootView);

/**
 * @param OnClickListner
 */
                    ok_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            // to start the Progress Dialog
                            showProgressDialog();

                            // API to get delete comments
                            deleteComments();

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
                LayoutInflater layoutInflater = LayoutInflater.from(ImageViewActivity.this);
                View dialogRootView = layoutInflater.inflate(R.layout.dialog_like, null);
                dialogMessage = dialogRootView.findViewById(R.id.dialogMessage);
                dialogMessage.setText(getString(R.string.please_login_to_delete));
                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ImageViewActivity.this);
                ok_btn = dialogRootView.findViewById(R.id.ok_btn);
                cancel_btn = dialogRootView.findViewById(R.id.cancel_btn);

                alertDialogBuilder.setView(dialogRootView);


/**
 * @param OnClickListner
 */
                ok_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(ImageViewActivity.this, LoginActivity.class);
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

                // API to get add a reply comments
                getPostCommentReplies();
            } else {

                // PopUpDailog to show message please login to view all replies
                LayoutInflater layoutInflater = LayoutInflater.from(ImageViewActivity.this);
                View dialogRootView = layoutInflater.inflate(R.layout.dialog_like, null);
                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ImageViewActivity.this);
                dialogMessage = dialogRootView.findViewById(R.id.dialogMessage);
                dialogMessage.setText(getString(R.string.please_login_to_viewall_replies));
                ok_btn = dialogRootView.findViewById(R.id.ok_btn);
                cancel_btn = dialogRootView.findViewById(R.id.cancel_btn);

                alertDialogBuilder.setView(dialogRootView);

/**
 * @param OnClickListner
 */
                ok_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(ImageViewActivity.this, LoginActivity.class);
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
                    LayoutInflater layoutInflater = LayoutInflater.from(ImageViewActivity.this);
                    View dialogRootView = layoutInflater.inflate(R.layout.dialog_like, null);
                    final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ImageViewActivity.this);
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
                            SharedPrefsData.putInt(getApplicationContext(), Constants.ISFROM, 1, Constants.PREF_NAME);
                            Intent intent = new Intent(ImageViewActivity.this, LoginActivity.class);
                            startActivity(intent);
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
                } else {

                    //popup dialog to show message add a reply comments
                    LayoutInflater layoutInflater = LayoutInflater.from(ImageViewActivity.this);
                    View dialogRootView = layoutInflater.inflate(R.layout.add_dialog_reply_comment, null);
                    final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ImageViewActivity.this);
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
                                Toast.makeText(ImageViewActivity.this, "Please Add Comments", Toast.LENGTH_SHORT).show();
                            } else {
                                comingFrom = "reply";
                                mDialog.setMessage("Please wait...");
                                mDialog.show();

                                // API to get add a public comments
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
                LayoutInflater layoutInflater = LayoutInflater.from(ImageViewActivity.this);
                View dialogRootView = layoutInflater.inflate(R.layout.dialog_like, null);
                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ImageViewActivity.this);
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
                        Intent intent = new Intent(ImageViewActivity.this, LoginActivity.class);
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
                        .setTarget(likeLay)
                        .setDismissText("GOT IT")
                        .setContentText("Click Here To Like Post")
                        .setContentTextColor(getResources().getColor(R.color.mediumTurquoise))
                        .setDismissTextColor(getResources().getColor(R.color.white))
                        .withCircleShape()
                        .setShapePadding(80)
                        .build());


        sequence.addSequenceItem(
                new MaterialShowcaseView.Builder(this)
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
                new MaterialShowcaseView.Builder(this)
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
                new MaterialShowcaseView.Builder(this)
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
