package com.baihuogou.systemlog.statistics;

import java.util.List;

public class OrderProduct{
	private String ProductId;
	private Integer Number;
	private List<String> IpAddress;
	
	public OrderProduct(){
		
	}
	
	public OrderProduct(String ProductId,Integer Number,List<String> IpAddress){
		this.ProductId=ProductId;
		this.Number=Number;
		this.IpAddress=IpAddress;
	}
	public String toString(){
		return null;
	}

	public String getProductId() {
		return ProductId;
	}

	public void setProductId(String productId) {
		ProductId = productId;
	}

	public List<String> getIpAddress() {
		return IpAddress;
	}

	public void setIpAddress(List<String> ipAddress) {
		IpAddress = ipAddress;
	}

	public Integer getNumber() {
		return Number;
	}

	public void setNumber(Integer number) {
		Number = number;
	}
	
	public String toAddressString(){
		StringBuilder str=new StringBuilder();
		for(String s:IpAddress){
			str.append(s+";");
		}
		return str.toString().substring(0,str.length()-1);
	}
}
