package no.minecraftfest.internationalization;

import net.md_5.bungee.api.chat.BaseComponent;
import org.cubeengine.dirigent.parser.component.Component;

public class ComponentHolder implements Component {

    private final BaseComponent[] component;

    public ComponentHolder(BaseComponent[] components) {
        this.component = components;
    }

    public static ComponentHolder of(BaseComponent[] components) {
        return new ComponentHolder(components);
    }

    public BaseComponent[] getComponent() {
        return component;
    }
}
