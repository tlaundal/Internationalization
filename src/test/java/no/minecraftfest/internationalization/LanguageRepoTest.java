package no.minecraftfest.internationalization;

import de.cubeisland.engine.i18n.language.DefinitionLoadingException;
import de.cubeisland.engine.i18n.language.LanguageDefinition;
import de.cubeisland.engine.i18n.plural.NotOneExpr;
import de.cubeisland.engine.i18n.translation.TranslationContainer;
import de.cubeisland.engine.i18n.translation.TranslationLoadingException;
import no.minecraftfest.internationalization.mock.MockLanguageDefinition;
import no.minecraftfest.internationalization.mock.MockTranslationDefinition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.URL;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

class LanguageRepoTest {

    private LanguageRepo repo;

    @BeforeEach
    void setUp() {
        this.repo = new LanguageRepo();
    }

    @Test
    void languageDefinition() {
        Locale locale = new Locale.Builder().setLanguage("nb").build();
        LanguageDefinition definition = new MockLanguageDefinition(locale, 2, new NotOneExpr());

        repo.addLanguage(definition);

        try {
            LanguageDefinition actual = repo.loadDefinition(locale);
            assertEquals(actual, definition, "Loaded definition should be same as added language");
        } catch (DefinitionLoadingException e) {
            fail(e);
        }
    }

    @Test
    void test1() {
        Locale locale = new Locale.Builder().setLanguage("nb").build();
        loadTestLanguage(this.repo);

        try {
            TranslationContainer container = repo.loadTranslations(new TranslationContainer(), locale);
            assertNotNull(container);
            assertTrue(container.getSingular("").contains("\n"));
        } catch (TranslationLoadingException e) {
            fail(e);
        }
    }

    static void loadTestLanguage(LanguageRepo repo) {
        Locale locale = new Locale.Builder().setLanguage("nb").build();
        URL url = LanguageRepoTest.class.getClassLoader().getResource("no.po");

        repo.addLanguage(new MockLanguageDefinition(locale, 2, new NotOneExpr()));
        repo.addTranslations(new MockTranslationDefinition(locale, url));
    }

}