package io.github.skepter.asm_bukkit;

import java.io.IOException;

import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import com.sun.tools.attach.AgentInitializationException;
import com.sun.tools.attach.AgentLoadException;
import com.sun.tools.attach.AttachNotSupportedException;

import io.github.skepter.asm_loader.Agent;
import io.github.skepter.asm_loader.Profile;
import io.github.skepter.asm_loader.ProfileClassAdapter;
import io.github.skepter.asm_loader.ProfileMethodAdapter;
import io.github.skepter.asm_loader.Util;

public class Main extends JavaPlugin implements Listener {

	@Override
	public void onEnable() {

		try {
			Util.attachAgentToJVM(Util.getPID(), Agent.class, Util.class, Profile.class, ProfileClassAdapter.class, ProfileMethodAdapter.class);
		} catch (IOException e) {
			System.out.println("IO Exception");
			e.printStackTrace();
		} catch (AttachNotSupportedException e) {
			System.out.println("Attaching isn't supported :(");
			e.printStackTrace();
		} catch (AgentLoadException e) {
			System.out.println("Agent won't load");
			e.printStackTrace();
		} catch (AgentInitializationException e) {
			System.out.println("Agent won't initialize");
			e.printStackTrace();
		}

	}
}
