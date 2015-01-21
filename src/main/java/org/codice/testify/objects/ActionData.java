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
 * The ActionData class stores the three types of action data provided by the test file: preTestSetterAction, preTestProcessorAction, and postTestProcessorAction
 */
public class ActionData {

    private final String preTestSetterAction;
    private final String preTestProcessorAction;
    private final String postTestProcessorAction;

    /**
     * The constructor for the ActionData class which stores preTestSetterAction, preTestProcessorAction, and postTestProcessorAction
     * @param preTestSetterAction the block of actions that are performed before any property variables are set in the test data
     * @param preTestProcessorAction the block of actions that are performed right before the test processor is run
     * @param postTestProcessorAction the block of actions that are performed right after the test processor is run
     */
    public ActionData(String preTestSetterAction, String preTestProcessorAction, String postTestProcessorAction){
        this.preTestSetterAction = preTestSetterAction;
        this.preTestProcessorAction = preTestProcessorAction;
        this.postTestProcessorAction = postTestProcessorAction;
    }

    /**
     * The getPreTestSetterAction method returns the preTestSetterAction block
     * @return the preTestSetterAction block
     */
    public String getPreTestSetterAction() {
        return preTestSetterAction;
    }

    /**
     * The getPreTestProcessorAction method returns the preTestSetterAction block
     * @return the preTestProcessorAction block
     */
    public String getPreTestProcessorAction(){
        return preTestProcessorAction;
    }

    /**
     * The getPostTestProcessorAction method returns the preTestSetterAction block
     * @return the postTestProcessorAction block
     */
    public String getPostTestProcessorAction(){
        return postTestProcessorAction;
    }

}
