
package com.stas.mobile.testing.framework.util.logger;

import org.apache.log4j.Level;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.stas.mobile.testing.framework.util.environment.EnvironmentUtil;

public class LogController
{
    private static StringBuffer methodLog = new StringBuffer();
    private static StringBuffer runLog = new StringBuffer();
    private static org.apache.log4j.Logger log4JLogger;
    private org.testng.log4testng.Logger testNGLogger;
    private static EnvironmentUtil env;
    private String className;
    private Level logLevel;

    public <T> LogController(Class<T> clazz)
    {
        env = EnvironmentUtil.getInstance();
        log4JLogger = org.apache.log4j.Logger.getLogger(clazz);
        this.testNGLogger = org.testng.log4testng.Logger.getLogger(clazz);
        this.className = clazz.getName();
        this.logLevel = env.getLogLevel();
        if (methodLog == null)
        {
            methodLog = new StringBuffer();
        }
        if (runLog == null)
        {
            runLog = new StringBuffer();
        }
        if (this.logLevel == null)
        {
            this.logLevel = Level.INFO;
        }
    }

    public void trace(String message)
    {
        this.logLevel = env.getLogLevel();
        log4JLogger.trace(message);
        this.testNGLogger.trace(message);
        if (this.logLevel.equals(Level.TRACE))
        {
            writeToBuffers(message);
        }
    }

    public void debug(String message)
    {
        this.logLevel = env.getLogLevel();
        log4JLogger.debug(message);
        this.testNGLogger.debug(message);
        if ((this.logLevel.equals(Level.TRACE)) || (this.logLevel.equals(Level.DEBUG)))
        {
            writeToBuffers(message);
        }
    }

    public void info(String message)
    {
        this.logLevel = env.getLogLevel();
        log4JLogger.info(message);
        this.testNGLogger.info(message);
        if ((this.logLevel.equals(Level.TRACE)) || (this.logLevel.equals(Level.DEBUG)) ||
            (this.logLevel.equals(Level.INFO)))
        {
            writeToBuffers(message);
        }
    }

    public void warn(String message)
    {
        this.logLevel = env.getLogLevel();
        log4JLogger.warn(message);
        this.testNGLogger.warn(message);
        if ((this.logLevel.equals(Level.TRACE)) || (this.logLevel.equals(Level.DEBUG)) ||
            (this.logLevel.equals(Level.INFO)) ||
            (this.logLevel.equals(Level.WARN)))
        {
            writeToBuffers(message);
        }
    }

    public void error(String message)
    {
        this.logLevel = env.getLogLevel();
        this.logLevel = log4JLogger.getLevel();
        log4JLogger.error(message);
        this.testNGLogger.error(message);
        if ((this.logLevel.equals(Level.TRACE)) || (this.logLevel.equals(Level.DEBUG)) ||
            (this.logLevel.equals(Level.INFO)) ||
            (this.logLevel.equals(Level.WARN)) ||
            (this.logLevel.equals(Level.ERROR)))
        {
            writeToBuffers(message);
        }
    }

    public void fatal(String message)
    {
        this.logLevel = env.getLogLevel();
        this.logLevel = log4JLogger.getLevel();
        log4JLogger.fatal(message);
        this.testNGLogger.fatal(message);
        if ((this.logLevel.equals(Level.TRACE)) || (this.logLevel.equals(Level.DEBUG)) ||
            (this.logLevel.equals(Level.INFO)) ||
            (this.logLevel.equals(Level.WARN)) ||
            (this.logLevel.equals(Level.ERROR)) ||
            (this.logLevel.equals(Level.FATAL)))
        {
            writeToBuffers(message);
        }
    }

    private void writeToBuffers(String message)
    {
        DateTime dt = new DateTime();
        DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyyMMdd-HH:ss");
        String dateStamp = fmt.print(dt);
        this.className = this.className.replace("com.applause.auto.framework.pageframework.", "");

        methodLog.append("\n" + dateStamp + " [" + this.className + "] : ");
        runLog.append("\n" + dateStamp + " [" + this.className + "] : ");

        methodLog.append(message);
        runLog.append(message);
    }
}
