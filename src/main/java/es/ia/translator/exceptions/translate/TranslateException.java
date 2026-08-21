package es.ia.translator.exceptions.translate;

import java.util.List;

public class TranslateException extends RuntimeException{


    private static final long serialVersionUID = 1L;
    private List<String> messages;

    public TranslateException(List<String> messages) {
        this.messages = messages;
    }

    public List<String> getMessages() {
        return messages;
    }

    public void setMessages(List<String> messages) {
        this.messages = messages;
    }



}