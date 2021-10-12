# kafka

This is to share/transfer knowledge of kafka

# What is stream and streaming
A stream is a continuous source of input with finite or infinite data. In today's world, we have tons of applications/sources which can act as a source of information. Data processing with a heavy load is challenging with synchronous approach. We can scale our application vertically or horizontally up to a point. Horizontal scaling may not be a perfect solution for real-time events with high loads with synchronous data consumers.

Our solution has to make sure no data loss in case of heavy load or back pressure. 

# Why we need streaming platform
The event streaming platform gives us a solution that decouples publishers from consumers. That implies a publisher will not care if the consumer is available to consume the messages or not.

Our streaming platform shall give us the following advantages (including but not limited to)
1) publisher/consumer decoupling
2) no message loss
3) runtime scalability
4) transaction management
5) high throughput and low latency

# Use case
1) Real-time data processing
2) Payment processing
3) Analytics
4) In event-driven application


# Kafka
  We have good numbers of event streaming platform which has their pros and cons. We will focus on kafka in this tutorial or excercise.
  
 
 # Example - payment application
 
We have an application that processes the user's payment and returns success or failure if payment is successful or failure otherwise as a response. The use case is simple enough to understand. Let me talk you through the functional requirements. Upon receiving a payment request, it shall do the following
 1) account and balance validation
 2) debiting of the account
 3) credit into creditor's account
 4) return success if all done, else failure if any step failed

step #1 to #4 shall be an atomic transaction.

This does not seem like much complex requirement at a first glance. We can have a microservice exposing an endpoint to receive a payment request, that will handle the request and process it.  
 
 ![](https://github.com/bhattharishbvp/kafka/blob/main/payment_processor_microservice.png)


# Scalibility issue in the above design
With the increase in the client's requests, our payment processor will not be able to handle the load. Most of the payments' requests will be lost and will not get a response back. Seems like we are still in the safe hand, thank god we had a good design in the first place :P

Now it's time to scale it, easy peasy. Add new nodes, scale horizontally. voila, that's it!

New imporved design is

New improved design is

![](https://github.com/bhattharishbvp/kafka/blob/main/payment_processor_microservice_scaling.png)

Yay, we have done the scaling and everything was working fine. Got an appraisal as well. Life was cool until one day.

# All of our payment processors are down

Today we have an outage and all of our servers are down. All of our payments clients are impacted. None of the clients is able to make the payment. All payments fall flat on the ground. Worst of all, we have no clue about the failed payment. 

Everyone is cursing the application designer. It was presented before the team and got approvals. C'mon, you gave an appraisal for this design:)

It's time to take a step back and think about what was wrong and how to avoid such technical issues in future.


# Streaming platform
Someone suggested streaming platform *KAFKA* in a retrospective meeting. Oh lord, it's a brilliant idea, why did we miss it before. Well, let's see what it will bring to the table.

1. In case of such outages, payment requests are still persisted in the steaming platform
2. If the streaming platform is down, the client will know beforehand. They can implement contingency plans beforehand in a planned manner.
3. Scaling consumer is easy now
4. If our streaming platform is a distributed system, we can spin our stream platform over multiple regions or zones
5. It will keep track of the last consumed message and our consumer will start from the next offset upon restart. It might give the flexibility to go back to past offset.

Let's see the new design
![](https://github.com/bhattharishbvp/kafka/blob/main/payment_processing_with_kafka.png)


# Transactional consumers and producers using spring for a message queue

# Design
![](https://github.com/bhattharishbvp/kafka/blob/main/system-architecture.png)

# Kafka Commands
kafka-console-producer.sh --bootstrap-server localhost:9092 --topic tutorials-test
kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic tutorials-test

or

#commands
 1. Producer - kafka-console-producer.bat --bootstrap-server localhost:9092 --topic messages
 2. Consumers - 
        kafka-console-consumer.bat --bootstrap-server=localhost:9092 --topic processed-message --isolation-level read_committed
        kafka-console-consumer.bat --bootstrap-server=localhost:9092 --topic abusive-message --isolation-level read_committed
        kafka-console-consumer.bat --bootstrap-server=localhost:9092 --topic dlq-message --isolation-level read_committed
    

#Scenarios:-
1) Happy Path
   - run producer using command #1
     > produce "Hi"
   - check consumers for processed-message
 
2) Abusive message
   - run producer using command #1
    > produce "Bad" or "Bad Message" (anything containing Bad word)
   - check consumers for abusive-message

3) Functional retry-able exception 
   - run producer using command #1
    > produce "retry"  (anything containing retry word)
   - check consumers for dlq-message

4) Scenario where the message contains abuse and should be retried multiple times
   - run producer using command #1
    > produce "bad retry"  (anything containing retry word)
   - check consumers for abusive-message with or without isolation level to see the transaction magic
     - kafka-console-consumer.bat --bootstrap-server=localhost:9092 --topic abusive-message
   - check consumers for dlq-message
