import java.util.Objects;


public final class TextTask {

    private TextTask() {
    }

    public static void main(String[] args) {
        try {
            String inputText = "Java makes string handling fun.  "
                    + "Swap the first and last words in every sentence! "
                    + "Edge cases: single-word? numbers123 stay as words.";

            StringBuffer source = new StringBuffer(inputText);
            StringBuffer transformed = swapFirstAndLastWordsInSentences(source);

            System.out.println("Original text:\n" + inputText);
            System.out.println("\nTransformed text:\n" + transformed.toString());
        } catch (IllegalArgumentException e) {
            System.err.println("Validation error: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static StringBuffer swapFirstAndLastWordsInSentences(StringBuffer text) {
        Objects.requireNonNull(text, "Text must not be null.");

        StringBuffer result = new StringBuffer(text.length());
        int position = 0;
        while (position < text.length()) {
            int sentenceEnd = findSentenceEnd(text, position);
            if (sentenceEnd == -1) {
                sentenceEnd = text.length() - 1;
            }

            StringBuffer sentence = new StringBuffer(sentenceEnd - position + 1);
            appendRange(sentence, text, position, sentenceEnd + 1);

            StringBuffer swapped = swapFirstAndLastWords(sentence);
            result.append(swapped);

            position = sentenceEnd + 1;
            while (position < text.length() && !isWordChar(text.charAt(position))) {
                result.append(text.charAt(position));
                position++;
            }
        }
        return result;
    }

    private static StringBuffer swapFirstAndLastWords(StringBuffer sentence) {
        int firstStart = findWordStart(sentence, 0);
        if (firstStart == -1) {
            return sentence;
        }
        int firstEnd = findWordEnd(sentence, firstStart);

        int lastEnd = findLastWordEnd(sentence);
        if (lastEnd == -1) {
            return sentence;
        }
        int lastStart = findLastWordStart(sentence, lastEnd);

        if (firstStart == lastStart) {
            return sentence;
        }

        StringBuffer rebuilt = new StringBuffer(sentence.length());
        appendRange(rebuilt, sentence, 0, firstStart);
        appendRange(rebuilt, sentence, lastStart, lastEnd + 1);
        appendRange(rebuilt, sentence, firstEnd + 1, lastStart);
        appendRange(rebuilt, sentence, firstStart, firstEnd + 1);
        appendRange(rebuilt, sentence, lastEnd + 1, sentence.length());
        return rebuilt;
    }

    private static int findSentenceEnd(StringBuffer text, int from) {
        for (int i = from; i < text.length(); i++) {
            if (isSentenceTerminator(text.charAt(i))) {
                return i;
            }
        }
        return -1;
    }

    private static int findWordStart(StringBuffer sentence, int from) {
        for (int i = from; i < sentence.length(); i++) {
            if (isWordChar(sentence.charAt(i))) {
                return i;
            }
        }
        return -1;
    }

    private static int findWordEnd(StringBuffer sentence, int start) {
        int index = start;
        while (index < sentence.length() && isWordChar(sentence.charAt(index))) {
            index++;
        }
        return index - 1;
    }

    private static int findLastWordEnd(StringBuffer sentence) {
        for (int i = sentence.length() - 1; i >= 0; i--) {
            if (isWordChar(sentence.charAt(i))) {
                return i;
            }
        }
        return -1;
    }

    private static int findLastWordStart(StringBuffer sentence, int end) {
        int index = end;
        while (index >= 0 && isWordChar(sentence.charAt(index))) {
            index--;
        }
        return index + 1;
    }

    private static void appendRange(StringBuffer target, StringBuffer source, int start, int endExclusive) {
        if (start < 0 || endExclusive > source.length() || start > endExclusive) {
            throw new IllegalArgumentException("Invalid range: " + start + ".." + endExclusive);
        }
        for (int i = start; i < endExclusive; i++) {
            target.append(source.charAt(i));
        }
    }

    private static boolean isWordChar(char character) {
        return Character.isLetterOrDigit(character);
    }

    private static boolean isSentenceTerminator(char character) {
        return character == '.' || character == '!' || character == '?';
    }
}
