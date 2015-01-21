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
import org.codice.testify.objects.Response;
import org.codice.testify.objects.Result;
import org.codice.testify.objects.TestData;
import org.codice.testify.writers.Writer;
import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

/**
 * The WriterHandler class takes in the testData, response, and result objects. It collects writer services and runs all of them.
 **/
public class WriterHandler {

    /**
     * The handleWriters method takes in the testData, response, and result objects for the test file, collects all writer services, and runs all of them.
     * @param testData the testData object for a specific test file
     * @param response the response from the processor
     * @param result the result data from the assertions
     * @param bundleContext the Felix bundle context
     */
    public void handleWriters(TestData testData, Response response, Result result, BundleContext bundleContext) {

        //Start the service tracker and grab all writer services
        TestifyLogger.debug("Looking for writers", this.getClass().getSimpleName());
        ServiceTracker serviceTracker = new ServiceTracker(bundleContext, Writer.class.getName(), null);
        serviceTracker.open();
        Object[] writerServices = serviceTracker.getServices();

        //Run every writer service
        for (Object writerService : writerServices) {
                ((Writer)writerService).writeResults(testData, response, result);
        }
    }
}