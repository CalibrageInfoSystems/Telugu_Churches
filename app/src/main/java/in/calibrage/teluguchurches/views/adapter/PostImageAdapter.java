package in.calibrage.teluguchurches.views.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import in.calibrage.teluguchurches.views.model.PostImageResponseModel;

/**
 * This adapter is helpfull to show church/pastor images in views
 */

public class PostImageAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private String comingFrom;
    private OnLoadMoreListener onLoadMoreListener;
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;
    private List<T> myDataSet;
    // The minimum amount of items to have below your current scroll position before loading more.
    private int visibleThreshold = 2;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    View v;
    ArrayList<PostImageResponseModel.ListResult> postImageList;
    private OnCartChangedListener onCartChangedListener;

    /**
     * Constructor provides information about, and access to, a single constructor for a class.
     *
     * @param context
     * @param myDataSet
     */
    public PostImageAdapter(Context context, String comingFrom, List<T> myDataSet, RecyclerView recyclerView) {
        this.context = context;
        this.comingFrom = comingFrom;
        this.myDataSet = myDataSet;
        postImageList = (ArrayList<PostImageResponseModel.ListResult>) myDataSet;


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
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_image_adapter, parent, false);
            vh = new PostImageAdapter.ViewHolder(v);
        } else {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.progress_item, parent, false);

            vh = new PostImageAdapter.ProgressViewHolder(v);
        }
        return vh;

    }

    /*
     * binding data to viewholder from API here
     * */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int i) {
        if (holder instanceof ViewHolder) {
            if (postImageList != null && !postImageList.equals("")) {
                if (comingFrom.equalsIgnoreCase("image")) {
                    ((ViewHolder) holder).tv_android.setText(postImageList.get(i).getTitle());
                    Glide.with(holder.itemView.getContext()).load(postImageList.get(i).getPostImage())
                            .fitCenter()
                            .error(R.drawable.loginimage)
                            .into(((ViewHolder) holder).img_android);
                    ((ViewHolder) holder).main_rel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            /**
                             * @param CartClickListner
                             */
                            onCartChangedListener.setCartClickListener("imagePosition", i);
                        }
                    });

                } else {
                    ((ViewHolder) holder).tv_android.setText(postImageList.get(i).getTitle());
                    Glide.with(holder.itemView.getContext()).load(postImageList.get(i).getPostImage())
                            .fitCenter()
                            .error(R.drawable.audio3)
                            .into(((ViewHolder) holder).img_android);
                    ((ViewHolder) holder).main_rel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            /**
                             * @param CartClickListner
                             */
                            onCartChangedListener.setCartClickListener("audioPosition", i);
                        }
                    });
                }


            }


        }
    }

    //interface for view more functionality
    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    //interface for view more functionality
    public interface OnLoadMoreListener {
        void onLoadMore();
    }

    // items count
    @Override
    public int getItemCount() {
        if (comingFrom.equalsIgnoreCase("image"))
            return this.myDataSet.size();
        else
            return this.myDataSet.size();
    }


    /*
     * initialising viewholder to recycler view
     * */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_android;
        private ImageView img_android;
        private RelativeLayout main_rel;


        public ViewHolder(View itemView) {
            super(itemView);
            tv_android = itemView.findViewById(R.id.tv_android);
            img_android = itemView.findViewById(R.id.img_android);
            main_rel = itemView.findViewById(R.id.main_rel);

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
