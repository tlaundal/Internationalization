package no.minecraftfest.internationalization.formatter;

import net.md_5.bungee.api.ChatColor;
import org.cubeengine.dirigent.parser.component.Component;

public class ColorComponent implements Component {

    private final ChatColor color;

    public ColorComponent(ChatColor color) {
        this.color = color;
    }

    public ChatColor getColor() {
        return color;
    }
}
