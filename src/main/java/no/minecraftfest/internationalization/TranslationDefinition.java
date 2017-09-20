package no.minecraftfest.internationalization;

import java.net.URL;
import java.util.Locale;

/**
 * A definition of a translation file
 * <p>
 * A translation file contains translations from the source locale into a locale
 * stored in a {@code .po} file.
 * </p>
 */
public interface TranslationDefinition {

    /**
     * Get the locale this translation translates to
     *
     * @return The locale this translation translates into
     */
    Locale getLocale();

    /**
     * Get the {@link URL} of the translation file
     *
     * @return The location of the translation file
     */
    URL getTranslationLocation();

}
