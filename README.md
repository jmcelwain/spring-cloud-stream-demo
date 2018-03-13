# Instructions

Start single node Kafka and Zookeper cluster with `docker-compose up`.

Listen to the topic defined in `DemoApplication.java`: `kafka-console-consumer --bootstrap-server localhost:9092 --topic processedDocs --from-beginning`.

Start the demo application.

Publish something to the `docs` Kafka topic: `echo <doc></doc> | kafka-console-producer --broker-list localhost:9092 --topic docs`.
