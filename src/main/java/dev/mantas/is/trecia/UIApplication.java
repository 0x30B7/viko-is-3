package dev.mantas.is.trecia;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Base64;
import java.util.Optional;

public class UIApplication extends JFrame {

    public JTextField txN;
    public JTextField txPhi;
    public JTextField txE;
    public JTextField txD;

    public JTextArea txInput;
    public JTextArea txOutput;

    public JButton btnSave;
    public JButton btnLoad;
    public Path selectedPath;

    public UIApplication(RSAProcessor rsaProcessor) {
        super("Trecia (RSA)");

        setSize(800, 635);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        add(panel);

        panel.setLayout(null);

        JLabel lbP = new JLabel("<html><i>p</i> value</html>");
        lbP.setFont(lbP.getFont().deriveFont(14F));
        lbP.setBounds(10, 5, 100, 15);
        panel.add(lbP);

        JTextField txP = new JTextField();
        txP.setBounds(10, 25, 110, 22);
        panel.add(txP);

        JLabel lbQ = new JLabel("<html><i>q</i> value</html>");
        lbQ.setFont(lbQ.getFont().deriveFont(14F));
        lbQ.setBounds(130, 5, 110, 15);
        panel.add(lbQ);

        JTextField txQ = new JTextField();
        txQ.setBounds(130, 25, 110, 22);
        panel.add(txQ);

        JButton btnGeneratePQ = new JButton("⚡");
        btnGeneratePQ.setToolTipText("Generate valid P and Q values");
        btnGeneratePQ.setBounds(245, 25, 40, 22);
        panel.add(btnGeneratePQ);

        btnGeneratePQ.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RSAProcessor.PQPair pqPair = rsaProcessor.processPQ("", "").get();
                txP.setText(pqPair.p());
                txQ.setText(pqPair.q());
                txN.setText(rsaProcessor.getN());
                txPhi.setText(rsaProcessor.getPhi());
                txE.setText(rsaProcessor.getPublicKeyComponent());
                txD.setText(rsaProcessor.getPrivateKeyComponent());
            }
        });

        FocusListener setupListener = new FocusListener() {
            public void focusGained(FocusEvent e) {}
            public void focusLost(FocusEvent e) {
                if (!txP.getText().isEmpty() && !txQ.getText().isEmpty()) {
                    Optional<RSAProcessor.PQPair> pqPair = rsaProcessor.processPQ(txP.getText(), txQ.getText());
                    if (!pqPair.isPresent()) {
                        openDialog("Invalid P and Q values");
                        return;
                    }

                    txP.setText(pqPair.get().p());
                    txQ.setText(pqPair.get().q());

                    txN.setText(rsaProcessor.getN());
                    txPhi.setText(rsaProcessor.getPhi());
                    txE.setText(rsaProcessor.getPublicKeyComponent());
                    txD.setText(rsaProcessor.getPrivateKeyComponent());
                }
            }
        };

        txP.addFocusListener(setupListener);
        txQ.addFocusListener(setupListener);

        // ======================================================================================================== //

        JLabel lbN = new JLabel("<html><i>n</i> value</html>");
        lbN.setFont(lbN.getFont().deriveFont(14F));
        lbN.setBounds(350, 5, 100, 15);
        panel.add(lbN);

        txN = new JTextField();
//        txN.setEditable(false);
        txN.setBounds(350, 25, 100, 22);
        panel.add(txN);

        txN.addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent e) {}
            public void focusLost(FocusEvent e) {
                if (txN.getText().isEmpty()) return;
                rsaProcessor.setN(txN.getText()).ifPresent(o -> {
                    txP.setText("");
                    txQ.setText("");
                    txPhi.setText("");
                    txN.setText(o);
                });
            }
        });

        JLabel lbPhi = new JLabel("<html><i>phi(n)</i> value</html>");
        lbPhi.setFont(lbPhi.getFont().deriveFont(14F));
        lbPhi.setBounds(455, 5, 100, 15);
        panel.add(lbPhi);

        txPhi = new JTextField();
//        txPhi.setEditable(false);
        txPhi.setBounds(455, 25, 100, 22);
        panel.add(txPhi);

        JLabel lbE = new JLabel("<html><i>e</i> value</html>");
        lbE.setFont(lbE.getFont().deriveFont(14F));
        lbE.setBounds(565, 5, 100, 15);
        panel.add(lbE);

        txE = new JTextField();
//        txK.setEditable(false);
        txE.setBounds(565, 25, 100, 22);
        panel.add(txE);
        txE.addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent e) {}
            public void focusLost(FocusEvent e) {
                if (txE.getText().isEmpty()) return;
                rsaProcessor.setPrivateKeyComponent(txE.getText()).ifPresent(o -> {
                    txP.setText("");
                    txQ.setText("");
                    txPhi.setText("");
                    txE.setText(o);
                });
            }
        });

        JLabel lbD = new JLabel("<html><i>d</i> value</html>");
        lbD.setFont(lbD.getFont().deriveFont(14F));
        lbD.setBounds(675, 5, 100, 15);
        panel.add(lbD);

        txD = new JTextField();
//        txJ.setEditable(false);
        txD.setBounds(675, 25, 100, 22);
        panel.add(txD);
        txD.addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent e) {}
            public void focusLost(FocusEvent e) {
                if (txD.getText().isEmpty()) return;
                rsaProcessor.setPublicKeyComponent(txD.getText()).ifPresent(o -> {
                    txP.setText("");
                    txQ.setText("");
                    txPhi.setText("");
                    txD.setText(o);
                });
            }
        });

        // ======================================================================================================== //

        JLabel lbInput = new JLabel("Input");
        lbInput.setFont(lbInput.getFont().deriveFont(14F));
        lbInput.setBounds(10, 60, 100, 15);
        panel.add(lbInput);

        txInput = new JTextArea();
        txInput.setLineWrap(true);
        txInput.setBounds(10, 80, 765, 200);
        panel.add(txInput);

        // ======================================================================================================== //

        JButton btnEncrypt = new JButton("Encrypt");
        btnEncrypt.setFont(btnEncrypt.getFont().deriveFont(14F));
        btnEncrypt.setBounds(10, 290, 100, 22);
        panel.add(btnEncrypt);

        btnEncrypt.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String n = txN.getText();
                String k = txE.getText();

                if (n.isEmpty() || k.isEmpty()) {
                    String p = txP.getText();
                    String q = txQ.getText();

                    Optional<RSAProcessor.PQPair> pqPair = rsaProcessor.processPQ(p, q);
                    if (!pqPair.isPresent()) {
                        openDialog("Invalid P and Q values");
                        return;
                    }

                    txP.setText(pqPair.get().p());
                    txQ.setText(pqPair.get().q());

                    txN.setText(rsaProcessor.getN());
                    txPhi.setText(rsaProcessor.getPhi());
                    txE.setText(rsaProcessor.getPublicKeyComponent());
                    txD.setText(rsaProcessor.getPrivateKeyComponent());
                }

                txOutput.setText(rsaProcessor.encrypt(txInput.getText()));
            }
        });

        JButton btnDecrypt = new JButton("Decrypt");
        btnDecrypt.setFont(btnDecrypt.getFont().deriveFont(14F));
        btnDecrypt.setBounds(115, 290, 100, 22);
        panel.add(btnDecrypt);

        btnDecrypt.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String n = txN.getText();
                String d = txD.getText();

                if (n.isEmpty() || d.isEmpty()) {
                    String p = txP.getText();
                    String q = txQ.getText();

                    Optional<RSAProcessor.PQPair> pqPair = rsaProcessor.processPQ(p, q);
                    if (!pqPair.isPresent()) {
                        openDialog("Invalid P and Q values");
                        return;
                    }

                    txP.setText(pqPair.get().p());
                    txQ.setText(pqPair.get().q());

                    txN.setText(rsaProcessor.getN());
                    txPhi.setText(rsaProcessor.getPhi());
                    txE.setText(rsaProcessor.getPublicKeyComponent());
                    txD.setText(rsaProcessor.getPrivateKeyComponent());
                }

                txOutput.setText(rsaProcessor.decrypt(txInput.getText()));
            }
        });

        JButton btnFile = new JButton("Select File...");
        btnFile.setFont(btnDecrypt.getFont().deriveFont(14F));
        btnFile.setBounds(445, 290, 120, 22);
        panel.add(btnFile);

        btnFile.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setApproveButtonText("Select");
                int option = fileChooser.showOpenDialog(UIApplication.this);

                if (option == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                    selectedPath = file.toPath();
                    btnSave.setEnabled(true);
                    btnLoad.setEnabled(true);
                }
            }
        });

        btnSave = new JButton("Save");
        btnSave.setEnabled(false);
        btnSave.setFont(btnSave.getFont().deriveFont(14F));
        btnSave.setBounds(570, 290, 100, 22);
        panel.add(btnSave);

        btnSave.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (txOutput.getText().isEmpty()) {
                    openDialog("You must first encrypt some text");
                    return;
                }

                File file = selectedPath.toFile();

                try {
                    if (!file.exists()) file.createNewFile();
                } catch (Exception ex) {
                    openDialog("Could not create file: " + ex.getMessage());
                    ex.printStackTrace();
                    return;
                }

                try (DataOutputStream out = new DataOutputStream(new FileOutputStream(file))) {
                    out.writeUTF(rsaProcessor.getN());
                    out.writeUTF(rsaProcessor.getPublicKeyComponent());
                    out.writeUTF(Base64.getEncoder().encodeToString(txOutput.getText().getBytes(StandardCharsets.UTF_8)));
                    openDialog("Output and public key saved to file");
                } catch (IOException ex) {
                    openDialog("Could not save to file: " + ex.getMessage());
                    ex.printStackTrace();
                }
            }
        });

        btnLoad = new JButton("Load");
        btnLoad.setEnabled(false);
        btnLoad.setFont(btnLoad.getFont().deriveFont(14F));
        btnLoad.setBounds(675, 290, 100, 22);
        panel.add(btnLoad);

        btnLoad.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent ev) {
                try (DataInputStream in = new DataInputStream(new FileInputStream(selectedPath.toFile()))) {
                    String n = in.readUTF();
                    String e = in.readUTF();
                    String cipherText = new String(Base64.getDecoder().decode(in.readUTF()), StandardCharsets.UTF_8);

                    txP.setText("");
                    txQ.setText("");
                    txPhi.setText("");

                    rsaProcessor.setN(n).ifPresent(o -> txN.setText(o));
                    rsaProcessor.setPublicKeyComponent(e).ifPresent(o -> txE.setText(o));

                    rsaProcessor.updatePrivateKey();
                    txD.setText(rsaProcessor.getPrivateKeyComponent());

                    txInput.setText(cipherText);
                    txOutput.setText("");
                } catch (IOException ex) {
                    openDialog("Could not load from file: " + ex.getMessage());
                    ex.printStackTrace();
                }
            }
        });

        // ======================================================================================================== //

        JLabel lbOutput = new JLabel("Output");
        lbOutput.setFont(lbOutput.getFont().deriveFont(14F));
        lbOutput.setBounds(10, 365, 100, 15);
        panel.add(lbOutput);

        txOutput = new JTextArea();
        txOutput.setLineWrap(true);
        txOutput.setEditable(false);
        txOutput.setBounds(10, 395, 765, 200);
        panel.add(txOutput);
    }

    private void openDialog(String text) {
        JOptionPane.showMessageDialog(this, text);
    }

    public void open() {
        setVisible(true);
    }

}
