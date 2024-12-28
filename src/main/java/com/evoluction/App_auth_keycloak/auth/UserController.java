package com.evoluction.App_auth_keycloak.auth;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.core.JsonProcessingException;

@RequestMapping("/user")
@RestController
public class UserController {

    @PostMapping("/")
    public ResponseEntity<String> criarUsuario(@RequestBody User user) {
        // Obtenção do token
        String token = obterToken(user);

        // Se não obteve o token, retorna erro
        if (token == null) {
            return ResponseEntity.status(400).body("Erro ao obter o token");
        }

        // Configuração dos dados para a criação do usuário
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + token);

        // Criação do corpo da requisição para criação do usuário
        UserRequestBody userRequestBody = new UserRequestBody(
                user.username(),
                user.email(),
                user.enabled(),
                user.firstName(),
                user.lastName(),
                user.credentials()
        );

        HttpEntity<UserRequestBody> entity = new HttpEntity<>(userRequestBody, headers);

        RestTemplate rt = new RestTemplate();
        try {
            ResponseEntity<String> result = rt.exchange(
                    "http://localhost:8080/admin/realms/Alibou/users", 
                    HttpMethod.POST, entity, String.class);

            return result;
        } catch (HttpClientErrorException e) {
            System.out.println("Erro ao tentar criar o usuário: " + e.getStatusCode());
            System.out.println("Mensagem de erro: " + e.getResponseBodyAsString());
            return ResponseEntity.status(500).body("Erro ao tentar criar o usuário: " + e.getResponseBodyAsString());
        }
    }

    private String obterToken(User user) {
        HttpHeaders headers = new HttpHeaders();
        RestTemplate rt = new RestTemplate();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
    
        // Utilizando client_credentials
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("client_id", "alibou-resti-api");  // Definido como 'alibou-resti-api'
        formData.add("client_secret", "xWXT0qjAwCae7roBuWbu6yq2yso4zzrE");  // Definido como seu client_secret
        formData.add("grant_type", "client_credentials"); // Tipo de grant utilizado
    
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(formData, headers);
    
        try {
            // Realizando a requisição para obter o token
            ResponseEntity<String> result = rt.exchange(
                    "http://localhost:8080/realms/Alibou/protocol/openid-connect/token", 
                    HttpMethod.POST, entity, String.class);
    
            if (result.getStatusCode().is2xxSuccessful()) {
                String responseBody = result.getBody();
                ObjectMapper objectMapper = new ObjectMapper();
                try {
                    // Parsing do token JSON
                    JsonNode jsonNode = objectMapper.readTree(responseBody);
                    return jsonNode.get("access_token").asText(); // Retorna o token de acesso
                } catch (JsonProcessingException e) {
                    // Erro no processamento do JSON
                    System.out.println("Erro ao processar o JSON: " + e.getMessage());
                    return null;
                }
            } else {
                // Caso o status não seja 2xx (sucesso)
                System.out.println("Erro ao obter o token: " + result.getStatusCode());
                System.out.println("Mensagem de erro: " + result.getBody());
                return null;
            }
        } catch (HttpClientErrorException e) {
            // Exibindo detalhes do erro na requisição HTTP
            System.out.println("Erro: " + e.getStatusCode());
            System.out.println("Corpo da resposta de erro: " + e.getResponseBodyAsString());
            return null;
        }
    }
    

    // Objeto para mapear o corpo da requisição para a criação do usuário
    public record UserRequestBody(String username, String email, boolean enabled,
                                  String firstName, String lastName, Object credentials) {}

    // Atualizado para corresponder à estrutura JSON de entrada
    public record User(String password, String clientId, String username, String email, boolean enabled,
                       String firstName, String lastName, Object credentials) {}
}
