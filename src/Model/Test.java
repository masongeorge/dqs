package Model;

import java.util.Map;

public class Test {
    public Question questions[] = new Question[5];
    public String answers[] = {"","","","",""};
    private int corrects = 0;
    private int wrongs = 0;

    public Test(Map<String, String> multipleChoices, Map<String, String> regulars, int question1Number){
        // Registering each question
        int questionIndex = question1Number;
        while(questionIndex < question1Number+5){
            String questionID = "question" + questionIndex;

            if(multipleChoices.containsKey(questionID)){
                // Question is multiple choice type
                this.questions[questionIndex-question1Number] = new MultipleQuestion(multipleChoices.get(questionID), "m", multipleChoices.get(questionID+"_q1"), multipleChoices.get(questionID+"_q2"), multipleChoices.get(questionID+"_q3"), multipleChoices.get(questionID+"_c"));
            }else{
                // Question is text type
                this.questions[questionIndex-question1Number] = new TextQuestion(regulars.get(questionID), "n", regulars.get(questionID+"_c"));
            }

            questionIndex++;
        }
    }

    public boolean isQuestionMultiple(int index){
        return questions[index].getType() == "m";
    }

    public int calculateScore(){
        int score = 0;

        for (int i = 0; i < 5; i++) {
            if(questions[i].getType() == "m"){
                MultipleQuestion question = (MultipleQuestion) questions[i];
                if(answers[i].equals(question.getCorrectAnswer())){
                    score++;
                    corrects++;
                }else{
                    wrongs++;
                }
            }else{
                TextQuestion question = (TextQuestion) questions[i];
                if(answers[i].toLowerCase().equals(question.getCorrectAnswer().toLowerCase())){
                    score ++;
                    corrects++;
                }else{
                    wrongs++;
                }
            }
        }

        return score;
    }


    public int correctAnswers(){
        return corrects;
    }

    public int wrongAnswers(){
        return wrongs;
    }


}





