package com.example.recipe_fourth.ui.steps;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipe_fourth.Class.Step;
import com.example.recipe_fourth.R;
import com.example.recipe_fourth.ui.Activity.RecipeDetails;
import com.example.recipe_fourth.ui.Recycle.StepAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepsFragment extends Fragment implements StepAdapter.OnStepListener {

    @BindView(R.id.step_rvv)@Nullable() RecyclerView stepRv;
    StepAdapter stepAdapter;
    List<Step> steps;
    OnCardViewClickListener cardViewClickLisener;
    @Nullable


    @Override
    public void onStepListener(int position) {
        cardViewClickLisener.setOnClickListener(position);

    }

    public interface OnCardViewClickListener{
        void setOnClickListener(int position);
    }
    public  StepsFragment(){

    }
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_step, container, false);
        ButterKnife.bind(this,root);
        ButterKnife.setDebug(true);
        Bundle bundle = getArguments();
        if(bundle!=null) {
            steps = bundle.getParcelableArrayList(((RecipeDetails)getActivity()).LIST_STEPS);

        }else{
            steps=new ArrayList<>();
        }

        stepAdapter=new StepAdapter(steps,this);
        stepRv.setLayoutManager(new LinearLayoutManager(getContext()));
        stepRv.setAdapter(stepAdapter);
        stepAdapter.notifyDataSetChanged();
        return root;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof OnCardViewClickListener){
            cardViewClickLisener=(OnCardViewClickListener)context;
        }else{

        }
    }
}