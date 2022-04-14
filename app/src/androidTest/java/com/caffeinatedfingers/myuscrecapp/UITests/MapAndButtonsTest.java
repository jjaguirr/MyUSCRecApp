package com.caffeinatedfingers.myuscrecapp.UITests;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.pressImeActionButton;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import com.caffeinatedfingers.myuscrecapp.R;
import com.caffeinatedfingers.myuscrecapp.WelcomePage;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class MapAndButtonsTest {

    @Rule
    public ActivityTestRule<WelcomePage> mActivityTestRule = new ActivityTestRule<>(WelcomePage.class);

    @Test
    public void mapAndButtonsTest() throws InterruptedException {
        ViewInteraction materialButton = onView(
                Matchers.allOf(ViewMatchers.withId(R.id.btn_login), withText("Log in"),
                        childAtPosition(
                                allOf(withId(R.id.container),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                1),
                        isDisplayed()));
        materialButton.perform(click());
        Thread.sleep(3000);

        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.email),
                        childAtPosition(
                                allOf(withId(R.id.container),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                0),
                        isDisplayed()));
        appCompatEditText.perform(replaceText("ttrojan@usc.edu"), closeSoftKeyboard());
        Thread.sleep(3000);
        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.password),
                        childAtPosition(
                                allOf(withId(R.id.container),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                1),
                        isDisplayed()));
        appCompatEditText2.perform(replaceText("fighton!"), closeSoftKeyboard());
        Thread.sleep(3000);
        ViewInteraction appCompatEditText3 = onView(
                allOf(withId(R.id.password), withText("fighton!"),
                        childAtPosition(
                                allOf(withId(R.id.container),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                1),
                        isDisplayed()));
        appCompatEditText3.perform(pressImeActionButton());
        Thread.sleep(3000);
        ViewInteraction materialButton2 = onView(
                allOf(withId(R.id.btn_login), withText("Log in"),
                        childAtPosition(
                                allOf(withId(R.id.container),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                2),
                        isDisplayed()));
        materialButton2.perform(click());
        Thread.sleep(3000);
        ViewInteraction button = onView(
                allOf(withId(R.id.btn_my_profile), withText("PROFILE"),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        button.check(matches(isDisplayed()));
        Thread.sleep(3000);
        ViewInteraction button2 = onView(
                allOf(withId(R.id.btn_my_reservations), withText("RESERVATIONS"),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        button2.check(matches(isDisplayed()));
        Thread.sleep(3000);
        ViewInteraction textView = onView(
                allOf(withId(R.id.rec_center_header), withText("Select a Rec Center"),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        textView.check(matches(withText("Select a Rec Center")));
        Thread.sleep(3000);
        ViewInteraction imageView = onView(
                allOf(withId(R.id.imageView3), withContentDescription("map"),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        imageView.check(matches(isDisplayed()));
        Thread.sleep(3000);
        ViewInteraction button3 = onView(
                allOf(withId(R.id.btn_lyon_center), withText("LYON CENTER"),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        button3.check(matches(isDisplayed()));
        Thread.sleep(3000);
        ViewInteraction button4 = onView(
                allOf(withId(R.id.btn_village_center), withText("USC VILLAGE REC CENTER"),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        button4.check(matches(isDisplayed()));
        Thread.sleep(3000);
        ViewInteraction button5 = onView(
                allOf(withId(R.id.btn_aquatics), withText("UYTENGSU AQUATICS CENTER"),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        button5.check(matches(isDisplayed()));
        Thread.sleep(3000);
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
