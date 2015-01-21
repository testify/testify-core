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

package org.codice.testify.engine;

import org.codice.testify.handlers.ActionHandler;
import org.codice.testify.handlers.AssertionHandler;
import org.codice.testify.handlers.ProcessorHandler;
import org.codice.testify.handlers.WriterHandler;
import org.codice.testify.objects.*;
import org.osgi.framework.BundleContext;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

/**
 * The TestEngine is the main engine class for the Testify.
 */
public class TestEngine {

    /**
     * The testifyRunner method is called by the main method. It sets up and runs all the Testify functions.
     * @param testDir the directory containing all test files
     * @param resultDir the directory where results and logs should be stored
     * @param config the location of the config file or directory
     * @param logLevel the log level that the Testify should be run at
     */
    public void testifyRunner(String testDir, String resultDir, String config, String logLevel) {

        //Set result directory for the log file
        System.setProperty("result.dir", resultDir);

        //Set Testify log level
        TestifyLogger.setRootLevel(logLevel);

        TestifyLogger.info("Starting Testify", this.getClass().getSimpleName());

        //Get and store Testify Start time
        String timestamp = new SimpleDateFormat("yyyy-MM-dd'T'HH.mm.ss").format(Calendar.getInstance().getTime());
        AllObjects.setObject("timestamp", timestamp);
        TestifyLogger.debug("Testify start time set to " + timestamp, this.getClass().getSimpleName());

        //Start Felix framework and install bundles
        FelixBuilder felixBuilder = new FelixBuilder();
        BundleContext bundleContext = felixBuilder.startFramework();

        //Parse config file and store properties
        TestifyLogger.info("Parsing configuration file(s): " + config, this.getClass().getSimpleName());
        TestPropertiesBuilder testPropertiesBuilder = new TestPropertiesBuilder();
        TestProperties testProperties = testPropertiesBuilder.buildTestProperties(config, bundleContext);
        AllObjects.setObject("testProperties", testProperties);

        TestifyLogger.info("Test result parent directory: " + resultDir, this.getClass().getSimpleName());
        TestifyLogger.info("Collecting test files from directory: " + testDir, this.getClass().getSimpleName());

        //Get list of all test data files
        TestFileBuilder testFileBuilder = new TestFileBuilder();
        List<TestData> testFiles = testFileBuilder.buildTestFiles(testDir, resultDir, testProperties, bundleContext);

        //Loop through each test file in test directory
        TestifyLogger.debug("Starting loop through " + testFiles.size() + " test files", this.getClass().getSimpleName());
        for (TestData testFile : testFiles) {

            //Store testFile in AllObjects
            AllObjects.setObject("testFile", testFile);

            //Run preTestProcessorActions using action handler
            if ( testFile.getParsedData().getActionData().getPreTestProcessorAction() != null ){
                TestifyLogger.debug("Running preTestProcessor Actions", this.getClass().getSimpleName());
                ActionHandler actionHandler = new ActionHandler();
                actionHandler.handleActions( testFile.getParsedData().getActionData().getPreTestProcessorAction(), bundleContext );
                testFile = (TestData) AllObjects.getObject("testFile");
            }

            //Update test file properties that may have been added by previous tests and store testFile in AllObjects
            TestProperties dynamicProperties = (TestProperties) AllObjects.getObject("testProperties");
            testFile = updateTestifyProperties(testFile, dynamicProperties);
            AllObjects.setObject("testFile", testFile);

            TestifyLogger.info("Running test: " + testFile.getTestName(), this.getClass().getSimpleName());
            TestifyLogger.debug("Processor: " + testFile.getParsedData().getRequest().getType() + System.lineSeparator() +
                    "Endpoint: " + testFile.getParsedData().getRequest().getEndpoint() + System.lineSeparator() +
                    "Test Block: " + testFile.getParsedData().getRequest().getTestBlock() + System.lineSeparator() +
                    "Assertion Block: " + testFile.getParsedData().getAssertionBlock() + System.lineSeparator() +
                    "Pre Test Processor Actions: " + testFile.getParsedData().getActionData().getPreTestProcessorAction() + System.lineSeparator() +
                    "Post Test Processor Actions: " + testFile.getParsedData().getActionData().getPostTestProcessorAction() + System.lineSeparator(), this.getClass().getSimpleName());

            //Run processor using processor handler and store response
            ProcessorHandler processorHandler = new ProcessorHandler();
            TestifyLogger.debug("Executing test using processors: " + testFile.getParsedData().getRequest().getType(), this.getClass().getSimpleName());
            Response response = processorHandler.runProcessor( testFile.getParsedData().getRequest(), bundleContext );
            AllObjects.setObject("response", response);
            TestifyLogger.debug("Processor response: " + response.getResponse(), this.getClass().getSimpleName());
            TestifyLogger.debug("Processor response code: " + response.getResponseCode(), this.getClass().getSimpleName());
            TestifyLogger.debug("Processor response headers: " + response.getResponseHeaders(), this.getClass().getSimpleName());
            TestifyLogger.debug("Processor response attachments: " + response.getResponseAttachments(), this.getClass().getSimpleName());

            //Run postTestProcessorActions using action handler
            if ( testFile.getParsedData().getActionData().getPostTestProcessorAction() != null ){
                TestifyLogger.debug("Running postTestProcessor Actions", this.getClass().getSimpleName());
                ActionHandler actionHandler = new ActionHandler();
                actionHandler.handleActions( testFile.getParsedData().getActionData().getPostTestProcessorAction(), bundleContext );
                testFile = (TestData) AllObjects.getObject("testFile");
            }

            //Run assertions handler and obtain results
            AssertionHandler ah = new AssertionHandler();
            Result result = ah.handleAssertions(testFile.getParsedData().getAssertionBlock(), response, bundleContext);

            //Run writers handler
            WriterHandler writerHandler = new WriterHandler();
            writerHandler.handleWriters(testFile, response, result, bundleContext);
            TestifyLogger.debug("-----------------------------------------------------------------------------", this.getClass().getSimpleName());
        }
        TestifyLogger.info("Closing Testify", this.getClass().getSimpleName());
    }

    /**
     * The updateTestifyProperties method is called before each test file is run in order to update the test file with properties that may have been added by previous tests
     * @param testFile a TestData object
     * @param dynamicProperties the newly updated properties at the start of a test file run
     * @return an updated TestData Object
     */
    private TestData updateTestifyProperties(TestData testFile, TestProperties dynamicProperties) {

        //Obtain latest Testify properties
        TestifyLogger.debug("Retrieving updated properties", this.getClass().getSimpleName());
        ParsedData parsedData = testFile.getParsedData();
        Request request = parsedData.getRequest();
        ActionData actionData = parsedData.getActionData();
        String endpoint = request.getEndpoint();
        String testBlock = request.getTestBlock();
        String assertionBlock = parsedData.getAssertionBlock();
        String preTestProcessorAction = actionData.getPreTestProcessorAction();
        String postTestProcessorAction = actionData.getPostTestProcessorAction();

        //Loop through properties and update testFile
        TestifyLogger.debug("Updating properties", this.getClass().getSimpleName());
        for( String key : dynamicProperties.getPropertyNames() ){
            String value = dynamicProperties.getFirstValue( key );
            endpoint = replaceProp(endpoint, key, value);
            testBlock = replaceProp( testBlock, key, value );
            assertionBlock = replaceProp( assertionBlock, key, value );
            preTestProcessorAction = replaceProp( preTestProcessorAction, key, value );
            postTestProcessorAction = replaceProp( postTestProcessorAction, key, value );
        }

        //Return new testFile
        Request newRequest = new Request(request.getType(), endpoint, testBlock);
        ActionData newActionData = new ActionData(actionData.getPreTestSetterAction(), preTestProcessorAction, postTestProcessorAction);
        ParsedData newParsedData = new ParsedData(newRequest, assertionBlock, newActionData);
        return new TestData(testFile.getTestName(), newParsedData, testFile.getResultFolder());
    }

    /**
     * The replaceProp method is called by the updateTestifyProperties method to update specific data sets
     * @param requestData the data which needs to be updated
     * @param key the key that represents the variable to be updated
     * @param value the value for the variable
     * @return the updated data
     */
    private String replaceProp( String requestData, String key, String value ) {

        //Replace any property keys found in a given String with a given value and return updated String
        if ( requestData != null ) {
            requestData = requestData.replaceAll( "\\$" + "\\{" + key + "\\}", value );
        }
        return requestData;
    }
}