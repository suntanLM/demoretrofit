package com.example.phuon.demojson;

import android.content.res.Resources;
import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    List<Product> listing;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView) findViewById(R.id.recycle);
        ImageLoaderConfiguration config=new ImageLoaderConfiguration.Builder(this).build();
        ImageLoader.getInstance().init(config);
        listing = new ArrayList<>();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api2.mytrendin.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        APiService service = retrofit.create(APiService.class);
        Call<List<Product>> call = service.getbookdetail();
        call.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                List<Product> list = response.body();
                Product product = null;
                for (int i=0;i<list.size();i++)
                {
                    product= new Product();
                    String name = list.get(i).getProductname();
                    String color = list.get(i).getColor();
                    String image = list.get(i).getImageurl();
                    String price = list.get(i).getPrice();
                    product.setProductname(name);
                    product.setColor(color);
                    product.setImageurl(image);
                    product.setPrice(price);
                    listing.add(product);
                }
                Recycleradapter recyclerAdapter = new Recycleradapter(listing,ImageLoader.getInstance());
                RecyclerView.LayoutManager recyce = new GridLayoutManager(MainActivity.this,2);
                // RecyclerView.LayoutManager recyce = new LinearLayoutManager(MainActivity.this);
                recyclerView.addItemDecoration(new GridSpacingdecoration(2, dpToPx(10), true));
                recyclerView.setLayoutManager(recyce);
                recyclerView.setItemAnimator( new DefaultItemAnimator());
                recyclerView.setAdapter(recyclerAdapter);


            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {

            }
        });

    }
    public class GridSpacingdecoration extends RecyclerView.ItemDecoration {

        private int span;
        private int space;
        private boolean include;

        public GridSpacingdecoration(int span, int space, boolean include) {
            this.span = span;
            this.space = space;
            this.include = include;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view);
            int column = position % span;

            if (include) {
                outRect.left = space - column * space / span;
                outRect.right = (column + 1) * space / span;

                if (position < span) {
                    outRect.top = space;
                }
                outRect.bottom = space;
            } else {
                outRect.left = column * space / span;
                outRect.right = space - (column + 1) * space / span;
                if (position >= span) {
                    outRect.top = space;
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }
}