# yukontest
Service monitor

"Design and implement in (java) a service monitoring class. 
This monitor will be used to monitor the status of multiple services.
A service is defined as a host/port combination.
To check if a service is up, the monitor will establish a TCP connection to the host on the specified port.
If a connection is established, the service is up, if the connection is refused, the service is not up.

The monitor will allow callers to register interest in a service, and a polling frequency. The callers will be notified when the service goes up and down.

The monitor should detect multiple callers registering interest in the same service, and should not poll any service more frequently than once a second.

At any time a service can be configured with a planned service outage; however, not all services need to specify an outage. The service outage will specify a start and end time for which no notifications for that service will be delivered.

The monitor should allow callers to define a grace time.  If a service is not responding, the monitor will wait for the grace time to expire before notifying any clients.  If the service goes back on line during this grace time, no notification will be sent.  If the grace time is less than the polling frequency, the monitor should schedule extra checks of the service.

The code should include a set of unit tests.

The code should be delivered via GitHub."

