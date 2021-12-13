package com.example.whitelistverification.backend.smtp

import javax.mail.internet.MimeMessage
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Component

@Component
class EmailServiceImpl(val emailSender: JavaMailSender) {

    fun sendVerificationMessage(to: String, subject: String, text: String) {
        var message = emailSender.createMimeMessage()
        var helper = MimeMessageHelper(message, "utf-8")
        helper.setFrom("\"Minecraft Whitelist\" <innominecraft.whitelist@gmail.com>")
        helper.setTo(to)
        helper.setSubject(subject)
        helper.setText(text, true)
        emailSender.send(message)
    }

}
