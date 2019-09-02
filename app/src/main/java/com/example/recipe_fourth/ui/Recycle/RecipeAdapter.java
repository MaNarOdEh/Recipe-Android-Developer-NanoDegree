package com.example.recipe_fourth.ui.Recycle;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipe_fourth.R;
import com.example.recipe_fourth.Class.Recipe;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ViewHolder> {
    List<Recipe>recipes;
    private  OnRecipeListener onRecipeListener;
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.name_recipe_tv) @Nullable() TextView nameRecipeTv;
        @BindView(R.id.recipe_img) @Nullable()   ImageView recipeImg;
        OnRecipeListener onRecipeListener;
        Context context ;


        public ViewHolder(View viewItems,OnRecipeListener onRecipeListener) {
            super(viewItems);
            ButterKnife.bind(this, viewItems);
            context = viewItems.getContext();
            this.onRecipeListener=onRecipeListener;
            nameRecipeTv.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            onRecipeListener.onRecipeClick(getAdapterPosition());
        }
    }
    public interface OnRecipeListener{
        public void  onRecipeClick(int position);
    }
    public RecipeAdapter(){

    }
    public RecipeAdapter(List<Recipe>list,OnRecipeListener onRecipeListener){
        if(list!=null) {
            this.recipes = list;
            this.onRecipeListener = onRecipeListener;
        }
        else
            this.recipes=new ArrayList<>();
    }
    @NonNull
    @Override
    public RecipeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View contactView= LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_card_layout,parent,false);
        return new ViewHolder(contactView,onRecipeListener);
    }


    @Override
    public void onBindViewHolder(@NonNull final RecipeAdapter.ViewHolder holder, int position) {
         final Recipe recipe=recipes.get(position);
         holder.nameRecipeTv.setText(recipe.getName()!=null?recipe.getName():"Delicious Meal");
        String url_img = ""+recipe.getImage();
        if(url_img==null||url_img.isEmpty()) {
           url_img="No";
           String name=holder.nameRecipeTv.getText().toString();
            if(name.equals("Nutella Pie")){
                Picasso.get()
                        .load(url_img)
                        .placeholder(R.drawable.nutella)
                        .error(R.drawable.nutella)
                        .into(holder.recipeImg);
            }else if(name.equals("Brownies")){
                Picasso.get()
                        .load(url_img)
                        .placeholder(R.drawable.brownies)
                        .error(R.drawable.brownies)
                        .into(holder.recipeImg);
            }else if(name.equals("Cheesecake")){
                Picasso.get()
                        .load(url_img)
                        .placeholder(R.drawable.cheesecake)
                        .error(R.drawable.cheesecake)
                        .into(holder.recipeImg);

            }
            else if(name.equals("Yellow Cake")){
                Picasso.get()
                        .load(url_img)
                        .placeholder(R.drawable.yellow_cake)
                        .error(R.drawable.yellow_cake)
                        .into(holder.recipeImg);
            }else{
                Picasso.get()
                        .load(url_img)
                        .placeholder(R.drawable.recipe_icons)
                        .error(R.drawable.recipe_icons)
                        .into(holder.recipeImg);
            }
        }else{
            Picasso.get()
                    .load(url_img)
                    .placeholder(R.drawable.recipe_icons)
                    .error(R.drawable.recipe_icons)
                    .into(holder.recipeImg);
        }
    }


    @Override
    public int getItemCount() {
        return (recipes==null)?0:recipes.size();
    }
}
