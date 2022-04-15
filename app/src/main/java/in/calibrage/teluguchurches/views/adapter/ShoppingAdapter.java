package in.calibrage.teluguchurches.views.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import in.calibrage.teluguchurches.R;
import in.calibrage.teluguchurches.views.model.AllItemsResponseModel;

/**
 * this adapter is helpful to show the shopping items list in online shopping screen
 */

public class ShoppingAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private OnChangedListener onChangedListener;
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;
    private List<T> myDataSet;
    // The minimum amount of items to have below your current scroll position before loading more.
    private int visibleThreshold = 2;
    private int lastVisibleItem, totalItemCount, subscibeval, churchId, authorId;
    private boolean loading;
    View v;
    ArrayList<AllItemsResponseModel.ListResult> itemResponse;
    private ShoppingAdapter.OnLoadMoreListener onLoadMoreListener;

    /**
     * Constructor provides information about, and access to, a single constructor for a class.
     *
     * @param context
     * @param myDataSet
     */
    public ShoppingAdapter(Context context, List<T> myDataSet, RecyclerView recyclerView) {
        this.context = context;
        this.myDataSet = myDataSet;
        itemResponse = (ArrayList<AllItemsResponseModel.ListResult>) myDataSet;


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

    /*
     * creating viewholder to recycler view here
     * */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        if (viewType == VIEW_ITEM) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_shopping, parent, false);
            vh = new ShoppingAdapter.TextViewHolder(v);
        } else {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.progress_item, parent, false);
            vh = new ShoppingAdapter.ProgressViewHolder(v);
        }
        return vh;
    }

    /*
     * binding data to viewholder from API here
     * */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof TextViewHolder) {
            ((TextViewHolder) holder).descriptionText.setText(itemResponse.get(position).getDesc());
            // ((TextViewHolder) holder).priceText.setText("₹ " + itemResponse.get(position).getPrice());
            ((TextViewHolder) holder).priceText.setText("₹ " + "---");
            ((TextViewHolder) holder).bookText.setText(itemResponse.get(position).getName());
            ((TextViewHolder) holder).authorText.setText(itemResponse.get(position).getAuthor());

            ((TextViewHolder) holder).mainLay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onChangedListener.setClickListener("itemClick", position);
                }
            });
            Glide.with(context).load(itemResponse.get(position).getItemImage())
                    .fitCenter()
                    .error(R.drawable.loginimage)
                    .into(((TextViewHolder) holder).itemImg);
        }
    }

    // items count
    @Override
    public int getItemCount() {
        return this.myDataSet.size();
    }

    /**
     * @param onChangedListener
     */
    public void setOnChangedListener(OnChangedListener onChangedListener) {
        this.onChangedListener = onChangedListener;


    }

    /**
     * Container Activity must implement this interface
     * you can define any parameter as per your requirement
     */
    public interface OnChangedListener {
        void setClickListener(String status, int position);
    }

    /*
     * initialising viewholder to recycler view
     * */
    public static class TextViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout mainLay;
        private TextView descriptionText, priceText, bookText, authorText;
        private ImageView itemImg;

        public TextViewHolder(View v) {
            super(v);
            mainLay = v.findViewById(R.id.mainLay);
            descriptionText = v.findViewById(R.id.descriptionText);
            priceText = v.findViewById(R.id.priceText);
            bookText = v.findViewById(R.id.bookText);
            authorText = v.findViewById(R.id.authorText);
            itemImg = v.findViewById(R.id.itemImg);
        }
    }

    public class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressViewHolder(View v) {
            super(v);
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

    @Override
    public int getItemViewType(int position) {
        return myDataSet.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }
}
