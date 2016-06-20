package practice.com.restfulservice;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.net.URL;
import java.util.List;

/**
 * Created by user on 6/15/2016.
 */
public class FlowerAdapter extends RecyclerView.Adapter<FlowerAdapter.FlowerView> {

    private Context context;
    private List<Flower> flowerList;
    private final LayoutInflater in;
    private LruCache<Integer, Bitmap> imageCache;

    public FlowerAdapter(Context context, int resource, List<Flower> objects) {
        this.context = context;
        in = LayoutInflater.from(context);
        this.flowerList = objects;

        //memory available
        final int maxMemory = (int) Runtime.getRuntime().maxMemory() / 1024;
        //use 1/8 memory for cache
        final int cacheSize = maxMemory / 8;

        imageCache = new LruCache<>(cacheSize);
    }

    public class FlowerView extends RecyclerView.ViewHolder {
        TextView content;
        ImageView image;

        public FlowerView(final View item) {
            super(item);
            content = (TextView) item.findViewById(R.id.content);
            image = (ImageView) item.findViewById(R.id.image);
        }
    }

    @Override
    public FlowerAdapter.FlowerView onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = in.inflate(R.layout.flower_item, parent, false);
        FlowerView holder = new FlowerView(view);

        return holder;
    }


    @Override
    public void onBindViewHolder(FlowerAdapter.FlowerView holder, int position) {
        Flower fl = flowerList.get(position);
        holder.content.setText(fl.getName());

        Bitmap bitmap = imageCache.get(fl.getProductId());

        if(bitmap != null) {
            holder.image.setImageBitmap(fl.getBitmap());
        } else {
            holder.image.setImageDrawable(null);
            FlowerAndView container = new FlowerAndView(fl, holder);

            ImageLoader loader = new ImageLoader();
            loader.execute(container);
        }
    }

    public int getItemCount() {
        return flowerList.size();
    }

    class FlowerAndView {
        public Flower flower;
        public FlowerView holder;

        public FlowerAndView(Flower flower, FlowerView holder) {
            this.flower = flower;
            this.holder = holder;
        }
    }

    private class ImageLoader extends AsyncTask<FlowerAndView, Void, FlowerAndView> {
        @Override
        protected FlowerAndView doInBackground(FlowerAndView... params) {

            FlowerAndView container = params[0];
            Flower flower = container.flower;

            try {
                String imageURL = MainActivity.baseURL + flower.getPhoto();
                InputStream in = (InputStream) new URL(imageURL).getContent();
                Bitmap bitmap = BitmapFactory.decodeStream(in);
                flower.setBitmap(bitmap);
                in.close();
//                container.bitmap = bitmap;
                imageCache.put(container.flower.getProductId(), container.flower.getBitmap());

                return container;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(FlowerAndView result) {
            result.holder.image.setImageBitmap(result.flower.getBitmap());
        }
    }

}