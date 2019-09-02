package com.example.recipe_fourth.Class;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;


public class Recipe implements  Parcelable {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("ingredients")
    @Expose
    private List<Ingredient> ingredients = new ArrayList<>();
    @SerializedName("steps")
    @Expose
    private List<Step> steps = new ArrayList<>();
    @SerializedName("servings")
    @Expose
    private Integer servings;
    @SerializedName("image")
    @Expose
    private String image;
    public final static Creator<Recipe> CREATOR = new Creator<Recipe>() {


        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        public Recipe[] newArray(int size) {
            return (new Recipe[size]);
        }

    };

    protected Recipe(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        in.readTypedList(this.ingredients, (Ingredient.CREATOR));
        in.readTypedList(this.steps, (Step.CREATOR));
        this.servings = in.readInt();
        this.image = in.readString();
    }

    public Recipe() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public List<Step> getSteps() {
        return steps;
    }

    public void setSteps(List<Step> steps) {
        this.steps = steps;
    }

    public Integer getServings() {
        return servings;
    }

    public void setServings(Integer servings) {
        this.servings = servings;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void writeToParcel(Parcel dest, int flags) {
        if(id==null){
            id=0;
        }if(name==null){
            name="";
        }
        if(ingredients==null){
            ingredients=new ArrayList<>();
        }if(steps==null){
            steps=new ArrayList<>();
        }
        if(servings==null){
            servings=0;
        }
        if(image==null){
            image="";
        }
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeTypedList(ingredients);
        dest.writeTypedList(steps);
        dest.writeInt(servings);
        dest.writeString(image);
    }

    public int describeContents() {
        return 0;
    }

}
