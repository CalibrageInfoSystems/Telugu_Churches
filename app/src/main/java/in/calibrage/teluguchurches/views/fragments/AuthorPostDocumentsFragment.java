package in.calibrage.teluguchurches.views.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
import java.util.ArrayList;

import in.calibrage.teluguchurches.R;
import in.calibrage.teluguchurches.services.ChurchService;
import in.calibrage.teluguchurches.services.ServiceFactory;
import in.calibrage.teluguchurches.util.BaseFragment;
import in.calibrage.teluguchurches.views.Activities.PdfReaderActivity;
import in.calibrage.teluguchurches.views.adapter.PostDocumentAdapter;
import in.calibrage.teluguchurches.views.model.PostImageResponseModel;
import in.calibrage.teluguchurches.views.model.PostRequestModel;
import io.fabric.sdk.android.Fabric;
import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class AuthorPostDocumentsFragment extends BaseFragment implements PostDocumentAdapter.OnCartChangedListener {
    private View rootView;
    private Context mContext;
    private RecyclerView recyclerView;
    private Subscription mSubscription;
    private int adminId;
    private Integer pageIndex = 1, pageSize = 10;
    private Handler handler;
    private ArrayList<PostImageResponseModel.ListResult> listResults = new ArrayList<>();
    private ArrayList<PostImageResponseModel.ListResult> BindDataListResults = new ArrayList<>();
    private boolean loading = true;
    private PostDocumentAdapter adapter;
    private TextView noItemText;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext = getActivity();

        // added crash report's to mail
        Fabric.with(getActivity(), new Crashlytics());

        //assining layout
        rootView = inflater.inflate(R.layout.post_recyclerview, null, false);

        /*
        intializing and assigning ID's
        */
        initViews();

        return rootView;
    }

    private void initViews() {
         /*
        intializing and assigning ID's
        */
        Intent intent = getActivity().getIntent();
        Bundle bundle = intent.getExtras();
        adminId = bundle.getInt("id");
        recyclerView = rootView.findViewById(R.id.recyclerView);
        noItemText = rootView.findViewById(R.id.noItemText);

        // API to get Post by pastorID
        getPostByAuthorId();
    }

    private void getPostByAuthorId() {
        // API to get Post by pastorID
        JsonObject object = getPostByAuthorIdObject();
        ChurchService service = ServiceFactory.createRetrofitService(mContext, ChurchService.class);
        mSubscription = service.GetPostByAuthorId(object)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<PostImageResponseModel>() {
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
                    public void onNext(PostImageResponseModel mResponse) {
                        if (mResponse.getListResult().isEmpty()) {
                            noItemText.setVisibility(View.VISIBLE);

                        } else {
                            noItemText.setVisibility(View.GONE);
                        }
                        BindDataListResults = (ArrayList<PostImageResponseModel.ListResult>) mResponse.getListResult();
                        listResults = new ArrayList<>();
                        listResults.addAll(BindDataListResults);

                        recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
                        adapter = new PostDocumentAdapter(mContext, listResults, recyclerView);
                        recyclerView.setAdapter(adapter);
                        adapter.setOnCartChangedListener(AuthorPostDocumentsFragment.this);

                        int position = listResults.size() - pageSize;
                        recyclerView.smoothScrollToPosition(position);

                        handler = new Handler();
                        adapter.setOnLoadMoreListener(new PostDocumentAdapter.OnLoadMoreListener() {
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
                                        if (BindDataListResults.size() >= pageSize) {
                                            listResults.remove(listResults.size() - 1);
                                            adapter.notifyItemRemoved(listResults.size());
                                            pageIndex = pageIndex + 1;

                                           // API to get Post by pastorID
                                            getPostByAuthorId();
                                            adapter.notifyDataSetChanged();
                                            loading = false;
                                        }

                                        //or you can add all at once but do not forget to call mAdapter.notifyDataSetChanged();
                                    }
                                }, 1000);

                            }
                        });


                    }

                });
    }

    /**
     * Json Object of getPostByAuthorIdObject
     *
     * @return
     */
    private JsonObject getPostByAuthorIdObject() {
        PostRequestModel mRequest = new PostRequestModel();
        mRequest.setAuthorId(adminId);
        mRequest.setMediaTypeId(6);
        mRequest.setPageIndex(pageIndex);
        mRequest.setPageSize(pageSize);
        mRequest.setSortbyColumnName("updatedDate");
        mRequest.setSortDirection("desc");
        return new Gson().toJsonTree(mRequest).getAsJsonObject();
    }


    /*
     * intent value passing to PdfReaderActivity
     * @param  document
   */
    @Override
    public void setCartClickListener(String status, int position) {
        if (status.equalsIgnoreCase("position")) {
            startActivity(new Intent(mContext, PdfReaderActivity.class).putExtra("document", BindDataListResults.get(position).getPostImage()));
        }

    }
}
