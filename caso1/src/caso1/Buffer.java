package caso1;

import java.util.ArrayList;

public class Buffer {
	
	private int tam;
	
	private int numClientes;
	
	private boolean termino;
	
	private ArrayList<Mensaje> mensajes;
	
	public Buffer(int tam, int numClientes) {
		this.tam = tam;
		this.numClientes = numClientes;
		this.termino = false;
	}
	
	public synchronized void leer(Mensaje m) {
		while (tam <= mensajes.size()) {
			Thread.yield();
		}
		mensajes.add(m);
		notify();
		m.esperar();
	}
	
	public synchronized void terminoMensajes() {
		numClientes--;
		if (numClientes == 0) {
			termino = true;
		}
	}
	
	public synchronized void escribir() {
		while (!termino) {
			if (0 == mensajes.size()) {
				try {
					wait();
				} catch (InterruptedException ie) {
					ie.printStackTrace();
				}
			}
			Mensaje m = mensajes.remove(0);
			m.leer();
		}
	}
}
