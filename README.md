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
 
 We have application which process the user payment and return success or failure if payment is successful or failed respectively. Use case is simple enough to understand. Let's think of functional aspect only of our application. Upon receiving a payment request, it shall do following
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




