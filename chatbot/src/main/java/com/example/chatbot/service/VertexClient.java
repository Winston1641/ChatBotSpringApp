package com.example.chatbot.service;

// import org.apache.http.HttpEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class VertexClient {
    @Value("${vertex.project-id}")
    private String projectId;
    
    @Value("${vertex.service-account-key-path}")
    private String keyPath;

    @Value("${gemini.api-key}")
    private String apiKey;
    
    private final String VERTEX_ENDPOINT = "https://us-central1-aiplatform.googleapis.com/v1/projects/%s/locations/us-central1/publishers/google/models/gemini-1.5-flash:generateContent";
    private final String GEMINI_ENDPOINT = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=%s";
    // public String queryGemini(String prompt) {
    //     try {
    //         // Set up credentials
    //         GoogleCredentials credentials = GoogleCredentials
    //             .fromStream(new FileInputStream(keyPath))
    //             .createScoped("https://www.googleapis.com/auth/cloud-platform");
            
    //         // Create HTTP request factory
    //         HttpRequestFactory requestFactory = new NetHttpTransport().createRequestFactory();
            
    //         // Build JSON payload
    //         String jsonPayload = """
    //         {
    //           "contents": [{"role": "user", "parts": [{"text": "%s"}]}],
    //           "generationConfig": {"temperature": 0.7, "maxOutputTokens": 500}
    //         }
    //         """.formatted(prompt);
            
    //         // Build HTTP request
    //         String endpoint = String.format(VERTEX_ENDPOINT, projectId);
    //         HttpRequest request = requestFactory.buildPostRequest(
    //             new GenericUrl(endpoint),
    //             new ByteArrayContent("application/json", jsonPayload.getBytes())
    //         );
            
    //         // Set authorization header
    //         credentials.refreshIfExpired();
    //         request.getHeaders().setAuthorization("Bearer " + credentials.getAccessToken().getTokenValue());
            
    //         // Execute request and return response
    //         return request.execute().parseAsString();
            
    //     } catch (IOException e) {
    //         return "Error: " + e.getMessage();
    //     }
    // }

    public String queryGemini(String prompt) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            String body = """
            {
              "contents": [{"role": "user", "parts": [{"text": "%s"}]}]
            }
            """.formatted(prompt.replace("\"", "\\\""));

            HttpEntity<String> entity = new HttpEntity<>(body, headers);
            String url = String.format(GEMINI_ENDPOINT, apiKey);

            ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);
            return response.getBody();
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
}