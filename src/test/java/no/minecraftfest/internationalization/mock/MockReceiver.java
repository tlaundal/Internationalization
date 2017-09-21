package no.minecraftfest.internationalization.mock;

import net.md_5.bungee.api.chat.BaseComponent;

public class MockReceiver {

    private String lastMessage;

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public static void receive(MockReceiver receiver, BaseComponent[] baseComponents) {
        receiver.setLastMessage(BaseComponent.toPlainText(baseComponents));
    }
}
