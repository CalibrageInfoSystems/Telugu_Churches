package in.calibrage.teluguchurches.views.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import in.calibrage.teluguchurches.R;
import in.calibrage.teluguchurches.views.model.GetCategory;

/**
 * This adapter is helpfull to show images of categories in main screen
 */

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {
    private Context context;
    GetCategory mResponse;
    private OnCartChangedListener onCartChangedListener;
    String comingFrom;

    /**
     * Constructor provides information about, and access to, a single constructor for a class.
     *
     * @param context
     * @param mResponse
     */
    public ImageAdapter(Context context, GetCategory mResponse) {
        this.context = context;
        this.mResponse = mResponse;
    }

    /*
     * creating viewholder to recycler view here
     * */
    @Override
    public ImageAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.gridview_adapter, viewGroup, false);
        return new ViewHolder(view);
    }

    /*
     * binding data to viewholder here
     * */
    @Override
    public void onBindViewHolder(ImageAdapter.ViewHolder viewHolder, final int i) {
        viewHolder.tv_android.setText(mResponse.getListResult().get(i).getCategoryName());
        Glide.with(context).load(mResponse.getListResult().get(i).getCategoryImage())
                .fitCenter()
                .error(R.drawable.loginimage)
                .into(viewHolder.img_android);
        viewHolder.main_rel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCartChangedListener.setCartClickListener("position", i);
            }
        });


    }

    // items count
    @Override
    public int getItemCount() {
        return mResponse.getListResult().size();
    }

    /*
     * initialising viewholder to recycler view
     * */
    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_android;
        private ImageView img_android;
        private RelativeLayout main_rel;

        public ViewHolder(View view) {
            super(view);

            tv_android = view.findViewById(R.id.tv_android);
            img_android = view.findViewById(R.id.img_android);
            main_rel = view.findViewById(R.id.main_rel);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
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