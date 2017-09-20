package no.minecraftfest.internationalization;

import org.fedorahosted.tennera.jgettext.Catalog;
import org.fedorahosted.tennera.jgettext.Message;
import org.fedorahosted.tennera.jgettext.PoParser;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;

public class GettextLoader {

    private final PoParser parser;
    private final Charset charset;

    public GettextLoader(Charset charset) {
        this.parser = new PoParser();
        this.charset = charset;
    }

    public Catalog loadTranslations(List<URL> files) throws IOException {
        Catalog messages = new Catalog();
        for (URL url : files) {
            Catalog local = this.parser.parseCatalog(url.openStream(), charset, true);
            for (Message message : local) {
                messages.addMessage(message);
            }
        }
        return messages;
    }
}
