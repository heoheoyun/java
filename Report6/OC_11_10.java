package Report6;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class OC_11_10 extends JFrame {
    private JPanel contentPane;
    private JButton newTextButton;
    private JLabel sentenceLabel;
    private JLabel instructionsLabel;
    private String targetSentence;
    private ArrayList<String> words;
    private ArrayList<JLabel> wordLabels;
    private StringBuilder currentSentence;
    private int wordIndex;

    public OC_11_10() {
        setTitle("문장 완성 게임");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);

        contentPane = new JPanel();
        contentPane.setLayout(null);

        instructionsLabel = new JLabel("단어 조합 게임! 순서대로 단어를 클릭하세요~~");
        instructionsLabel.setBounds(50, 20, 400, 30);
        instructionsLabel.setFont(new Font("맑은 고딕", Font.BOLD, 14));
        instructionsLabel.setBackground(Color.GRAY);
        instructionsLabel.setOpaque(true);

        newTextButton = new JButton("New Text");
        newTextButton.setBounds(450, 20, 100, 30);
        newTextButton.setBackground(Color.WHITE);
        newTextButton.setOpaque(true);
        newTextButton.addActionListener(e -> generateNewText());

        sentenceLabel = new JLabel("완성된 문장: ");
        sentenceLabel.setBounds(50, 300, 500, 30);
        sentenceLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
        sentenceLabel.setForeground(Color.BLACK);
        sentenceLabel.setHorizontalAlignment(SwingConstants.CENTER);
        sentenceLabel.setOpaque(true);
        sentenceLabel.setBackground(Color.YELLOW);

        contentPane.add(instructionsLabel);
        contentPane.add(newTextButton);
        contentPane.add(sentenceLabel);

        setContentPane(contentPane);
        setVisible(true);
    }

    private void generateNewText() {
        String[] sentences = {
            "You are the dancing queen",
            "I am the walrus",
            "Hello world this is java",
            "Java is fun to learn",
            "Programming is awesome",
            "Swing GUI is great"
        };

        Random random = new Random();
        targetSentence = sentences[random.nextInt(sentences.length)];
        words = new ArrayList<>(Arrays.asList(targetSentence.split(" ")));
        Collections.shuffle(words);

        wordLabels = new ArrayList<>();
        currentSentence = new StringBuilder();
        wordIndex = 0;

        // 기존 단어 레이블들을 지우지 않고 새 단어들만 추가
        for (String word : words) {
            JLabel wordLabel = createWordLabel(word);
            wordLabels.add(wordLabel);
            contentPane.add(wordLabel);
        }

        contentPane.revalidate();  // 레이아웃 갱신
        contentPane.repaint();  // 화면 갱신
    }

    private JLabel createWordLabel(String word) {
        JLabel wordLabel = new JLabel(word);
        wordLabel.setForeground(Color.BLACK);
        wordLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 20));

        // 단어 크기를 계산하여 레이블의 크기 설정
        wordLabel.setPreferredSize(new Dimension(120, 30));

        wordLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleWordClick((JLabel) e.getSource());
            }
        });

        Point position = getRandomPositionForWord(wordLabel);
        wordLabel.setLocation(position);  // 위치 지정
        wordLabel.setSize(wordLabel.getPreferredSize());  // 크기 지정

        return wordLabel;
    }

    private Point getRandomPositionForWord(JLabel wordLabel) {
        Random random = new Random();
        Rectangle bounds = contentPane.getBounds();
        Rectangle yellowArea = sentenceLabel.getBounds();
        Rectangle grayArea = instructionsLabel.getBounds();

        int x, y;
        boolean positionValid;

        do {
            positionValid = true;
            x = random.nextInt(bounds.width - wordLabel.getWidth() - 50);
            y = random.nextInt(bounds.height - wordLabel.getHeight() - 100);

            // 레이블이 회색 또는 노란색 영역을 침범하지 않도록 검사
            if (yellowArea.intersects(new Rectangle(x, y, wordLabel.getWidth(), wordLabel.getHeight())) ||
                grayArea.intersects(new Rectangle(x, y, wordLabel.getWidth(), wordLabel.getHeight()))) {
                positionValid = false;
            }

            // 기존 단어들과 겹치지 않도록 검사
            for (JLabel otherLabel : wordLabels) {
                if (wordLabel.getBounds().intersects(otherLabel.getBounds())) {
                    positionValid = false;
                    break;
                }
            }
        } while (!positionValid);

        return new Point(x, y);
    }

    private void handleWordClick(JLabel clickedLabel) {
        if (clickedLabel.getText().equals(words.get(wordIndex))) {
            clickedLabel.setForeground(Color.LIGHT_GRAY);
            clickedLabel.removeMouseListener(clickedLabel.getMouseListeners()[0]);

            currentSentence.append(clickedLabel.getText()).append(" ");
            sentenceLabel.setText("완성된 문장: " + currentSentence.toString().trim());

            wordIndex++;

            if (wordIndex == words.size()) {
                sentenceLabel.setText("완성된 문장: " + currentSentence.toString().trim() + " >> 게임 종료!");
                newTextButton.setEnabled(false);  // 게임 종료 후 'New Text' 버튼 비활성화
            }
        }
    }

    public static void main(String[] args) {
        new OC_11_10();
    }
}
