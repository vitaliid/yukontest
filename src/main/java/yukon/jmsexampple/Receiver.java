package yukon.jmsexampple;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

/**
 * Created by vitalii on 01/02/2017.
 */
@Service
public class Receiver {

    @JmsListener(destination = "mailbox", containerFactory = "myFactory")
    public Email receiveMessage(Email email) {
        System.out.println("Received <" + email + ">");
        return new Email("123", "2345");
    }


}
