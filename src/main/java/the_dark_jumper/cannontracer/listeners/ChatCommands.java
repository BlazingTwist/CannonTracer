package the_dark_jumper.cannontracer.listeners;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import the_dark_jumper.cannontracer.Main;
import the_dark_jumper.cannontracer.util.ChatUtils;
import the_dark_jumper.cannontracer.util.StringUtils;

public enum ChatCommands {
	HELP("/tracer help"),
	DEBUG("/tracer debug"),
	TRACE_SAVE("/tracer save"),
	TRACE_DELETE("/tracer delete"),
	TRACE_LOAD("/tracer load"),
	TRACE_LIST("/tracer list");

	private String commandPrefix;

	ChatCommands(String commandPrefix) {
		this.commandPrefix = commandPrefix;
	}

	public boolean isCommand(String text) {
		return text.startsWith(commandPrefix);
	}

	public boolean handle(String text) {
		text = text.substring(commandPrefix.length());

		switch (this) {
			case HELP: {
				ChatUtils.messagePlayer("/tracer help", " - shows this message", true);
				ChatUtils.messagePlayer("/tracer debug", " - toggles debug printing of plugin messages", true);
				ChatUtils.messagePlayer("/tracer list", " - lists all saved traces", true);
				ChatUtils.messagePlayer("/tracer save [trace_name]", " - saves the rendered traces to the given trace_name", true);
				ChatUtils.messagePlayer("/tracer save [trace_name]", " - deletes the saved trace with the given trace_name", true);
				ChatUtils.messagePlayer("/tracer load [trace_name_1] [trace_name_2] ... [trace_name_n]", " - open one or more saved traces", true);
				break;
			}
			case DEBUG: {
				ServerChatListener serverChatListener = Main.getInstance().serverChatListener;
				boolean debugPrint = !serverChatListener.debugPrint;
				serverChatListener.debugPrint = debugPrint;
				ChatUtils.messagePlayer("Debug printing is now ", debugPrint ? "enabled" : "disabled", debugPrint);
				break;
			}
			case TRACE_LOAD: {
				List<String> traceNames = Arrays.stream(text.split(" "))
						.filter(x -> !StringUtils.isBlank(x))
						.map(String::trim)
						.collect(Collectors.toList());
				Main.getInstance().dataManager.loadTrace(traceNames);
				break;
			}
			case TRACE_SAVE: {
				Main.getInstance().dataManager.saveTrace(text.trim());
				break;
			}
			case TRACE_DELETE: {
				Main.getInstance().dataManager.deleteTrace(text.trim());
				break;
			}
			case TRACE_LIST: {
				List<String> traceNames = Main.getInstance().dataManager.getTraceNames();
				if(traceNames != null){
					String traceNameString = String.join(", ", traceNames);
					ChatUtils.messagePlayer("Found these traces: ", traceNameString, true);
				}else{
					ChatUtils.messagePlayer("No traces found.", "", true);
				}
				break;
			}
		}

		return true;
	}
}
