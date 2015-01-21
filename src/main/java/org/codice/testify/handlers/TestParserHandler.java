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
import org.codice.testify.objects.ParsedData;
import org.codice.testify.testParsers.TestParser;
import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import java.io.File;

/**
 * The TestParserHandler class takes in a test file, runs test file parsers for that file extension, and returns the parsed data.
 **/
public class TestParserHandler {

    /**
     * The handleTestParsers method takes in a file, checks for test parsers that support the file extension, and attempts to parse the file with each parser until data is returned.
     * @param fileName the file that needs to be parsed
     * @param bundleContext the felix bundle context
     * @return the parsed data
     */
    public ParsedData handleTestParsers(File fileName, BundleContext bundleContext) {

        //Get all references for the file extension
        String fileExtension = fileName.getName().substring(fileName.getName().lastIndexOf(".") + 1);
        TestifyLogger.debug("Looking for test parser for extension: " + fileExtension, this.getClass().getSimpleName());
        ServiceReference[] parserReferences = null;
        try {
            parserReferences = bundleContext.getServiceReferences(TestParser.class.getName(), "(extension=" + fileExtension + ")");
        } catch (InvalidSyntaxException e) {
            TestifyLogger.error(e.getMessage(), this.getClass().getSimpleName());
        }

        //If no test parsers were found for the given extension, then return null. Otherwise, start loop through each returned parser.
        if (parserReferences != null) {
            for (ServiceReference parserReference : parserReferences) {
                TestParser testParser = (TestParser)bundleContext.getService(parserReference);
                ParsedData parsedData = testParser.parseTest(fileName);

                //If the parser returns no data, then go on to the next one
                if (parsedData != null) {
                    TestifyLogger.debug("Test file parsed using: " + testParser.getClass().getSimpleName(), this.getClass().getSimpleName());
                    return parsedData;
                }
            }
        } else {
            TestifyLogger.debug("No parsers found for the given file type: " + fileExtension, this.getClass().getSimpleName());
        }
        return null;
    }
}