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
 * The TestData class stores all the data related to a specific test file
 */
public class TestData {

    private final String testName;
    private final ParsedData parsedData;
    private final String resultFolder;

    /**
     * The constructor for the TestData class sets the test name, parsed data, and result folder location
     * @param testName the name of the test file
     * @param parsedData the parsed data from the test file
     * @param resultFolder the location of the result folder
     */
    public TestData(String testName, ParsedData parsedData, String resultFolder){
        this.testName = testName;
        this.parsedData = parsedData;
        this.resultFolder = resultFolder;
    }

    /**
     * The getTestName method returns the name of the test file
     * @return the test file name
     */
    public String getTestName() {
        return testName;
    }

    /**
     * The getParsedData method returns the data parsed from the test file
     * @return the parsed data from the test file
     */
    public ParsedData getParsedData() {
        return parsedData;
    }

    /**
     * The getResultFolder method returns the folder location for the test file results
     * @return the test file result folder location
     */
    public String getResultFolder() {
        return resultFolder;
    }

}
