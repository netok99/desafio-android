package com.picpay.desafio.android.contact

import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.testing.launchFragment
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import com.picpay.desafio.android.contact.di.contactModule
import com.picpay.desafio.android.contact.view.ContactFragment
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.startKoin
import org.koin.core.context.unloadKoinModules
import org.koin.core.module.Module
import org.koin.test.KoinTest


@RunWith(AndroidJUnit4ClassRunner::class)
class ContactFragmentTest : KoinTest {

    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private lateinit var localModule: Module

    @Before
    fun setup() {
        localModule = contactModule
        startKoin { modules(localModule) }
    }

    @Test
    fun shouldDisplayTitle() {
        launchFragment<ContactFragment>(bundleOf())
        val expectedTitle = context.getString(R.string.title)

        onView(withId(R.id.title)).check(matches(withText(expectedTitle)))
        onView(withText(expectedTitle)).check(matches(isDisplayed()))
    }

    @Test
    fun checkItemRecyclerVIew() {
        launchFragment<ContactFragment>(bundleOf())

        onView(withId(R.id.recycler_view))
            .check(matches(atPosition(0, withText("Sandrine Spinka"), R.id.name)))
    }

    @After
    fun afterAndroidTest() {
        unloadKoinModules(localModule)
    }

    private fun atPosition(
        position: Int,
        itemMatcher: Matcher<View?>,
        targetViewId: Int
    ): Matcher<View?> =
        object : BoundedMatcher<View?, RecyclerView>(RecyclerView::class.java) {
            override fun describeTo(description: Description) {
                description.appendText("has item at position $position: ")
                itemMatcher.describeTo(description)
            }

            override fun matchesSafely(view: RecyclerView): Boolean {
                val viewHolder = view.findViewHolderForAdapterPosition(position) ?: return false
                val targetView = viewHolder.itemView.findViewById<View>(targetViewId)
                return itemMatcher.matches(targetView)
            }
        }
}
