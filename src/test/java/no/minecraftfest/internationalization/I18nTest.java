package no.minecraftfest.internationalization;

import de.cubeisland.engine.i18n.language.SourceLanguage;
import de.cubeisland.engine.i18n.plural.NotOneExpr;
import net.md_5.bungee.api.chat.BaseComponent;
import no.minecraftfest.internationalization.mock.MockCommandSender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

class I18nTest {

    private I18n i18n;
    private Locale defaultLocale;
    private Locale norwegianLocale;
    private MockCommandSender senderMock;
    private MockCommandSender norwegianMock;

    @BeforeEach
    void setup() {
        i18n = new I18n(new SourceLanguage(Locale.ENGLISH, Locale.ENGLISH.toLanguageTag(), 2, new NotOneExpr()), Locale.ENGLISH);
        this.defaultLocale = Locale.ENGLISH;
        this.norwegianLocale = new Locale.Builder().setLanguage("nb").build();
        this.senderMock = new MockCommandSender();

        this.norwegianMock = new MockCommandSender();
        i18n.setLocale(norwegianMock, norwegianLocale);

        LanguageRepoTest.loadTestLanguage(i18n.getRepo());
    }

    @Test
    void sendMessage() {
        String message = "This is a test message without composition";

        i18n.sendMessage(senderMock, message);

        assertEquals(message, senderMock.getLastMessage(), "Player was not sent message");
    }

    @Test
    void compose() {
        String raw = "Here is the argument: {}";
        String argument = "ARGUMENT";
        String expected = "Here is the argument: ARGUMENT";

        String actual = BaseComponent.toPlainText(i18n.compose(raw, argument));

        assertEquals(expected, actual, "Message should have been composed");
    }

    @Test
    void translate() {
        String message = "This is a message";
        String expected = "Dette er en melding";

        String actual = i18n.translate(norwegianMock, message);

        assertEquals(expected, actual, "Message should have been translated");
    }

    @Test
    void notTranslate() {
        String message = "This is a message";

        String actual = i18n.translate(senderMock, message);

        assertEquals(message, actual, "Message should not have been translated");
    }

    /**
     * Test that the locale for a user without locale is the default locale
     */
    @Test
    void defaultLocale() {
        Locale actual = i18n.getLocale(senderMock);

        assertEquals(defaultLocale, actual, "CommandSender should have the default locale");
    }

    /**
     * Test that the locale can be set and retrieved for a user
     */
    @Test
    void locale() {
        Locale actual = i18n.getLocale(norwegianMock);

        assertEquals(norwegianLocale, actual, "CommandSender should have norwegian locale");
    }

}