package com.example.recipe_fourth.Class;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Ingredient implements Parcelable {

    @SerializedName("quantity")
    @Expose
    private Double  quantity;
    @SerializedName("measure")
    @Expose
    private String measure;
    @SerializedName("ingredient")
    @Expose
    private String ingredient;
    public final static Creator<Ingredient> CREATOR = new Creator<Ingredient>() {

        public Ingredient createFromParcel(Parcel in) {
            return new Ingredient(in);
        }

        public Ingredient[] newArray(int size) {
            return (new Ingredient[size]);
        }

    };

    protected Ingredient(Parcel in) {
        this.quantity = in.readDouble();
        this.measure = in.readString();
        this.ingredient = in.readString();
    }

    public Ingredient() {
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    public String getIngredient() {
        return ingredient;
    }

    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }

    public void writeToParcel(Parcel dest, int flags) {
        if(quantity==null){
            quantity=0.0;
        }
        if(measure==null){
            measure="";
        }if(ingredient==null){
            ingredient="";
        }
        dest.writeDouble(quantity);
        dest.writeString(measure);
        dest.writeString(ingredient);
    }

    public int describeContents() {
        return 0;
    }

}
