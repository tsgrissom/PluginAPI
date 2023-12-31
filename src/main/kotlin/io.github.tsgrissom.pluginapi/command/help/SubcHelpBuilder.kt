package io.github.tsgrissom.pluginapi.command.help

import io.github.tsgrissom.pluginapi.extension.bukkit.appendc
import net.md_5.bungee.api.ChatColor.GRAY
import net.md_5.bungee.api.ChatColor.YELLOW
import net.md_5.bungee.api.chat.ClickEvent
import net.md_5.bungee.api.chat.ComponentBuilder
import net.md_5.bungee.api.chat.HoverEvent
import net.md_5.bungee.api.chat.TextComponent
import net.md_5.bungee.api.chat.hover.content.Text
import org.bukkit.permissions.Permission

class SubcHelpBuilder(val name: String) {

    val arguments: MutableList<SubcParameterBuilder> = mutableListOf()
    val description: MutableList<String> = mutableListOf()
    private var aliases: MutableList<String> = mutableListOf()
    var permission: Permission? = null
    private var suggestionOnClick: String? = null

    companion object {
        fun start(name: String) =
            SubcHelpBuilder(name)
    }

    fun withAliases(vararg s: String) : SubcHelpBuilder {
        aliases.addAll(s)
        return this
    }

    fun withArgument(arg: SubcParameterBuilder) : SubcHelpBuilder {
        arguments.add(arg)
        return this
    }

    fun withDescription(vararg s: String) : SubcHelpBuilder {
        description.addAll(s)
        return this
    }

    fun withPermission(s: String) : SubcHelpBuilder {
        permission = Permission(s)
        return this
    }

    fun withPermission(p: Permission) : SubcHelpBuilder {
        permission = p
        return this
    }

    fun withSuggestion(data: String) : SubcHelpBuilder {
        suggestionOnClick = data
        return this
    }

    fun clearSuggestion() : SubcHelpBuilder {
        suggestionOnClick = null
        return this
    }

    fun toComponent() : TextComponent {
        val text = TextComponent(name)

        if (suggestionOnClick != null) {
            val onClick = ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, suggestionOnClick)
            text.clickEvent = onClick
            text.isUnderlined = true

            if (aliases.isEmpty()) {
                val onHover = HoverEvent(HoverEvent.Action.SHOW_TEXT, Text("${GRAY}Click to suggest command"))
                text.hoverEvent = onHover
            }
        }

        if (aliases.isNotEmpty()) {
            val hoverBuilder = ComponentBuilder()

            hoverBuilder.appendc("Aliases: ", GRAY)

            for ((i, alias) in aliases.withIndex()) {
                hoverBuilder.appendc(alias, YELLOW)
                if (i != (aliases.size - 1))
                    hoverBuilder.appendc(",", GRAY)
            }

            val onHover = HoverEvent(HoverEvent.Action.SHOW_TEXT, Text(hoverBuilder.create()))
            text.hoverEvent = onHover
        }

        return text
    }
}