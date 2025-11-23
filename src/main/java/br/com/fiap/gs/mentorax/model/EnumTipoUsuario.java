package br.com.fiap.gs.mentorax.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum EnumTipoUsuario {

    ADMIN("Administrador"),
    MENTOR("Mentor"),
    MENTORADO("Mentorado");

    private final String descricao;

    EnumTipoUsuario(String descricao) {
        this.descricao = descricao;
    }

    @JsonCreator
    public static EnumTipoUsuario fromString(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("O campo tipoUsuario não pode ser nulo ou vazio.");
        }

        value = value.trim().toUpperCase();

        return switch (value) {
            case "A", "ADMIN", "ADMINISTRADOR" -> ADMIN;
            case "M", "MENTOR" -> MENTOR;
            case "D", "MENTORADO" -> MENTORADO;
            default -> throw new IllegalArgumentException(
                    "Valor inválido para tipoUsuario. Valores permitidos: ADMIN (A), MENTOR (M), MENTORADO (D)."
            );
        };
    }

    @JsonValue
    public String toValue() {
        return this.name();
    }
}