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

package org.codice.testify.assertions;

import org.codice.testify.objects.AssertionStatus;
import org.codice.testify.objects.Response;

/**
 * Interface for all assertions
 */
public interface Assertion {

    /**
     * The evaluateAssertion method takes in actionInfo and returns the results of the assertion
     * @param assertionInfo all information sent by the test file to the assertion
     * @param response the processor response
     * @return an AssertionStatus object
     */
    public AssertionStatus evaluateAssertion(String assertionInfo, Response response);

}