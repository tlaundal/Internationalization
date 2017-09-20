package no.minecraftfest.internationalization.mock;

import no.minecraftfest.internationalization.TranslationDefinition;

import java.net.URL;
import java.util.Locale;

public class MockTranslationDefinition implements TranslationDefinition {
    private final Locale locale;
    private final URL url;

    public MockTranslationDefinition(Locale locale, URL url) {
        this.locale = locale;
        this.url = url;
    }

    @Override
    public Locale getLocale() {
        return this.locale;
    }

    @Override
    public URL getTranslationLocation() {
        return this.url;
    }
}
