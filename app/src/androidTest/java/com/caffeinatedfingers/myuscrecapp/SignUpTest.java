package com.caffeinatedfingers.myuscrecapp;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.pressImeActionButton;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.ViewInteraction;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class SignUpTest {

    @Rule
    public ActivityTestRule<WelcomePage> mActivityTestRule = new ActivityTestRule<>(WelcomePage.class);

    @Test
    public void signUpTest() throws InterruptedException {
        ViewInteraction materialButton = onView(
                allOf(withId(R.id.btn_sign_up), withText("Sign up"),
                        childAtPosition(
                                allOf(withId(R.id.container),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                0),
                        isDisplayed()));
        materialButton.perform(click());
        Thread.sleep(3000);
        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.fullName),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                2),
                        isDisplayed()));
        appCompatEditText.perform(replaceText("Tommy Trojan"), closeSoftKeyboard());
        Thread.sleep(3000);
        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.fullName), withText("Tommy Trojan"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                2),
                        isDisplayed()));
        appCompatEditText2.perform(pressImeActionButton());
        Thread.sleep(3000);
        ViewInteraction appCompatEditText3 = onView(
                allOf(withId(R.id.user_ID),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                3),
                        isDisplayed()));
        appCompatEditText3.perform(replaceText("3710164646"), closeSoftKeyboard());
        Thread.sleep(3000);
        ViewInteraction appCompatEditText4 = onView(
                allOf(withId(R.id.user_ID), withText("3710164646"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                3),
                        isDisplayed()));
        appCompatEditText4.perform(pressImeActionButton());
        Thread.sleep(3000);
        ViewInteraction appCompatEditText5 = onView(
                allOf(withId(R.id.email),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                4),
                        isDisplayed()));
        appCompatEditText5.perform(replaceText("ttrojan@usc.edu"), closeSoftKeyboard());
        Thread.sleep(3000);
        ViewInteraction appCompatEditText6 = onView(
                allOf(withId(R.id.email), withText("ttrojan@usc.edu"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                4),
                        isDisplayed()));
        appCompatEditText6.perform(pressImeActionButton());
        Thread.sleep(3000);
        ViewInteraction appCompatEditText7 = onView(
                allOf(withId(R.id.password),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                5),
                        isDisplayed()));
        appCompatEditText7.perform(replaceText("fighton!"), closeSoftKeyboard());
        Thread.sleep(3000);
        ViewInteraction appCompatEditText8 = onView(
                allOf(withId(R.id.password), withText("fighton!"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                5),
                        isDisplayed()));
        appCompatEditText8.perform(pressImeActionButton());
        Thread.sleep(3000);
        ViewInteraction materialButton2 = onView(
                allOf(withId(R.id.btn_sign_up), withText("Sign up"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                1),
                        isDisplayed()));
        materialButton2.perform(click());
        Thread.sleep(3000);
//        ViewInteraction frameLayout = onView(
//                allOf(withId(android.R.id.content),
//                        withParent(allOf(withId(R.id.decor_content_parent),
//                                withParent(IsInstanceOf.<View>instanceOf(android.widget.FrameLayout.class)))),
//                        isDisplayed()));
//        frameLayout.check(matches(isDisplayed()));
//        Thread.sleep(3000);
//        ViewInteraction materialButton3 = onView(
//                allOf(withId(R.id.btn_my_profile), withText("Profile"),
//                        childAtPosition(
//                                childAtPosition(
//                                        withId(android.R.id.content),
//                                        0),
//                                2),
//                        isDisplayed()));
//        materialButton3.perform(click());
//        Thread.sleep(3000);
//        ViewInteraction materialButton4 = onView(
//                allOf(withId(R.id.btn_upload_photo), withText("Upload Photo"),
//                        childAtPosition(
//                                childAtPosition(
//                                        withClassName(is("android.widget.LinearLayout")),
//                                        4),
//                                1),
//                        isDisplayed()));
//        materialButton4.perform(click());
//        Thread.sleep(3000);
//        ViewInteraction imageView = onView(
//                allOf(withId(R.id.profile_image), withContentDescription("Profile image"),
//                        withParent(withParent(IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class))),
//                        isDisplayed()));
//        imageView.check(matches(isDisplayed()));
//        Thread.sleep(3000);
//        ViewInteraction textView = onView(
//                allOf(withId(R.id.fullName_profile), withText("Tommy Trojan"),
//                        withParent(withParent(withId(android.R.id.content))),
//                        isDisplayed()));
//        textView.check(matches(withText("Tommy Trojan")));
//        Thread.sleep(3000);
//        ViewInteraction textView2 = onView(
//                allOf(withId(R.id.uscID_profile), withText("3710164646"),
//                        withParent(withParent(withId(android.R.id.content))),
//                        isDisplayed()));
//        textView2.check(matches(withText("3710164646")));
//        Thread.sleep(3000);
//        ViewInteraction button = onView(
//                allOf(withId(R.id.btn_my_reservations), withText("MY RESERVATIONS"),
//                        withParent(withParent(withId(android.R.id.content))),
//                        isDisplayed()));
//        button.check(matches(isDisplayed()));
//        Thread.sleep(3000);
//        ViewInteraction button2 = onView(
//                allOf(withId(R.id.btn_logout), withText("LOGOUT"),
//                        withParent(withParent(IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class))),
//                        isDisplayed()));
//        button2.check(matches(isDisplayed()));
//        Thread.sleep(3000);
//        ViewInteraction button3 = onView(
//                allOf(withId(R.id.btn_upload_photo), withText("UPLOAD PHOTO"),
//                        withParent(withParent(IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class))),
//                        isDisplayed()));
//        button3.check(matches(isDisplayed()));
//        Thread.sleep(3000);
//        ViewInteraction materialButton5 = onView(
//                allOf(withId(R.id.btn_logout), withText("Logout"),
//                        childAtPosition(
//                                childAtPosition(
//                                        withClassName(is("android.widget.LinearLayout")),
//                                        4),
//                                0),
//                        isDisplayed()));
//        materialButton5.perform(click());
//        Thread.sleep(3000);
//        ViewInteraction button4 = onView(
//                allOf(withId(R.id.btn_sign_up), withText("SIGN UP"),
//                        withParent(allOf(withId(R.id.container),
//                                withParent(withId(android.R.id.content)))),
//                        isDisplayed()));
//        button4.check(matches(isDisplayed()));
//        Thread.sleep(3000);
//        ViewInteraction button5 = onView(
//                allOf(withId(R.id.btn_login), withText("LOG IN"),
//                        withParent(allOf(withId(R.id.container),
//                                withParent(withId(android.R.id.content)))),
//                        isDisplayed()));
//        button5.check(matches(isDisplayed()));
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
