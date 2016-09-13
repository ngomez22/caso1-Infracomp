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

	public synchronized boolean enviar(Mensaje m) {
		if (tam <= mensajes.size()) {
			notifyAll();
			return false;
		}
		mensajes.add(m);
		System.out.println("Buffer: Se añadio el mensaje " + m.getValor() + ". Ahora hay " + mensajes.size() + " mensajes.");
		notifyAll();
		return true;
	}

	public synchronized void terminoMensajes() {
		numClientes--;
		if (numClientes == 0) {
			termino = true;
			notifyAll();
		}
		notifyAll();
	}

	public synchronized void retirar() {
		while (0 == mensajes.size() && !termino) {
			try {
				wait();
			} catch (InterruptedException ie) {
				ie.printStackTrace();
			}
		}
		if (!termino) {
			Mensaje m = mensajes.remove(0);
			m.leer();
		}
	}

	public boolean termino() {
		return termino;
	}

	public void setTermino(boolean termino) {
		this.termino = termino;
	}
}
