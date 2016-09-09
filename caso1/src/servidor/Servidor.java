package servidor;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import caso1.Buffer;

public class Servidor extends Thread {
	
	private int id;
	
	private Buffer buffer;
	
	public Servidor(int id, Buffer buffer) {
		super();
		this.buffer = buffer;
	}

	public Buffer getBuffer() {
		return buffer;
	}

	public void setBuffer(Buffer buffer) {
		this.buffer = buffer;
	}
	
	public void run() {
		
	}
	
	public static void main (String[] args) {
		Properties prop = new Properties();
		try {
			//Cargar el archivo properties
			FileInputStream in = new FileInputStream("docs/setup.properties");
			prop.load(in);
			in.close();
			
			//Leer las propiedades
			int nThreads = Integer.parseInt(prop.getProperty("numServidores"));
			String[] threadsClientes = (prop.getProperty("numsConsultasPorCliente")).split(",");
			int[] nThreadsPorCliente = new int[threadsClientes.length];
			int nClientes = threadsClientes.length;
			for (int i=0; i<threadsClientes.length; i++){
				nThreadsPorCliente[i] = Integer.parseInt(threadsClientes[i]);
			}
			int tamBuffer = Integer.parseInt("tamBuffer");
			
			//Crear los componentes
			//Buffer b = new Buffer(tamBuffer, nClientes);
			Servidor[] threads = new Servidor[nThreads];
			for (int i=0; i<nThreads; i++) {
				threads[i] = new Servidor(i, null);
				threads[i].start();
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
