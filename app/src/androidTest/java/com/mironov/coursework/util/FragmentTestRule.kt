package com.mironov.coursework.util

import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.testing.FragmentScenario
import com.github.tomakehurst.wiremock.junit.WireMockRule
import com.mironov.coursework.R
import org.junit.rules.RuleChain
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

class FragmentTestRule(
    private val fragment: Fragment,
    private val fragmentArguments: Bundle = bundleOf()
): TestRule {

    val wiremockRule = WireMockRule()

    override fun apply(base: Statement?, description: Description?): Statement {
        FragmentScenario.launchInContainer(fragment::class.java, fragmentArguments, R.style.Base_Theme_Hw1)

        return RuleChain.emptyRuleChain()
            .around(wiremockRule)
            .apply(base, description)
    }
}