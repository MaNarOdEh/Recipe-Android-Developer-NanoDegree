package com.example.recipe_fourth.API_Service;

import com.example.recipe_fourth.Class.Recipe;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ServerConcect {
    @GET("topher/2017/May/59121517_baking/baking.json")
    Call <List<Recipe>> recipeServer();
}

