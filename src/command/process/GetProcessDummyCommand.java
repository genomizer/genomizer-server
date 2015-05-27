package command.process;

import command.Command;
import command.Process;
import command.UserRights;
import command.ValidateException;
import response.GetProcessStatusResponse;
import response.Response;

import java.util.LinkedList;
import java.util.UUID;

/**
 * Created by c12slm on 2015-05-18.
 */
public class GetProcessDummyCommand extends Command {

    @Override
    public int getExpectedNumberOfURIFields() {
        return 2;
    }

    @Override
    public void validate() throws ValidateException {
        hasRights(UserRights.getRights(this.getClass()));
    }

    @Override
    public Response execute() {
        Process p1 = new Process(new PutProcessCommand());
        p1.experimentName = "Exp1";
        p1.status = "Finished";
        p1.outputFiles = new String[]{"file1.wig"};
        p1.author = "Yuri";
        p1.timeAdded = 1431944527592L;
        p1.timeStarted = p1.timeAdded + 3004300;
        p1.timeFinished = p1.timeStarted + 10012000;
        p1.PID = UUID.randomUUID().toString();

        Process p2 = new Process(new PutProcessCommand());
        p2.experimentName = "Exp2";
        p2.status = "Waiting";
        p2.outputFiles = new String[]{"file2.wig"};
        p2.author = "Jan";
        p2.timeAdded = 1431944527592L - 85971432;
        p2.timeStarted = 0;
        p2.timeFinished = 0;
        p2.PID = UUID.randomUUID().toString();

        Process p3 = new Process(new PutProcessCommand());
        p3.experimentName = "Exp3";
        p3.status = "Crashed";
        p3.outputFiles = new String[]{"file3.wig"};
        p3.author = "Per";
        p3.timeAdded = 1431944527592L - 56198;
        p3.timeStarted = p3.timeAdded + 18297612;
        p3.timeFinished = p3.timeStarted + 76511243;
        p3.PID = UUID.randomUUID().toString();

        Process p4 = new Process(new PutProcessCommand());
        p4.experimentName = "Exp4";
        p4.status = "Started";
        p4.outputFiles = new String[]{"file4.wig"};
        p4.author = "Niklasf och Anna";
        p4.timeAdded = 1431944527592L - 6178231;
        p4.timeStarted = p1.timeAdded + 3004140;
        p4.timeFinished = p1.timeStarted + 112300000;
        p4.PID = UUID.randomUUID().toString();

        LinkedList<command.Process> getProcessStatuses = new LinkedList<>();

        getProcessStatuses.add(p1);
        getProcessStatuses.add(p2);
        getProcessStatuses.add(p3);
        getProcessStatuses.add(p4);
        return new GetProcessStatusResponse(getProcessStatuses);
    }
}

/*
[
    {
        "experimentName": "Exp1",
            "processID": "123",
            "status": "Finished",
            "outputFiles": [
        "file1",
                "file2"
        ],
        "author": "yuri",
            "timeAdded": 1400245668744,
            "timeStarted": 1400245668756,
            "timeFinished": 1400245669756
    },
    {
        "experimentName": "Exp2",
            "processID": "123",
            "status": "Finished",
            "outputFiles": [
        "file1",
                "file2"
        ],
        "author": "janne",
            "timeAdded": 1400245668746,
            "timeStarted": 1400245669756,
            "timeFinished": 1400245670756
    },
    {
        "experimentName": "Exp43",
            "processID": "123",
            "status": "Crashed",
            "outputFiles": [
        "file1",
                "file2"
        ],
        "author": "philge",
            "timeAdded": 1400245668748,
            "timeStarted": 1400245670756,
            "timeFinished": 1400245671757
    },
    {
        "experimentName": "Exp234",
            "processID": "123",
            "status": "Started",
            "outputFiles": [
        "file1",
                "file2"
        ],
        "author": "per",
            "timeAdded": 1400245668753,
            "timeStarted": 1400245671757,
            "timeFinished": 0
    },
    {
        "experimentName": "Exp6",
            "processID": "123",
            "status": "Waiting",
            "outputFiles": [],
        "author": "yuri",
            "timeAdded": 1400245668755,
            "timeStarted": 0,
            "timeFinished": 0
    }
    ];
*/