package com.example.recipe_fourth.ui.Recycle;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipe_fourth.Class.Ingredient;
import com.example.recipe_fourth.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class IngrdientAdapter extends RecyclerView.Adapter<IngrdientAdapter.ViewHolder>  {
    List<Ingredient> ingredients;

    public static class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.quantity_tv) @Nullable() TextView quantityTv;
        @BindView(R.id.type_quantity_tv) @Nullable()  TextView typeQuantityTv;
        @BindView(R.id.name_ingredient_tv) @Nullable()   TextView nameIngredientTv;
        @BindView(R.id.quantity_measure_img)@Nullable() ImageView quantityMeasureImg;
        Context context ;
        public ViewHolder(View viewItems) {
            super(viewItems);
            ButterKnife.bind(this, viewItems);
            context = viewItems.getContext();

        }

    }
    public IngrdientAdapter(){
        this.ingredients=new ArrayList<>();
    }


    public IngrdientAdapter(List<Ingredient> ingredients){
        if(ingredients!=null) {
            this.ingredients = ingredients;
        }else{
            this.ingredients=new ArrayList<>();
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View contactView= LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_ingredien_card,parent,false);
        return new ViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Ingredient ingredient=ingredients.get(position);
        DecimalFormat df = new DecimalFormat("###.#");
        holder.quantityTv.setText(ingredient.getQuantity()!=null?df.format(ingredient.getQuantity())+"":"0");
        holder.typeQuantityTv.setText(ingredient.getMeasure()!=null?ingredient.getMeasure():"depends on what you prefer");
        holder.nameIngredientTv.setText(ingredient.getIngredient()!=null?ingredient.getIngredient():"Step");
        String measure=ingredient.getMeasure();
        if(measure!=null&&!measure.isEmpty()){
            measure=measure.toUpperCase();
            Drawable res ;
            switch (measure) {
                case "CUP":
                    res = holder.context.getResources().getDrawable(R.drawable.measuring_cup);
                    holder.quantityMeasureImg.setImageDrawable(res);
                    break;
                case "TBLSP":
                    res = holder.context.getResources().getDrawable(R.drawable.spoon);
                    holder.quantityMeasureImg.setImageDrawable(res);
                    break;
                case "TSP":
                    res = holder.context.getResources().getDrawable(R.drawable.measuringspoon);
                    holder.quantityMeasureImg.setImageDrawable(res);
                    break;
                default:
                    res = holder.context.getResources().getDrawable(R.drawable.measuring_spoons);
                    holder.quantityMeasureImg.setImageDrawable(res);
                    break;
            }
        }else{
            Drawable res = holder.context.getResources().getDrawable(R.drawable.measuring_spoons);
            holder.quantityMeasureImg.setImageDrawable(res);
        }


    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }
}
