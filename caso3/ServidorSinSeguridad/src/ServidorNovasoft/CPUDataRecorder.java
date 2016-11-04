package ServidorNovasoft;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.management.ManagementFactory;

import com.sun.management.OperatingSystemMXBean;

public class CPUDataRecorder extends Thread{
	
	private String name;
	
	private boolean generateCPUData;
	
	public CPUDataRecorder(String name) {
		this.name = name;
		generateCPUData = true;
	}
	
	@Override
	public void run() {
		File file = new File("./data/CPU_Load_" + name + ".csv");
		OperatingSystemMXBean bean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
		int time = 0;
		try(BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
			file.createNewFile();
			bw.write("Time (s),CPU Load (%)\n");
			while(generateCPUData) {
				try{
		    		Thread.sleep(1000);
		    	}catch(InterruptedException e){
		    		e.printStackTrace();
		    	}
				String load = String.valueOf(100*bean.getSystemCpuLoad());
				bw.write(time + "," + load + "\n");
				time++;
			}
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void stopData() {
		generateCPUData = false;
	}
}
