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
import org.codice.testify.objects.TestProperties;
import org.codice.testify.propertiesParsers.PropertiesParser;
import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

/**
 * The PropertiesParserHandler is responsible for running a PropertiesParser service
 */
public class PropertiesParserHandler {

    /**
     * The handlePropParsers method is called by the TestPropertiesBuilder. It identifies and runs the first PropertiesParser service that it finds.
     * @param configFile a configuration file that needs to be parsed
     * @param bundleContext the felix bundle context
     * @return a TestProperties object containing the parsed data
     */
    public TestProperties handlePropParsers(String configFile, BundleContext bundleContext) {

        //Start the service tracker and grab all PropertiesParser services
        TestifyLogger.debug("Looking for properties parser", this.getClass().getSimpleName());
        ServiceTracker serviceTracker = new ServiceTracker(bundleContext, PropertiesParser.class.getName(), null);
        serviceTracker.open();
        Object[] propertiesParserServices = serviceTracker.getServices();

        //Run the first properties parser service found
        return ((PropertiesParser)propertiesParserServices[0]).getTestProperties(configFile);
    }
}