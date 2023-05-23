package io.github.skyblockcore.event;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.apache.logging.log4j.core.filter.AbstractFilter;

public class ConsoleLogFilter extends AbstractFilter {

    private static final String UNWANTED_LOG_MESSAGE = "Ignoring player info update for unknown player";

    public ConsoleLogFilter() {
        super(Result.NEUTRAL, Result.DENY);
    }

    @Override
    public Result filter(LogEvent event) {
        if (ConfigManager.getConfig() != null && ConfigManager.getConfig().isUnknownPlayer() &&
                event.getMessage().getFormattedMessage().contains(UNWANTED_LOG_MESSAGE)) {
            return Result.DENY; // Ignore the log event
        }
        return Result.NEUTRAL; // Allow the log event to proceed
    }

    public static void installFilter() {
        LoggerContext loggerContext = (LoggerContext) LogManager.getContext(false);
        Configuration configuration = loggerContext.getConfiguration();
        LoggerConfig loggerConfig = configuration.getLoggerConfig(LogManager.ROOT_LOGGER_NAME);
        loggerConfig.addFilter(new ConsoleLogFilter());
        loggerContext.updateLoggers();
    }
}





// if you're reading this you're a femboy now
// Meow! >.<