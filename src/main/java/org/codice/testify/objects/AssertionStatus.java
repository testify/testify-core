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

/**
 * The AssertionStatus class stores the results of the assertion and any potential failure details
 */
public class AssertionStatus {

    private final boolean success;
    private final String failureDetails;

    /**
     * The constructor for an assertion status. This takes in failure details from the assertion. If a null value is provided then the assertion was a success.
     * @param failureDetails the details of what caused the assertion failure. If null is provided then the assertion was a success.
     */
    public AssertionStatus(String failureDetails) {

        //If a null value is provided by the assertion, then the assertion was successful. Is failure details are provided, then it was not successful.
        if (failureDetails == null) {
            this.success = true;
            this.failureDetails = "Success";
        } else {
            this.success = false;
            this.failureDetails = failureDetails;
        }
    }

    /**
     * The isSuccess method returns a boolean representing whether the assertion passed or failed
     * @return true if success or false if fail
     */
    public boolean isSuccess() {
        return success;
    }

    /**
     * The getFailureDetails method returns the failure details of the assertion. It will return "Success" if there were no failure details.
     * @return the failure details of the assertion or "Success" if there were no failure details
     */
    public String getFailureDetails() {
        return failureDetails;
    }

}