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

package org.codice.testify.testParsers;

import org.codice.testify.objects.ParsedData;
import java.io.File;

/**
 * Interface for all test file parsers
 */
public interface TestParser {

    /**
     * The parseTest method takes in a test file and parses it
     * @param fileName the location of the test file
     * @return the parsed data
     */
    public ParsedData parseTest(File fileName);

}