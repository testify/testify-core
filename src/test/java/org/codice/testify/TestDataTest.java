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

import org.codice.testify.objects.ParsedData;
import org.codice.testify.objects.TestData;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class TestDataTest {

    @Test
    public void testGetTestName() {
        TestData testData = new TestData("Test Name", null, "Result Folder");
        assert ( testData.getTestName().equals("Test Name") );
    }

    @Test
    public void testGetParsedData() {
        ParsedData parsedData = new ParsedData(null,null,null);
        TestData testData = new TestData("Test Name", parsedData, "Result Folder");
        assert ( testData.getParsedData().equals(parsedData) );
    }

    @Test
    public void testGetResultFolder() {
        TestData testData = new TestData("Test Name", null, "Result Folder");
        assert ( testData.getResultFolder().equals("Result Folder") );
    }

}