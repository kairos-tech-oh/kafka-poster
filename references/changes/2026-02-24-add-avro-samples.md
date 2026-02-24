# 2026-02-24 Add Avro sample schemas

Scope

- Added several Avro schema sample files under `src/main/avro` to provide richer test data and show nested/cross-referenced schemas for the Kafka test app.

Files added

- `src/main/avro/addressData.avsc` - Reusable `AddressData` record.
- `src/main/avro/party.avsc` - `Party` record referencing `AddressData` and a map of accounts.
- `src/main/avro/transfer.avsc` - `Transfer` record referencing `Party`, including nested `Item` record and enum `TransferStatus`.
- `src/main/avro/payment.avsc` - `Payment` record referencing `Transfer`, demonstrates unions, fixed, and decimal logicalType.

Rationale

- These schemas increase coverage for the UI publisher selection feature. They demonstrate nested records, enums, arrays, maps, unions, logical types, and fixed/bytes usage. This lets the app use Instancio to create randomized instances of complex Avro types and supports testing of path-based id replacement (e.g., `transfer.id` or `transfer.from.id`).

Notes

- Follow-up: If code generation is used (Maven Avro plugin), regenerate Java sources so the `generated-sources/avro` directory includes these classes. This commit only adds the Avro schema sources.

