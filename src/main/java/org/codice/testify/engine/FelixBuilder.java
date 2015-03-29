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

import org.codice.testify.objects.TestifyLogger;
import org.apache.felix.framework.Felix;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.osgi.framework.Constants;
import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * The FelixBuilder Class is responsible for starting felix and installing bundles in the deploy directory
 */
public class FelixBuilder {

    /**
     * The startFramework method is called by the TestEngine to configure and start an embedded felix instance. It also searches for and installs all bundles in the deploy directory.
     * @return the bundle context for the embedded felix system
     */
    public BundleContext startFramework() {

        //Get path of Testify jar and set the deploy directory path
        File deployDir = null;
        File testifyHome = null;
        try {
            File f = new File(FelixBuilder.class.getProtectionDomain().getCodeSource().getLocation().toURI());
            testifyHome = f.getParentFile().getParentFile();
            deployDir = new File(testifyHome.toString() + File.separator + "bundles");
            TestifyLogger.debug("The deploy directory is set to: " + deployDir, this.getClass().getSimpleName());
        } catch (URISyntaxException e) {
            TestifyLogger.error("Could not retrieve Testify directory. Error: " + e.getMessage(), this.getClass().getSimpleName());
        }

        //Create a config map for embedded felix
        Map configMap = new HashMap<>();

        //Set felix cache behavior
        configMap.put("org.osgi.framework.storage.clean", "onFirstInit");

        //Set felix cache directory
        configMap.put("org.osgi.framework.storage", testifyHome + File.separator + "testify-cache");

        //Add system packages that will be available to other bundles
        configMap.put(Constants.FRAMEWORK_SYSTEMPACKAGES_EXTRA, "org.codice.testify.actions," + "org.codice.testify.assertions," + "org.codice.testify.objects," + "org.codice.testify.propertiesParsers," + "org.codice.testify.testParsers," + "org.codice.testify.processors," + "org.codice.testify.writers," + "org.apache.log4j");

        //Create Felix object using config map
        Felix felix = new Felix(configMap);

        //Start felix
        TestifyLogger.debug("Starting embedded Felix", this.getClass().getSimpleName());
        try {
            felix.start();
        } catch (BundleException e) {
            TestifyLogger.error(e.getMessage(), this.getClass().getSimpleName());
        }

        //Check that deployDir exists
        if (deployDir != null) {

            //Set up objects for bundle file drill down
            ArrayList<File> dirNames = new ArrayList<>();
            dirNames.add(deployDir);
            ArrayList<File> addToDir = new ArrayList<>();

            //Continue loop while more folders exist
            while (dirNames.size() > 0) {

                //Loop through each directory in the array
                for (File dirName : dirNames) {

                    //List contents of directory and run through each one
                    if (dirName.isDirectory()) {

                        //Check that directory contains files/folders
                        File[] contentNames = dirName.listFiles();
                        if (contentNames != null) {

                            for (File contentName : contentNames) {

                                //If the content is a directory, add it to the found list of directories. Otherwise, start the contained bundles.
                                if (contentName.isDirectory()) {
                                    addToDir.add(contentName);

                                } else {
                                    String bundleFileName = contentName.getName();

                                    //Check if file is a jar file before attempting to start it
                                    if (bundleFileName.endsWith(".jar")) {

                                        TestifyLogger.debug("Starting bundle: " + bundleFileName, this.getClass().getSimpleName());
                                        try {

                                            //Start the bundle
                                            Bundle bundle = felix.getBundleContext().installBundle("file:" + contentName.toString());
                                            bundle.start();

                                        } catch (BundleException e) {
                                            e.printStackTrace();
                                            TestifyLogger.error(e.getMessage(), this.getClass().getSimpleName());
                                        }

                                    } else {
                                        TestifyLogger.debug("Skipping " + bundleFileName + " because it is not a jar file", this.getClass().getSimpleName());
                                    }
                                }
                            }
                        } else {
                            TestifyLogger.debug("The directory: " + dirName.getAbsolutePath() + " contains no files", this.getClass().getSimpleName());
                        }
                    } else {
                        TestifyLogger.info("The deploy directory: " + dirName.getAbsolutePath() + " is not a directory so nothing could be installed from it", this.getClass().getSimpleName());
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
        } else {
            TestifyLogger.info("No bundle deploy directory found", this.getClass().getSimpleName());
        }
        TestifyLogger.info("Installed Testify bundles: " + Arrays.toString(felix.getBundleContext().getBundles()), this.getClass().getSimpleName());
        return felix.getBundleContext();
    }
}