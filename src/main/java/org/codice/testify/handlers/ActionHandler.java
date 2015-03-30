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

package org.codice.testify.handlers;

import org.codice.testify.actions.Action;
import org.codice.testify.objects.TestifyLogger;
import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

/**
 * The ActionHandler class takes in an action block, collects action services, and runs the matching actions
 **/
public class ActionHandler {

    /**
     * The handleActions method is called at three different points during a Testify run. It takes in an actionBlock and splits it into individual actions and information. It then looks for matching services and runs them.
     * @param actionBlock the set of actions that need to be run
     * @param bundleContext the Felix bundle context
     */
    public void handleActions(String actionBlock, BundleContext bundleContext) {

        //Split action block into separate actions
        String[] actions = actionBlock.split( System.lineSeparator() );

        //Collect all action services in the Testify
        TestifyLogger.debug("Looking for Action Services", this.getClass().getSimpleName());
        ServiceTracker serviceTracker = new ServiceTracker(bundleContext, Action.class.getName(), null);
        serviceTracker.open();
        Object[] actionServices = serviceTracker.getServices();

        //Loop through each action and split it into its components
        TestifyLogger.debug("Starting loop through test actions", this.getClass().getSimpleName());
        for (String action : actions) {
            String actionInfo = null;
            String[] actionComponents = action.split("::");
            if (actionComponents.length > 1) {
                actionInfo = actionComponents[1];
            }
            TestifyLogger.debug("Looking for Action: " + actionComponents[0].trim() + " and passing action info: " + actionInfo, this.getClass().getSimpleName());

            //Set a match boolean to check if any services match the test file action
            boolean match = false;

            //Loop through each action service
            for (Object actionService : actionServices) {

                TestifyLogger.debug("Comparing Action Service: " + ((Action) actionService).getClass().getSimpleName(), this.getClass().getSimpleName());

                //If the name of the action service matches the action in the test file, run the action and return true
                if (((Action)actionService).getClass().getSimpleName().equalsIgnoreCase(actionComponents[0].trim())) {
                    ((Action)actionService).executeAction(actionInfo);
                    match = true;
                }
            }

            //If there is no match, let the user know
            if (!match) {
                TestifyLogger.error("No Action Services with name: " + actionComponents[0].trim(), this.getClass().getSimpleName());
            }
        }
    }
}