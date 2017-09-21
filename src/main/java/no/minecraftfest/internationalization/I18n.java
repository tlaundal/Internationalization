package no.minecraftfest.internationalization;

import de.cubeisland.engine.i18n.I18nService;
import de.cubeisland.engine.i18n.language.SourceLanguage;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import no.minecraftfest.internationalization.builder.ComponentMessageBuilder;
import no.minecraftfest.internationalization.formatter.ColorFormatter;
import no.minecraftfest.internationalization.formatter.ComponentFormatter;
import no.minecraftfest.internationalization.formatter.ComponentsFormatter;
import org.cubeengine.dirigent.Dirigent;
import org.cubeengine.dirigent.builder.BuilderDirigent;

import java.util.Locale;
import java.util.WeakHashMap;
import java.util.function.BiConsumer;

/**
 * The main class for the Internationalization library.
 *
 * <p>
 * All messages that should be somehow displayed to a user should go through
 * this class, mainly through the following functions:
 * <ul>
 *     <li>
 *         {@code sendMessage(ReceiverT, String message, Object... args}
 *     </li>
 *     <li>
 *         {@code translate(ReceiverT, String message, Object... args}
 *     </li>
 * </ul>
 * </p>
 *
 * <p>
 * These functions will find the appropriate language for the receiver, look
 * for a translation of the message and use it. All strings go through
 * {@link Dirigent}, and may use any of its features.
 * </p>
 *
 * @param <ReceiverT> The type of the receiver, used to store locale and
 *                   for sending the messages through the messageSender.
 */
public class I18n<ReceiverT> {

    // TODO - plurals

    private final Locale defaultLocale;
    private final BiConsumer<ReceiverT, BaseComponent[]> messageSender;

    private final LanguageRepo repo;
    private final I18nService service;
    private final BuilderDirigent<BaseComponent[], ComponentBuilder> dirigent;

    private final WeakHashMap<ReceiverT, Locale> locales;

    /**
     * Construct a new I18n instance
     *
     * @param sourceLanguage The language the source code is in. This is used
     *                       to decide when the source messages can be shown
     *                       without translation
     * @param defaultLocale The default locale for receiver which doesn't have
     *                      a locale set
     * @param messageSender The function which sends the messages. Receives the
     *                      ReceiverT instance that should receive the message
     *                      and the {@code BaseComponent[]} array which
     *                      represents the message
     */
    public I18n(SourceLanguage sourceLanguage, Locale defaultLocale,
                BiConsumer<ReceiverT, BaseComponent[]> messageSender) {
        this.defaultLocale = defaultLocale;
        this.messageSender = messageSender;

        this.repo = new LanguageRepo();
        this.service = new I18nService(sourceLanguage, repo, repo, defaultLocale);
        this.dirigent = new BuilderDirigent<>(new ComponentMessageBuilder());
        dirigent.registerFormatter(new ColorFormatter());
        dirigent.registerFormatter(new ComponentFormatter());
        dirigent.registerFormatter(new ComponentsFormatter());

        this.locales = new WeakHashMap<>();
    }

    /**
     * Translate, compose and send a message to a receiver
     * <p>
     * The message will be translated to the appropriate language for the
     * receiver, and sent through {@link Dirigent} for composition.
     * </p>
     * <p>
     * The {@code messageSender} supplied in the constructor is used to send
     * the message.
     * </p>
     *
     * @param receiver The {@link ReceiverT} instance which should receive the
     *                 message
     * @param message The message to translate, compose and send
     * @param args The composition arguments for use by the {@link Dirigent}
     */
    public void sendMessage(ReceiverT receiver, String message, Object... args) {
        messageSender.accept(receiver, this.translateAndCompose(receiver, message, args));
    }

    /**
     * Translate and compose a message
     * <p>
     * This is a convenience method and is equivalent to:
     * {@code i18n.compose(i18n.translate(receiver, message), args}
     * </p>
     *
     * @param receiver The receiver to translate for
     * @param message The message to translate
     * @param args The composition arguments
     * @return The translated and composed message
     */
    public BaseComponent[] translateAndCompose(ReceiverT receiver, String message, Object... args) {
        String translated = this.translate(receiver, message);
        return this.compose(translated, args);
    }

    /**
     * Translate the message to the appropriate language for a
     * {@link ReceiverT}
     * <p>
     * If the {@link ReceiverT}'s language is not found, the source
     * language will be used, and thus the same message as is applied to this
     * function will be returned
     * </p>
     *
     * @param receiver The {@link ReceiverT} which should receive the
     *                 message
     * @param message The message to translate
     * @return The translated message
     */
    public String translate(ReceiverT receiver, String message) {
        Locale into = this.getLocale(receiver);

        return service.translate(into, message);
    }

    /**
     * Compose this message with the {@link Dirigent}.
     *
     * @see Dirigent
     * @param message The message to compose
     * @param args The composition arguments
     * @return The composed message
     */
    public BaseComponent[] compose(String message, Object... args) {
        return dirigent.compose(message, args);
    }

    /**
     * Set the locale for this {@link ReceiverT}
     * <p>
     * The locale will be used to try to find translations when translating
     * messages for this user.
     * </p>
     *
     * @param receiver The receiver to set the locale for
     * @param locale The locale the receiver should preferably receive its
     *               messages in
     */
    public void setLocale(ReceiverT receiver, Locale locale) {
        this.locales.put(receiver, locale);
    }

    /**
     * Get the {@link Locale} for this {@link ReceiverT}
     * <p>
     * This is the locale that will be used for translating messages for this
     * user, if the translations exists. If no locale is set, the default
     * locale will be returned.
     * </p>
     *
     * @param receiver The receiver to get the locale of
     * @return The {@link Locale} of this receiver
     */
    public Locale getLocale(ReceiverT receiver) {
        return this.locales.getOrDefault(receiver, this.defaultLocale);
    }

    /**
     * Get the language repo this I18n instance uses.
     *
     * @return The {@link LanguageRepo} of this instance
     */
    public LanguageRepo getRepo() {
        return this.repo;
    }

    /**
     * Get the dirigent used for composition.
     * <p>
     * This method is provided to allow for registration of formatters and
     * other processors on the {@link Dirigent} instance used when composing
     * messages.
     * </p>
     *
     * @return The dirigent instance used for composition.
     */
    public Dirigent<BaseComponent[]> getDirigent() {
        return this.dirigent;
    }

}
