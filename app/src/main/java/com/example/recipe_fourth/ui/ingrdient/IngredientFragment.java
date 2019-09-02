package com.example.recipe_fourth.ui.ingrdient;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipe_fourth.Class.Ingredient;
import com.example.recipe_fourth.R;
import com.example.recipe_fourth.ui.Activity.RecipeDetails;
import com.example.recipe_fourth.ui.Recycle.IngrdientAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class IngredientFragment extends Fragment {

    @BindView(R.id.ingredient_rv) @Nullable RecyclerView ingredientRv ;
    IngrdientAdapter ingrdientAdapter;
    List<Ingredient> ingredient;
    public IngredientFragment(){

    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.ingredient_home, container, false);
        ButterKnife.bind(this,root);
        ButterKnife.setDebug(true);
        ingredientRv.setLayoutManager(new LinearLayoutManager(getActivity()));

        Bundle bundle = getArguments();
        if(bundle!=null) {
            ingredient = bundle.getParcelableArrayList(((RecipeDetails)getActivity()).LIST_INGREDIENTS);
        }else{
            ingredient=new ArrayList<>();
        }
        ingrdientAdapter=new IngrdientAdapter(ingredient);
        ingredientRv.setAdapter(ingrdientAdapter);
        ingrdientAdapter.notifyDataSetChanged();
        return root;
    }

}