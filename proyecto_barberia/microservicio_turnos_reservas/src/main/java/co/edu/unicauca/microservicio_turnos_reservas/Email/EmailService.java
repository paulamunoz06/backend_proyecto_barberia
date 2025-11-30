package co.edu.unicauca.microservicio_turnos_reservas.Email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class EmailService {

    @Autowired
    private WebClient sendGridClient;

    public Mono<String> enviarCorreo(String para, String asunto, String contenido) {

        String body = """
        {
          "personalizations": [{
            "to": [{"email": "%s"}],
            "subject": "%s"
          }],
          "from": {"email": "tu_correo@tudominio.com"},
          "content": [{
            "type": "text/plain",
            "value": "%s"
          }]
        }
        """.formatted(para, asunto, contenido);

        return sendGridClient.post()
                .uri("/mail/send")
                .bodyValue(body)
                .retrieve()
                .bodyToMono(String.class)
                .onErrorResume(e -> Mono.just("Error enviando correo: " + e.getMessage()));
    }
}