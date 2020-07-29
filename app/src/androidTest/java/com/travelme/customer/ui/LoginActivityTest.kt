package com.travelme.customer.ui

import android.view.View
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.rule.ActivityTestRule
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.travelme.customer.R
import com.travelme.customer.activities.login.LoginActivity
import com.travelme.customer.activities.login.LoginViewModel
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.test.KoinTest
import org.koin.test.inject

@RunWith(androidx.test.ext.junit.runners.AndroidJUnit4::class)
class LoginActivityTest : KoinTest {

    val loginViewModel: LoginViewModel by inject()

    @JvmField
    @Rule
    var mActivityRule: ActivityTestRule<LoginActivity> = ActivityTestRule(LoginActivity::class.java)

    val activityScenario = ActivityScenario.launch(LoginActivity::class.java)

    private fun setTextToView(text: String, textInputEditText: TextInputEditText) {
        mActivityRule.runOnUiThread {
            textInputEditText.setText(text)
        }
    }

    @Test
    fun testWithEmptyEmailAndEmptyPassword() {
        val wrongEmail = ""
        val wrongPassword = ""
        val etEmail = mActivityRule.activity?.findViewById<TextInputEditText>(R.id.et_email)!!
        val etPassword = mActivityRule.activity.findViewById<TextInputEditText>(R.id.et_password)!!
        setTextToView(wrongEmail, etEmail)
        setTextToView(wrongPassword, etPassword)
        onView(withId(R.id.btn_login)).perform(ViewActions.click())
        onView(withId(R.id.til_email)).check(
            matches(
                hasTextInputLayoutErrorText("email tidak boleh kosong")
            )
        )
        onView(withId(R.id.til_password)).check(
            matches(
                hasTextInputLayoutErrorText("password tidak boleh kosong")
            )
        )
        Thread.sleep(3000)
    }

    @Test
    fun testWithCorrectEmailAndEmptyPassword() {
        val wrongEmail = "helfanza@gmail.com"
        val wrongPassword = ""
        val etEmail = mActivityRule.activity?.findViewById<TextInputEditText>(R.id.et_email)!!
        val etPassword = mActivityRule.activity.findViewById<TextInputEditText>(R.id.et_password)!!
        setTextToView(wrongEmail, etEmail)
        setTextToView(wrongPassword, etPassword)
        onView(withId(R.id.btn_login)).perform(ViewActions.click())
        onView(withId(R.id.til_email)).check(
            matches(
                hasNoErrorText()
            )
        )
        onView(withId(R.id.til_password)).check(
            matches(
                hasTextInputLayoutErrorText("password tidak boleh kosong")
            )
        )
        Thread.sleep(3000)
    }

    @Test
    fun testWithIsInValidEmailAndEmptyPassword() {
        val wrongEmail = "helfanza"
        val wrongPassword = ""
        val etEmail = mActivityRule.activity?.findViewById<TextInputEditText>(R.id.et_email)!!
        val etPassword = mActivityRule.activity.findViewById<TextInputEditText>(R.id.et_password)!!
        setTextToView(wrongEmail, etEmail)
        setTextToView(wrongPassword, etPassword)
        onView(withId(R.id.btn_login)).perform(ViewActions.click())
        onView(withId(R.id.til_email)).check(
            matches(
                hasTextInputLayoutErrorText("email tidak valid")
            )
        )
        onView(withId(R.id.til_password)).check(
            matches(
                hasNoErrorText()
            )
        )
        Thread.sleep(3000)
    }

    private fun hasNoErrorText(): Matcher<View?>? {
        return object : BoundedMatcher<View?, TextInputLayout>(TextInputLayout::class.java) {
            override fun describeTo(description: Description) {}
            override fun matchesSafely(view: TextInputLayout): Boolean {
                return view.error == null
            }
        }
    }

    private fun hasTextInputLayoutErrorText(expectedErrorText: String): Matcher<View?>? {
        return object : TypeSafeMatcher<View?>() {
            override fun matchesSafely(item: View?): Boolean {
                if (item !is TextInputLayout) {
                    return false
                }
                val error = item.error ?: return false
                val hint = error.toString()
                return expectedErrorText == hint
            }

            override fun describeTo(description: Description?) {}
        }
    }
}