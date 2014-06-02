package authentication;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
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

				Iterator<String> uuids = latestRequests.keySet().iterator();

				while(uuids.hasNext()) {
					String uuid = uuids.next();
					Date date = latestRequests.get(uuid);

					if(getDateDiff(date, new Date(), TimeUnit.HOURS) >= INACTIVE_LIMIT_HOURS) {
						Debug.log("REMOVING INACTIVE UUID: " + uuid + " - USERNAME: " + Authenticate.getUsername(uuid));
						Authenticate.deleteUser(uuid);
						uuids = latestRequests.keySet().iterator();
					}
				}

				Debug.log("REMAINING USERS");
				Iterator<String> uuids2 = latestRequests.keySet().iterator();
				String next_uuid;
				while(uuids2.hasNext()) {
					next_uuid = uuids2.next();
					Debug.log(next_uuid + " - USERNAME: " + Authenticate.getUsername(next_uuid));
				}

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
