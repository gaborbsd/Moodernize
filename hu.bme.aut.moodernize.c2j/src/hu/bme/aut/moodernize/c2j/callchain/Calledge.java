package hu.bme.aut.moodernize.c2j.callchain;

public class Calledge {
	private String caller;

	public String getCaller() {
		return caller;
	}

	public void setCaller(String caller) {
		this.caller = caller;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	private String target;

	public Calledge(String caller, String target) {
		this.caller = caller;
		this.target = target;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Calledge) {
			Calledge other = (Calledge) obj;
			if (other.getCaller().equals(this.getCaller()) && other.getTarget().equals(this.getTarget())) {
				return true;
			}
		}
		return false;
	}
}
