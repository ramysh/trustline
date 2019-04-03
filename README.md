# trustline

<b>Uses Java 12</b>

## Design diagram

   Please go through the diagram here
   
   https://github.com/ramysh/trustline/blob/master/trustline-design-diagram.jpg

## Clone


    git clone https://github.com/ramysh/trustline.git
    
## Compile
    
    
    /Users/rpurigella/test/trustline
    rpurigella-mbp-15:trustline rpurigella$ mvn clean install

## Run

   #####Server A (runs on 8080)
     
    rpurigella-mbp-15:trustline-payment-server rpurigella$ mvn spring-boot:run -Dspring.config.name=alice
    
    
   #####Server B (runs on 8081)
    
    rpurigella-mbp-15:trustline-payment-server rpurigella$ mvn spring-boot:run -Dspring.config.name=bob
    
    
   #####Client
    
    rpurigella-mbp-15:trustline-payment-client rpurigella$ mvn exec:java
    
    The console app will guide you through the input process
    
    
## Integration tests

    
    
    Make sure both the servers are started, then run,
    
    rpurigella-mbp-15:trustline-payment-service-integration-tests rpurigella$ mvn verify -Pfailsafe
    

## Additional Info

##### Ports
* The server ports for alice and bob are in

https://github.com/ramysh/trustline/blob/master/trustline-payment-server/src/main/resources/alice.properties

https://github.com/ramysh/trustline/blob/master/trustline-payment-server/src/main/resources/bob.properties


* The user service is currently hardcoded, so of you change the ports they run on, you have to change them here as well

https://github.com/ramysh/trustline/blob/master/trustline-user-service-impl/src/main/resources/user.service.properties

##### Integration tests

* Concurrently does the following 
  * Sends a series of 300 TransferRequest's over 30 threads from Server A to B
  * Sends the same requests over 30 threads from Server B to A, but doesnt send the last one.
 
* The final balance at each server is verified to correctly reflect the TransferRequest's sent to both servers

##### User Service (user lookup service)

* Currently the user is hard coded and the implementation is just embedded in the client. 
* But the idea is that it can be its own stand alone service and the payment servers will register when they start up.

##### Unit tests
* trustline-payment-service-impl has good unit test coverage.
* Improvements - The controller in trustline-payment-service-impl, trustline-http need unit tests.