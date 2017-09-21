package no.minecraftfest.internationalization.builder;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import no.minecraftfest.internationalization.formatter.ColorComponent;
import org.cubeengine.dirigent.builder.MessageBuilder;
import org.cubeengine.dirigent.context.Context;
import org.cubeengine.dirigent.parser.component.Component;
import org.cubeengine.dirigent.parser.component.TextComponent;
import org.cubeengine.dirigent.parser.component.UnresolvableMacro;
import org.cubeengine.dirigent.parser.element.Macro;
import org.cubeengine.dirigent.parser.element.NamedMacro;

import java.util.WeakHashMap;

public class ComponentMessageBuilder extends MessageBuilder<BaseComponent[], ComponentBuilder> {

    private final WeakHashMap<ComponentBuilder, ChatColor> lastColor;

    public ComponentMessageBuilder() {
        lastColor = new WeakHashMap<>();
    }

    @Override
    public ComponentBuilder newBuilder() {
        return new ComponentBuilder("");
    }

    @Override
    public BaseComponent[] finalize(ComponentBuilder builder, Context context) {
        return builder.create();
    }

    @Override
    protected void buildText(TextComponent text, ComponentBuilder builder, Context context) {
        builder.append(text.getText());
        builder.color(lastColor.getOrDefault(builder, ChatColor.WHITE));
    }

    @Override
    protected void buildUnresolvable(UnresolvableMacro unresolved, ComponentBuilder builder, Context context) {
        ComponentBuilder localBuilder = new ComponentBuilder("")
                .bold(true).underlined(true).color(ChatColor.RED)
                .append("{{unresolved");

        Macro macro = unresolved.getMacro();
        if (macro instanceof NamedMacro) {
            localBuilder.append(": ")
                            .append(((NamedMacro)macro).getName())
                            .append("}}");
        }

        localBuilder.append("}}");

        buildOther(ComponentHolder.of(localBuilder.create()), builder, context);
    }

    @Override
    protected void buildOther(Component component, ComponentBuilder builder, Context context) {
        if (component instanceof ColorComponent) {
            ChatColor color = ((ColorComponent)component).getColor();
            lastColor.put(builder, color);
        } else if (component instanceof ComponentHolder) {
            ComponentHolder holder = (ComponentHolder) component;

            builder.append(holder.getComponents(), ComponentBuilder.FormatRetention.NONE);
            builder.color(holder.hasColor() ? holder.getColor().getColor()
                                            : lastColor.getOrDefault(builder, ChatColor.WHITE));
        } else {
            throw new IllegalArgumentException("Got unknown component: " + component.getClass().getSimpleName());
        }
    }

}
