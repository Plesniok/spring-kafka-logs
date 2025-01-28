package sgu.kafkaLog;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import sgu.kafkaLog.middlewares.HttpServletRequestCopier;
import sgu.kafkaLog.middlewares.HttpServletResponseCopier;
import org.springframework.stereotype.Component;
import jakarta.servlet.http.HttpFilter;

@Component
public class KafkaFilter extends HttpFilter {
    private FilterConfig filterConfigObj;
    private final Gson gson = new Gson();
    private final KafkaService kafkaService;

    public KafkaFilter(KafkaService kafkaService) {
        this.kafkaService = kafkaService;
    }

    public void init(FilterConfig config) {
        this.filterConfigObj = config;
    }

    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        KafkaLog kafkaLog = getKafkaLogFromCopies((HttpServletRequest) request, (HttpServletResponse) response, chain);
        kafkaService.sendKafkaMessage(kafkaLog);
    }

    private String getRequestQuery(HttpServletRequest request){

        Map<String, Object> parametersMap = new HashMap<>();
        Enumeration<String> requestParameters = request.getParameterNames();
        while (requestParameters.hasMoreElements()) {
            String name = requestParameters.nextElement();
            parametersMap.put(name, request.getParameter(name));
        }
        final Gson gson = new Gson();
        return gson.toJson(parametersMap);

    }

    private Map<String, Object> jsonToMap(String jsonString){
        return gson.fromJson(jsonString, new TypeToken<Map<String, Object>>() {}.getType());
    }

    private KafkaLog getKafkaLogFromCopies(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException {
        HttpServletResponseCopier responseCopier = new HttpServletResponseCopier((HttpServletResponse) response);
        HttpServletRequestCopier requestCopier = new HttpServletRequestCopier((HttpServletRequest) request);

        try {
            chain.doFilter(requestCopier, responseCopier);
            responseCopier.flushBuffer();
        } finally {
            byte[] responseCopy = responseCopier.getCopy();
            byte[] requestCopy = requestCopier.getCopy();
            if(request.getMethod().equals("GET")) {
                return new KafkaLog(
                        jsonToMap(getRequestQuery(requestCopier)),
                        jsonToMap(new String(responseCopy, response.getCharacterEncoding())),
                        request.getRequestURI(),
                        request.getAttribute("kafka-log-uuid").toString()
                );
            }

            return new KafkaLog(
                    jsonToMap(new String(requestCopy, response.getCharacterEncoding())
),
                    jsonToMap(new String(responseCopy, response.getCharacterEncoding())),
                    request.getRequestURI(),
                    request.getAttribute("kafka-log-uuid").toString()
            );

        }
    }
}