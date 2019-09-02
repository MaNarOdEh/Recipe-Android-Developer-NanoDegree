package com.example.recipe_fourth.ui.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.recipe_fourth.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class IngredientDetails extends AppCompatActivity {

    @BindView(R.id.video_layout) @Nullable() TextView appwidgetText;
    String content="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredient_details);
        ButterKnife.bind(this);
        ButterKnife.setDebug(true);
        Intent intent=getIntent();
        if(savedInstanceState!=null){
            content=savedInstanceState.getString("CONTENT");
        }
        if(intent!=null&&content.isEmpty()){
            content=intent.getStringExtra("CONTENT");
            appwidgetText.setText(intent.getStringExtra("CONTENT"));

        }else{
            startActivity(new Intent(IngredientDetails.this,MainActivity.class));
           // finish();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("CONTENT",content);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onRestoreInstanceState(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onRestoreInstanceState(savedInstanceState, persistentState);
    }
}
