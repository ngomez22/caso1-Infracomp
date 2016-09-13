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
		while (0 < numMensajes) {
			int num = rand.nextInt(100)+1;
			Mensaje m = new Mensaje(num);
			buffer.enviar(m);
			m.esperar();
			if (m.getValor() == num+1) {
				System.out.println("Mensaje " + numMensajes + " del cliente " + id + " fue procesado correctamente");
			} else {
				System.out.println("ERROR: Mensaje " + numMensajes + " del cliente " + id + " fue procesado incorrectamente");
			}
			numMensajes--;
		}
		buffer.terminoMensajes();
		System.out.println("Cliente " + id +  " termina su ejecución");
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
