package cliente;

import caso1.Buffer;
import caso1.Mensaje;

public class Cliente extends Thread{
	
	private int numMensajes;
	
	private Buffer buffer;
	
	public Cliente(int numMensajes, Buffer buffer) {
		this.numMensajes = numMensajes;
		this.buffer = buffer;
	}
	
	public void run() {
		while (0 < numMensajes) {
			Mensaje m = new Mensaje((int)Math.random()*101);
			buffer.enviar(m);
		}
	}
}
