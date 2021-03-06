package com.github.kristofa.brave;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

/**
 * Contains logic for dealing with response being returned at server side.
 */
public class ServerResponseInterceptor {

    private final static Logger LOGGER = LoggerFactory.getLogger(ServerResponseInterceptor.class);

    private final ServerTracer serverTracer;

    public ServerResponseInterceptor(ServerTracer serverTracer) {
        this.serverTracer = Objects.requireNonNull(serverTracer, "serverTracer should not be null.");
    }

    public void handle(ServerResponseAdapter adapter) {
        // We can submit this in any case. When server state is not set or
        // we should not trace this request nothing will happen.
        LOGGER.debug("Sending server send.");
        try {
            for(KeyValueAnnotation annotation : adapter.responseAnnotations())
            {
                serverTracer.submitBinaryAnnotation(annotation.getKey(), annotation.getValue());
            }
            serverTracer.setServerSend();
        } finally {
            serverTracer.clearCurrentSpan();
        }
    }
}
