package sgu.kafkaLog;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;
import java.util.UUID;

@Component
public class RequestInterceptor implements HandlerInterceptor {

    private static final String TOPIC = "logs-topic";

    private static final Logger logger = LoggerFactory.getLogger(RequestInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        final String newKafkaLogUuid = UUID.randomUUID().toString();
        request.setAttribute("kafka-log-uuid", newKafkaLogUuid);
        logger.info("Request ID: {} - Kafka log: {} - Request URL: {}", request.getRequestId(), newKafkaLogUuid,  request.getRequestURL());

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws IOException {
        logger.info("Request ID: {} - Kafka log: {} - Request URL: {}", request.getRequestId(), request.getAttribute("kafka-log-uuid"),  request.getRequestURL());

    }
}
