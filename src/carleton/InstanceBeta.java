package carleton;

import java.io.File;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

public class InstanceBeta extends Instance {

	public int [][] cpu;
	public int[][] mem;
	public int[][] packets;
	public int[][] bandwidth;
	public int[][] wifiType;
	public int season_cnt;
	public InstanceBeta(int clentsnum, int k_cnt, String path) {
		super(clentsnum, k_cnt,path);
		season_cnt = this.cpu.length;
	}
	
	@Override
	public void serializer(String path) {
		try {
		WritableWorkbook workbook = Workbook.createWorkbook(new File(path+".xls"));
		WritableSheet sheet = workbook.createSheet("Sheet1",0);
		//Writing Label
		for(int i=0;i<labelPosition.length;i++) {
		sheet.addCell(new Label(labelPosition[i][1],labelPosition[i][0],labelName[i]));
		}
		//Writing user-fog distance 
		int p=1;
		for(int i= 0;i<clents;i++) {
			for(int j = 0;j<fogs;j++) {
				sheet.addCell(new Number(0,p,i+1));
				sheet.addCell(new Number(1,p,j+1));
				double dis = Math.sqrt(Math.pow((rx[i]-Nodes[j][0]),2)+Math.pow((ry[i]-Nodes[j][1]),2));
				sheet.addCell(new Number(2,p,dis));
				p++;
			}
		}
		//Writing fog-fog distance
		p=1;
		for(int i=0;i<fogs;i++) {
		for (int j=0;j<fogs;j++)
		{
			sheet.addCell(new Number(4,p,j+1));
			sheet.addCell(new Number(5,p,j+1));
			double dis = Math.sqrt(Math.pow((Nodes[i][0]-Nodes[j][0]),2)+Math.pow((Nodes[i][0]-Nodes[j][1]),2));
			sheet.addCell(new Number(6,p,dis));
			p++;
			
		}
		}
		//Writing user-request table
		p=1;
		for(int i=0;i<clents;i++) {
			sheet.addCell(new Number(9,p,i+1));
				for(int j=0;j<cpu.length;j++) {
				 sheet.addCell(new Number(10+j*5,p,cpu[j][i]));
					
					sheet.addCell(new Number(11+j*5,p,mem[j][i]));
					sheet.addCell(new Number(12+j*5,p,packets[j][i]));
					sheet.addCell(new Number(13+j*5,p,bandwidth[j][i]));
					sheet.addCell(new Number(14+j*5,p,wifiType[j][i]));
				}
				
			p++;	
			}
		
			
		workbook.write();
		workbook.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	@Override
	public void InstanceBuilder(int clentsnum, int k_cnt, String path) {
		this.InstanceBuilder(clentsnum, k_cnt, 2, path);
	}
	public void InstanceBuilder(int clentsnum, int k_cnt, int season_cnt, String path ) {
		File varTmpDir = new File(path+".ser");
		System.out.println("checking path:  "+path +".ser");
		boolean exists = varTmpDir.exists();
		if(!exists) {
			
		clents = clentsnum;
		fogs = k_cnt;
		results  = new ConcurrentLinkedQueue<Object[]>();
		rx = new double[clentsnum];
		ry = new double[clentsnum];
		w = new double[clentsnum];
		cpu = new int[season_cnt][clentsnum];
		mem= new int[season_cnt][clentsnum];
		packets = new int[season_cnt][clentsnum];
		bandwidth = new int[season_cnt][clentsnum];
		wifiType = new int[season_cnt][clentsnum];
		//Nodes = nodeAdder(10.0,12,3);
		Nodes = nodeRandomizer(areaScale, k_cnt);
        //Nodes = nodeCutter((areaScale),(int)Math.sqrt(k_cnt));
		for (int i = 0; i < clentsnum; i++) {
			
            rx[i] = areaScale[0] * Math.random();
            ry[i] = areaScale[1] * Math.random();
            Random r = new Random();
            int nn = r.nextInt(141)+10;
            w[i] = r.nextGaussian()*10+50;
            cpu[0][i] = nn*(r.nextInt(4)+1);
            mem[0][i]= nn*(r.nextInt(40)+1);
            packets[0][i]= nn*(r.nextInt(64)+1);
            bandwidth[0][i] = packets[0][i] *1500; 
            wifiType[0][i]=nn*((r.nextInt(6)+2)*10);
          
        
		for(int j=1;j<season_cnt;j++) {
			if(Math.random()<0.5) {
				cpu[j][i] = (int) (cpu[j-1][i]*1.3);
	            mem[j][i]= (int) (mem[j-1][i]*1.3);
	            packets[j][i]= (int) (packets[j-1][i]*1.3);
	            bandwidth[j][i] = (int) (bandwidth[j-1][i]*1.3);
	            wifiType[j][i]= (int) (wifiType[j-1][i]*1.3);
			}else {
				cpu[j][i] = (int) (cpu[j-1][i]*0.7);
	            mem[j][i]= (int) (mem[j-1][i]*0.7);
	            packets[j][i]= (int) (packets[j-1][i]*0.7);
	            bandwidth[j][i] = (int) (bandwidth[j-1][i]*0.7);
	            wifiType[j][i]= (int) (wifiType[j-1][i]*0.7);
			}
		}
		}
		this.serializer(path);
		this.objectSerializer(path);
		}else {
			System.out.println("Instance exists, loading cached ser");
			InstanceBeta tmp = objectDeserializer(path);
			clents = tmp.clents;
			fogs = tmp.fogs;
			results  = new ConcurrentLinkedQueue<Object[]>();
			rx = tmp.rx;
			ry = tmp.ry;
			w = tmp.w;
			cpu = tmp.cpu;
			mem= tmp.mem;
			packets = tmp.packets;
			bandwidth = tmp.bandwidth;
			wifiType = tmp.wifiType;
			//Nodes = nodeAdder(10.0,12,3);
			Nodes = tmp.Nodes;

		}
		
		
		
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String path = (1000)+"-"+(5)+"-Sep21-season-instance";
		InstanceBeta t = new InstanceBeta(1000,5,path);
	}

}
