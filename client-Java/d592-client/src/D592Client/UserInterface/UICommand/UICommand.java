package D592Client.UserInterface.UICommand;

import D592Client.NetUtils.NetWorker;

import java.io.IOException;


public interface UICommand {
    /**
     * Test if this command can be executed using the 'execute()' method
     *
     * If the command cannot be executed, use 'toString()' to identify it
     */
    public boolean isExecutable();

    /**
     * Execute a UI command
     * @throws IOException if an error happens when executing the command
     * @throws UnsupportedOperationException if execute() is not supported
     */
    public void execute(NetWorker nwk, long tick, int senderId) throws IOException, UnsupportedOperationException;

    /**
     * @return a string representation of this command
     * @apiNote the output may be used to identify the command if it cannot be executed
     */
    public String toString();
}


