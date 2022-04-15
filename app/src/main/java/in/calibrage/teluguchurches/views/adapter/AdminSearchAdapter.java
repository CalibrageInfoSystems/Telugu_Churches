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
import in.calibrage.teluguchurches.util.CommonUtil;
import in.calibrage.teluguchurches.views.model.GetAllAdminResponseModel;

/*
 * this adater is helpful to show the pastor's list search
 * */

public class AdminSearchAdapter extends RecyclerView.Adapter<AdminSearchAdapter.ViewHolder> {
    private Context context;
    private String openTime, closeTime;
    GetAllAdminResponseModel dataList = new GetAllAdminResponseModel();
    private OnChangedListener onChangedListener;

    /**
     * Constructor provides information about, and access to, a single constructor for a class.
     *  @param context
     * @param dataList
     */
    public AdminSearchAdapter(Context context, GetAllAdminResponseModel dataList) {
        this.dataList = dataList;
        this.context = context;
    }


    @Override
    public AdminSearchAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_admin, parent, false);
        return new AdminSearchAdapter.ViewHolder(view);

    }

    /*
     * binding data to viewholder from API here
     * */
    @Override
    public void onBindViewHolder(AdminSearchAdapter.ViewHolder holder, final int position) {
        if (holder instanceof AdminSearchAdapter.ViewHolder) {
            ((ViewHolder) holder).churchNameText.setText(CommonUtil.fromHtml("<b><font color=\"#ffffff\">" + "Church Name: " + "</font><b>" + dataList.getListResult().get(position).getChurchName()));
            ((ViewHolder) holder).adminText.setText(CommonUtil.fromHtml("<b><font color=\"#ffffff\">" + "Name: " + "</font><b>" + dataList.getListResult().get(position).getChurchAdmin()));
            ((ViewHolder) holder).numberText.setText(CommonUtil.fromHtml("<b><font color=\"#ffffff\">" + "Mobile Number: " + "</font><b>" + dataList.getListResult().get(position).getMobileNumber()));
            ((ViewHolder) holder).emailText.setText(CommonUtil.fromHtml("<b><font color=\"#ffffff\">" + "Email: " + "</font><b>" + dataList.getListResult().get(position).getEmail()));

            ((ViewHolder) holder).mainrlty.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    onChangedListener.setClickListener("MainSearch", position);
                }
            });

            Glide.with(context).load(dataList.getListResult().get(position).getChurchImage())
                    .fitCenter()
                    .error(R.drawable.pastor)
                    .into(((ViewHolder) holder).userImg);

        }

    }

    // items count
    @Override
    public int getItemCount() {
        return dataList.getListResult().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView churchNameText, adminText, numberText, emailText;
        private RelativeLayout mainrlty;
        private ImageView userImg;

        public ViewHolder(View v) {
            super(v);
            churchNameText =  v.findViewById(R.id.churchNameText);
            adminText =  v.findViewById(R.id.adminText);
            numberText =  v.findViewById(R.id.numberText);
            emailText = v.findViewById(R.id.emailText);
            mainrlty =  v.findViewById(R.id.mainrlty);
            userImg = v.findViewById(R.id.userImg);
        }
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


}
