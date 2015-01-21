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

import java.util.HashMap;

/**
 * The AllObjects class provides a HashMap to all Testify bundles, allowing them to share objects
 */
public class AllObjects {

    private static HashMap<String,Object> allObjects = new HashMap<>();

    /**
     * The getObject method takes in a key and returns an object from the HashMap
     * @param key the key that corresponds to a needed object
     * @return the object that corresponds to the given key
     */
    public static Object getObject(String key) {
        return allObjects.get(key);
    }

    /**
     * The setObject method takes an object and a key for the object and adds them to the HashMap
     * @param key a string used to find the object in the HashMap
     * @param object an object that will be stored in the HashMap
     */
    public static void setObject(String key, Object object) {
        allObjects.put(key,object);
    }
}