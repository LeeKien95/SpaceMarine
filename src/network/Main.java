package network;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
	public static void main(String[] args) {
		int port = 8085;
		UdpUnicastServer server = new UdpUnicastServer(port);
		UdpUnicastClient client = new UdpUnicastClient(port);
		
		ExecutorService executorService = Executors.newFixedThreadPool(2);
		executorService.submit(client);
		executorService.submit(server);
	}
}
