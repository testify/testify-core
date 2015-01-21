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

import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * The TestifyLogger class can be called by any method of Testify or associated services to write a log message or set the log level
 */
public class TestifyLogger {

    /**
     * The setRootLevel method sets the minimum logging level for the root logger which will be used by all subsequent logging in Testify
     * @param rootLevel the minimum logging level for Testify
     */
    public static void setRootLevel(String rootLevel) {

        //Get the root logger for Testify
        Logger rootLogger = LogManager.getRootLogger();

        //Set the log level based on the rootLevel input. If the rootLevel value is invalid, nothing will be changed.
        switch (rootLevel.toUpperCase()) {
            case "TRACE":
                rootLogger.setLevel(Level.TRACE);
                break;
            case "DEBUG":
                rootLogger.setLevel(Level.DEBUG);
                break;
            case "INFO":
                rootLogger.setLevel(Level.INFO);
                break;
            case "WARN":
                rootLogger.setLevel(Level.WARN);
                break;
            case "ERROR":
                rootLogger.setLevel(Level.ERROR);
                break;
            case "FATAL":
                rootLogger.setLevel(Level.FATAL);
                break;
            default:
                break;
        }
    }

    /**
     * The trace method writes a trace level log
     * @param message the log message
     * @param logName the name of the logger
     */
    public static void trace(String message, String logName) {
        Logger logger = LogManager.getLogger(logName);
        logger.trace(message);
    }

    /**
     * The debug method writes a debug level log
     * @param message the log message
     * @param logName the name of the logger
     */
    public static void debug(String message, String logName) {
        Logger logger = LogManager.getLogger(logName);
        logger.debug(message);
    }

    /**
     * The info method writes a info level log
     * @param message the log message
     * @param logName the name of the logger
     */
    public static void info(String message, String logName) {
        Logger logger = LogManager.getLogger(logName);
        logger.info(message);
    }

    /**
     * The warn method writes a warn level log
     * @param message the log message
     * @param logName the name of the logger
     */
    public static void warn(String message, String logName) {
        Logger logger = LogManager.getLogger(logName);
        logger.warn(message);
    }

    /**
     * The error method writes a error level log
     * @param message the log message
     * @param logName the name of the logger
     */
    public static void error(String message, String logName) {
        Logger logger = LogManager.getLogger(logName);
        logger.error(message);
    }

    /**
     * The fatal method writes a fatal level log
     * @param message the log message
     * @param logName the name of the logger
     */
    public static void fatal(String message, String logName) {
        Logger logger = LogManager.getLogger(logName);
        logger.fatal(message);
    }
}