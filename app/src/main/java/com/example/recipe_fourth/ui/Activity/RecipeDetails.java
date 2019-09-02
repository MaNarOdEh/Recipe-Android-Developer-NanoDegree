package com.example.recipe_fourth.ui.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.recipe_fourth.Class.Recipe;
import com.example.recipe_fourth.Class.SimpleIdlingResource;
import com.example.recipe_fourth.Class.Step;
import com.example.recipe_fourth.MyWidget;
import com.example.recipe_fourth.R;
import com.example.recipe_fourth.ui.ingrdient.IngredientFragment;
import com.example.recipe_fourth.ui.steps.StepsFragment;
import com.example.recipe_fourth.ui.video.VideoFragment;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.paperdb.Paper;


public class RecipeDetails extends AppCompatActivity implements StepsFragment.OnCardViewClickListener {

    public Recipe recipe;
    public static final String LIST_INGREDIENTS="INGREDIENTS";
    public static final String LIST_STEPS="STEPS";
    private  static final String CURRENTFRGEMENTNUMBER="currentFragment";
    private static final String POSITION="position";
    private  static final String RECIPE="Recipe_Details";
    @BindView(R.id.video_layout) @Nullable() LinearLayout videoLayout;
    @BindView(R.id.details_linear)@Nullable() LinearLayout detailsLinear;
    @BindView(R.id.btn_add_widget)@Nullable() Button mBtnAddWidget;
    public static boolean is_appear=false;
    boolean tabletSize;
    int orientation;
    int position;
    @Nullable
    FragmentManager fragmentManager;
    public  static  ArrayList<String> arrayList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);
        ButterKnife.bind(this);
        ButterKnife.setDebug(true);
        Paper.init(this);
        tabletSize= getResources().getBoolean(R.bool.is_tablet);
        orientation = getResources().getConfiguration().orientation;
        mBtnAddWidget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int app_widget= AppWidgetManager.INVALID_APPWIDGET_ID;
               arrayList=new ArrayList<>();
                Toast.makeText(RecipeDetails.this, "MUST SAVE THE GREDIENTS", Toast.LENGTH_SHORT).show();
                String content="";
                for(int i=0;i<recipe.getIngredients().size();i++){
                   arrayList.add(recipe.getIngredients().get(i).getIngredient());
                }
                Paper.book().write("INGREDIENTS",arrayList);
                Paper.book().write("ID",recipe.getId()+"");

                Intent intent_meeting_update=new  Intent(RecipeDetails.this, MyWidget.class);
                intent_meeting_update.setAction(MyWidget.UPDATE_MEETING_ACTION);
                sendBroadcast(intent_meeting_update);
            }
        });
        if(savedInstanceState!=null) {
            recipe = (Recipe) savedInstanceState.getParcelable(RECIPE);
            // Toast.makeText(this, "  "+savedInstanceState.getInt(CURRENTFRGEMENTNUMBER), Toast.LENGTH_SHORT).show();
            if (!tabletSize && savedInstanceState.getInt(CURRENTFRGEMENTNUMBER) == 3) {
                position = savedInstanceState.getInt(POSITION);
                detailsLinear.setVisibility(View.GONE);
                videoLayout.setVisibility(View.VISIBLE);
            }

            if (tabletSize) {
                position = savedInstanceState.getInt(POSITION);
            }
        }
            Intent intent=getIntent();
            if(intent!=null){
                recipe=((Recipe)intent.getParcelableExtra(RECIPE));
            }
            if(savedInstanceState==null) {
                addFragment();
            }
            if(tabletSize){
                videoLayout.setVisibility(View.VISIBLE);
            }else{
                videoLayout.setVisibility(View.GONE);
            }


    }

    @Override
    protected void onResume() {
        super.onResume();
        is_appear=true;
    }

    private void addFragment() {
        IngredientFragment ingredientFragment=new IngredientFragment();
         fragmentManager=getSupportFragmentManager();
        Bundle bundle=new Bundle();
        if(recipe!=null){
        bundle.putParcelableArrayList(LIST_INGREDIENTS, (ArrayList<? extends Parcelable>) recipe.getIngredients());
        }
        ingredientFragment.setArguments(bundle);
        fragmentManager.beginTransaction().addToBackStack(null)
                .add(R.id.fragment_Ingredient,ingredientFragment)
                .commit();

        StepsFragment stepsFragment=new StepsFragment();

        Bundle bundle2=new Bundle();
        if(recipe!=null)
        bundle2.putParcelableArrayList(LIST_STEPS, (ArrayList<? extends Parcelable>) recipe.getSteps());

        stepsFragment.setArguments(bundle2);

        fragmentManager.beginTransaction().addToBackStack(null)
                .add(R.id.fragment_Step,stepsFragment)
                .commit();


        if(tabletSize) {
            VideoFragment videoFragment = new VideoFragment();
            Bundle bundle3 = new Bundle();
            bundle3.putParcelableArrayList(LIST_STEPS, (ArrayList<? extends Parcelable>) recipe.getSteps());
            bundle3.putInt("POSITION", 0);
            videoFragment.setArguments(bundle3);
            fragmentManager.beginTransaction().addToBackStack(null)
                    .add(R.id.fragment_video, videoFragment)
                    .commit();
        }
    }
    private  void add_fragmentsVideos(){
        if(!tabletSize) {
            detailsLinear.setVisibility(View.GONE);
            mBtnAddWidget.setVisibility(View.GONE);
            mBtnAddWidget.setVisibility(View.GONE);
        }
        videoLayout.setVisibility(View.VISIBLE);
        VideoFragment videoFragment = new VideoFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(LIST_STEPS, (ArrayList<? extends Parcelable>) recipe.getSteps());
        bundle.putInt("POSITION", position);
        videoFragment.setArguments(bundle);
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_video, videoFragment).addToBackStack("third")
                .commit();
    }

    @Override
    public void onBackPressed() {
        if(tabletSize){
            finish();
        }
        else if (getSupportFragmentManager().getBackStackEntryCount() == 2) {
            finish();
        }else if(getSupportFragmentManager().getBackStackEntryCount()==3){
            super.onBackPressed();
            detailsLinear.setVisibility(View.VISIBLE);
            videoLayout.setVisibility(View.GONE);
            mBtnAddWidget.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void setOnClickListener(int position) {
        this.position=position;
        add_fragmentsVideos();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(CURRENTFRGEMENTNUMBER, getSupportFragmentManager().getBackStackEntryCount());
        outState.putInt(POSITION, position);
        outState.putParcelable(RECIPE,recipe);
        super.onSaveInstanceState(outState);

    }

    @Override
    public void onRestoreInstanceState(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onRestoreInstanceState(savedInstanceState, persistentState);
    }
    public Step getStepNext(int position){
        if(recipe.getSteps().size()<=position+1){
            return null;
        }else {
            return recipe.getSteps().get(position+1);
        }
    }
    public Step getStepPrev(int position){
        if(position-1<0){
            return null;
        }else {
         return    recipe.getSteps().get(position - 1);
        }

    }
    public Step getStep(int position){
        if(position>=0&&position<recipe.getSteps().size()){
            return recipe.getSteps().get(position);
        }
        return null;
    }
}
