package yukon;

import yukon.service.ServiceDTO;
import yukon.service.ServiceStatus;

/**
 * Created by vitalii on 01/02/2017.
 */
public interface Caller {

    default void receiveNotification(ServiceDTO service, ServiceStatus status) {
        System.out.println("ServiceDTO " + service.toString() + " status: " + status);
    }
}
