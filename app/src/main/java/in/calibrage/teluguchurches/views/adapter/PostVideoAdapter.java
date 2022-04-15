package in.calibrage.teluguchurches.views.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.calibrage.teluguchurches.R;
import in.calibrage.teluguchurches.util.Constants;
import in.calibrage.teluguchurches.util.localData.SharedPrefsData;
import in.calibrage.teluguchurches.views.Activities.YoutubePlayerActivity;
import in.calibrage.teluguchurches.views.model.PostImageResponseModel;

/**
 * This adapter is helpfull to play Video's/Audio's in views
 */

public class PostVideoAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements YouTubeThumbnailView.OnInitializedListener {
    private Context context;
    public int postId;
    public String link, url;
    private ArrayList<String> linkArray, parts;
    private Map<View, YouTubeThumbnailLoader> mLoaders;
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;
    private List<T> myDataSet;
    // The minimum amount of items to have below your current scroll position before loading more.
    private int visibleThreshold = 2;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    View v;
    ArrayList<PostImageResponseModel.ListResult> postVideoList;
    private PostVideoAdapter.OnLoadMoreListener onLoadMoreListener;
    private OnCartChangedListener onCartChangedListener;

    /**
     * Constructor provides information about, and access to, a single constructor for a class.
     *
     * @param linkArray
     * @param context
     * @param myDataSet
     */
    public PostVideoAdapter(Context context, ArrayList<String> linkArray, List<T> myDataSet, RecyclerView recyclerView) {
        this.context = context;
        this.linkArray = linkArray;
        mLoaders = new HashMap<>();
        this.myDataSet = myDataSet;
        postVideoList = (ArrayList<PostImageResponseModel.ListResult>) myDataSet;
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
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_layout, parent, false);
            vh = new PostVideoAdapter.ViewHolder(v);
        } else {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.progress_item, parent, false);

            vh = new PostVideoAdapter.ProgressViewHolder(v);
        }
        return vh;

    }


    /*
     * binding data to viewholder from API here
     * */
    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof PostVideoAdapter.ViewHolder) {
            ((ViewHolder) holder).textView_title.setText(postVideoList.get(position).getTitle());
            ((ViewHolder) holder).imageView_thumbnail.setTag(linkArray.get(position));
            //  ((ViewHolder) holder).imageView_thumbnail.setImageBitmap(null);
            ((ViewHolder) holder).imageView_thumbnail.initialize(context.getString(R.string.DEVELOPER_KEY), this);

            final YouTubeThumbnailLoader loader = mLoaders.get(((ViewHolder) holder).imageView_thumbnail);

            if (loader == null) {
                //Loader is currently initialising
                ((ViewHolder) holder).imageView_thumbnail.setTag(linkArray.get(position));
            } else {
                //The loader is already initialised
                //Note that it's possible to get a DeadObjectException here
                try {
                    loader.setVideo(linkArray.get(position));
                } catch (IllegalStateException exception) {
                    //If the Loader has been released then remove it from the map and re-init
                    mLoaders.remove(((ViewHolder) holder).imageView_thumbnail);
                    ((ViewHolder) holder).imageView_thumbnail.initialize(context.getString(R.string.DEVELOPER_KEY), this);

                }
            }

            /**
             * @param onClickListner
             */
            ((ViewHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //sending data to screen using intent
                    Intent intent = new Intent(holder.itemView.getContext(), YoutubePlayerActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("link", linkArray.get(position))
                            .putExtra("url", postVideoList.get(position).getEmbededUrl())
                            .putExtra("postTypeId", postVideoList.get(position).getId())
                            .putExtra("likeCount", postVideoList.get(position).getLikeCount())
                            .putExtra("disLikeCount", postVideoList.get(position).getDisLikeCount());
                    postId = postVideoList.get(position).getId();
                    link = linkArray.get(position);
                    url = postVideoList.get(position).getEmbededUrl();

                    // storing required data in shared preferences
                    SharedPrefsData.putString(context, Constants.LinkEnd, link, Constants.PREF_NAME);
                    SharedPrefsData.putString(context, Constants.URL, url, Constants.PREF_NAME);
                    SharedPrefsData.putInt(context, Constants.PostId, postId, Constants.PREF_NAME);
                    holder.itemView.getContext().startActivity(intent);

                }
            });
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
        return this.myDataSet.size();
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
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textView_title;
        private YouTubeThumbnailView imageView_thumbnail;


        public ViewHolder(View itemView) {
            super(itemView);
            textView_title = itemView.findViewById(R.id.textView_title);
            imageView_thumbnail = itemView.findViewById(R.id.imageView_thumbnail);

        }
    }

    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public ProgressViewHolder(View v) {
            super(v);
            progressBar = v.findViewById(R.id.progressBar);
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
