package com.baihuogou.diviner;

import java.util.TimerTask;

public class DivinerTask extends TimerTask {

	@Override
	public void run() {
		Result.action();
		
	}

}
