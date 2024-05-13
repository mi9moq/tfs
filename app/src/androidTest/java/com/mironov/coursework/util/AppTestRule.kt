package com.mironov.coursework.util

import android.app.Activity
import android.content.Intent
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.github.tomakehurst.wiremock.junit.WireMockRule
import org.junit.rules.RuleChain
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

class AppTestRule<T : Activity>(
    startActivityIntent: Intent,
) : TestRule {

    val wiremockRule = WireMockRule()
    val activityScenarioRule = ActivityScenarioRule<T>(startActivityIntent)

    override fun apply(base: Statement?, description: Description?): Statement {
        return RuleChain.emptyRuleChain()
            .around(wiremockRule)
            .around(activityScenarioRule)
            .apply(base, description)
    }
}