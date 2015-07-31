package utils;

public class FileModel {
	private String attribute = "Name";
	private String value;
	
	public FileModel (String name, String value){
		this.attribute = name;
		this.value = value;
	}
	
	public String getAttribute(){
		return attribute;
	}
	
	public String getValue(){
		return value;
	}

}
