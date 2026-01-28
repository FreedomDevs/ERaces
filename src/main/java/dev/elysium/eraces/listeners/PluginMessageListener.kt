package dev.elysium.eraces.listeners

import com.google.common.io.ByteArrayDataInput
import com.google.common.io.ByteArrayDataOutput
import com.google.common.io.ByteStreams
import dev.elysium.eraces.ERaces
import dev.elysium.eraces.abilities.AbilsManager
import dev.elysium.eraces.abilities.interfaces.IAbility
import kotlinx.serialization.json.buildJsonArray
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import org.bukkit.entity.Player
import org.bukkit.plugin.messaging.PluginMessageListener

class PluginMessageListener : PluginMessageListener {

    override fun onPluginMessageReceived(channel: String, player: Player, message: ByteArray) {
        if (channel != "elysium:eraces_cast") return

        val input: ByteArrayDataInput = ByteStreams.newDataInput(message)

        val type: String
        val payload: String
        try {
            type = input.readUTF()
            payload = input.readUTF()
        } catch (e: Exception) {
            ERaces.getInstance().logger.warning("Ошибка парсинга пакета eraces_cast от ${player.name}")
            return
        }

        if (type != "activate_ability") {
            ERaces.getInstance().logger.warning("Неизвестный тип пакета eraces_cast от ${player.name} ($type)")
            return
        }

        AbilsManager.getInstance().activate(player, payload)
    }

    companion object {
        fun sendAbilities(player: Player) {
            val out: ByteArrayDataOutput = ByteStreams.newDataOutput()
            out.writeUTF("abilities_list")

            val abilities: List<IAbility> = AbilsManager.getInstance().getPlayerAbilities(player)

            val abilitiesArray = buildJsonArray {
                for (ability in abilities) {
                    add(
                        buildJsonObject {
                            put("id", ability.id)
                            put("name", ability.name)
                            put("description", ability.description)
                        }
                    )
                }
            }

            val payload = abilitiesArray.toString()
            out.writeUTF(payload)

            ERaces.logger().info(payload)

            player.sendPluginMessage(ERaces.getInstance(), "elysium:eraces_cast", out.toByteArray())
        }
    }
}
