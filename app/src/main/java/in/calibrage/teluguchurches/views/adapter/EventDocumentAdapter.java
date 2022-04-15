package in.calibrage.teluguchurches.views.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import in.calibrage.teluguchurches.R;
import in.calibrage.teluguchurches.views.model.GetPostByCategoryIdResponseModel;
import in.calibrage.teluguchurches.views.model.GetPostbyEventIdResponseModel;

/*
 * this adater is helpful to show douments
 * */
public class EventDocumentAdapter extends RecyclerView.Adapter<EventDocumentAdapter.ViewHolder> {
    private Context context;
    GetPostbyEventIdResponseModel dataList = new GetPostbyEventIdResponseModel();
    private OnCartChangedListener onCartChangedListener;


    /**
     * Constructor provides information about, and access to, a single constructor for a class.
     *
     * @param context
     * @param dataList
     */
    public EventDocumentAdapter(Context context, GetPostbyEventIdResponseModel dataList) {
        this.context = context;
        this.dataList = dataList;

    }

    /*
     * creating viewholder to recycler view here
     * */
    @Override
    public EventDocumentAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_category, parent, false);
        return new EventDocumentAdapter.ViewHolder(view);
    }

    /*
     * binding data to viewholder from API here
     * */
    @Override
    public void onBindViewHolder(EventDocumentAdapter.ViewHolder holder, final int position) {
        holder.tv_android.setText(dataList.getResult().getDocuments().get(position).getTitle());
        holder.img_android.setImageResource(R.drawable.ic_action_pdf);

        /*Glide.with(context).load(android.get(i).getAndroid_image_url())
                .fitCenter()
                .into(viewHolder.img_android);*/

        holder.main_rel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCartChangedListener.setCartClickListener("position", position);
            }
        });
    }

    // items count
    @Override
    public int getItemCount() {
        return dataList.getResult().getDocuments().size();
    }

    /*
     * initialising viewholder to recycler view
     * */
    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView img_android;
        private TextView tv_android;
        private RelativeLayout main_rel;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_android = itemView.findViewById(R.id.tv_android);
            img_android = itemView.findViewById(R.id.img_android);
            main_rel = itemView.findViewById(R.id.main_rel);
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

