package authentication;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import server.Debug;
import server.ErrorLogger;

public class InactiveUuidsRemover implements Runnable {

	private static final int INACTIVE_LIMIT_HOURS = 24;
	private static final int SLEEP_MILLIS = 3600000; // 3600000 milliseconds = 1 hour.

	@Override
	public void run() {
		try{
			while(true) {
				Debug.log("LOOKING FOR INACTIVE UUIDS.");
				HashMap<String, Date> latestRequests = Authenticate.getLatestRequestsMap();

				// Collect all inactive User IDs...
				ArrayList<String> usersToDelete = new ArrayList<String>();
				for (String uid : latestRequests.keySet()) {
					Date date = latestRequests.get(uid);
					if(getDateDiff(date, new Date(), TimeUnit.HOURS) >= INACTIVE_LIMIT_HOURS) {
						usersToDelete.add(uid);
					}
				}
				// ... and remove them from Authenticate.
				for (String uid : usersToDelete) {
					Debug.log("REMOVING INACTIVE UUID: " + uid + " - USERNAME: " + Authenticate.getUsername(uid));
					Authenticate.deleteUser(uid);
				}

				// Then print some debugging output...
				Debug.log("REMAINING USERS:");
				for(String uid : latestRequests.keySet()) {
					Debug.log(uid + " - USERNAME: " + Authenticate.getUsername(uid));
				}

				// ...and go to sleep.
				try {
					Thread.sleep(SLEEP_MILLIS);
				} catch (InterruptedException e) {
					Debug.log("INACTIVE UUIDS REMOVER - THREAD SLEEP ERROR. "+e.getMessage());
				}
			}
		} catch(Exception e) {
			ErrorLogger.log("SERVER", "INACTIVE UUIDS REMOVER - FAILED, CONTINUING PROGRAM WITHOUT THIS FUNCTION."+e.getMessage());
			System.err.println("INACTIVE UUIDS REMOVER - FAILED, CONTINUING PROGRAM WITHOUT THIS FUNCTION."+e.getMessage());
		}
	}

	private static long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
	    long diffInMillies = date2.getTime() - date1.getTime();
	    return timeUnit.convert(diffInMillies,TimeUnit.MILLISECONDS);
	}

}
