package br.com.fiap.gs.mentorax.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Date;

@Getter
@AllArgsConstructor
public class JWTLoginResponseDTO {

    private String token;
    private String subject;
    private Date IssuedAt;
    private Date expiration;
}
