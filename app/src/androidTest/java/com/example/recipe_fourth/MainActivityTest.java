package com.example.recipe_fourth;

import android.content.Intent;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;


import static androidx.test.espresso.Espresso.onView;


import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import static androidx.test.espresso.action.ViewActions.click;

import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasExtra;



import com.example.recipe_fourth.ui.Activity.MainActivity;
import com.example.recipe_fourth.ui.Activity.RecipeDetails;


import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class);
    @Before
    public void setUp() throws Exception{
        Intents.init();
    }

    @Test
    public void onRecycleViewItemClicked() {
        //loop until the Recycle View is Ready and finished methods FetchData on MainActivity
     while(!mActivityRule.getActivity().IdlingResource) {}

      //if there is an internet connection and the data returns correctly..
     if(!mActivityRule.getActivity().no_check) {
         //clicked on the first items on my Recipe RecycleView...

         onView(ViewMatchers.withId(R.id.recipe_card_rv))
                 .perform(RecyclerViewActions.actionOnItemAtPosition(0,
                         click()));
         //Move to the Next Activity Recipe Activity
         mActivityRule.launchActivity(new Intent());
         //make sure that the intent goes to RecipeDetails Activity
         intended(hasComponent(RecipeDetails.class.getName()));

         //check if the intent have an extra data when moving to details Activity..
         hasExtra("Recipe_Details", "MY EXTRA VALUE");
     }
    }
    @After
    public void tearDown() throws Exception{
        Intents.release();
    }



    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {
        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
