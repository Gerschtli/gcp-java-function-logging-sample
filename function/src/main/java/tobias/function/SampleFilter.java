package tobias.function;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.spi.FilterReply;

public class SampleFilter extends Filter<ILoggingEvent> {
    @Override
    public FilterReply decide(final ILoggingEvent event) {
        if (event.getMessage().contains("ignore me")) {
            return FilterReply.DENY;
        }

        return FilterReply.NEUTRAL;
    }
}
