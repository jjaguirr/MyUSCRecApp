package com.caffeinatedfingers.myuscrecapp.UITests;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.pressImeActionButton;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

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
import org.hamcrest.core.IsInstanceOf;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class VillageBookingTest {

    @Rule
    public ActivityTestRule<WelcomePage> mActivityTestRule = new ActivityTestRule<>(WelcomePage.class);

    @Test
    public void villageBookingTest() throws InterruptedException {
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
        ViewInteraction materialButton3 = onView(
                allOf(withId(R.id.btn_village_center), withText("USC Village Rec Center"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                4),
                        isDisplayed()));
        materialButton3.perform(click());
        Thread.sleep(3000);
        ViewInteraction textView = onView(
                allOf(withId(R.id.gym_name), withText("USC Village Center"),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        textView.check(matches(withText("USC Village Center")));
        Thread.sleep(3000);
        ViewInteraction button = onView(
                allOf(withId(R.id.btn_today), withText("TODAY"),
                        withParent(withParent(IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class))),
                        isDisplayed()));
        button.check(matches(isDisplayed()));
        Thread.sleep(3000);
        ViewInteraction button2 = onView(
                allOf(withId(R.id.btn_tomorrow), withText("TOMORROW"),
                        withParent(withParent(IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class))),
                        isDisplayed()));
        button2.check(matches(isDisplayed()));
        Thread.sleep(3000);
//        ViewInteraction viewGroup = onView(
//                allOf(withParent(withParent(withId(R.id.rv))),
//                        isDisplayed()));
//        viewGroup.check(matches(isDisplayed()));
//        Thread.sleep(3000);
//        ViewInteraction materialButton4 = onView(
//                allOf(withId(R.id.btn_book), withText("BOOK"),
//                        childAtPosition(
//                                childAtPosition(
//                                        withClassName(is("androidx.cardview.widget.CardView")),
//                                        0),
//                                0),
//                        isDisplayed()));
//        materialButton4.perform(click());
//        Thread.sleep(3000);
//        ViewInteraction materialButton5 = onView(
//                allOf(withId(R.id.btn_tomorrow), withText("tomorrow"),
//                        childAtPosition(
//                                childAtPosition(
//                                        withClassName(is("android.widget.LinearLayout")),
//                                        2),
//                                1),
//                        isDisplayed()));
//        materialButton5.perform(click());
//        Thread.sleep(3000);
//        ViewInteraction materialButton6 = onView(
//                allOf(withId(R.id.btn_book), withText("BOOK"),
//                        childAtPosition(
//                                childAtPosition(
//                                        withClassName(is("androidx.cardview.widget.CardView")),
//                                        0),
//                                0),
//                        isDisplayed()));
//        materialButton6.perform(click());
//        Thread.sleep(3000);
//        ViewInteraction viewGroup2 = onView(
//                allOf(withParent(withParent(withId(R.id.rv))),
//                        isDisplayed()));
//        viewGroup2.check(matches(isDisplayed()));
//        Thread.sleep(3000);
//        pressBack();
//
//        ViewInteraction materialButton7 = onView(
//                allOf(withId(R.id.btn_my_reservations), withText("reservations"),
//                        childAtPosition(
//                                childAtPosition(
//                                        withId(android.R.id.content),
//                                        0),
//                                1),
//                        isDisplayed()));
//        materialButton7.perform(click());
//        Thread.sleep(3000);
//        ViewInteraction materialButton8 = onView(
//                allOf(withId(R.id.btn_tomorrow), withText("tomorrow"),
//                        childAtPosition(
//                                childAtPosition(
//                                        withClassName(is("android.widget.LinearLayout")),
//                                        2),
//                                1)));
//        materialButton8.perform(scrollTo(), click());
//        Thread.sleep(3000);
//        ViewInteraction viewGroup3 = onView(
//                allOf(withParent(withParent(withId(R.id.rv))),
//                        isDisplayed()));
//        viewGroup3.check(matches(isDisplayed()));
//        Thread.sleep(3000);
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
