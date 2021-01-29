package the_dark_jumper.cannontracer.configsaving;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.Queue;
import the_dark_jumper.cannontracer.tracking.SingleTickMoveData;

public class SavedTrace {
	@JsonProperty("traces")
	private ArrayList<SingleTickMoveData> traces;

	public SavedTrace() {
	}

	@JsonIgnore
	public SavedTrace(Queue<SingleTickMoveData> traces){
		setTraces(traces);
	}

	@JsonIgnore
	public ArrayList<SingleTickMoveData> getTraces() {
		return traces;
	}

	@JsonIgnore
	public SavedTrace setTraces(ArrayList<SingleTickMoveData> traces) {
		this.traces = traces;
		return this;
	}

	@JsonIgnore
	public SavedTrace setTraces(Queue<SingleTickMoveData> traces) {
		this.traces = new ArrayList<>();
		this.traces.addAll(traces);
		return this;
	}
}
