package files;

import java.io.Serializable;

public class ChatMessage implements Serializable {
        protected static final long serialVersionUID = 1112122200L;

        // The different types of message sent by the Client
        // WHOISIN to receive the list of the users connected
        // MESSAGE an ordinary message
        // LOGOUT to disconnect from the Server
        // WRITE_TO select to whom woud you like to write
        static final int WHOISIN = 0, MESSAGE = 1, LOGOUT = 2, WRITE_TO=3;
        private int type;
        private String message;

        ChatMessage(int type, String message) {
            this.type = type;
            this.message = message;
        }

        // getters
        int getType() {
            return type;
        }
        String getMessage() {
            return message;
        }
    }
