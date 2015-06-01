package command.process;

import command.ValidateException;

import java.util.Map;

/**
 * File:        ProcessCommand.java
 * Author:      Niklas Fries
 * Contact:     niklasf@cs.umu.se
 * Date:        2015-05-22
 */

public abstract class ProcessCommand {

    public abstract void doProcess(Map.Entry<String,String> filePath);

    public abstract void validate() throws ValidateException;

}
