# 2026-02-24 Add env selector and topic override

Scope

- Added an environment dropdown and a topic override text field to the Swing UI so users can pick an environment (the keys under a publisher's `topics` map) and optionally edit the resulting topic before sending.

Files modified

- `src/main/java/com/example/kafkaswing/ui/MainWindow.java` - added `envSelector` (JComboBox) populated with keys from the selected publisher's `topics` map and `topicField` (JTextField) which auto-fills from the selected env and is editable for overrides. `send()` now uses the text in `topicField` instead of a fixed selection.
- `references/ReadMe.md` - updated to mention the env selector and topic override field.

Rationale

- Gives users an easy way to switch between environment-specific topics (e.g., local/dev/test) and allows manual override if needed.

Notes

- Ensure Avro-generated Java classes are present (run `mvn generate-sources`) if you want the UI to resolve and generate Avro-structured payloads.

