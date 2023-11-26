package org.test.darkstoriesai;

public class ListItem {
    private String header;
    private String question;

    public ListItem(String header, String question) {
        this.header = header;
        this.question = question;
    }

    public String getHeader() {
        return header;
    }

    public String getQuestion() {
        return question;
    }
}
