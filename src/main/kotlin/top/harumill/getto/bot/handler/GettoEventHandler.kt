package top.harumill.getto.bot.handler

import net.mamoe.mirai.event.Event
import net.mamoe.mirai.event.GlobalEventChannel
import net.mamoe.mirai.event.Listener
import net.mamoe.mirai.event.events.BotEvent
import net.mamoe.mirai.event.events.MessageEvent
import top.harumill.getto.bot.GettoBot

object GettoEventHandler {
    val gettoEventChannel = GlobalEventChannel.filter { it is BotEvent && it.bot.id == GettoBot.bot.id }

    inline fun <reified E : Event> listenEvent(noinline handler: suspend E.(E) -> Unit): Listener<E> {
        return gettoEventChannel.filter { it is E }.subscribeAlways(handler = handler)
    }

    inline fun <reified E : MessageEvent> listenMessageEvent(noinline handler: suspend E.(E) -> Unit): Listener<E> {
        return gettoEventChannel.filter { it is E }.subscribeAlways(handler = handler)
    }


}