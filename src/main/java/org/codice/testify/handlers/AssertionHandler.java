/*
 * Copyright 2015 Codice Foundation
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package org.codice.testify.handlers;

import org.codice.testify.assertions.Assertion;
import org.codice.testify.objects.AssertionStatus;
import org.codice.testify.objects.TestifyLogger;
import org.codice.testify.objects.Response;
import org.codice.testify.objects.Result;
import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;
import java.util.LinkedHashMap;

/**
 * The AssertionHandler class takes in the assertion block, collects assertion services, and runs the matching assertions
 **/
public class AssertionHandler {

    /**
     * The handleAssertions method is called by the Test Engine. It takes in an assertion block and response, splits the assertion block into assertions, and runs any matching services.
     * @param assertionBlock the block of assertions in the test file
     * @param response the response from the test processor
     * @param bundleContext the Felix bundle context
     * @return a Result object
     */
    public Result handleAssertions(String assertionBlock, Response response, BundleContext bundleContext) {

        //Set objects to capture test results
        boolean testSuccess = true;
        LinkedHashMap<String,String> assertionResults = new LinkedHashMap<>();
        AssertionStatus assertionStatus;

        //Split assertion block into individual assertions
        String[] assertions = assertionBlock.split( System.lineSeparator() );

        //Start service tracker and collect all Assertion services
        TestifyLogger.debug("Looking for assertion services", this.getClass().getSimpleName());
        ServiceTracker serviceTracker = new ServiceTracker(bundleContext, Assertion.class.getName(), null);
        serviceTracker.open();
        Object[] assertionServices = serviceTracker.getServices();

        //Loop through each assertion
        TestifyLogger.debug("Starting loop through test assertions", this.getClass().getSimpleName());
        for (String assertion : assertions) {

            //Evaluate the assertion
            assertionStatus = evaluateAssertion(assertion.trim(), response, assertionServices);

            //Add assertion results into assertionResults map and change overall test result to false if assertion is failure
            assertionResults.put(assertion.trim(),assertionStatus.getFailureDetails());
            if (!assertionStatus.isSuccess()) {
                testSuccess = false;
            }
        }
        return new Result(testSuccess, assertionResults);
    }

    /**
     * The evaluateAssertion method is called by the handleAssertions method. It takes in a specific assertion line, splits the line into components, checks for matching services, and runs the assertion service.
     * @param assertionLine a specific assertion line from the assertion block
     * @param response the response from the test processor
     * @param assertionServices an array of all assertion services gathered by the service tracker
     * @return an assertionStatus object
     */
    private AssertionStatus evaluateAssertion(String assertionLine, Response response, Object[] assertionServices) {

        //Split assertion into its components
        String assertionInfo = null;
        String[] assertionComponents = assertionLine.split("::");
        if (assertionComponents.length > 1) {
            assertionInfo = assertionComponents[1];
        }
        TestifyLogger.debug("Looking for assertion: " + assertionComponents[0] + " and passing assertion info: " + assertionInfo, this.getClass().getSimpleName());

        //Loop through collected Assertion services
        for (Object assertionService : assertionServices) {

            TestifyLogger.debug("Comparing Assertion service: " + ((Assertion) assertionService).getClass().getSimpleName(), this.getClass().getSimpleName());

            //If the assertion name matches any of the assertion services, then run the assertion
            if (((Assertion)assertionService).getClass().getSimpleName().equalsIgnoreCase(assertionComponents[0])) {
                return ((Assertion)assertionService).evaluateAssertion(assertionInfo, response);
            }
        }

        //If no assertions match, return a failed assertionStatus
        return new AssertionStatus("Could not find Assertion service " + assertionComponents[0]);
    }
}