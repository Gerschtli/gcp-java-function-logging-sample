package tobias.logging;

import java.util.stream.Collectors;

import ch.qos.logback.classic.pattern.ExtendedThrowableProxyConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.CoreConstants;
import ch.qos.logback.core.LayoutBase;

public class CustomLayout extends LayoutBase<ILoggingEvent> {
    private static final ExtendedThrowableProxyConverter THROWABLE_CONVERTER = new ExtendedThrowableProxyConverter();

    static {
        THROWABLE_CONVERTER.start();
    }

    @Override
    public String doLayout(final ILoggingEvent event) {
        final var builder = new StringBuilder(128);

        builder.append("{\"severity\": \"");
        builder.append(event.getLevel());
        builder.append("\", ");

        if (event.getMarker() != null) {
            builder.append("\"marker\": \"");
            builder.append(event.getMarker());
            builder.append("\", ");
        }

        if (!event.getMDCPropertyMap().isEmpty()) {
            builder.append("\"mdc\": {");
            builder.append(
                    event.getMDCPropertyMap()
                            .entrySet()
                            .stream()
                            .map(entry -> "\"" + entry.getKey() + "\": \"" + entry.getValue() + "\"")
                            .collect(Collectors.joining(", "))
            );
            builder.append("}, ");
        }

        if (event.getThrowableProxy() != null) {
            builder.append("\"stacktrace\": \"");
            builder.append(THROWABLE_CONVERTER.convert(event).replace("\n", "\\n"));
            builder.append("\", ");
        }

        builder.append("\"message\": \"");
        builder.append(event.getFormattedMessage());
        builder.append("\"}");
        builder.append(CoreConstants.LINE_SEPARATOR);

        return builder.toString();
    }
}
