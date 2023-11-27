package org.test.darkstoriesai;

public class ListItem {
    private String header;
    private String question;
    private Boolean isLoading;

    public ListItem(String header, String question) {
        this.header = header;
        this.question = question;
    }

    public ListItem(boolean isLoading) {
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
