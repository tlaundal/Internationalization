package no.minecraftfest.internationalization;

import de.cubeisland.engine.i18n.language.SourceLanguage;
import de.cubeisland.engine.i18n.plural.NotOneExpr;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import no.minecraftfest.internationalization.mock.MockReceiver;
import org.fedorahosted.tennera.jgettext.PoParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.charset.Charset;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

class I18nTest {

    private I18n<MockReceiver> i18n;
    private Locale defaultLocale;
    private Locale norwegianLocale;
    private MockReceiver senderMock;
    private MockReceiver norwegianMock;

    @BeforeEach
    void setup() {
        i18n = new I18n<>(
                new SourceLanguage(Locale.ENGLISH, Locale.ENGLISH.toLanguageTag(), 2, new NotOneExpr()),
                Locale.ENGLISH,
                new LanguageRepo(new GettextLoader(new PoParser(), Charset.forName("UTF-8"))),
                MockReceiver::receive);
        this.defaultLocale = Locale.ENGLISH;
        this.norwegianLocale = new Locale.Builder().setLanguage("nb").build();
        this.senderMock = new MockReceiver();

        this.norwegianMock = new MockReceiver();
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

    @Test
    void composeColorFormat() {
        String toCompose = "{color:red}red";
        String expected = "§cred";

        BaseComponent[] components = i18n.compose(toCompose);
        String actual = BaseComponent.toLegacyText(components);

        assertTrue(actual.endsWith(expected), "Text should become colored");
    }

    @Test
    void composeWithComponent() {
        String toCompose = "{color:red}before {0:component} after";
        TextComponent middle = new TextComponent("middle");
        middle.setColor(ChatColor.GREEN);

        BaseComponent[] components = i18n.compose(toCompose, middle);
        String actual = BaseComponent.toLegacyText(components);

        assertTrue(actual.contains("§c after"), "After should have correct color");
    }

}