package no.minecraftfest.internationalization;

import de.cubeisland.engine.i18n.language.DefinitionLoadingException;
import de.cubeisland.engine.i18n.language.LanguageDefinition;
import de.cubeisland.engine.i18n.language.LanguageLoader;
import de.cubeisland.engine.i18n.translation.TranslationContainer;
import de.cubeisland.engine.i18n.translation.TranslationLoader;
import de.cubeisland.engine.i18n.translation.TranslationLoadingException;
import org.fedorahosted.tennera.jgettext.Catalog;
import org.fedorahosted.tennera.jgettext.Message;

import java.io.IOException;
import java.net.URL;
import java.util.*;

/**
 * The language repository.
 *
 * <p>
 * All languages that should be available must be registered in this repo.
 * </p>
 */
public class LanguageRepo extends LanguageLoader implements TranslationLoader {

    private final GettextLoader loader;
    private final Map<Locale, LanguageDefinition> definitions;
    private final Map<Locale, List<URL>> translations;

    public LanguageRepo(GettextLoader loader) {
        this.loader = loader;
        definitions = new HashMap<>();
        translations = new HashMap<>();
    }

    /**
     * Check that this repo contains definitions and translations for the
     * supplied locale
     * <p>
     *     This method is intended to be used to check that messages can be
     *     requested in the locale from {@link I18n} services using this
     *     {@link LanguageRepo} instance.
     * </p>
     *
     * @param locale The locale to test for
     * @return Whether this locale has definitions and translations
     */
    public boolean hasLocale(Locale locale) {
        return this.definitions.containsKey(locale) && this.translations.containsKey(locale);
    }

    /**
     * Register a translation.
     * <p>
     * This method will fail with a {@link RuntimeException} if the translation
     * translates to a locale that does not have a registered language.
     * </p>
     *
     * @param translation The {@link TranslationDefinition} of the language
     */
    public void addTranslations(TranslationDefinition translation) {
        if (!this.translations.containsKey(translation.getLocale())) {
            throw new RuntimeException("Did not have locale: " + translation.getLocale().toLanguageTag());
        }

        this.translations.get(translation.getLocale()).add(translation.getTranslationLocation());
    }

    /**
     * Register a language in this repo.
     * <p>
     * A {@link LanguageDefinition} describes the language, and is needed in
     * order to load translations.
     * </p>
     *
     * @param definition The language to register
     */
    public void addLanguage(LanguageDefinition definition) {
        this.definitions.put(definition.getLocale(), definition);
        if (!this.translations.containsKey(definition.getLocale())) {
            this.translations.put(definition.getLocale(), new ArrayList<>());
        }
    }

    /**
     * Create a definition for the language in this locale.
     *
     * @param locale The locale to create a definition for.
     * @return The {@link LanguageDefinition} of the language in the supplied
     *         locale
     * @throws DefinitionLoadingException If the language could not be loaded
     */
    @Override
    public LanguageDefinition loadDefinition(Locale locale) throws DefinitionLoadingException {
        if (this.definitions.containsKey(locale)) {
            return this.definitions.get(locale);
        } else {
            throw new DefinitionLoadingException("Did not have definition for locale: " + locale.toLanguageTag());
        }
    }

    /**
     * Load the translations for a locale into a container
     *
     * @param container The container to load the messages into
     * @param locale The container to load translations for
     * @return The container now holding the messages
     * @throws TranslationLoadingException If an error occurs while loading the translations
     */
    @Override
    public TranslationContainer loadTranslations(TranslationContainer container, Locale locale) throws TranslationLoadingException {
        if (this.translations.containsKey(locale)) {
            List<URL> urls = this.translations.get(locale);
            Catalog catalog;
            try {
                catalog = loader.loadTranslations(urls);
            } catch (IOException e) {
                throw new TranslationLoadingException(e);
            }

            Map<String, String> singularMessages = new HashMap<>();
            Map<String, String[]> pluralMessages = new HashMap<>();
            for (Message message : catalog) {
                singularMessages.put(message.getMsgid(), message.getMsgstr());
                pluralMessages.put(message.getMsgidPlural(),
                        message.getMsgstrPlural().toArray(new String[message.getMsgstrPlural().size()]));
            }

            container.merge(singularMessages, pluralMessages);
        }
        return container;
    }
}
