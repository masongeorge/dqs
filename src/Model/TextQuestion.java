package Model;

public class TextQuestion extends Question{
    private String correctAnswer;

    public TextQuestion(String title, String type, String correctAnswer) {
        super(title, type);
        this.correctAnswer = correctAnswer;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }
}