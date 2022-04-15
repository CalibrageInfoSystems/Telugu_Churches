package in.calibrage.teluguchurches.views.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import in.calibrage.teluguchurches.R;
import in.calibrage.teluguchurches.util.CommonUtil;
import in.calibrage.teluguchurches.views.model.GetAllEventsResponeModel;


public class EventAdapterYear<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;
    private List<T> myDataSet;

    // The minimum amount of items to have below your current scroll position before loading more.
    private int visibleThreshold = 2;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    private EventAdapterYear.OnLoadMoreListener onLoadMoreListener;
    Context context;
    ArrayList<GetAllEventsResponeModel.ListResult> dataList;
    private OnCartChangedListener onCartChangedListener;

    /**
     * Constructor provides information about, and access to, a single constructor for a class.
     *
     * @param context
     * @param myDataSet
     */

    public EventAdapterYear(Context context, List<T> myDataSet, RecyclerView recyclerView) {
        this.myDataSet = myDataSet;
        this.context = context;
        dataList = (ArrayList<GetAllEventsResponeModel.ListResult>) myDataSet;

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
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.adapter_allevent, parent, false);

            vh = new EventAdapterYear.TextViewHolder(v);
        } else {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.progress_item, parent, false);

            vh = new EventAdapterYear.ProgressViewHolder(v);
        }
        return vh;
    }

    /*
     * binding data to viewholder from API here
     * */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof EventAdapterYear.TextViewHolder) {
            if (dataList.get(position).getChurchName() != null) {
                ((TextViewHolder) holder).churchNameText.setText(CommonUtil.fromHtml("<b><font color=\"#606060\">" + context.getString(R.string.church_name) + ": " + "</font><b>" + dataList.get(position).getChurchName()));
            } else {
                ((TextViewHolder) holder).churchNameText.setVisibility(View.GONE);
                //((TextViewHolder) holder).churchNameText.setText(CommonUtil.fromHtml("<b><font color=\"#606060\">" + context.getString(R.string.church_name) + ": " + "</font><b>" + ""));
            }

            if (dataList.get(position).getAuthorName() != null) {
                ((TextViewHolder) holder).authorNameText.setText(CommonUtil.fromHtml("<b><font color=\"#606060\">" + context.getString(R.string.author_name) + ": " + "</font><b>" + dataList.get(position).getAuthorName()));
            } else {
                ((TextViewHolder) holder).authorNameText.setVisibility(View.GONE);
                // ((TextViewHolder) holder).authorNameText.setText(CommonUtil.fromHtml("<b><font color=\"#606060\">" + context.getString(R.string.author_name) + ": " + "</font><b>" + ""));
            }


            ((TextViewHolder) holder).eventText.setText(CommonUtil.fromHtml("<b><font color=\"#606060\">" + context.getString(R.string.event_name) + ": " + "</font><b>" + dataList.get(position).getTitle()));
            ((TextViewHolder) holder).numberText.setText(CommonUtil.fromHtml("<b><font color=\"#606060\">" + context.getString(R.string.mobile_number_event) + ": " + "</font><b>" + dataList.get(position).getContactNumber()));
            String from = "<b><font color='#606060'>" + context.getString(R.string.start_date) + ": " + " </font>  <b><font color='#979494'>" + CommonUtil.formatDateTimeUi(dataList.get(position).getStartDate()) + "</font>";
            String to = "<b><font color='#606060'>" + context.getString(R.string.end_date) + ": " + " </font>  <b><font color='#979494'>" + CommonUtil.formatDateTimeUi(dataList.get(position).getEndDate()) + "</font>";
            ((TextViewHolder) holder).openTimeText.setText(Html.fromHtml(from + " "));
            ((TextViewHolder) holder).closeTimeText.setText(Html.fromHtml(to + " "));

            Glide.with(context).load(dataList.get(position).getEventImage())
                    .fitCenter()
                    .error(R.drawable.church12)
                    .into(((TextViewHolder) holder).userImg);

            /**
             * @param setOnClickListner
             */
            ((TextViewHolder) holder).mainrlty.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onCartChangedListener.setCartClickListener("allevent", position);
                }
            });

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
    public void setOnLoadMoreListener(EventAdapterYear.OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    public interface OnLoadMoreListener {
        void onLoadMore();
    }

    /*
     * initialising viewholder to recycler view
     * */
    public static class TextViewHolder extends RecyclerView.ViewHolder {
        private TextView churchNameText, authorNameText, eventText, numberText, openTimeText, closeTimeText;
        private ImageView userImg;
        private RelativeLayout mainrlty;

        public TextViewHolder(View v) {
            super(v);
            churchNameText = v.findViewById(R.id.churchNameText);
            authorNameText = v.findViewById(R.id.authorNameText);
            eventText = v.findViewById(R.id.eventText);
            numberText = v.findViewById(R.id.numberText);
            openTimeText = v.findViewById(R.id.openTimeText);
            closeTimeText = v.findViewById(R.id.closeTimeText);
            userImg = v.findViewById(R.id.userImg);
            mainrlty = v.findViewById(R.id.mainrlty);

        }
    }

    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public ProgressViewHolder(View v) {
            super(v);
            progressBar = (ProgressBar) v.findViewById(R.id.progressBar);
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
