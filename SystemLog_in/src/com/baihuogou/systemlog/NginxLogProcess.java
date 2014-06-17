package com.baihuogou.systemlog;

public class NginxLogProcess extends Thread {
	
	private int StartRow,EndRow;
	
	
	public NginxLogProcess(int StartRow,int EndRow){
		this.StartRow=StartRow;
		this.EndRow=EndRow;
	}

	@Override
	public void run(){
		proc();
	}
	
	private void proc(){
		
	}
}
