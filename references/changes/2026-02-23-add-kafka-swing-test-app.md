# 2026-02-23 Add Kafka Swing Test App

Scope:
- Add a Spring Boot desktop application that embeds Kafka and provides a Swing UI to generate and send Avro-derived JSON messages.

Files added:
- pom.xml
- src/main/avro/sample.avsc
- src/main/resources/application.yml
- src/main/java/com/example/kafkaswing/KafkaSwingApplication.java
- src/main/java/com/example/kafkaswing/config/EmbeddedKafkaConfig.java
- src/main/java/com/example/kafkaswing/config/AppProperties.java
- src/main/java/com/example/kafkaswing/config/PublisherConfig.java
- src/main/java/com/example/kafkaswing/model/Sample.java
- src/main/java/com/example/kafkaswing/service/Publisher.java
- src/main/java/com/example/kafkaswing/ui/MainWindow.java
- src/main/java/com/example/kafkaswing/util/JsonUtil.java
- src/main/java/com/example/kafkaswing/util/BeanWrapperUtil.java

Rationale:
- Single Publisher class to enforce constraint. Avro schema used to create sample payloads with Instancio. EmbeddedKafkaBroker used to run Kafka locally without external setup.

