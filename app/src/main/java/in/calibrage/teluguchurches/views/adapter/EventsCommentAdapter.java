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

import in.calibrage.teluguchurches.R;
import in.calibrage.teluguchurches.util.Constants;
import in.calibrage.teluguchurches.util.localData.SharedPrefsData;
import in.calibrage.teluguchurches.views.model.EventViewAllRepliesResponseModel;
import in.calibrage.teluguchurches.views.model.GetUpComingEventsModel;


// This adapter is helpfull to show events commnets in Evnets Detailview

public class EventsCommentAdapter extends RecyclerView.Adapter<EventsCommentAdapter.MyViewHolder> {

    Context context;
    GetUpComingEventsModel dataList = new GetUpComingEventsModel();
    EventViewAllRepliesResponseModel mDataList = new EventViewAllRepliesResponseModel();
    OnCartChangedListener onCartChangedListener;

    private String comingForm;
    private AlertDialog alert;
    private AlertDialog.Builder builder;
    private int userId;
    private int datalistUserId;

    /**
     * Constructor provides information about, and access to, a single constructor for a class.
     * @param context
     * @param dataList
     * @param mDataList
     * @param comingForm
     */
    public EventsCommentAdapter(Context context, GetUpComingEventsModel dataList, String comingForm, EventViewAllRepliesResponseModel mDataList) {
        this.context = context;
        this.dataList = dataList;
        this.comingForm = comingForm;
        this.mDataList = mDataList;
    }

    /*
     * creating viewholder to recycler view here
     * */
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_comment, parent, false);
        return new EventsCommentAdapter.MyViewHolder(view);
    }

    /*
     * binding data to viewholder from API here
     * */
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        if (comingForm.equalsIgnoreCase("post")) {
            userId = SharedPrefsData.getInt(context, Constants.ID, Constants.PREF_NAME);
            datalistUserId = dataList.getResult().getCommentDetails().get(position).getUserId();
            if (userId == datalistUserId) {
                holder.imageDots.setVisibility(View.VISIBLE);

            }


            Glide.with(context).load(dataList.getResult().getCommentDetails().get(position).getUserImage())
                    .fitCenter()
                    .error(R.drawable.ic_userp)
                    .into(holder.image);


            holder.commentText.setText(dataList.getResult().getCommentDetails().get(position).getComment());
            holder.userText.setText(dataList.getResult().getCommentDetails().get(position).getCommentByUser());
            if (dataList.getResult().getCommentDetails().get(position).getReplyCount() > 0) {
                holder.replyCount.setText("" + dataList.getResult().getCommentDetails().get(position).getReplyCount());
                holder.viewReply.setVisibility(View.VISIBLE);
            } else {
                holder.replyCount.setText("0");
                holder.viewReply.setVisibility(View.INVISIBLE);
            }

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
                holder.imageDots.setVisibility(View.VISIBLE);

            } else {
                holder.viewReply.setVisibility(View.GONE);
                holder.replyImg.setVisibility(View.GONE);
                holder.imageDots.setVisibility(View.GONE);
            }

            Glide.with(context).load(mDataList.getListResult().get(position).getUserImage())
                    .fitCenter()
                    .error(R.drawable.ic_userp)
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
    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView commentText, userText, viewReply, replyCount;
        private RelativeLayout mainLay;
        private ImageView imageDots, replyImg, image;
        private CardView cardLay;

        public MyViewHolder(View itemView) {
            super(itemView);

            commentText = itemView.findViewById(R.id.commentText);
            userText = itemView.findViewById(R.id.userText);
            mainLay = itemView.findViewById(R.id.mainLay);
            imageDots = itemView.findViewById(R.id.imageDots);
            viewReply = itemView.findViewById(R.id.viewReply);
            replyCount = itemView.findViewById(R.id.replyCount);
            replyImg = itemView.findViewById(R.id.replyImg);
            image = itemView.findViewById(R.id.image);
            cardLay = itemView.findViewById(R.id.cardLay);
            imageDots.setVisibility(View.GONE);
            viewReply.setVisibility(View.INVISIBLE);
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

