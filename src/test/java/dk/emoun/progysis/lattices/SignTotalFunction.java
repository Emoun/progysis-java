package dk.emoun.progysis.lattices;

import java.util.Map;

public class SignTotalFunction extends TotalFunction<SignTotalFunction, String, SignPowerSet>{

//Constructors
	protected SignTotalFunction(Map<String, SignPowerSet> mapping, SignPowerSet lattice) {
		super(mapping, lattice);
		(new SignPowerSet());
	}
	

	public SignTotalFunction(SignPowerSet lattice, String[] keys) {
		super(lattice, keys);
	}

//Overriding methods
	@Override
	protected SignTotalFunction constructTotalFunction(Map<String, SignPowerSet> mapping) {
		// TODO Auto-generated method stub
		return new SignTotalFunction(mapping, getLattice().getBottom());
	}

}
