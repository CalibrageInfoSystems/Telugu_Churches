package in.calibrage.teluguchurches.views.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import in.calibrage.teluguchurches.R;
import in.calibrage.teluguchurches.util.CommonUtil;
import in.calibrage.teluguchurches.views.model.GetAllAdminResponseModel;
import rx.Subscription;

/*
 * this adater is helpful to show the pastor's list
 * */

public class AdminAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;
    private List<T> myDataSet;
    private int visibleThreshold = 2;
    private int lastVisibleItem, totalItemCount, subscibeval, churchId, authorId;
    private boolean loading;
    View v;
    private Subscription mSubscription;
    private OnLoadMoreListener onLoadMoreListener;
    Context context;
    ArrayList<GetAllAdminResponseModel.ListResult> adminResponse;
    private OnCartChangedListener onCartChangedListener;
    private String openTime, closeTime;
    public Button subscribeBtn;


    /**
     * Constructor provides information about, and access to, a single constructor for a class.
     *  @param context
     * @param myDataSet
     */
    public AdminAdapter(Context context, List<T> myDataSet, RecyclerView recyclerView) {
        this.myDataSet = myDataSet;
        this.context = context;

        adminResponse = (ArrayList<GetAllAdminResponseModel.ListResult>) myDataSet;

        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {

            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);

                    totalItemCount = linearLayoutManager.getItemCount();
                    lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                    if (!loading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                        // End has been reached
                        // Do something
                        if (onLoadMoreListener != null) {
                            onLoadMoreListener.onLoadMore();
                        }
                        loading = true;
                    }
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        return myDataSet.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }

    /*
     * creating viewholder to recycler view here
     * */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        if (viewType == VIEW_ITEM) {
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.adapter_admin, parent, false);
            subscribeBtn = (Button) v.findViewById(R.id.subscribeBtnAuthor);
            vh = new TextViewHolder(v);
        } else {
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.progress_item, parent, false);

            vh = new ProgressViewHolder(v);
        }
        return vh;
    }

    /*
     * binding data to viewholder from API here
     * */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof TextViewHolder) {

            if (adminResponse.get(position).getChurchName() != null) {
                ((TextViewHolder) holder).churchNameText.setText(CommonUtil.fromHtml("<b><font color=\"#606060\">"
                        + context.getString(R.string.church) + " " + ":" + " " + "</font><b>" + adminResponse.get(position).getChurchName()));
            } else {
                ((TextViewHolder) holder).churchNameText.setVisibility(View.GONE);
            }
            if (adminResponse.get(position).getMobileNumber() != null) {
                ((TextViewHolder) holder).numberText.setText(CommonUtil.fromHtml("<b><font color=\"#606060\">"
                        + context.getString(R.string.mobile_no) + "</font><b>" + " " + ":" + " " + adminResponse.get(position).getMobileNumber()));
            } else {
                ((TextViewHolder) holder).numberText.setVisibility(View.GONE);
            }
            if (adminResponse.get(position).getEmail() != null) {
                ((TextViewHolder) holder).emailText.setText(CommonUtil.fromHtml("<b><font color=\"#606060\">"
                        + context.getString(R.string.emails) + "</font><b>" + adminResponse.get(position).getEmail()));
            } else {
                ((TextViewHolder) holder).emailText.setVisibility(View.GONE);
            }

            ((TextViewHolder) holder).adminText.setText(CommonUtil.fromHtml("<b><font color=\"#606060\">" +
                    context.getString(R.string.pator_name) + "</font><b>" + " " + ":" + " " + adminResponse.get(position).getChurchAdmin()));


            subscibeval = adminResponse.get(position).getIsSubscribed();
            if (churchId != 0 && authorId != 0) {
                churchId = adminResponse.get(position).getChurchId();
                authorId = adminResponse.get(position).getId();
            }

            ((TextViewHolder) holder).mainrlty.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onCartChangedListener.setCartClickListener("MainClick", position);
                }
            });

            Glide.with(context).load(adminResponse.get(position).getUserImage())
                    .fitCenter()
                    .error(R.drawable.pastor1)
                    .into(((TextViewHolder) holder).userImg);
        }

    }

    //interface for view more functionality
    public void setLoaded() {
        loading = false;
    }

    // items count
    @Override
    public int getItemCount() {
        return this.myDataSet.size();
    }

    //interface for view more functionality
    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    public interface OnLoadMoreListener {
        void onLoadMore();
    }

    /*
     * initialising viewholder to recycler view
     * */
    public static class TextViewHolder extends RecyclerView.ViewHolder {
        public TextView churchNameText, adminText, numberText, emailText;
        private RelativeLayout mainrlty;
        private ImageView userImg;
        private Button subscribeBtn;


        public TextViewHolder(View v) {
            super(v);
            churchNameText = v.findViewById(R.id.churchNameText);
            adminText =  v.findViewById(R.id.adminText);
            numberText =  v.findViewById(R.id.numberText);
            emailText = v.findViewById(R.id.emailText);
            mainrlty =  v.findViewById(R.id.mainrlty);
            userImg =  v.findViewById(R.id.userImg);
            /* subscribeBtn=(Button)v.findViewById(R.id.subscribeBtnAuthor);*/

        }
    }

    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public ProgressViewHolder(View v) {
            super(v);
            progressBar = v.findViewById(R.id.progressBar);
        }
    }

    /**
     * @param onCartChangedListener
     */
    public void setOnCartChangedListener(OnCartChangedListener onCartChangedListener) {
        this.onCartChangedListener = onCartChangedListener;
    }

    /**
     * Container Activity must implement this interface
     * you can define any parameter as per your requirement
     */
    public interface OnCartChangedListener {
        void setCartClickListener(String status, int position);
    }
}