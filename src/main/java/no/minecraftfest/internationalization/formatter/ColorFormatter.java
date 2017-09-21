package no.minecraftfest.internationalization.formatter;

import com.google.common.collect.Sets;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;
import no.minecraftfest.internationalization.builder.ComponentHolder;
import org.cubeengine.dirigent.context.Arguments;
import org.cubeengine.dirigent.context.Context;
import org.cubeengine.dirigent.formatter.ConstantFormatter;

import java.util.Set;

public class ColorFormatter extends ConstantFormatter {

    private static final Set<String> NAMES = Sets.newHashSet("color", "style");

    @Override
    public ColorComponent format(Context context, Arguments arguments) {
        for (ChatColor color : ChatColor.values()) {
            if (arguments.has(color.name().toLowerCase())) {
                return new ColorComponent(color);
            }
        }

        StringBuilder values = new StringBuilder();
        arguments.getValues().forEach(v -> values.append(v).append(", "));

        throw new IllegalArgumentException("Could not find color in values: " + values);
    }

    @Override
    public Set<String> getNames() {
        return NAMES;
    }
}
