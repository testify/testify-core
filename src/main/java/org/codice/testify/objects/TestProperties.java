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

import java.util.*;

/**
 * The TestProperties class stores all the test file properties parsed from the PropertiesParsers or set by actions
 */
public class TestProperties {
    
    private Map<String,List<String>> properties = null;

    /**
     * The constructor to set a new test properties object
     */
    public TestProperties(){
        properties = new HashMap<>();
    }

    /**
     * The getFirstValue method returns the first value that matches a property name
     * @param propertyName the name of the property
     * @return the first value found
     */
    public String getFirstValue( String propertyName ){
        return getValue( propertyName, 0 );
    }

    /**
     * The getValue method returns a property value given a property name and an index
     * @param propertyName the name of the property
     * @param index the index of the value under that property name
     * @return the value matching the specific property name and index
     */
    public String getValue( String propertyName, int index ){

        //Retrieve the list of values under the given property name
        List<String> props = properties.get( propertyName );
        String value = null;

        //Check that there are property values under the given name and that the index is valid
        if ( props != null && props.size() >= index ){
            value = props.get( index );
        }
        return value;
    }

    /**
     * The getValues method returns all values under a given property name
     * @param propertyName the name of the property
     * @return a list of all values under a property name
     */
    public List<String> getValues( String propertyName ){
        return properties.get( propertyName );
    }

    /**
     * The addProperty method adds a value to the test properties
     * @param propertyName the name of the property
     * @param value the value under the property name
     */
    public void addProperty( String propertyName, String value ){

        List<String> propList = properties.get( propertyName );

        //Checks if there are property values that already exist for the given property name
        if ( propList == null ){

            //If there are no values, then a new entry is created
            propList = new ArrayList<>();
            propList.add( value );
            properties.put( propertyName, propList );

        }else{

            //If there are values, then the new value is added to the list
            propList.add( value );
        }
    }

    /**
     * The addProperties method adds a property name and list of values to the test properties
     * @param propertyName the name of the property
     * @param values the list of values
     */
    public void addProperties( String propertyName, List<String> values ){
        properties.put( propertyName, values );
    }

    /**
     * The getPropertyNames method returns a list of property names in the test properties
     * @return the list of property names
     */
    public Set<String> getPropertyNames(){
        return properties.keySet();
    }

    /**
     * The getUniqueCombinations method returns all possible combinations of property names and values for a specific list of property names
     * @param specificProps a list of property names
     * @return a list all unique sets of property names and values for a given property name list
     */
    public List<Map<String,String>> getUniqueCombinations(List<String> specificProps){
        int keySize = specificProps.size();
        String[][] multi = new String[keySize][];
        int i=0;
        for( String name : specificProps ){
            multi[i++] = properties.get( name ).toArray( new String[properties.get( name ).size() ] );
        }
        Set<List<String>> combinationSet = allComb( multi );
        
        List<Map<String,String>> propertyList = new ArrayList<>();

        String[] names = specificProps.toArray( new String[keySize] );
        for ( List<String> props : combinationSet ) {
            int index = keySize;
            Map<String,String> map = new HashMap<>();
            for ( String prop : props ) {
                
                map.put( names[--index], prop );
                
            }
            propertyList.add( map );
        }
        return propertyList;
    }

    /**
     * The allComb method returns allComb of a property and value set
     * @param opts the property and value parameters
     * @return a HashSet of all combinations
     */
    private static Set<List<String>> allComb(String[][] opts) {

        Set<List<String>> results = new HashSet<>();

        if (opts.length == 1) {
            for (String s : opts[0]) {
                results.add(new ArrayList<>(Arrays.asList(s)));
            }
        } else {
            for (String str : opts[0]) {
                String[][] tail = Arrays.copyOfRange(opts, 1, opts.length);
                for (List<String> combs : allComb(tail)) {
                    combs.add(str);
                    results.add(combs);
                }
            }
        }
        return results;
    }

    /**
     * The propertyExists method returns whether there is a specific property name in the test properties
     * @param propertyName the name of the property
     * @return true if the property name is in the test properties or false if it is not
     */
    public boolean propertyExists(String propertyName) {
        return properties.get(propertyName) != null;
    }
}
