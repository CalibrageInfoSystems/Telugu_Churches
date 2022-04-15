package in.calibrage.teluguchurches.views.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import in.calibrage.teluguchurches.R;
import in.calibrage.teluguchurches.util.CommonUtil;
import in.calibrage.teluguchurches.views.model.GetAllJobDetailsResponseModel;

/**
 * This adapter is helpfull to show the careers list in screen
 */
public class CareerAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;
    private List<T> myDataSet;
    private int visibleThreshold = 2;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    View view;
    private OnLoadMoreListener onLoadMoreListener;
    Context context;
    ArrayList<GetAllJobDetailsResponseModel.ListResult> mResponseModel;
    private OnCartChangedListener onCartChangedListener;

    /**
     * Constructor provides information about, and access to, a single constructor for a class.
     *  @param context
     * @param myDataSet
     */

    public CareerAdapter(Context context, List<T> myDataSet, RecyclerView recyclerViewCareers) {
        this.context = context;
        this.myDataSet = myDataSet;


        mResponseModel = (ArrayList<GetAllJobDetailsResponseModel.ListResult>) myDataSet;

        if (recyclerViewCareers.getLayoutManager() instanceof LinearLayoutManager) {

            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerViewCareers.getLayoutManager();
            recyclerViewCareers.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_careers, parent, false);
            vh = new TextViewHolder(view);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.progress_item, parent, false);
            vh = new ProgressViewHolder(view);
        }
        return vh;
    }

    /*
     * binding data to viewholder from API here
     * */

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        if (holder instanceof TextViewHolder) {
            ((TextViewHolder) holder).jobTitleText.setText(CommonUtil.fromHtml("<b><font color=\"#606060\">" + context.getString(R.string.job_titles) + "</font><b>" + mResponseModel.get(position).getJobTitle()));
            ((TextViewHolder) holder).qulificationText.setText(CommonUtil.fromHtml("<b><font color=\"#606060\">" + context.getString(R.string.qualifications) + "</font><b>" + mResponseModel.get(position).getQualification()));
            ((TextViewHolder) holder).churchNameText.setText(CommonUtil.fromHtml("<b><font color=\"#606060\">" + context.getString(R.string.church_names) + "</font><b>" + mResponseModel.get(position).getChurchName()));
            ((TextViewHolder) holder).contactNoText.setText(CommonUtil.fromHtml("<b><font color=\"#606060\">" + context.getString(R.string.contact_num) + "</font><b>" + mResponseModel.get(position).getContactNumber()));
            ((TextViewHolder) holder).lastdateText.setText(CommonUtil.fromHtml("<b><font color=\"#606060\">" + context.getString(R.string.last_date_to_applys) + "</font><b>" + CommonUtil.formatDateTimeUi(mResponseModel.get(position).getLastDateToApply())));
            ((TextViewHolder) holder).main_rel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onCartChangedListener.setCartClickListener("MainClick", position);
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
    public static class TextViewHolder extends RecyclerView.ViewHolder {

        private TextView jobTitleText, qulificationText, churchNameText, contactNoText, lastdateText;
        private RelativeLayout main_rel;

        public TextViewHolder(View itemView) {
            super(itemView);
            main_rel = itemView.findViewById(R.id.main_rel);
            churchNameText = itemView.findViewById(R.id.churchNameText);
            jobTitleText = itemView.findViewById(R.id.jobTitleText);
            qulificationText = itemView.findViewById(R.id.qulificationText);
            contactNoText = itemView.findViewById(R.id.contactNoText);
            lastdateText = itemView.findViewById(R.id.lastdateText);
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
