# Architecture Notes

- EmbeddedKafkaBroker from spring-kafka-test is used to run Kafka locally inside the JVM. This simplifies setup for testing.
- Avro schemas are stored in `src/main/avro` and generated via the avro-maven-plugin.
- UI is Swing-based and runs on the Event Dispatch Thread (EDT). The application is a Spring Boot application but primarily a desktop UI.

