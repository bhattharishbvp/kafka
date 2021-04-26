# kafka

This is to share/transfer knowledge of kafka

# What is stream and streaming
A stream is continuous source of input with finite or infinite data. In today's world, we have tons of application/source which can act as a source of information. Data processing with heavy load is challenging with synchronous approach. We can scale our application vertically or horizontally upto a point. Scaling may not be a perfect design for real time event with high load with sychronous data consumers.

Our solution has to make sure no data loss in case of heavy load or back pressure. 

# Why we need streaming platform
Event streaming platform gives us a solution which decouples publisher and consumers. Which means publisher will not care if consumer is available to consumer the messages.

our streaming platform shall give us following advantage (including but not limited to)
1) publisher/consumer decoupling
2) no message loss
3) runtime scalability
4) transaction management
5) high throughput and low latency

# Use case
1) Real time data processing
2) Payment processing
3) Analytics
4) In event driven application


# Kafka
  We have good numbers of event streaming platform which has their pros and cons. We will focus on kafka in this tutorial or excercise.
  
 
 # Example - payment application
 
 We have application which process the user payment and return success or failure if payment is successful or failed respectively. Use case is tutorials.simple enough to understand. Let's think of functional aspect only of our application. Upon receiving a payment request, it shall do following
 1) account and balance validation
 2) debiting of account
 3) credit into creditor's account
 4) return success if all done, else failure if any step failed

step #1 to #4 shall be an atomic transaction.

This does not seem much complex requirement at first glance. We can have a microservice which shall expose a endpoint to receive payment request, which will handle the request and process it.
 
 ![](https://github.com/bhattharishbvp/kafka/blob/main/payment_processor_microservice.png)


# Scalibility issue in above design
With increase in client, our payment processor is not able to handle the load. Most of the payment request is lost and did not get the response back. Seems like we are still in the safe hand, thank god we had a good design in the first place :P

Now it's time to make it scalable, easy peasy. Add new nodes. voila, that's it!

New imporved design is

![](https://github.com/bhattharishbvp/kafka/blob/main/payment_processor_microservice_scaling.png)

Yay, we have done the scaling and everything was working fine. Got appraisal as well. Life was cool until one day.

# All of our payment processors are down

Today we have an outage all of our servers are down. All of our payment client is impacted due to this. None of the client is able to make the payment. All payment fall flat on the ground. Worst of all, we have no clue about the failed payment. 

All eyes were on the application designer, it was presented before the team and got approvals. C'mon, you gave appraisal for this :)

Time to take a step back and think what was wrong and how to avoid such technical issues in future.


# Streaming platform
Someone suggested streaming platform *KAFKA* in retropestive meeting. Oh gosh, it's brilliant idea, why did we miss it before. Well let's see what it will bring on the table.

1. In case of such outage, payment request are still presisted in steaming platform
2. If streaming platform is down, client will know before hand. They can implement contingency plan before hand in palnned manner.
3. Scaling consumer is easy now
4. If our streaming platform support distributed system or over cloud, we can span our application over multiple region or zones
5. It will keep track of last consumed message and our consumer will start from next offset upon restart. It might gives flexibility to go back to past offset.

Let's see the new design
![](https://github.com/bhattharishbvp/kafka/blob/main/payment_processing_with_kafka.png)


# Transactional consumers and producer using spring

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

3) Functional retryable exception 
  - run producer using command #1
   > produce "retry"  (anything containing retry word)
  - check consumers for dlq-message

4) Scenario where message contains abuse and should be retried multiple times
  - run producer using command #1
   > produce "bad retry"  (anything containing retry word)
  - check consumers for abusive-message with or without isolation level to see the transaction magic
    - kafka-console-consumer.bat --bootstrap-server=localhost:9092 --topic abusive-message
  - check consumers for dlq-message
