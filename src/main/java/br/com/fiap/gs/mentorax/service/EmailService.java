package br.com.fiap.gs.mentorax.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Paths;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${app.mail.from}")
    private String from;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void enviarCodigoRecuperacao(String email, String nome, String codigo) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");

            // Lê o template HTML do diretório resources
            String conteudoHtml = Files.readString(Paths.get("src/main/resources/templates/email/porteira_recuperacao_senha.html"));

            // Substitui as variáveis dinâmicas
            conteudoHtml = conteudoHtml
                    .replace("[[Nome]]", nome)
                    .replace("[[Codigo]]", codigo);

            helper.setFrom(from);
            helper.setTo(email);
            helper.setSubject("🔐 Recuperação de Conta - MentoraX");
            helper.setText(conteudoHtml, true); // true = HTML

            mailSender.send(mimeMessage);

        } catch (MessagingException e) {
            throw new RuntimeException("Erro ao montar a mensagem de e-mail: " + e.getMessage(), e);
        } catch (MailException e) {
            throw new RuntimeException("Erro ao enviar e-mail: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("Erro inesperado ao processar template de e-mail: " + e.getMessage(), e);
        }
    }
}