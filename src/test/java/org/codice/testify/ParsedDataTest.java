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

package org.codice.testify;

import org.codice.testify.objects.ActionData;
import org.codice.testify.objects.ParsedData;
import org.codice.testify.objects.Request;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class ParsedDataTest {

    @Test
    public void testGetAssertionBlock() {
        ParsedData parsedData = new ParsedData(null, "Assertion Block", null);
        assert ( parsedData.getAssertionBlock().equals("Assertion Block") );
    }

    @Test
    public void testGetRequest() {
        Request request = new Request(null, null, null);
        ParsedData parsedData = new ParsedData(request, "AssertionBlock", null);
        assert ( parsedData.getRequest().equals(request) );
    }

    @Test
    public void testGetActionData() {
        ActionData actionData = new ActionData(null, null, null);
        ParsedData parsedData = new ParsedData(null, "AssertionBlock", actionData);
        assert ( parsedData.getActionData().equals(actionData) );
    }

}
