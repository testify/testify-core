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

import org.codice.testify.objects.TestifyLogger;
import org.codice.testify.objects.Request;
import org.codice.testify.objects.Response;
import org.codice.testify.processors.TestProcessor;
import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

/**
 * The ProcessorHandler class takes in an request object, collects processor services, and runs the matching processor
 **/
public class ProcessorHandler {

    /**
     * The runProcessor method takes in a request object, looks for a matching processor service, and runs the processor. If no processors match, it returns a "null" response.
     * @param request an object containing all information needed to find and run a processor
     * @param bundleContext the Felix bundle context
     * @return a Response object
     */
    public Response runProcessor(Request request, BundleContext bundleContext) {

        //Start the service tracker and grab all TestProcessor services
        TestifyLogger.debug("Looking for processor: " + request.getType(), this.getClass().getSimpleName());
        ServiceTracker serviceTracker = new ServiceTracker(bundleContext, TestProcessor.class.getName(), null);
        serviceTracker.open();
        Object[] processorServices = serviceTracker.getServices();

        //Loop through each processor service
        for (Object processorService : processorServices) {

            TestifyLogger.debug("Comparing TestProcessor service: " + ((TestProcessor) processorService).getClass().getSimpleName(), this.getClass().getSimpleName());

            //If the name of a processor service matches the processor in the test file, run the processor and return the response
            if (((TestProcessor)processorService).getClass().getSimpleName().equalsIgnoreCase(request.getType())) {
                return ((TestProcessor) processorService).executeTest(request);
            }
        }

        //If no processor service matches return the string "null"
        TestifyLogger.info("No processor service: " + request.getType() + " found for this test file", this.getClass().getSimpleName());
        return new Response(null);
    }
}
