package com.example.recipe_fourth;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.IdlingResource;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.rule.ActivityTestRule;

import com.example.recipe_fourth.Class.Ingredient;
import com.example.recipe_fourth.Class.Recipe;
import com.example.recipe_fourth.Class.Step;
import com.example.recipe_fourth.ui.Activity.MainActivity;
import com.example.recipe_fourth.ui.Activity.RecipeDetails;
import com.example.recipe_fourth.ui.ingrdient.IngredientFragment;
import com.example.recipe_fourth.ui.steps.StepsFragment;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static android.service.autofill.Validators.not;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.isClickable;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

public class RecipeDetailsTest {
    StepsFragment fragment;
    //
   /* @Rule
    public ActivityTestRule<RecipeDetails> mActivityRule =
            new ActivityTestRule<RecipeDetails>(RecipeDetails.class);*/

    // third parameter is set to false which means the activity is not started automatically
   @Rule
   public ActivityTestRule<RecipeDetails> mActivityRule =
            new ActivityTestRule<RecipeDetails>(RecipeDetails.class,true,false);


    private Activity launchedActivity;

    @Before
    public  void init(){
        Intent intent = new Intent();

        Step step=new Step();
        step.setDescription("Food!");
        step.setShortDescription("First Step is buying the ingredient :):)");
        step.setId(5);
        step.setThumbnailURL("");
        step.setVideoURL("");


        ArrayList <Step>steps=new ArrayList<>();
        steps.add(step);
        steps.add(step);
        steps.add(step);

        Ingredient ingredient=new Ingredient();
        ingredient.setMeasure("TSB");
        ingredient.setIngredient("Apple");
        ingredient.setQuantity(35.0);

        ArrayList<Ingredient>ingredients=new ArrayList<>();
        ingredients.add(ingredient);
        ingredients.add(ingredient);
        ingredients.add(ingredient);


        Recipe recipe=new Recipe();
        recipe.setId(0);
        recipe.setImage("");
        recipe.setName("Nutella");
        recipe.setServings(5);
        recipe.setIngredients(ingredients);
        recipe.setSteps(steps);
        intent.putExtra("Recipe_Details", recipe);

        launchedActivity=  mActivityRule.launchActivity(intent);
    }
    @Test
    public  void comeBack(){
        //check if btn widget is appear..
        onView(ViewMatchers.withId(R.id.btn_add_widget)).check(matches(isDisplayed()));

        //check if btn is clickable
        onView(ViewMatchers.withId(R.id.btn_add_widget)).check(matches(isClickable()));

        //check if Step Fragments Appear Correctly
        onView(ViewMatchers.withId(R.id.step_rvv)).check(matches((isDisplayed())));

        //check if Ingredient Fragments   Appear Correctly
        onView(ViewMatchers.withId(R.id.ingredient_rv)).check(matches(isDisplayed()));

    }
    @Before
    public void loadStepFragments(){
    }

    @Test
    public void checkTextDisplayedInDynamicallyCreatedFragment() throws InterruptedException {

      while(!RecipeDetails.is_appear){}//just to make sure that data is appear here!!
        onView(allOf(isDisplayed(), withId(R.id.step_rvv))).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

         //check if the video layout fragment shown after clicked on Step Card!!
        onView(ViewMatchers.withId(R.id.video_layout)).check(matches(isDisplayed()));

        //check if the next button  moves to the next step..
        ViewInteraction appCompatImageView = onView(
                allOf(withId(R.id.move_next_img), withContentDescription("Add to the home Screen"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        4),
                                2)));
        appCompatImageView.perform(scrollTo(), click());

        //prev button moves to the prev step..
        ViewInteraction appCompatImageView2 = onView(
                allOf(withId(R.id.move_prev_img), withContentDescription("Add to the home Screen"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        4),
                                0)));
        appCompatImageView2.perform(scrollTo(), click());



        //Back and finished..
        pressBack();

    }
    public static <T> Matcher<T> result(final Matcher<T> matcher, final int i) {
        return new BaseMatcher<T>() {
            private int resultIndex = -1;
            @Override
            public boolean matches(final Object item) {
                if (matcher.matches(item)) {
                    resultIndex++;
                    return resultIndex == i;
                }
                return false;
            }

            @Override
            public void describeTo(final Description description) {
            }
        };
    }
    public static Matcher<View> withIndex(final Matcher<View> matcher, final int index) {
        return new TypeSafeMatcher<View>() {
            int currentIndex = 0;

            @Override
            public void describeTo(Description description) {
                description.appendText("with index: ");
                description.appendValue(index);
                matcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                return matcher.matches(view) && currentIndex++ == index;
            }
        };
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
