# Kafka Swing Tester

This project provides a lightweight Spring Boot application that runs an embedded Kafka broker and a Swing UI to generate and publish sample Avro-based JSON payloads.

Run:
- mvn clean package
- mvn spring-boot:run

Configuration:
- See `src/main/resources/application.yml` for publisher mappings.

Notes:
- Only one Publisher class is implemented; UI exposes multiple Avro options that map to configured topics.
- After generating a payload you can edit the JSON in the UI before sending.
- When you change the selected publisher in the UI the JSON editor is refreshed with a sample payload for that publisher (falls back to a simple `Sample` object if the Avro-generated class isn't available).
- Use the "env" dropdown to pick the environment key from the publisher's `topics` map (for example `local`, `dev`, `test`). The topic field next to it will auto-fill with the associated topic value and can be edited for overrides.

Avro Schemas:
- The project includes Avro schemas under `src/main/avro`. New sample schemas were added on 2026-02-24 to demonstrate nested and cross-referenced types:
  - `addressData.avsc` (com.example.kafkaswing.avro.common.AddressData)
  - `party.avsc` (com.example.kafkaswing.avro.common.Party)
  - `transfer.avsc` (com.example.kafkaswing.avro.transfer.Transfer)
  - `payment.avsc` (com.example.kafkaswing.avro.payment.Payment)

If you use the Maven Avro plugin, run `mvn generate-sources` or a full build to regenerate Java classes into `target/generated-sources/avro/`.
