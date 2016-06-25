/**
 * Copyright (c) 2016, Anthony Anderson<Illyohs>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *     * Redistributions of source code must retain the above copyright notice, this
 *        list of conditions and the following disclaimer.
 *
 *     * Redistributions in binary form must reproduce the above copyright notice,
 *        this list of conditions and the following disclaimer in the documentation
 *        and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package us.illyohs.showstopper

import java.util.{Timer, TimerTask}

import net.minecraft.command.{CommandBase, CommandException, ICommandSender}
import net.minecraft.server.MinecraftServer
import net.minecraft.util.text.TextComponentString
import net.minecraft.util.text.TextFormatting._

import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.Mod.EventHandler
import net.minecraftforge.fml.common.event.FMLServerStartingEvent

import com.mojang.authlib.GameProfile

@Mod(
  name = "Show Stopper",
  modid = "showstopper",
  modLanguage = "scala",
  acceptableRemoteVersions = "*",
  version = "1.0.1"
)
object ShowStopper {

  @EventHandler
  def serverStart(e:FMLServerStartingEvent): Unit = {
    e.registerServerCommand(new StapCommand)
  }
}

class StapCommand extends CommandBase {


  override def getCommandName: String = "showstop"

  override def getCommandUsage(sender: ICommandSender): String = "/showstop: Stops the server"

  override def getRequiredPermissionLevel: Int = 0

  @throws[CommandException]
  override def execute(server: MinecraftServer, sender: ICommandSender, args: Array[String]): Unit = {
    val gp:GameProfile = server.getPlayerList().getWhitelistedPlayers().getByName(sender.getName)
    if (server.getPlayerList.getWhitelistedPlayers.isWhitelisted(gp)) {
      server.getPlayerList.sendChatMsg(new TextComponentString(AQUA + sender.getName + RESET +":"+ GOLD +
        " Issued a stop command \n"+ RED +"The Server will be stopping in 15 seconds"))

      try {
        new Timer().schedule(new TimerTask {
          override def run(): Unit = server.initiateShutdown()
        }, 15000)
      } catch {
        case e: Exception => e.printStackTrace()
      }

    } else {
      sender.addChatMessage(new TextComponentString(RED + "You do not have permission to use this command"))
    }
  }

  override def checkPermission(server: MinecraftServer, sender: ICommandSender): Boolean = true

}
