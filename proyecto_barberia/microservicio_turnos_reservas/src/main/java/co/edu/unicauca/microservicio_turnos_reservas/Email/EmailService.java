package co.edu.unicauca.microservicio_turnos_reservas.Email;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class EmailService {

    private final SendGrid sendGrid;

    public EmailService() {
        this.sendGrid = new SendGrid(System.getenv("SENDGRID_API_KEY"));
    }

    public void enviarCorreo(String destinatario, String asunto, String contenidoTexto) {
        System.out.println("Enviando correo a : " + destinatario);
        Email from = new Email("paula060704@gmail.com");
        Email to = new Email(destinatario);
        Content content = new Content("text/plain", contenidoTexto);
        Mail mail = new Mail(from, asunto, to, content);

        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());

            Response response = sendGrid.api(request);

            System.out.println("Código de respuesta al enviar email: " + response.getStatusCode());
        } catch (Exception ex) {
            throw new RuntimeException("Error enviando el correo: " + ex.getMessage(), ex);
        }
    }
}