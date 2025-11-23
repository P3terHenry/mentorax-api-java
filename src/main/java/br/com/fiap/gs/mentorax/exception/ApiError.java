package br.com.fiap.gs.mentorax.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Setter
public class ApiError {

    private LocalDateTime timestamp; // Quando o erro ocorreu
    private int status;              // Código HTTP (ex: 404, 500)
    private String error;            // Razão (ex: "Not Found", "Bad Request")
    private String message;          // Mensagem amigável
    private String path;             // URI da requisição
    private String exception;        // Classe da exceção (ex: "DataIntegrityViolationException")
    private String method;           // Método HTTP (GET, POST, etc)
    private Map<String, Object> details; // Informações extras contextuais

    public ApiError(HttpStatus status, String message, String path) {
        this.timestamp = LocalDateTime.now();
        this.status = status.value();
        this.error = status.getReasonPhrase();
        this.message = message;
        this.path = path;
    }
}
