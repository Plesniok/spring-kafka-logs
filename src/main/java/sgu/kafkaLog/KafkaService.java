package sgu.kafkaLog;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class KafkaService {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final String topic = "logs-topic";

    private static final Logger logger = LoggerFactory.getLogger(KafkaService.class);

    public KafkaService(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }


    public void sendKafkaMessage(KafkaLog kafkaLog) {
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(2);
        Future kafkaFuture = executor.submit(() -> {
            kafkaTemplate.send(topic, kafkaLog.toString());
        });

        executor.schedule(new Runnable() {
            public void run() {
                kafkaFuture.cancel(true);
            }
        }, 20000, TimeUnit.MILLISECONDS);
    }


}
