package com.smxworld.ecommerce.notification.internal.application;

import com.smxworld.ecommerce.order.OrderConfirmedEvent;
import com.smxworld.ecommerce.payment.PaymentFailedEvent;
import com.smxworld.ecommerce.shipment.OrderShippedEvent;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
class NotificationService {

    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);
    private static final String FROM = "noreply@smxecommerce.local";

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    NotificationService(JavaMailSender mailSender, TemplateEngine templateEngine) {
        this.mailSender     = mailSender;
        this.templateEngine = templateEngine;
    }

    @ApplicationModuleListener
    void on(OrderConfirmedEvent event) {
        Context ctx = new Context();
        ctx.setVariable("orderId", event.orderId());
        ctx.setVariable("totalAmount", event.totalAmount());
        sendHtml(event.userId(), "Il tuo ordine è confermato", "mail/order-confirmed", ctx);
    }

    @ApplicationModuleListener
    void on(PaymentFailedEvent event) {
        Context ctx = new Context();
        ctx.setVariable("orderId", event.orderId());
        ctx.setVariable("reason", event.reason());
        sendHtml(/* userId not in PaymentFailedEvent — use orderId as fallback address key */
                event.orderId().toString(), "Problema con il pagamento", "mail/payment-failed", ctx);
    }

    @ApplicationModuleListener
    void on(OrderShippedEvent event) {
        Context ctx = new Context();
        ctx.setVariable("orderId", event.orderId());
        ctx.setVariable("trackingNumber", event.trackingNumber());
        sendHtml(event.userId(), "Il tuo ordine è in viaggio", "mail/order-shipped", ctx);
    }

    // ─── Private helpers ──────────────────────────────────────────────────────

    private void sendHtml(String to, String subject, String template, Context ctx) {
        try {
            String body = templateEngine.process(template, ctx);
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, false, "UTF-8");
            helper.setFrom(FROM);
            helper.setTo(to + "@smxecommerce.local");
            helper.setSubject(subject);
            helper.setText(body, true);
            mailSender.send(message);
            log.info("Email sent to {} — subject: {}", to, subject);
        } catch (MessagingException e) {
            log.error("Failed to send email to {} — {}", to, e.getMessage());
        }
    }
}
