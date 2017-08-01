package ch.blutch.battlephoto.model.impl;

public enum Medal {

	BRONZE(1, "bronze.png"),
	SILVER(2, "silver.png"),
	GOLDER(3, "golden.png"),
	STAR(4, "star.png");
	
	private int value;
	private String filename;
	private static final String medalsFolder = "medals";
	
	Medal(int value, String filename) {
		this.value = value;
		this.filename = filename;
	}
	
	public String getMedalPath() {
		return medalsFolder + "/" + filename;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}
	
}
