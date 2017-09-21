package no.minecraftfest.internationalization.builder;

import net.md_5.bungee.api.chat.BaseComponent;
import no.minecraftfest.internationalization.formatter.ColorComponent;
import org.cubeengine.dirigent.parser.component.Component;

public class ComponentHolder implements Component {

    private final BaseComponent[] component;

    private ColorComponent color = null;

    public ComponentHolder(BaseComponent... components) {
        this.component = components;
    }

    public static ComponentHolder of(BaseComponent... components) {
        return new ComponentHolder(components);
    }

    public BaseComponent[] getComponents() {
        return component;
    }

    public ColorComponent getColor() {
        return color;
    }

    public void setColor(ColorComponent color) {
        this.color = color;
    }

    public boolean hasColor() {
        return this.color != null;
    }
}
