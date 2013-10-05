package il.ac.shenkar.common;

public class ListItem {

	private  int id;
	private  int iconId;
	private  String header;
	private String description;
	

	public ListItem(int id1, int iconId1, String header1,String description1) {

		id = id1;
		iconId = iconId1;
		header = header1;
		description=description1;
	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public int getIconFile() {
		return iconId;
	}


	public void setIconFile(int iconId) {
		this.iconId = iconId;
	}


	public String getHeader() {
		return header;
	}


	public void setHeader(String header) {
		this.header = header;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}

}
