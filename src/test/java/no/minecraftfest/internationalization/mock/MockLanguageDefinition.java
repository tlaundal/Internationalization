package no.minecraftfest.internationalization.mock;

import de.cubeisland.engine.i18n.language.LanguageDefinition;
import de.cubeisland.engine.i18n.plural.PluralExpr;

import java.util.Locale;

public class MockLanguageDefinition implements LanguageDefinition {
    private final Locale locale;
    private final int pluralCount;
    private final PluralExpr pluralExpression;

    public MockLanguageDefinition(Locale locale, int pluralCount, PluralExpr pluralExpression) {
        this.locale = locale;
        this.pluralCount = pluralCount;
        this.pluralExpression = pluralExpression;
    }

    @Override
    public Locale getLocale() {
        return this.locale;
    }

    @Override
    public String getName() {
        return getLocale().getDisplayLanguage();
    }

    @Override
    public String getLocalName() {
        return getLocale().getDisplayName();
    }

    @Override
    public Locale getParent() {
        return null;
    }

    @Override
    public Locale[] getClones() {
        return new Locale[0];
    }

    @Override
    public int getPluralCount() {
        return this.pluralCount;
    }

    @Override
    public PluralExpr getPluralExpression() {
        return this.pluralExpression;
    }
}
