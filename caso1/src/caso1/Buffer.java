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
		this.mensajes = new ArrayList<>();
	}
	
	public synchronized void enviar(Mensaje m) {
		System.out.println("Llego el mensaje " + m.getValor());
		while (tam <= mensajes.size()) {
			Thread.yield();
		}
		mensajes.add(m);
		System.out.println("Se añadio el mensaje " + m.getValor());
		notify();
		m.esperar();
	}
	
	public synchronized boolean terminoMensajes() {
		numClientes--;
		if (numClientes == 0) {
			termino = true;
		}
		return termino;
	}
	
	public synchronized void retirar() {
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
