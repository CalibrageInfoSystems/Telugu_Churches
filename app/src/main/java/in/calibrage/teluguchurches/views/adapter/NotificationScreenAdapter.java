package in.calibrage.teluguchurches.views.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import in.calibrage.teluguchurches.R;
import in.calibrage.teluguchurches.views.model.GetNotificationResponseModel;

/**
 * This adapter is helpfull to show notifications in Unread Notifications view
 */
public class NotificationScreenAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;
    private List<T> myDataSet;
    private int visibleThreshold = 2;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    private NotificationScreenAdapter.OnLoadMoreListener onLoadMoreListener;
    private Context mContext;
    GetNotificationResponseModel dataList;
    private OnCartChangedListener onCartChangedListener;
    boolean isRead;
    ArrayList<GetNotificationResponseModel.NotificationsList> listResults;

    /**
     * Constructor provides information about, and access to, a single constructor for a class.
     * @param mContext
     * @param myDataSet
     */
    public NotificationScreenAdapter(Context mContext, List<T> myDataSet, RecyclerView notificationRecyclerView) {
        this.myDataSet = myDataSet;
        this.mContext = mContext;
        listResults = (ArrayList<GetNotificationResponseModel.NotificationsList>) myDataSet;


        if (notificationRecyclerView.getLayoutManager() instanceof LinearLayoutManager) {

            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) notificationRecyclerView.getLayoutManager();
            notificationRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.adapter_notification, parent, false);

            vh = new NotificationScreenAdapter.TextViewHolder(v);
        } else {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.progress_item, parent, false);

            vh = new NotificationScreenAdapter.ProgressViewHolder(v);
        }
        return vh;
    }

    /*
     * binding data to viewholder from API here
     * */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        if (holder instanceof NotificationScreenAdapter.TextViewHolder) {
            if (listResults.get(position).getIsRead()) {
                ((TextViewHolder) holder).card_view_new.setVisibility(View.GONE);
            } else {
                ((TextViewHolder) holder).card_view_new.setVisibility(View.VISIBLE);
                ((TextViewHolder) holder).titleText.setText(listResults.get(position).getName());
                ((TextViewHolder) holder).descriptionText.setText(listResults.get(position).getDesc());
                ((TextViewHolder) holder).mainLay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onCartChangedListener.setCartClickListener("itemPosition", position);
                    }
                });
            }

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

    //interface for view more functionality
    public interface OnLoadMoreListener {
        void onLoadMore();
    }

    /*
     * initialising viewholder to recycler view
     * */
    public class TextViewHolder extends RecyclerView.ViewHolder {
        private TextView titleText, descriptionText;
        private LinearLayout mainLay;
        private CardView card_view_new;

        public TextViewHolder(View itemView) {
            super(itemView);
            titleText = itemView.findViewById(R.id.titleText);
            descriptionText = itemView.findViewById(R.id.descriptionText);
            mainLay = itemView.findViewById(R.id.mainLay);
            card_view_new = itemView.findViewById(R.id.card_view_new);
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

