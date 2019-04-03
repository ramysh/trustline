# trustline

Uses Java 12

* Design

    https://github.com/ramysh/trustline/blob/master/trustline-design.jpg

* Clone


    git clone https://github.com/ramysh/trustline.git
    
* Compile
    
    
    /Users/rpurigella/test/trustline
    rpurigella-mbp-15:trustline rpurigella$ mvn clean install

* Run

    
    Server A (runs on 8080)
     
    rpurigella-mbp-15:trustline-payment-server rpurigella$ mvn spring-boot:run -Dspring.config.name=alice
    
    
    Server B (runs on 8081)
    
    rpurigella-mbp-15:trustline-payment-server rpurigella$ mvn spring-boot:run -Dspring.config.name=bob
    
    
    Client
    
    rpurigella-mbp-15:trustline-payment-client rpurigella$ mvn exec:java
    
    
* Integration tests

    
    
    Make sure both the servers are started,
    then run,
    rpurigella-mbp-15:trustline-payment-service-integration-tests rpurigella$ mvn verify -Pfailsafe
    
