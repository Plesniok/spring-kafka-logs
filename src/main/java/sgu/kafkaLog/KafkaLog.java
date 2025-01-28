package sgu.kafkaLog;

import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@AllArgsConstructor
@Getter
@Setter
public class KafkaLog {
    final private Map<String, Object> dataProvided;
    final private Map<String, Object> responseData;
    final private String endpoint;
    final private String kafkaUuid;

    @Override
    public String toString() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
