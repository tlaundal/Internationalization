package no.minecraftfest.internationalization.formatter;

import net.md_5.bungee.api.chat.BaseComponent;
import no.minecraftfest.internationalization.builder.ComponentHolder;
import org.cubeengine.dirigent.context.Arguments;
import org.cubeengine.dirigent.context.Context;
import org.cubeengine.dirigent.formatter.AbstractFormatter;
import org.cubeengine.dirigent.parser.component.Component;

public class ComponentFormatter extends AbstractFormatter<BaseComponent> {

    public ComponentFormatter() {
        super("component");
    }

    @Override
    protected Component format(BaseComponent component, Context context, Arguments arguments) {
        return ComponentHolder.of(component);
    }
}
