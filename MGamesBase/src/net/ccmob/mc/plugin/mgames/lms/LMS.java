package net.ccmob.mc.plugin.mgames.lms;

import java.util.ArrayList;

import net.ccmob.math.Vector;
import net.ccmob.mc.plugin.mgames.base.MGame;
import net.ccmob.xml.XMLConfig;
import net.ccmob.xml.XMLConfig.XMLNode;

import org.bukkit.entity.Player;

public class LMS extends MGame {

	public static LMS instance;

	private ArrayList<LMSArena> arenas = new ArrayList<LMSArena>();

	public LMS() {
		super("LMS");
		instance = this;
	}

	public void load() {
		loadArenas();
	}

	public void unload() {

	}

	@Override
	public boolean handleCommand(String name, String[] args, Player p) {
		if (name.equalsIgnoreCase("lms")) {
			System.out.println("[LMS] LMS command issued.");
			if (args.length > 1) {
				System.out.println("[LMS] Args[0] - " + args[0]);
				System.out.println("[LMS] Args[1] - " + args[1]);
				if (args[0].equalsIgnoreCase("join")) {
					System.out.println("[LMS] - Player " + p.getName() + " tries to join " + args[1]);
					for (LMSArena arena : LMS.instance.arenas) {
						if (arena.getArenaName().equalsIgnoreCase(args[1])) {
							arena.addPlayer(p);
							return true;
						}
					}
				} else if (args[0].equalsIgnoreCase("leave")) {
					System.out.println("[LMS] - Player " + p.getName() + " tries to leave " + args[1]);
					for (LMSArena arena : LMS.instance.arenas) {
						if (arena.getArenaName().equalsIgnoreCase(args[1])) {
							arena.removePlayer(p);
							return true;
						}
					}
				} else if (args[0].equalsIgnoreCase("start")) {
					System.out.println("[LMS] - Player " + p.getName() + " tries to start " + args[1]);
					for (LMSArena arena : LMS.instance.arenas) {
						if (arena.getArenaName().equalsIgnoreCase(args[1])) {
							arena.startGame();
							return true;
						}
					}
				} else if (args[0].equalsIgnoreCase("generate")) {
					System.out.println("[LMS] - Player " + p.getName() + " tries to generate " + args[1]);
					for (LMSArena arena : LMS.instance.arenas) {
						if (arena.getArenaName().equalsIgnoreCase(args[1])) {
							arena.rebuild();
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	public void loadArenas() {
		XMLConfig arenaConfig = new XMLConfig("plugins/lmsArenas.xml");
		XMLNode node = arenaConfig.getRootNode();
		if (!node.nodeExists("defaults")) {
			XMLNode defaults = new XMLNode("defaults");
			XMLNode cubePadding = new XMLNode("option");
			cubePadding.addAttribute("name", "cubePadding");
			cubePadding.addAttribute("value", "10");
			XMLNode cubeDiameter = new XMLNode("option");
			cubeDiameter.addAttribute("name", "cubeDiameter");
			cubeDiameter.addAttribute("value", "5");
			XMLNode cubeDistance = new XMLNode("option");
			cubeDistance.addAttribute("name", "cubeDistance");
			cubeDistance.addAttribute("value", "10");
			defaults.addChild(cubePadding);
			defaults.addChild(cubeDiameter);
			defaults.addChild(cubeDistance);
			node.addChild(defaults);
			arenaConfig.save("plugins/lmsArenas.xml");
		}
		if (node.getName().equalsIgnoreCase("arenas") && node.nodeExists("defaults")) {
			if (node.nodeExists("arena")) {
				for (XMLNode subNode : node.getChilds()) {
					if (subNode.getName().equalsIgnoreCase("arena")) {
						if (subNode.attributeExists("name")) {
							System.out.println("Found arena : " + (String) subNode.getAttribute("name").getAttributeValue());
							loadArena(subNode, node.getChild("defaults"));
						}
					}
				}
			}
		}
	}

	public void loadArena(XMLNode arenaNode, XMLNode defaultNode) {
		if (arenaNode.nodeExists("start") && arenaNode.nodeExists("end") && arenaNode.attributeExists("name") && arenaNode.attributeExists("world") && arenaNode.attributeExists("uid")) {
			LMSArena arena = new LMSArena();
			System.out.println("[LMS] Found valid arena : " + arenaNode.getAttribute("name").getAttributeValue() + " in world " + arenaNode.getAttribute("world").getAttributeValue());
			arena.setArenaName((String) arenaNode.getAttribute("name").getAttributeValue());
			arena.setWorldName((String) arenaNode.getAttribute("world").getAttributeValue());
			if (arenaNode.getChild("start").attributeExists("x") && arenaNode.getChild("start").attributeExists("y") && arenaNode.getChild("start").attributeExists("z") && arenaNode.getChild("end").attributeExists("x") && arenaNode.getChild("end").attributeExists("y") && arenaNode.getChild("end").attributeExists("z")) {
				int x = 0, y = 0, z = 0;
				x = Integer.valueOf((String) arenaNode.getChild("start").getAttribute("x").getAttributeValue());
				y = Integer.valueOf((String) arenaNode.getChild("start").getAttribute("y").getAttributeValue());
				z = Integer.valueOf((String) arenaNode.getChild("start").getAttribute("z").getAttributeValue());
				System.out.println("[LMS|" + arenaNode.getAttribute("name").getAttributeValue() +"] Start x,y,z: " + x + "," + y + "," + z);
				arena.setVectorStart(new Vector(x, y, z));
				x = Integer.valueOf((String) arenaNode.getChild("end").getAttribute("x").getAttributeValue());
				y = Integer.valueOf((String) arenaNode.getChild("end").getAttribute("y").getAttributeValue());
				z = Integer.valueOf((String) arenaNode.getChild("end").getAttribute("z").getAttributeValue());
				arena.setVectorEnd(new Vector(x, y, z));
				System.out.println("[LMS|" + arenaNode.getAttribute("name").getAttributeValue() +"] End x,y,z: " + x + "," + y + "," + z);
			}
			if (arenaNode.nodeExists("options")) {
				for (XMLNode option : arenaNode.getChild("options").getChilds()) {
					if (option.attributeExists("name") && option.attributeExists("value")) {
						if (((String) option.getAttribute("name").getAttributeName()).equalsIgnoreCase("cubeDiameter")) {
							arena.setCubeDistance(Integer.valueOf((String) option.getAttribute("cubeDiameter").getAttributeValue()));
						}
						if (((String) option.getAttribute("name").getAttributeName()).equalsIgnoreCase("cubeDistance")) {
							arena.setCubeDistance(Integer.valueOf((String) option.getAttribute("cubeDistance").getAttributeValue()));
						}
						if (((String) option.getAttribute("name").getAttributeName()).equalsIgnoreCase("cubePadding")) {
							arena.setMargin(Integer.valueOf((String) option.getAttribute("cubePadding").getAttributeValue()));
						}
					}
				}
			} else {
				for (XMLNode option : defaultNode.getChilds()) {
					if (option.attributeExists("name") && option.attributeExists("value")) {
						if (((String) option.getAttribute("name").getAttributeName()).equalsIgnoreCase("cubeDiameter")) {
							arena.setCubeDistance(Integer.valueOf((String) option.getAttribute("cubeDiameter").getAttributeValue()));
						}
						if (((String) option.getAttribute("name").getAttributeName()).equalsIgnoreCase("cubeDistance")) {
							arena.setCubeDistance(Integer.valueOf((String) option.getAttribute("cubeDistance").getAttributeValue()));
						}
						if (((String) option.getAttribute("name").getAttributeName()).equalsIgnoreCase("cubePadding")) {
							arena.setMargin(Integer.valueOf((String) option.getAttribute("cubePadding").getAttributeValue()));
						}
					}
				}
			}
			this.getArenas().add(arena);
		}
	}

	/**
	 * @return the arenas
	 */
	public ArrayList<LMSArena> getArenas() {
		return arenas;
	}

	/**
	 * @param arenas
	 *            the arenas to set
	 */
	public void setArenas(ArrayList<LMSArena> arenas) {
		this.arenas = arenas;
	}

}
