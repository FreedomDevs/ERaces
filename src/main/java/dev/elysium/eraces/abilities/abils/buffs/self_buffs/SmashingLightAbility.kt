package dev.elysium.eraces.abilities.abils.buffs.self_buffs

import dev.elysium.eraces.abilities.RegisterAbility
import dev.elysium.eraces.abilities.abils.base.BaseCooldownAbility
import org.bukkit.attribute.Attribute
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.player.PlayerQuitEvent
import java.util.*

data class PlayerAbilityData(
    val hitsCount: Int = 0,
    val activated: Boolean = false
)

@RegisterAbility
@Suppress("unused")
class SmashingLightAbility : BaseCooldownAbility(id = "smashinglight", defaultCooldown = "2m"), Listener {

    private val MAX_DAMAGE_BONUS_PERCENT = 80;
    private val MIN_DAMAGE_BONUS_PERCENT = 50;
    private val HITS_AMOUNT = 3;
    //трекаем состояние
    private val PLAYER_ABILITY_DATA: MutableMap<UUID, PlayerAbilityData> = mutableMapOf()

    override fun onActivate(player: Player) {
        //добавляем в мапу только тех, кто активирует способность
        PLAYER_ABILITY_DATA.put(player.uniqueId, PlayerAbilityData(0, true));
    }
    // очищаем мапу когда игрок выходит с сервера
    @EventHandler
    private fun onPlayerLeave(event: PlayerQuitEvent) {
        PLAYER_ABILITY_DATA.remove(event.player.uniqueId);
    }

    @EventHandler
    fun onPlayerHit(event: EntityDamageByEntityEvent) {
        if (event.damager !is Player)
            return;
        val player = event.damager as Player;

        if (event.entity !is LivingEntity)
            return;
        val victim = event.entity as LivingEntity;

        //если способность не активирована
        if (!PLAYER_ABILITY_DATA.get(player.uniqueId)!!.activated)
            return;


        //при ударе мы поднимаем счетчик. но сбрасываем его, если удар третий.
        PLAYER_ABILITY_DATA.compute(player.uniqueId) { _, abilityData ->
            val current = abilityData?.hitsCount ?: 0;

            //отнимаем 1 потому что нам нужно значение перед максимальным. нельзя, чтобы счетчик показывал максимальное значение
            if (current == HITS_AMOUNT - 1)
            // сбрасываем счетчик и выключаем способность
                PlayerAbilityData(0, false)
            else
            // инкрементируем удары
                PlayerAbilityData(current + 1, true)

        }

        val bonusDamage = countBonusDamage(player);
        victim.damage(bonusDamage, player)
    }

    //здесь мы считаем бонусный урон от оружия в руке
    private fun countBonusDamage(player: Player): Double {
        val itemHeldByPlayer = player.inventory.itemInMainHand;
        val itemMeta = itemHeldByPlayer.itemMeta;
        val attibutes = itemMeta.getAttributeModifiers();

        val baseAttackDamageAttribute = attibutes!!.get(Attribute.ATTACK_DAMAGE);

        if (baseAttackDamageAttribute == null)
          return 0.toDouble();

        var totalDamage = 0.toDouble();
        for (modifier in baseAttackDamageAttribute)
            totalDamage += modifier.getAmount()

        val bonusPercent = (MAX_DAMAGE_BONUS_PERCENT..MIN_DAMAGE_BONUS_PERCENT).random().toDouble() * 0.01;

        return totalDamage * bonusPercent;
    }
}