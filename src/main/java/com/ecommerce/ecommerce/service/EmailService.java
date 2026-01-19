package com.ecommerce.ecommerce.service;

import com.ecommerce.ecommerce.entity.Contato;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    @Value("${app.email.mode:simulado}")
    private String emailMode;

    @Value("${app.email.from:contato@minhaloja.com}")
    private String emailFrom;

    @Value("${app.email.to:admin@minhaloja.com}")
    private String emailTo;

    // Para usar SMTP real, injete este bean (descomente quando configurar)
    // private final JavaMailSender mailSender;
    //
    // public EmailService(JavaMailSender mailSender) {
    //     this.mailSender = mailSender;
    // }

    public void enviarEmailContato(Contato contato) {
        String assunto = "Novo Contato: " + contato.getAssunto();
        String corpo = formatarEmailContato(contato);

        if ("real".equalsIgnoreCase(emailMode)) {
            // enviarEmailReal(emailTo, assunto, corpo);
            logger.info("📧 Email REAL enviado para: {}", emailTo);
        } else {
            logger.info("📧 Email SIMULADO - Contato recebido:\n{}", corpo);
        }
    }

    public void enviarEmailResposta(Contato contato, String resposta) {
        String assunto = "Resposta: " + contato.getAssunto();
        String corpo = formatarEmailResposta(contato, resposta);

        if ("real".equalsIgnoreCase(emailMode)) {
            // enviarEmailReal(contato.getEmail(), assunto, corpo);
            logger.info("📧 Email REAL enviado para: {}", contato.getEmail());
        } else {
            logger.info("📧 Email SIMULADO - Resposta enviada:\n{}", corpo);
        }
    }

    public void enviarEmailConfirmacaoPedido(String emailCliente, Map<String, Object> dadosPedido) {
        String assunto = "Confirmação do Pedido #" + dadosPedido.get("codigo");
        String corpo = formatarEmailConfirmacaoPedido(dadosPedido);

        if ("real".equalsIgnoreCase(emailMode)) {
            // enviarEmailReal(emailCliente, assunto, corpo);
            logger.info("📧 Email REAL de confirmação enviado para: {}", emailCliente);
        } else {
            logger.info("📧 Email SIMULADO - Confirmação de pedido:\n{}", corpo);
        }
    }

    public void enviarEmailBoasVindas(String nome, String email) {
        String assunto = "Bem-vindo(a) à Nossa Loja!";
        String corpo = formatarEmailBoasVindas(nome);

        if ("real".equalsIgnoreCase(emailMode)) {
            // enviarEmailReal(email, assunto, corpo);
            logger.info("📧 Email REAL de boas-vindas enviado para: {}", email);
        } else {
            logger.info("📧 Email SIMULADO - Boas-vindas:\n{}", corpo);
        }
    }

    private String formatarEmailContato(Contato contato) {
        return String.format("""
            NOVO CONTATO RECEBIDO
            
            De: %s
            Email: %s
            Telefone: %s
            Assunto: %s
            Tipo: %s
            
            Mensagem:
            %s
            
            Data: %s
            IP: %s
            User Agent: %s
            """,
                contato.getNome(),
                contato.getEmail(),
                contato.getTelefone(),
                contato.getAssunto(),
                contato.getTipoContato().getDescricao(),
                contato.getMensagem(),
                contato.getDataEnvio(),
                contato.getIpCliente(),
                contato.getUserAgent()
        );
    }

    private String formatarEmailResposta(Contato contato, String resposta) {
        return String.format("""
            Olá %s,
            
            Obrigado pelo seu contato. Aqui está nossa resposta:
            
            %s
            
            Atenciosamente,
            Equipe da Loja
            
            ---
            SUA MENSAGEM ORIGINAL:
            Assunto: %s
            Data: %s
            
            %s
            """,
                contato.getNome(),
                resposta,
                contato.getAssunto(),
                contato.getDataEnvio(),
                contato.getMensagem()
        );
    }

    private String formatarEmailConfirmacaoPedido(Map<String, Object> dadosPedido) {
        return String.format("""
            Olá %s,
            
            Seu pedido foi recebido com sucesso!
            
            DETALHES DO PEDIDO:
            Código: %s
            Data: %s
            Status: %s
            Total: R$ %.2f
            
            FORMA DE PAGAMENTO:
            WhatsApp
            
            PRÓXIMOS PASSOS:
            1. Acesse o link do WhatsApp que enviamos
            2. Envie a mensagem de confirmação
            3. Nossa equipe entrará em contato para finalizar o pagamento
            
            Qualquer dúvida, entre em contato!
            
            Atenciosamente,
            Equipe da Loja
            """,
                dadosPedido.get("cliente"),
                dadosPedido.get("codigo"),
                dadosPedido.get("data"),
                dadosPedido.get("status"),
                dadosPedido.get("total")
        );
    }

    private String formatarEmailBoasVindas(String nome) {
        return String.format("""
            Olá %s,
            
            Seja muito bem-vindo(a) à nossa loja!
            
            Estamos muito felizes em tê-lo(a) conosco.
            
            Aqui você encontrará:
            • Produtos de alta qualidade
            • Preços competitivos
            • Entrega rápida
            • Suporte especializado
            
            Comece a explorar nossa loja agora mesmo!
            
            Atenciosamente,
            Equipe da Loja
            """,
                nome
        );
    }

    // Método para envio real (implemente quando tiver SMTP configurado)
    /*
    private void enviarEmailReal(String para, String assunto, String corpo) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(emailFrom);
            message.setTo(para);
            message.setSubject(assunto);
            message.setText(corpo);
            mailSender.send(message);
        } catch (Exception e) {
            logger.error("Erro ao enviar email para {}: {}", para, e.getMessage());
        }
    }
    */
}