package the_dark_jumper.cannontracer.configsaving;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SinglePlayerKeybinds {
	@JsonProperty("TracerModeSP")
	private KeybindEntry tracerModeSP = new KeybindEntry().addTrigger(20, 42);

	@JsonProperty("XRayTracesSP")
	private KeybindEntry xRayTracesSP = new KeybindEntry().addTrigger(45, 42);

	@JsonProperty("MenuSP")
	private KeybindEntry menuSP = new KeybindEntry().addTrigger(46, 42);

	@JsonProperty("RenderBoxesSP")
	private KeybindEntry renderBoxesSP = new KeybindEntry().addTrigger(48, 42);

	@JsonProperty("LoadLastSecondSP")
	private KeybindEntry loadLastSecondSP = new KeybindEntry().addTrigger(19).addExclude(42);

	@JsonProperty("ClearHistorySP")
	private KeybindEntry clearHistorySP = new KeybindEntry().addTrigger(19, 42);

	@JsonProperty("DisplayTickSP+")
	private KeybindEntry displayTickSPAdd = new KeybindEntry().addTrigger(333);

	@JsonProperty("DisplayTickSP-")
	private KeybindEntry displayTickSPSub = new KeybindEntry().addTrigger(331);

	public SinglePlayerKeybinds() {
	}

	public KeybindEntry getTracerModeSP() {
		return tracerModeSP;
	}

	public SinglePlayerKeybinds setTracerModeSP(KeybindEntry tracerModeSP) {
		this.tracerModeSP = tracerModeSP;
		return this;
	}

	public KeybindEntry getxRayTracesSP() {
		return xRayTracesSP;
	}

	public SinglePlayerKeybinds setxRayTracesSP(KeybindEntry xRayTracesSP) {
		this.xRayTracesSP = xRayTracesSP;
		return this;
	}

	public KeybindEntry getMenuSP() {
		return menuSP;
	}

	public SinglePlayerKeybinds setMenuSP(KeybindEntry menuSP) {
		this.menuSP = menuSP;
		return this;
	}

	public KeybindEntry getRenderBoxesSP() {
		return renderBoxesSP;
	}

	public SinglePlayerKeybinds setRenderBoxesSP(KeybindEntry renderBoxesSP) {
		this.renderBoxesSP = renderBoxesSP;
		return this;
	}

	public KeybindEntry getLoadLastSecondSP() {
		return loadLastSecondSP;
	}

	public SinglePlayerKeybinds setLoadLastSecondSP(KeybindEntry loadLastSecondSP) {
		this.loadLastSecondSP = loadLastSecondSP;
		return this;
	}

	public KeybindEntry getClearHistorySP() {
		return clearHistorySP;
	}

	public SinglePlayerKeybinds setClearHistorySP(KeybindEntry clearHistorySP) {
		this.clearHistorySP = clearHistorySP;
		return this;
	}

	public KeybindEntry getDisplayTickSPAdd() {
		return displayTickSPAdd;
	}

	public SinglePlayerKeybinds setDisplayTickSPAdd(KeybindEntry displayTickSPAdd) {
		this.displayTickSPAdd = displayTickSPAdd;
		return this;
	}

	public KeybindEntry getDisplayTickSPSub() {
		return displayTickSPSub;
	}

	public SinglePlayerKeybinds setDisplayTickSPSub(KeybindEntry displayTickSPSub) {
		this.displayTickSPSub = displayTickSPSub;
		return this;
	}
}
