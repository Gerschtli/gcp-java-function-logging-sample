package tobias.function;

import com.google.cloud.functions.HttpFunction;
import com.google.cloud.functions.HttpRequest;
import com.google.cloud.functions.HttpResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.slf4j.MarkerFactory;

@Slf4j
public class Function implements HttpFunction {
    @Override
    public void service(final HttpRequest request, final HttpResponse response) throws Exception {
        MDC.put("key", "value");
        MDC.put("key2", "value2");

        final var marker1 = MarkerFactory.getMarker("marker1");
        final var marker2 = MarkerFactory.getMarker("marker2");
        final var marker3 = MarkerFactory.getMarker("marker3");
        marker1.add(marker2);
        marker1.add(marker3);

        log.info(marker1, "message {}", "arg");

        MDC.remove("key");

        try {
            throwEx();
        } catch (final Exception exception) {
            log.error("message {}", "arg", exception);
        }

        log.info("please ignore me");

        final var writer = response.getWriter();
        writer.write("Hello world!");
    }

    private void throwEx() {
        throw new IndexOutOfBoundsException();
    }
}
