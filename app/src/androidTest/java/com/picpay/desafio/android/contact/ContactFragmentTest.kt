package com.picpay.desafio.android.contact

import androidx.core.os.bundleOf
import androidx.fragment.app.testing.launchFragment
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import com.picpay.desafio.android.contact.di.contactModule
import com.picpay.desafio.android.contact.view.ContactFragment
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

    @After
    fun afterAndroidTest() {
        unloadKoinModules(localModule)
    }
}
