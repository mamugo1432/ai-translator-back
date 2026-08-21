package es.ia.translator.exceptions.user;

import java.util.List;

public class UserException extends RuntimeException{


    private static final long serialVersionUID = 1L;
    private List<String> messages;

    public UserException(List<String> messages) {
        this.messages = messages;
    }

    public List<String> getMessages() {
        return messages;
    }

    public void setMessages(List<String> messages) {
        this.messages = messages;
    }



}