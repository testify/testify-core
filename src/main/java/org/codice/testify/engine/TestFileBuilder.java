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
import org.codice.testify.handlers.TestParserHandler;
import org.codice.testify.objects.*;
import org.osgi.framework.BundleContext;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * The TestFileBuilder class takes in the the test directory and other test file information and returns a list of TestData objects.
 */
public class TestFileBuilder {

    /**
     * The buildTestFiles method is called by the TestEngine. It takes in a test directory which it drills through, parses, and updates properties. It then stores this data with result directory data as a list of TestData objects.
     * @param testDir the directory containing the test files
     * @param resultDir the parent directory for all result files
     * @param testProperties the test properties that were parsed from a configuration file
     * @param bundleContext the felix bundle context
     * @return a list of all TestData gathered from the test directory
     */
    public List<TestData> buildTestFiles( String testDir, String resultDir, TestProperties testProperties, BundleContext bundleContext ) {

        //Drill down and store contents of test directory in a list
        File testDirFile = new File( testDir );
        ArrayList<File> dirNames = new ArrayList<>();
        dirNames.add( testDirFile );
        ArrayList<File> addToDir = new ArrayList<>();
        ArrayList<TestData> testList = new ArrayList<>();

        //Continue loop while more folders exist
        while ( dirNames.size() > 0 ) {

            //Loop through each directory in the array
            for ( File dirName : dirNames ) {

                //Set results folder string
                String folder = resultDir + File.separator + "results" + dirName.toString().replace( testDirFile.getParent(), "" );

                //List contents of directory and run through each one
                if ( dirName.isDirectory() ){
                    
                    //Check if there are any files in the directory
                    File[] contentNames = dirName.listFiles();
                    if (contentNames != null) {

                        //Sort the directory contents
                        Arrays.sort(contentNames);
                        for (File contentName : contentNames) {

                            // If the content is a directory, add it to the found list of directories. Otherwise, parse the content and add all test data to the list.
                            if (contentName.isDirectory()) {
                                addToDir.add(contentName);
                            } else {

                                //Parse the test file using the TestParserHandler and set parsed elements into separate Strings
                                TestifyLogger.debug("Parsing test file: " + contentName, this.getClass().getSimpleName());
                                TestParserHandler testParserHandler = new TestParserHandler();
                                ParsedData parsedData = testParserHandler.handleTestParsers(contentName, bundleContext);

                                if (parsedData != null) {
                                    String testBlock = parsedData.getRequest().getTestBlock();
                                    String endpoint = parsedData.getRequest().getEndpoint();
                                    String assertionBlock = parsedData.getAssertionBlock();
                                    String preTestSetterAction = parsedData.getActionData().getPreTestSetterAction();
                                    String preTestProcessorAction = parsedData.getActionData().getPreTestProcessorAction();
                                    String postTestProcessorAction = parsedData.getActionData().getPostTestProcessorAction();

                                    //Run preTestSetterActions
                                    if (preTestSetterAction != null) {
                                        TestifyLogger.debug("Running preTestSetter Actions", this.getClass().getSimpleName());
                                        ActionHandler actionHandler = new ActionHandler();
                                        actionHandler.handleActions(preTestSetterAction, bundleContext);
                                    }

                                    //Check if the parsed test file contains any of the TestProperties
                                    ArrayList<String> containedProps = new ArrayList<>();
                                    for (String prop : testProperties.getPropertyNames()) {
                                        if (hasProperty(testBlock, prop) || hasProperty(endpoint, prop) || hasProperty(assertionBlock, prop) || hasProperty(preTestProcessorAction, prop) || hasProperty(postTestProcessorAction, prop)) {
                                            containedProps.add(prop);
                                        }
                                    }

                                    //Set test file name counter to 1. This counter increases to create unique file names if there are multiple properties for a given variable in the test file.
                                    int counter = 1;

                                    //If the test file does not contain any properties then it should be added as is to the list of TestData. Otherwise it should have its properties set and then it should be added.
                                    if (!containedProps.isEmpty()) {

                                        //Go through every unique combination of TestProperties that are contained within the test file and create separate TestData objects for each set.
                                        for (Map<String, String> propertyMap : testProperties.getUniqueCombinations(containedProps)) {

                                            testBlock = parsedData.getRequest().getTestBlock();
                                            endpoint = parsedData.getRequest().getEndpoint();
                                            assertionBlock = parsedData.getAssertionBlock();
                                            preTestProcessorAction = parsedData.getActionData().getPreTestProcessorAction();
                                            postTestProcessorAction = parsedData.getActionData().getPostTestProcessorAction();

                                            //Replace the variables with the test properties
                                            for (String key : propertyMap.keySet()) {
                                                testBlock = replaceProperty(testBlock, key, propertyMap.get(key));
                                                endpoint = replaceProperty(endpoint, key, propertyMap.get(key));
                                                assertionBlock = replaceProperty(assertionBlock, key, propertyMap.get(key));
                                                preTestProcessorAction = replaceProperty(preTestProcessorAction, key, propertyMap.get(key));
                                                postTestProcessorAction = replaceProperty(postTestProcessorAction, key, propertyMap.get(key));
                                            }

                                            //Create the TestData object and add it to the list
                                            Request testRequest = new Request(parsedData.getRequest().getType(), endpoint, testBlock);
                                            ActionData actionData = new ActionData(preTestSetterAction, preTestProcessorAction, postTestProcessorAction);
                                            ParsedData changedParsedData = new ParsedData(testRequest, assertionBlock, actionData);
                                            TestData testData = new TestData(counter + "-" + contentName.getName(), changedParsedData, folder);
                                            testList.add(testData);
                                            counter = counter + 1;
                                        }

                                    } else {

                                        //Add the TestData object as is if it contains no test properties
                                        TestData testData = new TestData(counter + "-" + contentName.getName(), parsedData, folder);
                                        testList.add(testData);
                                    }
                                } else {
                                    TestifyLogger.debug("Test file: " + contentName + "returned no parsed data", this.getClass().getSimpleName());
                                }
                            }
                        }
                    } else {
                        TestifyLogger.debug("No files found in directory: " + dirName.getAbsolutePath(), this.getClass().getSimpleName());
                    }
                }else{
                    TestifyLogger.debug("The path: " + dirName.getAbsolutePath() + " is not a directory so no test files could not be parsed from it", this.getClass().getSimpleName());
                }
            }

            //Erase the old list of directories and set it to the found list of directories
            dirNames.clear();
            for ( File addDir : addToDir ) {
                dirNames.add( addDir );
            }

            //Clear the found list of directories
            addToDir.clear();
        }
        return testList;
    }

    /**
     * The hasProperty method is called by the buildTestFiles method to check if test file elements contain test properties
     * @param block a String block that will be checked for a value
     * @param checkProperty the value that is being checked for
     * @return true if the block contains the property or false if it does not
     */
    private boolean hasProperty( String block, String checkProperty ){

        //Set boolean default to false
        boolean answer = false;

        //If block is not null, set boolean to true if the block contains the property
        if (block != null) {
            if (block.contains( "$" + "{" + checkProperty + "}")) {
                answer = true;
            }
        }
        return answer;
    }

    /**
     * The replaceProperty method is called by the buildTestFiles method to replace variables in the test files with properties
     * @param content the content that may contain a variable
     * @param placeholder the variable name
     * @param newValue the value that will take the place of the variable
     * @return the updated content
     */
    private String replaceProperty( String content, String placeholder, String newValue ) {

        //If content is not null, replace the test file variable with the given value
        if ( content != null ) {
            content = content.replaceAll( "\\$" + "\\{" + placeholder + "\\}", newValue );
        }
        return content;
    }
}