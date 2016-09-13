package servidor;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import caso1.Buffer;
import cliente.Cliente;

public class Servidor extends Thread {
	
	private int id;
	
	private Buffer buffer;
	
	public Servidor(int id, Buffer buffer) {
		super();
		this.id = id;
		this.buffer = buffer;
	}
	
	public void run() {
		while (!buffer.termino()){
			buffer.retirar();
		}
		System.out.println("Servidor " + id + " termina su ejecución");
	}
	
	public long getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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
			int tamBuffer = Integer.parseInt(prop.getProperty("tamBuffer"));
			
			//Imprime resultados de la lectura en pantalla para verificación.
			System.out.println("------------------SETUP------------------------");
			System.out.println("Tamaño  del buffer: " + tamBuffer);
			System.out.println("Cantidad de servidores: " + nThreads);
			System.out.println("Cantidad de clientes: " + nClientes);
			System.out.println("-----------------------------------------------");
			
			//Crear el buffer
			Buffer b = new Buffer(tamBuffer, nClientes);
			//Crear los clientes
			Cliente[] clientes = new Cliente[nClientes];
			for (int i=0; i<nClientes; i++) {
				clientes[i] = new Cliente(i, nThreadsPorCliente[i], b);
				clientes[i].start();
			}
			//Crear los threads del servidor
			Servidor[] threads = new Servidor[nThreads];
			for (int i=0; i<nThreads; i++) {
				threads[i] = new Servidor(i, b);
				threads[i].start();
			}
			
			
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
