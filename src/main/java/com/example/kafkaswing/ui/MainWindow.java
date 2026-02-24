package com.example.kafkaswing.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import com.example.kafkaswing.config.AppProperties;
import com.example.kafkaswing.config.PublisherConfig;
import com.example.kafkaswing.service.Publisher;
import com.example.kafkaswing.util.JsonUtil;
import com.example.kafkaswing.model.Sample;
import org.instancio.Instancio;
import org.springframework.stereotype.Component;

@Component
public class MainWindow {
    private final AppProperties appProperties;
    private final Publisher publisher;

    private JFrame frame;
    private JComboBox<String> avroSelector;
    private JComboBox<String> envSelector;
    private JTextArea jsonArea;
    private JTextField topicField;
    private Class<?> currentAvroClass;

    public MainWindow(AppProperties appProperties, Publisher publisher) {
        this.appProperties = appProperties;
        this.publisher = publisher;
        SwingUtilities.invokeLater(this::createAndShow);
    }

    private void createAndShow() {
        frame = new JFrame("Kafka Swing Tester");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(800, 600));

        JPanel top = new JPanel();

        avroSelector = new JComboBox<>(appProperties.getPublishers().keySet().toArray(new String[0]));
        avroSelector.addActionListener(e -> loadTemplate());
        top.add(avroSelector);

        envSelector = new JComboBox<>();
        envSelector.addActionListener(e -> onEnvSelected());
        top.add(envSelector);

        topicField = new JTextField(30);
        top.add(topicField);

        JButton genBtn = new JButton("Generate Sample");
        genBtn.addActionListener(e -> generateSample());
        top.add(genBtn);

        JButton sendBtn = new JButton("Send");
        sendBtn.addActionListener(e -> send());
        top.add(sendBtn);

        frame.add(top, BorderLayout.NORTH);

        jsonArea = new JTextArea();
        frame.add(new JScrollPane(jsonArea), BorderLayout.CENTER);

        frame.pack();
        frame.setVisible(true);

        loadTemplate();
    }

    @SuppressWarnings("unchecked")
    private void loadTemplate() {
        String avroName = (String) avroSelector.getSelectedItem();
        if (avroName == null) return;
        PublisherConfig cfg = appProperties.getPublishers().get(avroName);
        envSelector.removeAllItems();
        for (String key : cfg.getTopics().keySet()) {
            envSelector.addItem(key);
        }
        if (envSelector.getItemCount() > 0) {
            envSelector.setSelectedIndex(0);
            String firstEnv = (String) envSelector.getSelectedItem();
            String topicVal = cfg.getTopics().get(firstEnv);
            topicField.setText(topicVal != null ? topicVal : "");
        } else {
            topicField.setText("");
        }
        currentAvroClass = resolveClassForPublisher(avroName);
        Object sampleObj;
        try {
            sampleObj = Instancio.create((Class) currentAvroClass);
        } catch (Exception ex) {
            sampleObj = Instancio.create(Sample.class);
            currentAvroClass = Sample.class;
        }
        String json = JsonUtil.toJson(sampleObj);
        jsonArea.setText(json);
    }

    @SuppressWarnings("unchecked")
    private void generateSample() {
        if (currentAvroClass == null) {
            String avroName = (String) avroSelector.getSelectedItem();
            currentAvroClass = resolveClassForPublisher(avroName);
        }
        Object sampleObj;
        try {
            sampleObj = Instancio.create((Class) currentAvroClass);
        } catch (Exception ex) {
            sampleObj = Instancio.create(Sample.class);
            currentAvroClass = Sample.class;
        }
        jsonArea.setText(JsonUtil.toJson(sampleObj));
    }

    @SuppressWarnings("unchecked")
    private void send() {
        String env = (String) envSelector.getSelectedItem();
        String topic = topicField.getText();
        if (topic == null || topic.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Select or enter a topic first");
            return;
        }
        String payload = jsonArea.getText();
        try {
            Class<?> validateClass = currentAvroClass == null ? Sample.class : currentAvroClass;
            JsonUtil.fromJson(payload, (Class) validateClass);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame, "Invalid JSON: " + ex.getMessage());
            return;
        }
        // determine bootstrap servers for the selected env; default to embedded if not configured
        String bootstrap = appProperties.getBrokers().getOrDefault(env, "embedded");
        publisher.sendToBootstrap(bootstrap, topic, payload);
        JOptionPane.showMessageDialog(frame, "Message sent to " + topic);
    }

    private void onEnvSelected() {
        String avroName = (String) avroSelector.getSelectedItem();
        if (avroName == null) return;
        PublisherConfig cfg = appProperties.getPublishers().get(avroName);
        String env = (String) envSelector.getSelectedItem();
        if (env == null) return;
        String topicVal = cfg.getTopics().get(env);
        topicField.setText(topicVal != null ? topicVal : "");
    }

    private Class<?> resolveClassForPublisher(String avroName) {
        if (avroName == null || avroName.isEmpty()) return Sample.class;
        String pascal = toPascal(avroName);
        String[] candidates = new String[] {
            "com.example.kafkaswing.model." + pascal,
            "com.example.kafkaswing.avro.common." + pascal,
            "com.example.kafkaswing.avro." + avroName + "." + pascal,
            "com.example.kafkaswing.avro." + pascal,
            "com.example.kafkaswing." + pascal
        };
        for (String fq : candidates) {
            try {
                return Class.forName(fq);
            } catch (ClassNotFoundException e) {
                // try next
            }
        }
        return Sample.class;
    }

    private String toPascal(String name) {
        if (name == null || name.isEmpty()) return name;
        if (name.contains("-") || name.contains("_")) {
            String[] parts = name.split("[-_]");
            StringBuilder sb = new StringBuilder();
            for (String p : parts) {
                if (p.isEmpty()) continue;
                sb.append(Character.toUpperCase(p.charAt(0)));
                if (p.length() > 1) sb.append(p.substring(1));
            }
            return sb.toString();
        }
        return Character.toUpperCase(name.charAt(0)) + name.substring(1);
    }
}
