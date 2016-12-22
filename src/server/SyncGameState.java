package server;

import iohelper.packet.Packet01SyncState;

class SyncGameState implements Runnable {
	private Server server;

	public SyncGameState(Server server) {
		this.server = server;
	}

	@Override
	public void run() {
		while (true) {
			
			//server.sendResponse();
			
			synchronized(this.server.getFlag()) {
				
				 while (this.server.getGame().isLocked()) {
					 System.out.println("Wait for it...");
					 try {
						this.server.getGame().wait();
					 } catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					 }
				 }
				
//				 Packet01SyncState responsePacket = new
//				 Packet01SyncState(game.composeState());
//				 responsePacket.writeData(this);
//					System.out.println("state of send" + this.getState());

				 
				 Packet01SyncState responsePacket = new Packet01SyncState(this.server.getGame().composeState());
				 responsePacket.writeData(this.server);
				 
				 this.server.getFlag().notifyAll();
				 
			 }
			
			// try {
			// server.sendResponse();
			// } catch (InterruptedException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }
			//// if(server.game.isChanged) {
			//// Packet02ClientAction actionPacket =
			// client.game.getClientPacket();
			//// actionPacket.writeData(client);
			//// }
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

}