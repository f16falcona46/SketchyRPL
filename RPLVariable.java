import java.util.ArrayList;

public class RPLVariable {
	private String name;
	private RPLObject data;
	
	RPLVariable(String newName, RPLObject newData) {
		name=newName;
		data=newData;
	}
	
	public void setName(String newName) {
		name=newName;
	}
	
	public void setData(RPLObject newData) {
		data = newData;
	}
	
	public String getName() {
		return name;
	}
	
	public RPLObject getData() {
		return data.copy();
	}
	
	@Override
	public String toString() {
		return name;
	}
	
	@Override
	public boolean equals(Object otherName) {
		return this.getName().equals(((RPLVariable)otherName).getName());
	}
}
