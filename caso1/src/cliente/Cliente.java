package cliente;

import java.util.Random;

import caso1.Buffer;
import caso1.Mensaje;

public class Cliente extends Thread{
	
	private int id;
	
	private int numMensajes;
	
	private Buffer buffer;
	
	public Cliente(int id, int numMensajes, Buffer buffer) {
		this.id = id;
		this.numMensajes = numMensajes;
		this.buffer = buffer;
	}
	
	public void run() {
		Random rand = new Random();
		while (0 <= numMensajes) {
			Mensaje m = new Mensaje(rand.nextInt(100)+1);
			buffer.enviar(m);
			m.esperar();
			numMensajes--;
			System.out.println(id + " " + numMensajes);
		}
		buffer.terminoMensajes();
	}

	public long getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getNumMensajes() {
		return numMensajes;
	}

	public void setNumMensajes(int numMensajes) {
		this.numMensajes = numMensajes;
	}
	
	
}
