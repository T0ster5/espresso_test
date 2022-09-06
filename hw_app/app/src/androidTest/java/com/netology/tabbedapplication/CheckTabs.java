package com.netology.tabbedapplication;


import androidx.test.espresso.ViewInteraction;
import androidx.test.filters.LargeTest;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.*;
import static androidx.test.espresso.assertion.ViewAssertions.*;
import static androidx.test.espresso.matcher.ViewMatchers.*;


import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class CheckTabs {

    @Rule
    public ActivityScenarioRule<MainActivity> mActivityScenarioRule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void checkTabsFirst() {
        onView(
                allOf(
                        withContentDescription(R.string.tab_text_1),
                        withParent(withParent(withId(R.id.tabs))),
                        isDisplayed())).check(matches(isSelected()));

        ViewInteraction textViewFirstTab = onView(
                allOf(
                        withParent(
                                allOf(withContentDescription(R.string.tab_text_1),
                                withParent(IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class)))),
                        isDisplayed()));
        textViewFirstTab.check(matches(withText("TAB 1")));


        ViewInteraction textViewFirstPage = onView(
                allOf(withId(R.id.section_label),
                        withParent(allOf(withId(R.id.constraintLayout),
                                withParent(withId(R.id.view_pager)))),
                        isDisplayed()));
        textViewFirstPage.check(matches(withText("Page: 1")));

        onView(
                allOf(
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.tabs),
                                        0),
                                1),
                        isDisplayed())).perform(click());

        ViewInteraction textViewSecTabs = onView(
                allOf(withParent(allOf(withContentDescription(R.string.tab_text_2),
                                withParent(IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class)))),
                        isDisplayed()));
        textViewSecTabs.check(matches(withText("TAB 2")));

        ViewInteraction textSecPage = onView(
                allOf(withId(R.id.section_label),
                        withParent(allOf(withId(R.id.constraintLayout),
                                withParent(withId(R.id.view_pager)))),
                        isDisplayed()));
        textSecPage.check(matches(withText("Page: 2")));
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
