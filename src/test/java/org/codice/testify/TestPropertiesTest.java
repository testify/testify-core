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

import org.codice.testify.objects.TestProperties;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import java.util.*;

@RunWith(JUnit4.class)
public class TestPropertiesTest {

    @Test
    public void testAddProperty(){
        TestProperties testProperties = new TestProperties();
        testProperties.addProperty("Property","Value");
        assert ( testProperties.propertyExists("Property") );
        assert ( testProperties.getFirstValue("Property").equals("Value") );
    }

    @Test
    public void testAddProperties(){
        TestProperties testProperties = new TestProperties();
        List<String> values = Arrays.asList("Value1", "Value2", "Value3");
        testProperties.addProperties("Property", values);
        assert ( testProperties.propertyExists("Property") );
        assert ( testProperties.getValues("Property").equals(values) );
        assert ( testProperties.getValue("Property",1).equals("Value2") );
    }

    @Test
    public void testGetPropertyNames(){
        TestProperties testProperties = new TestProperties();
        Set<String> properties = new HashSet<>(Arrays.asList("Property1", "Property2", "Property3"));
        testProperties.addProperty("Property1", "Value1");
        testProperties.addProperty("Property2", "Value2");
        testProperties.addProperty("Property3", "Value3");
        assert ( testProperties.getPropertyNames().equals(properties) );
    }

    @Test
    public void testGetUniqueCombinations(){
        TestProperties testProperties = new TestProperties();
        List<String> values = Arrays.asList("Value1", "Value2", "Value3");
        Map<String,String> map1 = new HashMap<>();
        Map<String,String> map2 = new HashMap<>();
        Map<String,String> map3 = new HashMap<>();
        Map<String,String> map4 = new HashMap<>();
        map1.put("Property1","Value");
        map2.put("Property2","Value1");
        map2.put("Property1","Value");
        map3.put("Property2","Value2");
        map3.put("Property1","Value");
        map4.put("Property2","Value3");
        map4.put("Property1","Value");
        testProperties.addProperty("Property1", "Value");
        testProperties.addProperties("Property2", values);

        List<String> specificProps = Arrays.asList("Property1");
        assert ( testProperties.getUniqueCombinations(specificProps).size() == 1 );
        assert ( testProperties.getUniqueCombinations(specificProps).contains(map1) );

        specificProps = Arrays.asList("Property1","Property2");
        assert ( testProperties.getUniqueCombinations(specificProps).size() == 3 );
        assert ( testProperties.getUniqueCombinations(specificProps).contains(map2) );
        assert ( testProperties.getUniqueCombinations(specificProps).contains(map3) );
        assert ( testProperties.getUniqueCombinations(specificProps).contains(map4) );
    }
}
