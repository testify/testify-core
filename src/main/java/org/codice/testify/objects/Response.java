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
 * The Response class stores the processor response
 */
public class Response {

    private final String response;
    private int responseCode = -1;
    private String responseHeaders = null;
    private String responseAttachments = null;

    /**
     * The constructor for the Response class stores the processor response
     * @param response the processor response
     */
    public Response(String response) {
        this.response = response;
    }

    /**
     * The getResponse method returns the processor response
     * @return the processor response
     */
    public String getResponse() {
        return response;
    }

    /**
     * The getResponseCode method returns the response code if included with the response
     * @return the response code
     */
    public int getResponseCode() {
        return responseCode;
    }

    /**
     * The setResponseCode method sets the responseCode integer
     * @param responseCode the code returned with the response
     */
    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    /**
     * The getResponseHeaders method returns headers if included with the response
     * @return the response headers
     */
    public String getResponseHeaders() {
        return responseHeaders;
    }

    /**
     * The setResponseHeaders method sets the responseHeaders string
     * @param responseHeaders the headers returned with the response
     */
    public void setResponseHeaders(String responseHeaders) {
        this.responseHeaders = responseHeaders;
    }

    /**
     * The getResponseAttachments method returns attachments if included with the response
     * @return the response attachments
     */
    public String getResponseAttachments() {
        return responseAttachments;
    }

    /**
     * The setResponseAttachments method sets the responseAttachments string
     * @param responseAttachments the attachments returned with the response
     */
    public void setResponseAttachments(String responseAttachments) {
        this.responseAttachments = responseAttachments;
    }

}