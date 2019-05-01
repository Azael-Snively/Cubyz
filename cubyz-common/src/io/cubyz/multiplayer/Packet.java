package io.cubyz.multiplayer;

public class Packet {

	// server info related
	public static final byte PACKET_GETVERSION = 15;
	public static final byte PACKET_PINGPONG = 16; // TODO ping
	public static final byte PACKET_PINGDATA = 13;
	
	// player related
	public static final byte PACKET_SETBLOCK = 3;
	public static final byte PACKET_MOVE = 4;
	public static final byte PACKET_CHATMSG = 5;
	
}
