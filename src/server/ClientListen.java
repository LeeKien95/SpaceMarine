package server;

import java.net.DatagramPacket;
import java.net.InetAddress;

import game.Player;
import iohelper.packet.Packet;
import iohelper.packet.Packet00Login;
import iohelper.packet.Packet02ClientAction;
import iohelper.packet.Packet.PacketTypes;

class ClientListen implements Runnable {
	private Server server;

	public ClientListen(Server server) {
		this.server = server;
	}

	@Override
	public void run() {
		while (true) {
//			try {
//				server.getRequest();
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			
			 DatagramPacket packet = server.getIO().getPacket();
//			 System.out.println("got a request");

//			this.server.getGame().setLocked(true);
			
//			this.server.getGame().setLocked(false);
			
			synchronized (this.server.getFlag()) {

				while (this.server.getGame().isLocked()) {
					System.out.println("Wait for it...");
					try {
						this.server.getGame().wait();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				this.server.getFlag().notifyAll();

				// DatagramPacket packet = server.getIO().getPacket();
				// System.out.println("got a request");

//				this.server.getGame().setLocked(true);
				//parsePacket(packet.getData(), packet.getAddress(), packet.getPort());
//				this.server.getGame().setLocked(false);
				
				parsePacket(packet.getData(), packet.getAddress(), packet.getPort());
				this.server.getFlag().notifyAll();
			}
			
			// if(server.game.isChanged) {
			// Packet02ClientAction actionPacket =
			// client.game.getClientPacket();
			// actionPacket.writeData(client);
			// }
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private void parsePacket(byte[] data, InetAddress address, int port) {
		String message = new String(data).trim();
		PacketTypes type = Packet.lookupPacket(Integer.parseInt(message.substring(0, 2)));
		switch (type) {
		default:
		case INVALID:
			break;
		case LOGIN:
			// Convert data to Packet to use the actual meaningful data
			Packet00Login packet = new Packet00Login(data);
			System.out.println(address + ":" + port + " has connected...");

			// Them player moi vao game
			Player newPlayer = new Player(packet.getPlayerName(), address, port);
			server.getGame().setCurrentPlayerName(packet.getPlayerName());
			server.getGame().jetfighters.add(newPlayer);

//			// Send response to all client
//			Packet01SyncState responsePacket = new Packet01SyncState(game.composeState());
//			responsePacket.writeData(this);
		case SYNC:

			break;

		case ACTION:
			Packet02ClientAction actionPacket = new Packet02ClientAction(data);
			// System.out.println("Got an action from client " +
			// address.getHostName() + ":" + port);
			// SERVER SE XU LY ACTION O DAY (CAP NHAT STATE)
			// DUNG actionPacket.getClientName(), getXDirection()....
//			System.out.println("received " + actionPacket.isShot());
			server.getGame().moveJet(actionPacket.getClientName(), actionPacket.getxDirection(), actionPacket.getyDirection(),
					actionPacket.isMoving(), actionPacket.isShot());
			break;

		case DISCONNECT:
			break;
		}
	}

}