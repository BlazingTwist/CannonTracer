package the_dark_jumper.cannontracer.configsaving;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MultiPlayerKeybinds {
	@JsonProperty("XRayTracesMP")
	private KeybindEntry xRayTracesMP = new KeybindEntry().addTrigger(45, 42);

	@JsonProperty("MenuMP")
	private KeybindEntry menuMP = new KeybindEntry().addTrigger(46, 42);

	@JsonProperty("PullDataMP")
	private KeybindEntry pullDataMP = new KeybindEntry().addTrigger(19).addExclude(42);

	@JsonProperty("ClearDataMP")
	private KeybindEntry clearDataMP = new KeybindEntry().addTrigger(19, 42);

	@JsonProperty("DisplayTickMP+")
	private KeybindEntry displayTickMPAdd = new KeybindEntry().addTrigger(333);

	@JsonProperty("DisplayTickMP-")
	private KeybindEntry displayTickMPSub = new KeybindEntry().addTrigger(331);

	@JsonProperty("DisplayDespawnTickMP")
	private KeybindEntry displayDespawnTickMP = new KeybindEntry().addTrigger(42, 333);

	@JsonProperty("DisplayPrevDespawnTickMP")
	private KeybindEntry displayPreviousDespawnTickMP = new KeybindEntry().addTrigger(42, 331);

	@JsonProperty("ShowFirstTickMP")
	private KeybindEntry showFirstTickMP = new KeybindEntry().addTrigger(328);

	@JsonProperty("ShowLastTickMP")
	private KeybindEntry showLastTickMP = new KeybindEntry().addTrigger(336);

	public MultiPlayerKeybinds() {
	}

	public KeybindEntry getxRayTracesMP() {
		return xRayTracesMP;
	}

	public MultiPlayerKeybinds setxRayTracesMP(KeybindEntry xRayTracesMP) {
		this.xRayTracesMP = xRayTracesMP;
		return this;
	}

	public KeybindEntry getMenuMP() {
		return menuMP;
	}

	public MultiPlayerKeybinds setMenuMP(KeybindEntry menuMP) {
		this.menuMP = menuMP;
		return this;
	}

	public KeybindEntry getPullDataMP() {
		return pullDataMP;
	}

	public MultiPlayerKeybinds setPullDataMP(KeybindEntry pullDataMP) {
		this.pullDataMP = pullDataMP;
		return this;
	}

	public KeybindEntry getClearDataMP() {
		return clearDataMP;
	}

	public MultiPlayerKeybinds setClearDataMP(KeybindEntry clearDataMP) {
		this.clearDataMP = clearDataMP;
		return this;
	}

	public KeybindEntry getDisplayTickMPAdd() {
		return displayTickMPAdd;
	}

	public MultiPlayerKeybinds setDisplayTickMPAdd(KeybindEntry displayTickMPAdd) {
		this.displayTickMPAdd = displayTickMPAdd;
		return this;
	}

	public KeybindEntry getDisplayTickMPSub() {
		return displayTickMPSub;
	}

	public MultiPlayerKeybinds setDisplayTickMPSub(KeybindEntry displayTickMPSub) {
		this.displayTickMPSub = displayTickMPSub;
		return this;
	}

	public KeybindEntry getDisplayDespawnTickMP() {
		return displayDespawnTickMP;
	}

	public MultiPlayerKeybinds setDisplayDespawnTickMP(KeybindEntry displayDespawnTickMP) {
		this.displayDespawnTickMP = displayDespawnTickMP;
		return this;
	}

	public KeybindEntry getDisplayPreviousDespawnTickMP() {
		return displayPreviousDespawnTickMP;
	}

	public MultiPlayerKeybinds setDisplayPreviousDespawnTickMP(KeybindEntry displayPreviousDespawnTickMP) {
		this.displayPreviousDespawnTickMP = displayPreviousDespawnTickMP;
		return this;
	}

	public KeybindEntry getShowFirstTickMP() {
		return showFirstTickMP;
	}

	public MultiPlayerKeybinds setShowFirstTickMP(KeybindEntry showFirstTickMP) {
		this.showFirstTickMP = showFirstTickMP;
		return this;
	}

	public KeybindEntry getShowLastTickMP() {
		return showLastTickMP;
	}

	public MultiPlayerKeybinds setShowLastTickMP(KeybindEntry showLastTickMP) {
		this.showLastTickMP = showLastTickMP;
		return this;
	}
}
