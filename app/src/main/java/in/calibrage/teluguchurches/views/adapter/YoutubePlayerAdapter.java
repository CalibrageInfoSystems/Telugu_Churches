package in.calibrage.teluguchurches.views.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import in.calibrage.teluguchurches.R;
import in.calibrage.teluguchurches.util.Constants;
import in.calibrage.teluguchurches.util.localData.SharedPrefsData;
import in.calibrage.teluguchurches.views.Activities.YoutubePlayerActivity;
import in.calibrage.teluguchurches.views.model.GetPostByCategoryIdResponseModel;

/**
 * This adapter is helpfull to play Video's/Audio's in view
 */

public class YoutubePlayerAdapter extends RecyclerView.Adapter<YoutubePlayerAdapter.MyHolder> implements YouTubeThumbnailView.OnInitializedListener {
    private Context context;
    public int postId;
    public String link, url;
    private ArrayList<String> linkArray, parts;
    private Map<View, YouTubeThumbnailLoader> mLoaders;
    GetPostByCategoryIdResponseModel dataList = new GetPostByCategoryIdResponseModel();

    /**
     * Constructor provides information about, and access to, a single constructor for a class.
     *
     * @param context
     * @param dataList
     * @param linkArray
     */
    public YoutubePlayerAdapter(Context context, GetPostByCategoryIdResponseModel dataList, ArrayList<String> linkArray) {
        this.context = context;
        this.dataList = dataList;
        this.linkArray = linkArray;
        mLoaders = new HashMap<>();

    }

    /*
     * creating viewholder to recycler view here
     * */
    @Override
    public YoutubePlayerAdapter.MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_layout, null);
        YoutubePlayerAdapter.MyHolder mh = new YoutubePlayerAdapter.MyHolder(v);
        return mh;
    }

    /*
     * binding data to viewholder from API here
     * */
    @Override
    public void onBindViewHolder(YoutubePlayerAdapter.MyHolder holder, final int position) {
        holder.textView_title.setText(dataList.getResult().getVideos().get(position).getTitle());
        holder.imageView_thumbnail.setTag(linkArray.get(position));
        //   holder.imageView_thumbnail.setImageBitmap(null);
      //  holder.imageView_thumbnail.initialize(context.getString(R.string.DEVELOPER_KEY), this);
        holder.imageView_thumbnail.initialize("AIzaSyDt4waoZKSYTWq7b34qUwMW6Nc7dWlEZAw", this);

        final YouTubeThumbnailLoader loader = mLoaders.get(holder.imageView_thumbnail);

        if (loader == null) {
            //Loader is currently initialising
            holder.imageView_thumbnail.setTag(linkArray.get(position));
        } else {
            //The loader is already initialised
            //Note that it's possible to get a DeadObjectException here
            try {
                loader.setVideo(linkArray.get(position));
            } catch (IllegalStateException exception) {
                //If the Loader has been released then remove it from the map and re-init
                mLoaders.remove(holder.imageView_thumbnail);
                holder.imageView_thumbnail.initialize(context.getString(R.string.DEVELOPER_KEY), this);

            }
        }

        /**
         * @param onClickListner
         */
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //sending data to screen from intent
                Intent intent = new Intent(context, YoutubePlayerActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("link", linkArray.get(position))
                        .putExtra("url", dataList.getResult().getVideos().get(position).getEmbededUrl())
                        .putExtra("postTypeId", dataList.getResult().getVideos().get(position).getId())
                        .putExtra("likeCount", dataList.getResult().getVideos().get(position).getLikeCount())
                        .putExtra("disLikeCount", dataList.getResult().getVideos().get(position).getDisLikeCount());
                postId = dataList.getResult().getVideos().get(position).getId();
                link = linkArray.get(position);
                url = dataList.getResult().getVideos().get(position).getEmbededUrl();

                //storing data in shared preferences
                SharedPrefsData.putString(context, Constants.LinkEnd, link, Constants.PREF_NAME);
                SharedPrefsData.putString(context, Constants.URL, url, Constants.PREF_NAME);
                SharedPrefsData.putInt(context, Constants.PostId, postId, Constants.PREF_NAME);
                context.startActivity(intent);

            }
        });


    }

    // items count
    @Override
    public int getItemCount() {
        return linkArray.size();
    }

    /*
     * initialzing loader success method
     * */
    @Override
    public void onInitializationSuccess(YouTubeThumbnailView view, YouTubeThumbnailLoader loader) {
        mLoaders.put(view, loader);
        loader.setVideo((String) view.getTag());
    }

    /*
     * initialzing loader failure method
     * */
    @Override
    public void onInitializationFailure(YouTubeThumbnailView youTubeThumbnailView, YouTubeInitializationResult errorReason) {
        final String errorMessage = errorReason.toString();
        Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show();
    }

    /*
     * initialising viewholder to recycler view
     * */
    public class MyHolder extends RecyclerView.ViewHolder {
        private TextView textView_title;
        private YouTubeThumbnailView imageView_thumbnail;


        public MyHolder(View itemView) {
            super(itemView);
            textView_title = itemView.findViewById(R.id.textView_title);
            imageView_thumbnail = itemView.findViewById(R.id.imageView_thumbnail);
        }
    }


}
