package transfer;


import java.util.concurrent.LinkedBlockingQueue;

/**
 * Project: genomizer-Server
 * Package: transfer
 * User: c08esn
 * Date: 4/25/14
 * Time: 12:45 PM
 * To change this template use File | Settings | File Templates.
 */
public class FileTransferThread {

    private LinkedBlockingQueue<Command_Object> command_queue;

    public FileTransferThread(){
        command_queue = new LinkedBlockingQueue<Command_Object>();

    }


    public boolean addWorkToQueue(Command_Object co){
          return  command_queue.add(co);
    }

    public boolean popWorkFromQueue(){
       if(command_queue.poll() !=null){
           return true;
       }
        return false;
    }

    public int numberOfWorkInQueue() {
        return command_queue.size();
    }
}
