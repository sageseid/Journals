package com.noel201296gmail.journals;


import android.support.test.runner.AndroidJUnit4;
import android.support.test.rule.ActivityTestRule;

import com.noel201296gmail.journals.activities.SignupActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isClickable;
import static android.support.test.espresso.matcher.ViewMatchers.withId;


@RunWith(AndroidJUnit4.class)
public class SignUpActivityTest {

    @Rule
    public ActivityTestRule<SignupActivity> mActivityRule =
            new ActivityTestRule<>(SignupActivity.class);


    @Test
    public void buttonShouldRegisterUser(){
        String emailAddress = "firstname.lastname@g.com";
        String password = "password123456";

        onView(withId(R.id.email)).perform(typeText(emailAddress), closeSoftKeyboard());
        onView(withId(R.id.password)).perform(typeText(emailAddress), closeSoftKeyboard());

        onView(withId(R.id.sign_up_button)).perform(click());

        //onView(withId(getResourceId("fab_add_new_button"))).check(matches(withText("Done")));
        onView(withId(R.id.fab_add_new_button)).check(matches(isClickable()));

    }
}
