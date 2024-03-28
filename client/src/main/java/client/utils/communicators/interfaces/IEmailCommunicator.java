package client.utils.communicators.interfaces;

import commons.EmailRequest;

public interface IEmailCommunicator {

    EmailRequest sendEmail(EmailRequest emailRequest);

    EmailRequest getAll();

    String getOrigin();

}
