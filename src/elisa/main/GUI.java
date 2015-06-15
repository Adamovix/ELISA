package elisa.main;

import elisa.generator.Analyzer;
import elisa.generator.Generator;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

/**
 * @author Adam Goscicki
 */
public class GUI extends JFrame {
    private JPanel contentPanel;
    private JTextPane textArea1;
    private JLabel dbWordCountLabel;
    private JLabel lastUnknownWordLabel;
    private JLabel probabilityLabel;
    private JList<Map.Entry<String, Integer>> transitionList;
    private JButton sendButton;
    private JTextField textField1;
    private JButton loadTextButton;
    private JButton resetButton;

    private Analyzer analyzer = new Analyzer();
    private Generator generator = new Generator();

    public GUI() {
        super("ELISA");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setContentPane(contentPanel);
        transitionList.setCellRenderer(new ListCellRenderer<Map.Entry<String, Integer>>() {
            @Override
            public Component getListCellRendererComponent(JList<? extends Map.Entry<String, Integer>> list, Map.Entry<String, Integer> value, int index, boolean isSelected, boolean cellHasFocus) {
                return new JLabel(value.getKey() + " - " + value.getValue(), SwingConstants.CENTER) {
                    @Override
                    protected void paintComponent(Graphics g) {
                        Graphics2D g2 = (Graphics2D) g.create();

                        g2.setPaint(new GradientPaint(
                                        0,
                                        0,
                                        Color.WHITE,
                                        0,
                                        getHeight(),
                                        Color.ORANGE)
                        );

                        int width = getFontMetrics(getFont()).stringWidth(getText()) + 10;
                        g2.fillRect(
                                (getWidth() - width) / 2,
                                0,
                                width,
                                getHeight()
                        );

                        g2.setPaint(Color.ORANGE);
                        g2.drawRect(
                                (getWidth() - width) / 2,
                                0,
                                width,
                                getHeight()
                        );

                        super.paintComponent(g);
                    }
                };
            }
        });

        final DefaultListModel<Map.Entry<String, Integer>> transitionListModel = new DefaultListModel<>();
        transitionList.setModel(transitionListModel);
        textField1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String text = textField1.getText();
                textField1.setText(null);

                if (text.isEmpty()) {
                    return;
                }

                if (analyzer.isEmpty()) {
                    JOptionPane.showMessageDialog(
                            contentPanel,
                            "Brak wczytanych tekst\u00f3w!",
                            null,
                            JOptionPane.ERROR_MESSAGE
                    );
                    return;
                }

                addMessage(text, false);

                String starting = text.split("\\s+")[0];
                String response = generator.generate(starting, analyzer);
                addMessage(response, true);

                String[] split = response.split("\\s+");
                transitionListModel.clear();
                for (String word : split) {
                    transitionListModel.addElement(analyzer.getWord(word));
                }
            }
        });
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String text = textField1.getText();
                textField1.setText(null);

                if (text.isEmpty()) {
                    return;
                }

                if (analyzer.isEmpty()) {
                    JOptionPane.showMessageDialog(
                            contentPanel,
                            "Brak wczytanych tekst\u00f3w!",
                            null,
                            JOptionPane.ERROR_MESSAGE
                    );
                    return;
                }

                addMessage(text, false);

                String starting = text.split("\\s+")[0];
                String response = generator.generate(starting, analyzer);
                addMessage(response, true);

                String[] split = response.split("\\s+");
                transitionListModel.clear();
                for (String word : split) {
                    transitionListModel.addElement(analyzer.getWord(word));
                }
            }
        });

        loadTextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fc = new JFileChooser();
                if (fc.showDialog(GUI.this, "Wczytaj") == JFileChooser.APPROVE_OPTION) {
                    try {
                        analyzer.parse(fc.getSelectedFile());
                    } catch (FileNotFoundException e1) {
                        JOptionPane.showMessageDialog(
                                GUI.this,
                                "Podany plik nie istnieje!",
                                null,
                                JOptionPane.ERROR_MESSAGE
                        );
                    }
                }
            }
        });

        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                StyledDocument doc = textArea1.getStyledDocument();
                try {
                    doc.remove(0, doc.getLength());
                } catch (BadLocationException e1) {
                    e1.printStackTrace();
                }
            }
        });

        analyzer.addObserver(new Observer() {
            @Override
            public void update(Observable o, Object arg) {
                dbWordCountLabel.setText(String.valueOf(analyzer.getUniqueWordCount()));
            }
        });

        generator.addObserver(new Observer() {
            @Override
            public void update(Observable o, Object arg) {
                lastUnknownWordLabel.setText(generator.getLastUnknown());
                probabilityLabel.setText(String.valueOf(generator.getLastProbability()));
            }
        });

        pack();
    }

    private void addMessage(String text, boolean sentByBot) {
        StyledDocument doc = textArea1.getStyledDocument();
        try {
            doc.insertString(
                    doc.getLength(),
                    text + "\n",
                    sentByBot ? TextAttributes.BOT_ATTRIBUTE_SET : TextAttributes.USER_ATTRIBUTE_SET
            );
            textField1.setText(null);
        } catch (BadLocationException e1) {
            e1.printStackTrace();
        }
    }
}
