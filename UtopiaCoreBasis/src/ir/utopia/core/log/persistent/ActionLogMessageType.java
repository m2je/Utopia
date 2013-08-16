package ir.utopia.core.log.persistent;

import ir.utopia.core.messagehandler.MessageNamePair.MessageType;

public enum ActionLogMessageType {
	Info,Warninig,Error;
	
	public static  ActionLogMessageType convert(MessageType type){
		switch (type) {
		case info:
			return Info;
		case warning:
			return Warninig;
	
		default: return Error;
		}
	}
}
