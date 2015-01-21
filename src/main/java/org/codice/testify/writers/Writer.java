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

package org.codice.testify.writers;

import org.codice.testify.objects.Response;
import org.codice.testify.objects.Result;
import org.codice.testify.objects.TestData;

/**
 * Interface for all writers
 */
public interface Writer {

    /**
     * The writeResults method takes in the test file data, processor response, and assertion results and performs various writing actions
     * @param testData all test file data
     * @param response the processor response
     * @param result the assertion results
     */
    public void writeResults(TestData testData, Response response, Result result);

}