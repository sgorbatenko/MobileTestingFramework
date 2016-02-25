
package com.stas.mobile.testing.framework.util.logger;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.stas.mobile.testing.framework.util.environment.EnvironmentUtil;

public class LogController
{
    private StringBuffer methodLog = new StringBuffer();
    private static StringBuffer runLog = new StringBuffer();
    private Logger logger;
    private EnvironmentUtil env;
    private String className;
    private Level logLevel;

    public <T> LogController(Class<T> clazz)
    {
        env = EnvironmentUtil.getInstance();
        logger = Logger.getLogger(clazz);
        this.className = clazz.getName();
        this.logLevel = env.getLogLevel();
        if (this.logLevel == null)
        {
            this.logLevel = Level.INFO;
        }
    }

    public void trace(String message)
    {
        this.logLevel = env.getLogLevel();
        logger.trace(message);
        this.logger.trace(message);
        if (this.logLevel.equals(Level.TRACE))
        {
            writeToBuffers(message);
        }
    }

    public void debug(String message)
    {
        this.logLevel = env.getLogLevel();
        logger.debug(message);
        if ((this.logLevel.equals(Level.TRACE)) || (this.logLevel.equals(Level.DEBUG)))
        {
            writeToBuffers(message);
        }
    }

    public void info(String message)
    {
        this.logLevel = env.getLogLevel();
        logger.info(message);
        if ((this.logLevel.equals(Level.TRACE)) || (this.logLevel.equals(Level.DEBUG))
            ||
            (this.logLevel.equals(Level.INFO)))
        {
            writeToBuffers(message);
        }
    }

    public void warn(String message)
    {
        this.logLevel = env.getLogLevel();
        logger.warn(message);
        if ((this.logLevel.equals(Level.TRACE)) || (this.logLevel.equals(Level.DEBUG))
            ||
            (this.logLevel.equals(Level.INFO))
            ||
            (this.logLevel.equals(Level.WARN)))
        {
            writeToBuffers(message);
        }
    }

    public void error(String message)
    {
        // this.logLevel = env.getLogLevel();
        this.logLevel = logger.getLevel();
        logger.error(message);
        if ((this.logLevel.equals(Level.TRACE)) || (this.logLevel.equals(Level.DEBUG))
            ||
            (this.logLevel.equals(Level.INFO))
            ||
            (this.logLevel.equals(Level.WARN))
            ||
            (this.logLevel.equals(Level.ERROR)))
        {
            writeToBuffers(message);
        }
    }

    public void fatal(String message)
    {
        // this.logLevel = env.getLogLevel();
        this.logLevel = logger.getLevel();
        logger.fatal(message);
        if ((this.logLevel.equals(Level.TRACE)) || (this.logLevel.equals(Level.DEBUG))
            ||
            (this.logLevel.equals(Level.INFO))
            ||
            (this.logLevel.equals(Level.WARN))
            ||
            (this.logLevel.equals(Level.ERROR))
            ||
            (this.logLevel.equals(Level.FATAL)))
        {
            writeToBuffers(message);
        }
    }

    private void writeToBuffers(String message)
    {
        // DateTime dt = new DateTime();
        // DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyyMMdd-HH:ss");
        // String dateStamp = fmt.print(dt);
        // this.className = this.className.replace("com.applause.auto.framework.pageframework.", "");
        //
        // methodLog.append("\n" + dateStamp + " [" + this.className + "] : ");
        // runLog.append("\n" + dateStamp + " [" + this.className + "] : ");
        //
        // methodLog.append(message);
        // runLog.append(message);
    }
}
