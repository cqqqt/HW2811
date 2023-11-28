import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class Main {
    private static final Logger logger = LogManager.getLogger(Main.class);

    public static void main(String[] args) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        try {
            String inputText = "";

            System.out.println("�������� �������� ������ (1 - ������ � �������, 2 - �� �����):");
            int choice = Integer.parseInt(reader.readLine());

            if (choice == 1) {
                System.out.println("������� �����:");
                inputText = reader.readLine();
            } else if (choice == 2) {
                System.out.println("������� ���� � ���������� �����:");
                String filePath = reader.readLine();
                inputText = readFromFile(filePath);
            } else {
                System.out.println("������������ �����. ���������� ���������.");
                return;
            }

            // ������� ���������� ����
            int uniqueWordsCount = countUniqueWords(inputText);
            System.out.println("���������� ���������� ����: " + uniqueWordsCount);

            // ������ ����������� � ����
            writeToFile("_______________________\n" +
                    "���������� �����\n" +
                    "_______________________\n" +
                    "�������� �����: " + inputText + "\n" +
                    "���������� ���������� ����: " + uniqueWordsCount);

            // ���������� ������ �� �����
            String lettersResult = separateIntoLetters(inputText);
            System.out.println("��������� ���������� �� �����: " + lettersResult);

            // ������ ���������� ���� � ����
            int lettersCount = countLetters(lettersResult);
            writeToFile("_______________________\n" +
                    "���������� ����\n" +
                    "_______________________\n" +
                    "�������� �����: " + inputText + "\n" +
                    "���������: " + lettersResult + "\n" +
                    "���������� ����: " + lettersCount);

            // ����� �����
            String searchWord;
            int wordOccurrences;
            do {
                System.out.println("������� ����� ��� ������ (����� 2 ����):");
                searchWord = reader.readLine();
                try {
                    wordOccurrences = findWord(inputText, searchWord);
                    System.out.println("��������� ������: ������� " + wordOccurrences + " ����������");
                    // ������ ����������� ������ � ����
                    writeToFile("_______________________\n" +
                            "����� �����\n" +
                            "_______________________\n" +
                            "��������: " + inputText + "\n" +
                            "����� �� �����: " + searchWord + "\n" +
                            "���������: ������� " + wordOccurrences + " ����������");
                } catch (InvalidWordException e) {
                    logger.error("Invalid word entered: " + e.getMessage());
                }
            } while (!isValidWord(searchWord));

        } catch (IOException | NumberFormatException e) {
            logger.error("An error occurred: " + e.getMessage());
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                logger.error("Error closing the BufferedReader: " + e.getMessage());
            }
        }
    }

    private static int countUniqueWords(String text) {
        Map<String, Integer> wordCountMap = new HashMap<>();
        String[] words = text.split("\\s+");
        for (String word : words) {
            word = StringUtils.lowerCase(word);
            if (word.length() >= 3) {
                wordCountMap.put(word, wordCountMap.getOrDefault(word, 0) + 1);
            }
        }
        return wordCountMap.size();
    }

    private static String separateIntoLetters(String text) {
        StringBuilder result = new StringBuilder();
        String[] words = StringUtils.split(text);
        for (String word : words) {
            char[] letters = word.toCharArray();
            for (char letter : letters) {
                if (Character.isLetter(letter)) {
                    result.append(Character.toUpperCase(letter)).append(" ");
                }
            }
            result.append(" ");
        }
        return result.toString().trim();
    }

    private static boolean isValidWord(String word) {
        return word.matches("^\\p{L}{3,}$");
    }

    private static int findWord(String text, String word) throws InvalidWordException {
        if (!word.matches("^\\p{L}{3,}$")) {
            throw new InvalidWordException("������������ �����. ������� ����� ����� 2 ����.");
        }

        String[] words = text.split("\\s+");
        int count = 0;
        for (String w : words) {
            if (StringUtils.equalsIgnoreCase(w, word)) {
                count++;
            }
        }
        return count;
    }

    private static int countLetters(String text) {
        return StringUtils.remove(text, ' ').length();
    }

    private static String readFromFile(String filePath) throws IOException {
        StringBuilder content = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                content.append(line).append(" ");
            }
        }
        return content.toString();
    }

    private static void writeToFile(String content) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("results.txt", true))) {
            writer.append(content).append("\n\n");
        }
    }
}
