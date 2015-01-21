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

package org.codice.testify.objects;

import java.util.LinkedHashMap;

/**
 * The Result class stores the assertionResults map and overall test results for the test file
 */
public class Result {

    private final boolean testSuccess;
    private final LinkedHashMap<String,String> assertionResults;

    /**
     * The constructor for the Result class sets the assertionResults map and overall test results
     * @param testSuccess the result of the specific test which will be true if successful or false if any assertions fail.
     * @param assertionResults the results from all the assertions
     */
    public Result(boolean testSuccess, LinkedHashMap<String,String> assertionResults) {
        this.testSuccess = testSuccess;
        this.assertionResults = assertionResults;
    }

    /**
     * The getTestResult method returns the overall test results
     * @return true if test is successful or false if any assertions failed
     */
    public boolean getTestResult() {
        return testSuccess;
    }

    /**
     * The getAssertionResults method returns the results from all the assertions
     * @return a map containing all assertion results
     */
    public LinkedHashMap<String,String> getAssertionResults() {
        return assertionResults;
    }

}