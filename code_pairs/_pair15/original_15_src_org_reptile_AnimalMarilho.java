package org.reptile;

public class AnimalMarilho {

	public AnimalMarilho() {
		super();
	}
	
	protected int age;
	protected int name;
	protected int spead;
	protected String action;

	

	public int getName() {
		return name;
	}

	public void setName(int name) {
		this.name = name;
	}

	public int getSpead() {
		return spead;
	}

	public void setSpead(int spead) {
		this.spead = spead;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((action == null) ? 0 : action.hashCode());
		result = prime * result + age;
		result = prime * result + name;
		result = prime * result + spead;
		return result;
	}

}