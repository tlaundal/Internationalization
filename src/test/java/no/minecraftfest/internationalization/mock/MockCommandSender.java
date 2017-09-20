package no.minecraftfest.internationalization.mock;

import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.Plugin;

import java.util.Set;

public class MockCommandSender implements CommandSender {

    private String lastMessage = null;

    public String getLastMessage() {
        return lastMessage;
    }

    public void sendMessage(String s) {
        lastMessage = s;
    }

    public void sendMessage(String[] strings) {
        lastMessage = String.join("\n", strings);
    }

    public Server getServer() {
        return null;
    }

    public String getName() {
        return null;
    }

    @Override
    public Spigot spigot() {
        return new MockCommandSenderSpigot();
    }


    public boolean isPermissionSet(String s) {
        return false;
    }

    public boolean isPermissionSet(Permission permission) {
        return false;
    }

    public boolean hasPermission(String s) {
        return false;
    }

    public boolean hasPermission(Permission permission) {
        return false;
    }

    public PermissionAttachment addAttachment(Plugin plugin, String s, boolean b) {
        return null;
    }

    public PermissionAttachment addAttachment(Plugin plugin) {
        return null;
    }

    public PermissionAttachment addAttachment(Plugin plugin, String s, boolean b, int i) {
        return null;
    }

    public PermissionAttachment addAttachment(Plugin plugin, int i) {
        return null;
    }

    public void removeAttachment(PermissionAttachment permissionAttachment) {

    }

    public void recalculatePermissions() {

    }

    public Set<PermissionAttachmentInfo> getEffectivePermissions() {
        return null;
    }

    public boolean isOp() {
        return false;
    }

    public void setOp(boolean b) {

    }

    private class MockCommandSenderSpigot extends Spigot {

        @Override
        public void sendMessage(BaseComponent component) {
            lastMessage = component.toPlainText();
        }

        @Override
        public void sendMessage(BaseComponent... components) {
            lastMessage = BaseComponent.toPlainText(components);
        }
    }
}
