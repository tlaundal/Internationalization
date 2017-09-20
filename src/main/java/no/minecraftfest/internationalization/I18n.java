package no.minecraftfest.internationalization;

import de.cubeisland.engine.i18n.I18nService;
import de.cubeisland.engine.i18n.language.SourceLanguage;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.command.CommandSender;
import org.cubeengine.dirigent.Dirigent;
import org.cubeengine.dirigent.builder.BuilderDirigent;

import java.util.Locale;
import java.util.WeakHashMap;

/**
 * The main class for the Internationalization library.
 *
 * <p>
 * All messages that should be somehow displayed to a user should go through
 * this class, mainly through the following functions:
 * <ul>
 *     <li>
 *         {@code sendMessage(CommandSender, String message, Object... args}
 *     </li>
 *     <li>
 *         {@code translate(CommandSender, String message, Object... args}
 *     </li>
 * </ul>
 * </p>
 *
 * <p>
 * These functions will find the appropriate language for the user, look for a
 * translation of the message and use it. All strings go through
 * {@link Dirigent}, and may use any of its features.
 * </p>
 */
public class I18n {

    // TODO - plurals

    private final Locale defaultLocale;

    private final LanguageRepo repo;
    private final I18nService service;
    private final BuilderDirigent<BaseComponent[], ComponentBuilder> dirigent;

    private final WeakHashMap<CommandSender, Locale> locales;

    public I18n(SourceLanguage sourceLanguage, Locale defaultLocale) {
        this.defaultLocale = defaultLocale;

        this.repo = new LanguageRepo();
        this.service = new I18nService(sourceLanguage, repo, repo, defaultLocale);
        this.dirigent = new BuilderDirigent<>(new ComponentMessageBuilder());

        this.locales = new WeakHashMap<>();
    }

    /**
     * Translate, compose and send a message to a {@link CommandSender}
     * <p>
     * The message will be translated to the appropriate language for the
     * receiver, and sent through {@link Dirigent} for composition.
     * </p>
     *
     * @param receiver The {@link CommandSender} which should receive the
     *                 message
     * @param message The message to translate, compose and send
     * @param args The composition arguments for use by the {@link Dirigent}
     */
    public void sendMessage(CommandSender receiver, String message, Object... args) {
        receiver.spigot().sendMessage(this.translateAndCompose(receiver, message, args));
    }

    public BaseComponent[] translateAndCompose(CommandSender receiver, String message, Object... args) {
        String translated = this.translate(receiver, message);
        return this.compose(translated, args);
    }

    /**
     * Translate the message to the appropriate language for a
     * {@link CommandSender}
     * <p>
     * If the {@link CommandSender}'s language is not found, the source
     * language will be used, and thus the same message as is applied to this
     * function will be returned
     * </p>
     *
     * @param receiver The {@link CommandSender} which should receive the
     *                 message
     * @param message The message to translate
     * @return The translated message
     */
    public String translate(CommandSender receiver, String message) {
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
     * Set the locale for this {@link CommandSender}
     * <p>
     * The locale will be used to try to find translations when translating
     * messages for this user.
     * </p>
     *
     * @param receiver The receiver to set the locale for
     * @param locale The locale the receiver should preferably receive its
     *               messages in
     */
    public void setLocale(CommandSender receiver, Locale locale) {
        this.locales.put(receiver, locale);
    }

    /**
     * Get the {@link Locale} for this {@link CommandSender}
     * <p>
     * This is the locale that will be used for translating messages for this
     * user, if the translations exists. If no locale is set, the default
     * locale will be returned.
     * </p>
     *
     * @param receiver The receiver to get the locale of
     * @return The {@link Locale} of this receiver
     */
    public Locale getLocale(CommandSender receiver) {
        return this.locales.getOrDefault(receiver, this.defaultLocale);
    }

    /**
     * Get the language repo this I18n instance uses.
     *
     * @returns The {@link LanguageRepo} of this instance
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
