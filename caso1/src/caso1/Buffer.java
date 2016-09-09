package caso1;

import java.util.ArrayList;

public class Buffer {
	
	private int tam;
	
	private ArrayList<Mensaje> mensajes;
	
	public Buffer(int tam) {
		this.tam = tam;
	}
	
	public synchronized void escribir(Mensaje m) {
		while (tam <= mensajes.size()) {
			Thread.yield();
		}
		mensajes.add(m);
		m.esperar();
	}
}
