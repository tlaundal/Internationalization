package no.minecraftfest.internationalization.formatter;

import com.google.common.collect.Lists;
import net.md_5.bungee.api.ChatColor;
import org.cubeengine.dirigent.context.Arguments;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ColorFormatterTest {

    private ColorFormatter formatter;

    @BeforeEach
    void setup() {
        this.formatter = new ColorFormatter();
    }

    @Test
    void format() {
        ChatColor color = ChatColor.RED;

        Arguments arguments = new Arguments(Lists.newArrayList(color.getName()), null);
        ColorComponent component = formatter.format(null, arguments);

        assertEquals(color, component.getColor());
    }

    @Test
    void getNames() {
        Set<String> names = formatter.getNames();
        assertTrue(names.contains("color"), "Container should have color as name");
        assertTrue(names.contains("style"), "Container should have style as name");
    }

}