# 2026-02-24 Fix MainWindow template refresh

Scope

- Fixed a bug in the Swing UI where switching the selected publisher did not refresh the JSON payload shown in the editable text area.

Files modified

- `src/main/java/com/example/kafkaswing/ui/MainWindow.java` - updated to resolve the Java class for the selected publisher and generate a fresh sample JSON payload using Instancio whenever the publisher selection changes.
- `references/ReadMe.md` - updated to mention the behavior.

Rationale

- Previously the UI would continue to show the same payload when the user selected a different publisher. The fix resolves the Avro/model class for the selected publisher and regenerates a sample instance (falling back to `Sample` if the Avro class isn't available), then updates the JSON editor.

Notes

- If new Avro types are added, run `mvn generate-sources` to create the Java classes so they can be automatically resolved by the UI.

