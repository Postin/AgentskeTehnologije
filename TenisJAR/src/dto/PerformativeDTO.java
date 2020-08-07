package dto;

import model.Performative;

public class PerformativeDTO {

	private Performative performative;

	public PerformativeDTO() {
		
	}
	
	public PerformativeDTO(Performative performative) {
		super();
		this.performative = performative;
	}

	public Performative getPerformative() {
		return performative;
	}

	public void setPerformative(Performative performative) {
		this.performative = performative;
	}

	@Override
	public String toString() {
		return "PerformativeDTO [performative=" + performative + "]";
	}
	
	
}
