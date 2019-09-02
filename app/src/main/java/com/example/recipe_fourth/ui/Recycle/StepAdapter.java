package com.example.recipe_fourth.ui.Recycle;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.recipe_fourth.Class.Step;
import com.example.recipe_fourth.R;
import com.example.recipe_fourth.ui.Activity.RecipeDetails;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class StepAdapter extends RecyclerView.Adapter<StepAdapter.ViewHolder> {
    List<Step> steps;
    private  OnStepListener onStepListener;

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.step_name_tv) @Nullable() TextView stepNameTv;
        @BindView(R.id.card_step) @Nullable() CardView cardStep;
        @BindView(R.id.circle_video)@Nullable() CircleImageView circleVideo;
        Context context ;
        private  OnStepListener onStepListener;


        public ViewHolder(View viewItems,OnStepListener onStepListener) {
            super(viewItems);
            ButterKnife.bind(this, viewItems);
            context = viewItems.getContext();
            this.onStepListener=onStepListener;
            cardStep.setOnClickListener(this);
            circleVideo.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onStepListener.onStepListener(getAdapterPosition());

        }
    }
    public interface OnStepListener{
        public void  onStepListener(int position);
    }
    public StepAdapter(){
        steps=new ArrayList<>();
    }
    public StepAdapter(List<Step>steps,OnStepListener onStepListener){
        if(steps!=null){
            this.steps=steps;
            this.onStepListener=onStepListener;
        }else{
            this.steps=new ArrayList<>();
        }
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View contactView= LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_step_view,parent,false);
        return new ViewHolder(contactView,onStepListener);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
         Step step=steps.get(position);
         holder.stepNameTv.setText(step.getShortDescription()!=null?step.getShortDescription():"");
        RecipeDetails.is_appear=true;

        //Set OnClickListener Must be in the activity or fragment not here!!

    }

    @Override
    public int getItemCount() {
        return steps.size();
    }



}
