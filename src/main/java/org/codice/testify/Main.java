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

package org.codice.testify;

import org.codice.testify.engine.TestEngine;

/**
 * The Main class takes in user provided arguments and starts the Testify TestEngine
 */
public class Main {

    /**
     * The main method is the starting point for the Testify jar file. It calls the Testify TestEngine.
     * @param args the arguments provided by the user at runtime
     */
    public static void main(String[] args) {

        //Set the TestEngine parameters from the user provided arguments
        String testDir = args[0];
        String resultDir = args[1];
        String configFile = args[2];
        String logLevel = args[3];

        //Run the DTF TestEngine
        TestEngine dtr = new TestEngine();
        dtr.testifyRunner(testDir, resultDir, configFile, logLevel);
        System.exit(0);
    }
}