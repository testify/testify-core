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
 * The ParsedData class stores all data parsed from the test file
 */
public class ParsedData {

    private final Request request;
    private final String assertionBlock;
    private final ActionData actionData;

    /**
     * The constructor for ParsedData. This sets the request, assertionBlock, and actionData
     * @param request the data to be sent to the processor
     * @param assertionBlock the data to be sent to the AssertionHandler
     * @param actionData the data to be sent to the ActionHandler
     */
    public ParsedData(Request request, String assertionBlock, ActionData actionData){
        this.request = request;
        this.assertionBlock = assertionBlock;
        this.actionData = actionData;
    }

    /**
     * The getAssertionBlock method returns the assertionBlock
     * @return the assertionBlock
     */
    public String getAssertionBlock() {
        return assertionBlock;
    }

    /**
     * The getRequest method returns the request
     * @return the request
     */
    public Request getRequest() {
        return request;
    }

    /**
     * The getActionData method returns the actionData
     * @return the actionData
     */
    public ActionData getActionData(){
        return actionData;
    }

}
