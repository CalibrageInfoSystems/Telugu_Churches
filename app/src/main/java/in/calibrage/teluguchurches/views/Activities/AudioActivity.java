package in.calibrage.teluguchurches.views.Activities;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.TimeUnit;

import in.calibrage.teluguchurches.R;
import in.calibrage.teluguchurches.interfaces.AudioPlayerClickListeren;
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

public class AudioActivity extends BaseActivity implements AudioPlayerClickListeren, View.OnClickListener, CommentAdapter.OnCartChangedListener {

    private TextView startTime, endTime, editText, deleteText,addCommentText, commentTitleText,
            likeText, disLikeText, viewCount, titleText, dialogMessage;
    private EditText input, addCommentTxt;
    private ImageView disLikeImg, likeImg, clearDialog,play, pause, forward, backward, download,toolbar_image;
    private Button ok_btn, cancel_btn;
    private RelativeLayout shareLay, likeLay, diLikeLay;
    private RecyclerView commentRecyclerView, allReplyRecyclerView;
    private CommentAdapter adapter;
    private ViewAllRepliesAdapter adapterViewAllReplies;
    SeekBar seekbar;
    MediaPlayer player;
    private Context mContext;
    Toolbar toolbar;
    private int oTime = 0, sTime = 0, eTime = 0, fTime = 5000, bTime = 5000,postTypeId, value, userId, commentID, listPosition, parentCommentId;
    private Intent intent;
    private Subscription mSubscription;
    private GetPostByIdResponseModel commentResponse;
    private GetUpdateViewCountByPostIdResponseModel viewCountResponse;
    private LikeResponseModel mModel;
    Handler handler = new Handler();
    private boolean like, disLike;
    private AlertDialog alertDialog, alertDialogTest;
    private ProgressDialog mDialog,prgDialog;
    private BottomSheetDialog mBottomSheetDialog;
    private View dialogView;
    BottomSheetBehavior bottomSheetBehavior;
    private static String AudioURL,file_url, fileName, fileextention,comingFrom,authorizationToken,shareUrl;
    // Progress Dialog type (0 - for Horizontal progress bar)
    public static final int progress_bar_type = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getApplicationContext();

        // added crash report's to mail
        Fabric.with(getApplicationContext(), new Crashlytics());

        //assining layout
        setContentView(R.layout.audio_activty);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                // Should we show an explanation?
                if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                    // Explain to the user why we need to read the contacts
                }
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        1);

            }

        }
        intent = getIntent();
        if (intent != null) {
            /*
             * to check the intent value
             * @param  AudioURL
             *@param  postTypeId
            */
            AudioURL = intent.getStringExtra("audio");
            postTypeId = getIntent().getIntExtra("postTypeId", 0);
        }
        userId = SharedPrefsData.getInt(AudioActivity.this, Constants.ID, Constants.PREF_NAME);
        value = SharedPrefsData.getInt(AudioActivity.this, Constants.ISLOGIN, Constants.PREF_NAME);
        toolbar_image = findViewById(R.id.toolbar_image);

        /**
         * @param OnClickListner
         */
        toolbar_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AudioActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });
        /* intializing and assigning ID's */
        initView();

        /* Navigation's and using the views */
        setViews();


    }
    /* intializing and assigning ID's */
    private void initView() {

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
        play = findViewById(R.id.play);
        pause = findViewById(R.id.pause);
        seekbar =findViewById(R.id.seekbar);
        startTime =findViewById(R.id.starttime);
        endTime = findViewById(R.id.endtime);
        download =findViewById(R.id.download);
        commentRecyclerView =findViewById(R.id.commentRecyclerView);
        addCommentText =findViewById(R.id.addCommentText);
        commentTitleText = findViewById(R.id.commentTitleText);
        viewCount = findViewById(R.id.viewCount);
        titleText = findViewById(R.id.titleText);
        likeText =findViewById(R.id.likeText);
        disLikeText = findViewById(R.id.disLikeText);
        likeLay = findViewById(R.id.likeLay);
        diLikeLay = findViewById(R.id.diLikeLay);
        shareLay =findViewById(R.id.shareLay);
        disLikeImg =findViewById(R.id.disLikeImg);
        likeImg =findViewById(R.id.likeImg);
        mDialog = new ProgressDialog(AudioActivity.this);

        // to show the Progress Dialog
        showProgressDialog();

        // API to get Particular post details
        getPostById();

        // API to get ViewCount for Particular post
        getViewCountPostId();

        mBottomSheetDialog = new BottomSheetDialog(AudioActivity.this);
        dialogView = getLayoutInflater().inflate(R.layout.view_all_replies, null);
        mBottomSheetDialog.setContentView(dialogView);
        mBottomSheetDialog.setCancelable(false);
        bottomSheetBehavior = BottomSheetBehavior.from((View) dialogView.getParent());
        clearDialog = dialogView.findViewById(R.id.clearDialog);
        allReplyRecyclerView =dialogView.findViewById(R.id.allReplyRecyclerView);


        player = new MediaPlayer();

        /**
         * @param OnClickListner
         */
        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new DownloadMusicfromInternet().execute(file_url);

            }
        });


        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                play.setVisibility(View.VISIBLE);
                pause.setVisibility(View.GONE);
            }
        });
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    player.seekTo(progress);
                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        player.setAudioStreamType(AudioManager.STREAM_MUSIC);

        /**
         * @param OnClickListner
         */
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                play.setVisibility(View.GONE);
                pause.setVisibility(View.VISIBLE);
                try {
                    player.setDataSource(String.valueOf(AudioURL));
                    player.prepare();
                } catch (IllegalArgumentException e) {

                    e.printStackTrace();
                } catch (SecurityException e) {

                    e.printStackTrace();
                } catch (IllegalStateException e) {

                    e.printStackTrace();
                } catch (IOException e) {

                    e.printStackTrace();
                }
                player.start();

                eTime = player.getDuration();
                sTime = player.getCurrentPosition();
                if (oTime == 0) {
                    seekbar.setMax(eTime);
                    oTime = 1;
                }
                startTime.setText(String.format("%d min, %d sec", TimeUnit.MILLISECONDS.toMinutes(eTime), TimeUnit.MILLISECONDS.toSeconds(eTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(eTime))));
                endTime.setText(String.format("%d min, %d sec", TimeUnit.MILLISECONDS.toMinutes(sTime), TimeUnit.MILLISECONDS.toSeconds(sTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(sTime))));
                seekbar.setProgress(sTime);
                handler.postDelayed(runnable, 100);

            }
        });


        /**
         * @param OnClickListner
         */
        pause.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                pause.setVisibility(View.GONE);
                play.setVisibility(View.VISIBLE);
                if (player.isPlaying()) {
                    player.pause();
                } else {
                    player.stop();
                }
            }
        });
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

    // start time and end time of audio
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (player != null) {
                sTime = player.getCurrentPosition();
                endTime.setText(String.format("%d min, %d sec", TimeUnit.MILLISECONDS.toMinutes(sTime), TimeUnit.MILLISECONDS.toSeconds(sTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(sTime))));
                seekbar.setProgress(sTime);
                handler.postDelayed(this, 100);
            }
        }
    };

      //when onBack press audio play will be stopted
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        player.release();
        player = null;
        seekbar.setProgress(0);
        sTime = 0;
        fTime = 0;
    }



    // API to get Particular post details
    private void getPostById() {
        ChurchService service = ServiceFactory.createRetrofitService(getApplicationContext(), ChurchService.class);
        String url = APIConstants.GetPostById + postTypeId + "/" + SharedPrefsData.getInt(getApplicationContext(), Constants.ID, Constants.PREF_NAME);
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
                        file_url = mResponse.getResult().getPostDetails().get(0).getPostImage().toString();
                        fileName = mResponse.getResult().getPostDetails().get(0).getTitle().toString();
                        fileextention = mResponse.getResult().getPostDetails().get(0).getFileExtention();
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
                        commentTitleText.setText(mResponse.getResult().getCommentDetails().size() + " Comments");
                        titleText.setText(mResponse.getResult().getPostDetails().get(0).getTitle());
                        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                        commentRecyclerView.setHasFixedSize(true);
                        commentRecyclerView.setLayoutManager(mLayoutManager);
                        adapter = new CommentAdapter(getApplicationContext(), mResponse, "Post", null);
                        commentRecyclerView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                        adapter.setOnCartChangedListener(AudioActivity.this);


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
                            viewCount.setText(mResponse.getListResult().get(0).getViewCount() + " Views");
                            viewCount.setVisibility(View.VISIBLE);
                        } else {
                            viewCount.setVisibility(View.GONE);
                        }


                    }
                });

    }

    // API to get Like or Disk-like for the Post
    private void likeOrDislikePost() {
        if (authorizationToken != "null" || authorizationToken != null || authorizationToken.isEmpty()){
            authorizationToken = SharedPrefsData.getInstance(mContext).getStringFromSharedPrefs(Constants.Auth_Token);
        }

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
                        mDialog.dismiss();
                        if (mResponse.getIsSuccess()) {
                            mModel = mResponse;
                            getPostById();
                         //   Toast.makeText(AudioActivity.this, "" + mResponse.getEndUserMessage(), Toast.LENGTH_SHORT).show();

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
                            Toast.makeText(AudioActivity.this, "" + mResponse.getEndUserMessage(), Toast.LENGTH_SHORT).show();

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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.addCommentText:
                if (value == 0) {
                    //popup dialog please login to comments
                    LayoutInflater layoutInflater = LayoutInflater.from(AudioActivity.this);
                    View dialogRootView = layoutInflater.inflate(R.layout.dialog_like, null);
                    final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AudioActivity.this);
                    dialogMessage =dialogRootView.findViewById(R.id.dialogMessage);
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
                            Intent intent = new Intent(AudioActivity.this, LoginActivity.class);
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

                    LayoutInflater layoutInflater = LayoutInflater.from(AudioActivity.this);
                    View dialogRootView = layoutInflater.inflate(R.layout.add_dialog_comment, null);
                    final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AudioActivity.this);
                    ok_btn = dialogRootView.findViewById(R.id.ok_btn);
                    cancel_btn =dialogRootView.findViewById(R.id.cancel_btn);
                    addCommentTxt = dialogRootView.findViewById(R.id.addCommentTxt);
                    alertDialogBuilder.setView(dialogRootView);

                    /**
                     * @param OnClickListner
                     */
                    ok_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (addCommentTxt.getText().length() == 0) {
                                Toast.makeText(AudioActivity.this, "Please Add Comments", Toast.LENGTH_SHORT).show();
                            } else {
                                comingFrom = "add";
                                // to show the Progress Dialog
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
                if (value == 0) {

                    // PopUpDailog to show message please login to like for audio
                    LayoutInflater layoutInflater = LayoutInflater.from(AudioActivity.this);
                    View dialogRootView = layoutInflater.inflate(R.layout.dialog_like, null);
                    final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AudioActivity.this);
                    ok_btn = dialogRootView.findViewById(R.id.ok_btn);
                    cancel_btn = dialogRootView.findViewById(R.id.cancel_btn);

                    alertDialogBuilder.setView(dialogRootView);


                    /**
                     * @param OnClickListner
                     */
                    ok_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(AudioActivity.this, LoginActivity.class);
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
                    mDialog.setMessage("Please wait...");
                    mDialog.show();

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

                    // PopUpDailog to show message please login to dislike for audio
                    LayoutInflater layoutInflater = LayoutInflater.from(AudioActivity.this);
                    View dialogRootView = layoutInflater.inflate(R.layout.dialog_like, null);
                    final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AudioActivity.this);
                    dialogMessage = dialogRootView.findViewById(R.id.dialogMessage);
                    dialogMessage.setText(getString(R.string.please_login_to_dislike));
                    ok_btn = dialogRootView.findViewById(R.id.ok_btn);
                    cancel_btn =dialogRootView.findViewById(R.id.cancel_btn);

                    alertDialogBuilder.setView(dialogRootView);


                    /**
                     * @param OnClickListner
                     */
                    ok_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(AudioActivity.this, LoginActivity.class);
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
                    mDialog.setMessage("Please wait...");
                    mDialog.show();

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
        mSubscription = service.GetPostCommentReplies(url,authorizationToken)
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

                            // to show the Progress Dialog
                            showProgressDialog();

                            // API to get Particular post details
                            getPostById();
                            Toast.makeText(AudioActivity.this, "" + mResponse.getEndUserMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }

                });
    }

    private JsonObject deleteObject() {
        LikeRequestModel mRequest = new LikeRequestModel();
        mRequest.setId(commentID);
        mRequest.setPostId(postTypeId);
        mRequest.setChurchId(null);
        mRequest.setUserId("" + SharedPrefsData.getInt(getApplicationContext(), Constants.ID, Constants.PREF_NAME));
        return new Gson().toJsonTree(mRequest).getAsJsonObject();
    }

    // Interface between adapter and class
    @Override
    public void setCartClickListener(String status, final int position) {
        listPosition = position;
        if (status.equalsIgnoreCase("edit")) {
            if ("" + userId != null && userId != 0) {

                if (commentResponse.getResult().getCommentDetails().get(listPosition).getUserId().equals(userId)) {
                    commentID = commentResponse.getResult().getCommentDetails().get(listPosition).getId();
                    LayoutInflater layoutInflater = LayoutInflater.from(AudioActivity.this);
                    View dialogRootView = layoutInflater.inflate(R.layout.add_dialog_comment, null);
                    final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AudioActivity.this);
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
                                Toast.makeText(AudioActivity.this, "Please Add Comments", Toast.LENGTH_SHORT).show();
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
                // PopUpDailog to show message please login to edit comments
                LayoutInflater layoutInflater = LayoutInflater.from(AudioActivity.this);
                View dialogRootView = layoutInflater.inflate(R.layout.dialog_like, null);
                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AudioActivity.this);
                dialogMessage =dialogRootView.findViewById(R.id.dialogMessage);
                dialogMessage.setText(getString(R.string.please_login_to_edit));
                ok_btn = dialogRootView.findViewById(R.id.ok_btn);
                cancel_btn =dialogRootView.findViewById(R.id.cancel_btn);

                alertDialogBuilder.setView(dialogRootView);

                /**
                 * @param OnClickListner
                 */
                ok_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(AudioActivity.this, LoginActivity.class);
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
                    LayoutInflater layoutInflater = LayoutInflater.from(AudioActivity.this);
                    View dialogRootView = layoutInflater.inflate(R.layout.delete_dialog, null);
                    final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AudioActivity.this);
                    ok_btn = dialogRootView.findViewById(R.id.ok_btn);
                    cancel_btn = dialogRootView.findViewById(R.id.cancel_btn);
                    alertDialogBuilder.setView(dialogRootView);

                    /**
                     * @param OnClickListner
                     */
                    ok_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            // to show the Progress Dialog
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
                // PopUpDailog to show message please login to delete comments
                LayoutInflater layoutInflater = LayoutInflater.from(AudioActivity.this);
                View dialogRootView = layoutInflater.inflate(R.layout.dialog_like, null);
                dialogMessage = dialogRootView.findViewById(R.id.dialogMessage);
                dialogMessage.setText(getString(R.string.please_login_to_delete));
                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AudioActivity.this);
                ok_btn =dialogRootView.findViewById(R.id.ok_btn);
                cancel_btn = dialogRootView.findViewById(R.id.cancel_btn);

                alertDialogBuilder.setView(dialogRootView);

                /**
                 * @param OnClickListner
                 */
                ok_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(AudioActivity.this, LoginActivity.class);
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
            }else {

                   // PopUpDailog to show message please login to view all replies
                LayoutInflater layoutInflater = LayoutInflater.from(AudioActivity.this);
                View dialogRootView = layoutInflater.inflate(R.layout.dialog_like, null);
                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AudioActivity.this);
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
                        Intent intent = new Intent(AudioActivity.this, LoginActivity.class);
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

                    // PopUpDailog to show message please login to comments
                    LayoutInflater layoutInflater = LayoutInflater.from(AudioActivity.this);
                    View dialogRootView = layoutInflater.inflate(R.layout.dialog_like, null);
                    final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AudioActivity.this);
                    dialogMessage =dialogRootView.findViewById(R.id.dialogMessage);
                    dialogMessage.setText(getString(R.string.please_login_to_comment));
                    ok_btn =dialogRootView.findViewById(R.id.ok_btn);
                    cancel_btn =dialogRootView.findViewById(R.id.cancel_btn);

                    alertDialogBuilder.setView(dialogRootView);

                    /**
                     * @param OnClickListner
                     */
                    ok_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            SharedPrefsData.putInt(getApplicationContext(), Constants.ISFROM, 1, Constants.PREF_NAME);
                            Intent intent = new Intent(AudioActivity.this, LoginActivity.class);
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

                    // PopUpDailog to show message add a public comments
                    LayoutInflater layoutInflater = LayoutInflater.from(AudioActivity.this);
                    View dialogRootView = layoutInflater.inflate(R.layout.add_dialog_comment, null);
                    final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AudioActivity.this);
                    ok_btn = dialogRootView.findViewById(R.id.ok_btn);
                    cancel_btn =dialogRootView.findViewById(R.id.cancel_btn);
                    addCommentTxt =dialogRootView.findViewById(R.id.addCommentTxt);
                    alertDialogBuilder.setView(dialogRootView);

                    /**
                     * @param OnClickListner
                     */
                    ok_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (addCommentTxt.getText().length() == 0) {
                                Toast.makeText(AudioActivity.this, "Please Add Comments", Toast.LENGTH_SHORT).show();
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
            }else {

                // PopUpDailog to show message please login to reply for the comments
                LayoutInflater layoutInflater = LayoutInflater.from(AudioActivity.this);
                View dialogRootView = layoutInflater.inflate(R.layout.dialog_like, null);
                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AudioActivity.this);
                dialogMessage =dialogRootView.findViewById(R.id.dialogMessage);
                dialogMessage.setText(getString(R.string.please_login_to_reply));
                ok_btn = dialogRootView.findViewById(R.id.ok_btn);
                cancel_btn =dialogRootView.findViewById(R.id.cancel_btn);

                alertDialogBuilder.setView(dialogRootView);

                /**
                 * @param OnClickListner
                 */
                ok_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(AudioActivity.this, LoginActivity.class);
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

    // Show Dialog Box with Progress bar
    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case progress_bar_type:
                prgDialog = new ProgressDialog(this);
                prgDialog.setMessage("Downloading Mp3 file. Please wait...");
                prgDialog.setIndeterminate(false);
                prgDialog.setMax(100);
                prgDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                prgDialog.setCancelable(false);
                prgDialog.show();
                return prgDialog;
            default:
                return null;
        }
    }

    @Override
    public void setAudioPlayerListener(int position) {

    }

    // Async Task Class
    class DownloadMusicfromInternet extends AsyncTask<String, String, String> {

        // Show Progress bar before downloading Music

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Shows Progress Bar Dialog and then call doInBackground method
            showDialog(progress_bar_type);
        }

        // Download Music File from Internet
        @Override
        protected String doInBackground(String... f_url) {
            int count;
            try {
                URL url = new URL(f_url[0]);
                URLConnection conection = url.openConnection();
                conection.connect();
                // Get Music file length
                int lenghtOfFile = conection.getContentLength();
                // input stream to read file - with 8k buffer
                InputStream input = new BufferedInputStream(url.openStream(), 10 * 1024);
                File Mp3Directory = new File("/sdcard/TeluguChurchesAudio/");
                // have the object build the directory structure, if needed.
                if (!Mp3Directory.exists()) {
                    Mp3Directory.mkdirs();
                }

                // create a File object for the output file
                File outputFile = new File(Mp3Directory, fileName);
                OutputStream output = null;
                // now attach the OutputStream to the file object, instead of a String representation
                if (!outputFile.exists()) {
                    try {
                        output = new FileOutputStream(outputFile + ".mp3");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
                // Output stream to write file in SD card
             /*   String root = Environment.getExternalStorageDirectory().toString();
                OutputStream output = new FileOutputStream(root+"/test.mp3");*/
                //OutputStream output = new FileOutputStream(Environment.getExternalStorageDirectory().getPath());
                byte data[] = new byte[1024];
                long total = 0;
                while ((count = input.read(data)) != -1) {
                    total += count;
                    // Publish the progress which triggers onProgressUpdate method
                    publishProgress("" + (int) ((total * 100) / lenghtOfFile));

                    // Write data to file
                    output.write(data, 0, count);
                }
                // Flush output
                output.flush();
                // Close streams
                output.close();
                input.close();
            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }
            return null;
        }

        // While Downloading Music File
        protected void onProgressUpdate(String... progress) {
            // Set progress percentage
            prgDialog.setProgress(Integer.parseInt(progress[0]));
        }

        // Once Music File is downloaded
        @Override
        protected void onPostExecute(String file_url) {
            // Dismiss the dialog after the Music file was downloaded
            dismissDialog(progress_bar_type);

            LayoutInflater layoutInflater = LayoutInflater.from(AudioActivity.this);
            View dialogRootView = layoutInflater.inflate(R.layout.dialog_audio, null);
            AlertDialog.Builder builder = new AlertDialog.Builder(AudioActivity.this);
            builder.setView(dialogRootView);
            ok_btn =dialogRootView.findViewById(R.id.ok_btn);

            /**
             * @param OnClickListner
             */
            ok_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alertDialog.dismiss();

                }
            });
            alertDialog = builder.create();
            alertDialog.show();

            // Play the music

        }
    }

}