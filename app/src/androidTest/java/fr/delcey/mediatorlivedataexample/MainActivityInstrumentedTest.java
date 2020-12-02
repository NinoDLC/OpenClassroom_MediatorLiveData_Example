package fr.delcey.mediatorlivedataexample;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class MainActivityInstrumentedTest {

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule = new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void shouldNotDisplayInformationAtStartup() {
        onView(withId(R.id.number_textview))
            .check(matches(withText("")));
        onView(withId(R.id.sentence_textview))
            .check(matches(withText("")));
    }

    @Test
    public void addButtonShouldAddOneToNumber() {
        // Click on "+1"
        onView(withId(R.id.button_add)).perform(click());

        // Check that the displayed texts are correct
        onView(withId(R.id.number_textview))
            .check(matches(withText("1")));
        onView(withId(R.id.sentence_textview))
            .check(matches(withText("Le nombre 1 est impair, le nombre aléatoire est 1")));
    }

    @Test
    public void multiplyButtonShouldDisplay0() {
        // Click on "x2"
        onView(withId(R.id.button_multiply)).perform(click());

        // Check that the displayed texts are correct
        onView(withId(R.id.number_textview))
            .check(matches(withText("0")));
        onView(withId(R.id.sentence_textview))
            .check(matches(withText("Le nombre 0 est pair, le nombre aléatoire est 1")));
    }

    @Test
    public void addAnMultiplyButtonShouldDisplay2() {
        // Click on "+1"
        onView(withId(R.id.button_add)).perform(click());
        // Click on "x2"
        onView(withId(R.id.button_multiply)).perform(click());

        // Check that the displayed texts are correct
        onView(withId(R.id.number_textview))
            .check(matches(withText("2")));
        onView(withId(R.id.sentence_textview))
            .check(matches(withText("Le nombre 2 est pair, le nombre aléatoire est 1")));
    }
}