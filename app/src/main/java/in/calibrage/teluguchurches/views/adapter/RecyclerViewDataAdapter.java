package in.calibrage.teluguchurches.views.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import in.calibrage.teluguchurches.R;
import in.calibrage.teluguchurches.views.model.GetCategory;
import in.calibrage.teluguchurches.views.model.SectionDataModel;

/**
 * This adapter is helpfull to show the categories in Home screen
 */

public class RecyclerViewDataAdapter extends RecyclerView.Adapter<RecyclerViewDataAdapter.ItemRowHolder> {
    private ArrayList<SectionDataModel> mFilteredList;
    private Context mContext;
    GetCategory dataList;
    ArrayList<Integer> images;
    private OnCartChangedListener onCartChangedListener;

    /**
     * Constructor provides information about, and access to, a single constructor for a class.
     *
     * @param context
     * @param dataList
     */
    public RecyclerViewDataAdapter(Context context, GetCategory dataList) {
        this.dataList = dataList;
        this.mContext = context;
    }

    /*
     * creating viewholder to recycler view here
     * */
    @Override
    public ItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.category_list, null);
        ItemRowHolder mh = new ItemRowHolder(v);
        return mh;
    }

    /*
     * binding data to viewholder from API here
     * */
    @Override
    public void onBindViewHolder(ItemRowHolder itemRowHolder, final int i) {
        itemRowHolder.tvTitle.setText(dataList.getListResult().get(i).getCategoryName());
        Glide.with(mContext).load(dataList.getListResult().get(i).getCategoryImage()).fitCenter().error(R.drawable.loginimage).into(itemRowHolder.itemImage);
        itemRowHolder.main_rel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCartChangedListener.setCartClickListener("position", i);
            }
        });
    }

    // items count
    @Override
    public int getItemCount() {
        return dataList.getListResult().size();
    }

    /*
     * initialising viewholder to recycler view
     * */
    public class ItemRowHolder extends RecyclerView.ViewHolder {
        protected TextView itemTitle, tvTitle;
        public RecyclerView recycler_view_list;
        protected Button btnMore;
        private ImageView itemImage;
        private RelativeLayout main_rel;

        public ItemRowHolder(View view) {
            super(view);
            tvTitle = view.findViewById(R.id.tvTitle);
            itemImage = view.findViewById(R.id.itemImage);
            main_rel = view.findViewById(R.id.main_rel);

        }

    }

    /**
     * Container Activity must implement this interface
     * you can define any parameter as per your requirement
     */
    public interface OnCartChangedListener {
        void setCartClickListener(String status, int position);

    }

    /**
     * @param onCartChangedListener
     */
    public void setOnCartChangedListener(OnCartChangedListener onCartChangedListener) {
        this.onCartChangedListener = onCartChangedListener;
    }

}
