package in.calibrage.teluguchurches.views.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import in.calibrage.teluguchurches.R;
import in.calibrage.teluguchurches.views.Activities.ImageViewActivity;
import in.calibrage.teluguchurches.views.model.GetCategory;
import in.calibrage.teluguchurches.views.model.GetPostByCategoryIdResponseModel;

/**
 * This adapter is helpfull to show images in screen
 */
public class CategoryImageAdapter extends RecyclerView.Adapter<CategoryImageAdapter.ViewHolder> {
    private Context context;
    GetCategory mResponse;
    GetPostByCategoryIdResponseModel dataList;
    String comingFrom;
    private OnCartChangedListener onCartChangedListener;

    /**
     * Constructor provides information about, and access to, a single constructor for a class.
     *  @param context
     * @param comingFrom
     * @param dataList
     */
    public CategoryImageAdapter(Context context, GetPostByCategoryIdResponseModel dataList, String comingFrom) {
        this.context = context;
        this.mResponse = mResponse;
        this.dataList = dataList;
        this.comingFrom = comingFrom;
    }

    /*
     * creating viewholder to recycler view here
     * */
    @Override
    public CategoryImageAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.gridview_adapter, viewGroup, false);
        return new CategoryImageAdapter.ViewHolder(view);
    }


    /*
     * binding data to viewholder from API here
     * */
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int i) {
        if (comingFrom.equalsIgnoreCase("image")) {
            viewHolder.tv_android.setText(dataList.getResult().getImages().get(i).getTitle());
            Glide.with(context).load(dataList.getResult().getImages().get(i).getPostImage())
                    .fitCenter()
                    .error(R.drawable.loginimage)
                    .into(viewHolder.img_android);
            viewHolder.main_rel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, ImageViewActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("postTypeId", dataList.getResult().getImages().get(i).getId())
                            .putExtra("image", dataList.getResult().getImages().get(i).getPostImage());
                    context.startActivity(intent);
                }
            });
        } else {
            viewHolder.tv_android.setText(dataList.getResult().getAudios().get(i).getTitle());
            Glide.with(context).load(dataList.getResult().getAudios().get(i).getPostImage())
                    .fitCenter()
                    .error(R.drawable.audio3)
                    .into(viewHolder.img_android);
            viewHolder.main_rel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onCartChangedListener.setCartClickListener("audio", i);
                }
            });
        }


    }

    // items count
    @Override
    public int getItemCount() {
        if (comingFrom.equalsIgnoreCase("image")) {
            return dataList.getResult().getImages().size();
        } else {
            return dataList.getResult().getAudios().size();
        }

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