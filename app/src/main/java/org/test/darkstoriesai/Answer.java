package org.test.darkstoriesai;

public class Answer {
    private String header;
    private String question;
    private Boolean isLoading;

    public Answer(String header, String question) {
        this.header = header;
        this.question = question;
    }

    public Answer(boolean isLoading) {
        this.isLoading = isLoading;
    }

    public String getHeader() {
        return header;
    }

    public String getQuestion() {
        return question;
    }
    public boolean isLoading() {return isLoading != null && isLoading.booleanValue();}

}
