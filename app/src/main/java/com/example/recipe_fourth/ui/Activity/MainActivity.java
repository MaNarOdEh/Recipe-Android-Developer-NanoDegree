package com.example.recipe_fourth.ui.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.recipe_fourth.R;
import com.example.recipe_fourth.API_Service.ServerConcect;
import com.example.recipe_fourth.Class.InternetConnectivity.ConnectivityReceiver;
import com.example.recipe_fourth.Class.InternetConnectivity.MyApplication;
import com.example.recipe_fourth.Class.Recipe;
import com.example.recipe_fourth.ui.Recycle.RecipeAdapter;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener,RecipeAdapter.OnRecipeListener{

    @BindView(R.id.recipe_card_rv) @Nullable RecyclerView recipeCardRv;
    @BindView(R.id.internet_connection_layout) @Nullable LinearLayout internetConnectionLayout;
    //CountingIdlingResource countingIdlingResource=new CountingIdlingResource("IDLE_RESOURSE");
    public boolean IdlingResource=false;
    public boolean no_check=false;
    RecipeAdapter recipeAdapter;
    List<Recipe>list;
    public static final String BASE_API="https://d17h27t6h515a5.cloudfront.net/";
    private static final String SAVE_LIST="LISTSAVED";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        ButterKnife.setDebug(true);
        initialize();
        if(savedInstanceState!=null){
            list=savedInstanceState.getParcelableArrayList(SAVE_LIST);
            recipeAdapter=new RecipeAdapter(list,this);
            recipeCardRv.setAdapter(recipeAdapter);

        }else if(list==null){
            list=new ArrayList<>();
            fetchData();
        }

    }
    //https://www.raywenderlich.com/995-android-gridview-tutorial
    //https://medium.com/programming-lite/best-practices-for-supporting-android-application-in-multiple-screen-a685afa83493
    //https://www.androidauthority.com/android-fragments-852516/
    //http://android-er.blogspot.com/2013/04/handle-different-layout-for-phone-and.html
    //http://android-er.blogspot.com/2013/04/handle-different-layout-for-phone-and.html
    //https://androidwave.com/video-streaming-exoplayer-in-android/
    private void initialize() {
        recipeAdapter=new RecipeAdapter(list,this);
        recipeCardRv.setNestedScrollingEnabled(false);
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            recipeCardRv.setLayoutManager(new GridLayoutManager(MainActivity.this, getResources().getInteger(R.integer.gallery_columns)));
        }
        recipeCardRv.setLayoutManager(new GridLayoutManager(MainActivity.this, getResources().getInteger(R.integer.gallery_columns)));
        recipeCardRv.setAdapter(recipeAdapter);
        recipeAdapter.notifyDataSetChanged();

    }

    // Method to manually check connection status
    private boolean checkConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        return isConnected;
    }
    // Showing the status in Snackbar
    private void showSnack(String message) {
        View contextView = findViewById(R.id.context_view);
        Snackbar snackbar = Snackbar
                .make(contextView, message, Snackbar.LENGTH_LONG);
        snackbar.show();
        if(message.equals("Sorry! Not connected to internet")) {

             internetConnectionLayout.setVisibility(View.VISIBLE);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        MyApplication.getInstance().setConnectivityListener(this);

    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if(!isConnected)showSnack("Sorry! Not connected to internet");
    }
    private  void fetchData() {
        //countingIdlingResource.increment();
        if (checkConnection()) {
            IdlingResource=false;
            internetConnectionLayout.setVisibility(View.GONE);
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_API)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            ServerConcect services = retrofit.create(ServerConcect.class);
            Call<List<Recipe>> call = services.recipeServer();
            call.enqueue(new Callback<List<Recipe>>() {
                @Override
                public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                    if (response != null && response.body() != null) {
                        list = response.body();
                        recipeAdapter = new RecipeAdapter(list,MainActivity.this);
                      //  recipeAdapter.notifyDataSetChanged();
                         recipeCardRv.setAdapter(recipeAdapter);
                      //  countingIdlingResource.decrement();
                        IdlingResource=true;
                    }else{
                        no_check=true;
                        IdlingResource=true;

                    }
                }

                @Override
                public void onFailure(Call<List<Recipe>> call, Throwable t) {
                    showSnack(t.getMessage()+"   "+t.toString());
                    Log.e("WRONG",t.getMessage());
                 //   countingIdlingResource.decrement();
                    IdlingResource=true;
                    no_check=true;
                 // showSnack("Sorry! Something Wrong  we will try fixed this error in the a few minutes!!"+t.getMessage());
                }
            });
        }else{
            no_check=true;
            IdlingResource=true;

            showSnack("Sorry! Not connected to internet");
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    @Override
    public void onRecipeClick(int position) {
       // Intents.init()
       Intent intent=new Intent(MainActivity.this, RecipeDetails.class);
         intent.putExtra("Recipe_Details",((Parcelable) list.get(position)));
         startActivity(intent);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if(list!=null)
            outState.putParcelableArrayList(SAVE_LIST, (ArrayList<? extends Parcelable>) list);
        super.onSaveInstanceState(outState);

    }

    @Override
    public void onRestoreInstanceState(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onRestoreInstanceState(savedInstanceState, persistentState);
    }
}
