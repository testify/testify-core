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

import org.codice.testify.handlers.PropertiesParserHandler;
import org.codice.testify.objects.TestifyLogger;
import org.codice.testify.objects.TestProperties;
import org.osgi.framework.BundleContext;
import java.io.File;
import java.util.ArrayList;

/**
 * The TestPropertiesBuilder class takes in a configuration file or directory and returns a TestProperties object
 */
public class TestPropertiesBuilder {

    /**
     * The buildTestProperties method is called by the TestEngine. It takes in a config file or directory and returns the parsed properties.
     * @param config a path to a config directory or file
     * @param bundleContext the felix bundle context for Testify
     * @return a TestProperties object from one or more config files
     */
    public TestProperties buildTestProperties(String config, BundleContext bundleContext) {

        //Set up objects for test property drill down
        File testDirFile = new File(config);
        ArrayList<File> dirNames = new ArrayList<>();
        dirNames.add(testDirFile);
        ArrayList<File> addToDir = new ArrayList<>();
        TestProperties testProperties = new TestProperties();
        PropertiesParserHandler propertiesParserHandler = new PropertiesParserHandler();

        //Continue loop while more folders exist
        while (dirNames.size() > 0) {

            //Loop through each directory in the array
            for (File dirName : dirNames) {

                //Check if content is directory or file
                if (dirName.isDirectory()) {

                    //List contents of directory and run through each one if files exist in directory
                    File[] contentNames = dirName.listFiles();
                    if (contentNames != null) {
                        for (File contentName : contentNames) {

                            //If the content is a directory, add it to the found list of directories. Otherwise, call the PropertiesParserHandler to parse the file
                            if (contentName.isDirectory()) {

                                //If content of directory is a directory, store it in addToDir
                                addToDir.add(contentName);

                            } else {
                                String testFileName = contentName.getName();

                                //Check if file is a properties file before parsing
                                if (testFileName.endsWith(".properties")) {

                                    //Run the PropertiesParserHandler to parse the file
                                    TestProperties addTestProperties = propertiesParserHandler.handlePropParsers(contentName.toString(), bundleContext);

                                    //Add the returned properties to the Testify TestProperties
                                    for (String property : addTestProperties.getPropertyNames()) {
                                        testProperties.addProperties(property, addTestProperties.getValues(property));
                                    }

                                } else {
                                    TestifyLogger.debug("Skipping " + testFileName + " because it is not a properties file", this.getClass().getSimpleName());
                                }
                            }
                        }
                    }

                //If the content is a file,
                } else if (dirName.isFile()) {
                    String testFileName = dirName.getName();

                    //Check if file is a properties file before parsing
                    if (testFileName.endsWith(".properties")) {

                        //Run the PropertiesParserHandler to parse the file
                        TestProperties addTestProperties = propertiesParserHandler.handlePropParsers(dirName.toString(), bundleContext);

                        //Add the returned properties to the Testify TestProperties
                        for (String property : addTestProperties.getPropertyNames()) {
                            testProperties.addProperties(property, addTestProperties.getValues(property));
                        }

                    } else {
                        TestifyLogger.debug("Skipping " + testFileName + " because it is not a properties file", this.getClass().getSimpleName());
                    }

                } else {
                    TestifyLogger.debug("The path: " + dirName.getAbsolutePath() + " is not a file or directory so no config files could not be parsed from it", this.getClass().getSimpleName());
                }
            }

            //Erase the old list of directories and set it to the found list of directories
            dirNames.clear();
            for (File addDir : addToDir) {
                dirNames.add(addDir);
            }

            //Clear the found list of directories
            addToDir.clear();
        }
        return testProperties;
    }
}