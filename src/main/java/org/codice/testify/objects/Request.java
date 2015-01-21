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
 * The Request class stores all the data which is used to select a processor and run it
 */
public class Request {

    private final String type;
    private final String endpoint;
    private final String testBlock;

    /**
     * The constructor for the Request class sets the processor type being used, the endpoint, and the testBlock that will be sent to the processor
     * @param type the name of the processor
     * @param endpoint the endpoint that the processor will send information to
     * @param testBlock the information that the processor will utilize for running the test
     */
    public Request(String type, String endpoint, String testBlock) {
        this.type = type;
        this.endpoint = endpoint;
        this.testBlock = testBlock;
    }

    /**
     * The getType method returns the processor type
     * @return the processor type
     */
    public String getType() {
        return type;
    }

    /**
     * The getEndpoint method returns the test endpoint
     * @return the test endpoint
     */
    public String getEndpoint() {
        return endpoint;
    }

    /**
     * The getTestBlock method returns the test block
     * @return the test block
     */
    public String getTestBlock() {
        return testBlock;
    }

}
