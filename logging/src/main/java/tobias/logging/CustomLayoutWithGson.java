package tobias.logging;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ch.qos.logback.classic.pattern.ExtendedThrowableProxyConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.CoreConstants;
import ch.qos.logback.core.LayoutBase;

public class CustomLayoutWithGson extends LayoutBase<ILoggingEvent> {
    private static final ExtendedThrowableProxyConverter THROWABLE_CONVERTER = new ExtendedThrowableProxyConverter();
    private static final Gson GSON = new Gson();

    static {
        THROWABLE_CONVERTER.start();
    }

    @Override
    public String doLayout(final ILoggingEvent event) {
        final var stacktrace = THROWABLE_CONVERTER.convert(event);

        final var line = new Line(
                event.getLevel().levelStr,
                getMarkerValue(event),
                event.getMDCPropertyMap(),
                event.getFormattedMessage(),
                stacktrace.isEmpty() ? null : stacktrace
        );

        return GSON.toJson(line) + CoreConstants.LINE_SEPARATOR;
    }

    private MarkerValue getMarkerValue(final ILoggingEvent event) {
        final var marker = event.getMarker();

        if (marker == null) {
            return null;
        }

        final var references = new ArrayList<String>();
        final var iterator = marker.iterator();
        while (iterator.hasNext()) {
            references.add(iterator.next().getName());
        }

        return new MarkerValue(marker.getName(), references);
    }

    private static class Line {
        final String severity;
        final MarkerValue marker;
        final Map<String, String> mdc;
        final String message;
        final String stacktrace;

        public Line(
                final String severity, final MarkerValue marker, final Map<String, String> mdc, final String message,
                final String stacktrace
        ) {
            this.severity = severity;
            this.marker = marker;
            this.mdc = mdc;
            this.message = message;
            this.stacktrace = stacktrace;
        }
    }

    private static class MarkerValue {
        final String name;
        final List<String> references;

        private MarkerValue(final String name, final List<String> references) {
            this.name = name;
            this.references = references;
        }
    }
}
