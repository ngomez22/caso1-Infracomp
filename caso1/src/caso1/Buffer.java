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
		System.out.println("Se añadio el mensaje " + m.getValor() + ". Ahora hay " + mensajes.size());
		notify();
	}

	public synchronized void terminoMensajes() {
		numClientes--;
		if (numClientes == 0) {
			termino = true;
		}
	}

	public synchronized void retirar() {
		while (0 == mensajes.size()) {
			try {
				System.out.println("Esperando recibir");
				wait();
				System.out.println("Me despertaron");
			} catch (InterruptedException ie) {
				ie.printStackTrace();
			}
		}
		Mensaje m = mensajes.remove(0);
		System.out.println("Leyendo mensaje" + m.getValor());
		m.leer();
		System.out.println("Retiró el mensaje " + m.getValor());
	}

	public boolean termino() {
		return termino;
	}

	public void setTermino(boolean termino) {
		this.termino = termino;
	}
}
