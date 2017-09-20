package no.minecraftfest.internationalization;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.cubeengine.dirigent.builder.MessageBuilder;
import org.cubeengine.dirigent.context.Context;
import org.cubeengine.dirigent.parser.component.Component;
import org.cubeengine.dirigent.parser.component.TextComponent;
import org.cubeengine.dirigent.parser.component.UnresolvableMacro;
import org.cubeengine.dirigent.parser.element.Macro;
import org.cubeengine.dirigent.parser.element.NamedMacro;

public class ComponentMessageBuilder extends MessageBuilder<BaseComponent[], ComponentBuilder> {

    @Override
    public ComponentBuilder newBuilder() {
        return new ComponentBuilder("");
    }

    @Override
    public BaseComponent[] finalize(ComponentBuilder componentBuilder, Context context) {
        return componentBuilder.create();
    }

    @Override
    protected void buildText(TextComponent text, ComponentBuilder builder, Context context) {
        buildOther(ComponentHolder.of(new ComponentBuilder(text.getText()).create()), builder, context);
    }

    @Override
    protected void buildUnresolvable(UnresolvableMacro unresolved, ComponentBuilder builder, Context context) {
        ComponentBuilder localBuilder = new ComponentBuilder("")
                .bold(true).underlined(true)
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
        if (component instanceof ComponentHolder) {
            builder.append(((ComponentHolder)component).getComponent());
        } else {
            throw new IllegalArgumentException("Got unknown component: " + component.getClass().getSimpleName());
        }
    }

}
