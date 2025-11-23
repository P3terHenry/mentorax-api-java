package br.com.fiap.gs.mentorax.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.converter.HttpMessageNotReadableException;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ApiError> handleResponseStatusException(ResponseStatusException ex, HttpServletRequest request) {

        ApiError error = new ApiError(
                HttpStatus.valueOf(ex.getStatusCode().value()),
                ex.getReason() != null ? ex.getReason() : "Erro inesperado.",
                request.getRequestURI()
        );

        error.setException(ex.getClass().getSimpleName());
        error.setMethod(request.getMethod());

        return new ResponseEntity<>(error, ex.getStatusCode());
    }

    /**
     * Trata erros de desserialização JSON (como enums inválidos ou tipos incorretos)
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiError> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, WebRequest request) {
        Throwable rootCause = ex.getRootCause();

        String mensagem = (rootCause instanceof IllegalArgumentException)
                ? rootCause.getMessage()
                : "Erro ao processar a requisição. Verifique os dados enviados.";

        HttpServletRequest servletRequest = ((ServletWebRequest) request).getRequest();

        ApiError error = new ApiError(
                HttpStatus.BAD_REQUEST,
                mensagem,
                servletRequest.getRequestURI()
        );
        error.setException(rootCause != null ? rootCause.getClass().getSimpleName() : ex.getClass().getSimpleName());
        error.setMethod(servletRequest.getMethod());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    /**
     * Captura genérica para qualquer erro não tratado.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGenericException(Exception ex, HttpServletRequest request) {
        ex.printStackTrace();

        ApiError error = new ApiError(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Erro interno no servidor.",
                request.getRequestURI()
        );

        error.setException(ex.getClass().getSimpleName());
        error.setMethod(request.getMethod());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}
