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

import org.codice.testify.objects.Response;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class ResponseTest {

    @Test
    public void testGetResponse() {
        Response response = new Response("Test Response");
        assert ( response.getResponse().equals("Test Response") );
    }

    @Test
    public void testSetAndGetResponseCode() {
        Response response = new Response("Test Response");
        response.setResponseCode(100);
        assert ( response.getResponseCode() == 100 );
    }

    @Test
    public void testSetAndGetResponseHeaders() {
        Response response = new Response("Test Response");
        response.setResponseHeaders("Response Headers");
        assert ( response.getResponseHeaders().equals("Response Headers") );
    }

    @Test
    public void testSetAndGetResponseAttachments() {
        Response response = new Response("Test Response");
        response.setResponseAttachments("Response Attachments");
        assert ( response.getResponseAttachments().equals("Response Attachments") );
    }

}
