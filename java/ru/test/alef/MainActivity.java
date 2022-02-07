package ru.test.alef;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private String URL_JSON = "https://dev-tasks.alef.im/task-m-001/list.php";
    private JsonArrayRequest ArrayRequest ;
    private RequestQueue requestQueue ;
    private List<Photo> lst;
    private RecyclerView recyclerView;
    boolean isImageFitToScreen;

     private class ImageGalleryAdapter extends RecyclerView.Adapter<ImageGalleryAdapter.MyViewHolder>  {

         RequestOptions options;
         private List<Photo> mPhotos;
         private Context mContext;

        @Override
        public ImageGalleryAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            Context context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);
            View photoView = inflater.inflate(R.layout.item_photo, parent, false);
            ImageGalleryAdapter.MyViewHolder viewHolder = new ImageGalleryAdapter.MyViewHolder(photoView);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ImageGalleryAdapter.MyViewHolder holder, final int position) {


            //Photo photo = mPhotos[position];
            ImageView imageView = holder.mPhotoImageView;

                 Glide.with(mContext)
                        .load(mPhotos.get(position).getImage_url())
                        //photo.getUrl())
                        //.placeholder(R.drawable.col)
                        //.skipMemoryCache(true)
                        //.diskCacheStrategy(DiskCacheStrategy.NONE)
                        .into(imageView);
        }

        @Override
        public int getItemCount() {
            return mPhotos.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            public ImageView mPhotoImageView;

            public MyViewHolder(View itemView) {

                super(itemView);
                mPhotoImageView = (ImageView) itemView.findViewById(R.id.iv_photo);
                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View view) {

                int position = getAdapterPosition();
                if(position != RecyclerView.NO_POSITION) {
                    Photo fullPhoto = mPhotos.get(position);
                    Intent intent = new Intent(mContext, PhotoActivity.class);
                    intent.putExtra(PhotoActivity.EXTRA_PHOTO, fullPhoto.getImage_url());
                    startActivity(intent);
                }
            }
        }
        public ImageGalleryAdapter(Context context, List photos) {
            mContext = context;
            mPhotos = photos;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lst = new ArrayList<>();
        recyclerView=findViewById(R.id.rv_images);
        jsoncall();

        SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {


            @Override
            public void onRefresh() {
                Log.i("Refresh------", "Обновили");
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void jsoncall() {
        ArrayRequest = new JsonArrayRequest(URL_JSON, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                lst.clear();
                for (int i = 0 ; i<response.length();i++) {
                    try {
                        Photo photo = new Photo();
                        photo.setImage_url((String) response.get(i));
                        lst.add(photo);
                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                setuprecyclerview(lst);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        requestQueue = Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(ArrayRequest);

    }

    private void setuprecyclerview(List<Photo> lst) {

         RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);

         recyclerView.setHasFixedSize(true);
         recyclerView.setLayoutManager(layoutManager);


         ImageGalleryAdapter mAdapter = new ImageGalleryAdapter(this, lst) ;
         recyclerView.setAdapter(mAdapter);
    }
}