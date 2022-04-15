package in.calibrage.teluguchurches.views.adapter;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import de.hdodenhof.circleimageview.CircleImageView;
import in.calibrage.teluguchurches.R;
import in.calibrage.teluguchurches.util.Constants;
import in.calibrage.teluguchurches.util.localData.SharedPrefsData;
import in.calibrage.teluguchurches.views.model.GetPostByIdResponseModel;
import in.calibrage.teluguchurches.views.model.ViewAllRepliesResponseModel;


/*
 * This adapter is helpfull to show commnets replies in all view's
 * */
public class ViewAllRepliesAdapter extends RecyclerView.Adapter<ViewAllRepliesAdapter.MyHolder> {
    private Context context;
    GetPostByIdResponseModel dataList = new GetPostByIdResponseModel();
    ViewAllRepliesResponseModel mDataList = new ViewAllRepliesResponseModel();
    private AlertDialog alert;
    private AlertDialog.Builder builder;
    private OnCartChangedListener onCartChangedListener;
    private String comingForm;
    private int userId;
    private int datalistUserId;

    /**
     * Constructor provides information about, and access to, a single constructor for a class.
     *
     * @param context
     * @param dataList
     * @param mDataList
     * @param comingForm
     */
    public ViewAllRepliesAdapter(Context context, GetPostByIdResponseModel dataList, String comingForm, ViewAllRepliesResponseModel mDataList) {
        this.context = context;
        this.dataList = dataList;
        this.comingForm = comingForm;
        this.mDataList = mDataList;

    }

    /*
     * creating viewholder to recycler view here
     * */
    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_view_all_replies, parent, false);
        return new ViewAllRepliesAdapter.MyHolder(view);
    }

    /*
     * binding data to viewholder from API here
     * */
    @Override
    public void onBindViewHolder(final MyHolder holder, final int position) {
        if (comingForm.equalsIgnoreCase("post")) {
            userId = SharedPrefsData.getInt(context, Constants.ID, Constants.PREF_NAME);
            datalistUserId = dataList.getResult().getCommentDetails().get(position).getUserId();
            if (userId == datalistUserId) {
                holder.imageDots.setVisibility(View.VISIBLE);

            }
            holder.commentText.setText(dataList.getResult().getCommentDetails().get(position).getComment());
            holder.userText.setText(dataList.getResult().getCommentDetails().get(position).getCommentByUser());
            if (dataList.getResult().getCommentDetails().get(position).getReplyCount() > 0)
                holder.replyCount.setText("" + dataList.getResult().getCommentDetails().get(position).getReplyCount());

            holder.imageDots.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    PopupMenu popup = new PopupMenu(context, holder.imageDots);

                    popup.inflate(R.menu.popup_menu);

                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.editText:
                                    onCartChangedListener.setCartClickListener("edit", position);
                                    break;
                                case R.id.deleteText:
                                    onCartChangedListener.setCartClickListener("delete", position);
                                    break;

                            }
                            return false;
                        }
                    });

                    popup.show();
                }
            });

            /**
             * @param onLongClickListner
             */
            holder.mainLay.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    onCartChangedListener.setCartClickListener("commentClick", position);
                    return false;
                }
            });

            /**
             * @param onClickListner
             */
            holder.viewReply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onCartChangedListener.setCartClickListener("viewAllReplies", position);

                }
            });

            /**
             * @param onClickListner
             */
            holder.replyImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onCartChangedListener.setCartClickListener("reply", position);
                }
            });

        } else {
            holder.commentText.setText(mDataList.getListResult().get(position).getComment());
            holder.userText.setText(mDataList.getListResult().get(position).getCommentByUser());
            if (position == 0) {
                holder.cardLay.setBackgroundColor(context.getResources().getColor(R.color.light_bg));
                holder.viewReply.setVisibility(View.GONE);
                holder.replyImg.setVisibility(View.GONE);
                holder.imageDots.setVisibility(View.GONE);
            } else {
                holder.viewReply.setVisibility(View.GONE);
                holder.replyImg.setVisibility(View.GONE);
                holder.imageDots.setVisibility(View.GONE);
            }
            Glide.with(context).load(mDataList.getListResult().get(position).getUserImage())
                    .fitCenter()
                    .error(R.drawable.profile_pic)
                    .into(holder.image);

        }


    }

    // items count
    @Override
    public int getItemCount() {
        if (comingForm.equalsIgnoreCase("post"))
            return dataList.getResult().getCommentDetails().size();
        else
            return mDataList.getListResult().size();
    }

    /*
     * initialising viewholder to recycler view
     * */
    public class MyHolder extends RecyclerView.ViewHolder {
        private TextView commentText, userText, viewReply, replyCount;
        private RelativeLayout mainLay;
        private ImageView imageDots, replyImg;
        private CardView cardLay;
        private CircleImageView image;

        public MyHolder(View itemView) {
            super(itemView);
            commentText = itemView.findViewById(R.id.commentText);
            userText = itemView.findViewById(R.id.userText);
            viewReply = itemView.findViewById(R.id.viewReply);
            replyCount = itemView.findViewById(R.id.replyCount);
            mainLay = itemView.findViewById(R.id.mainLay);
            imageDots = itemView.findViewById(R.id.imageDots);
            replyImg = itemView.findViewById(R.id.replyImg);
            cardLay = itemView.findViewById(R.id.cardLay);
            image = itemView.findViewById(R.id.image);
            imageDots.setVisibility(View.GONE);

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
