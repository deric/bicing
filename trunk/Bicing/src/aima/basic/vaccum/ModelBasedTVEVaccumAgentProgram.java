package aima.basic.vaccum;

import aima.basic.Agent;
import aima.basic.AgentProgram;
import aima.basic.Percept;

/**
 * @author Ravi Mohan
 * 
 */
public class ModelBasedTVEVaccumAgentProgram extends AgentProgram {
	VaccumEnvironmentModel myModel;

	ModelBasedTVEVaccumAgentProgram(VaccumEnvironmentModel model) {
		myModel = model;
	}

	@Override
	public String execute(Percept percept) {
		String location = (String) percept.getAttribute("location");
		String locationStatus = (String) percept.getAttribute("status");
		myModel.setLocationStatus(location, locationStatus);

		if (myModel.bothLocationsClean()) {
			return Agent.NO_OP;
		} else if (locationStatus.equals("Dirty")) {
			return "Suck";
		} else if (location.equals("A")) {
			return "Right";
		} else if (location.equals("B")) {
			return "Left";
		} else
			return "None";

	}

}