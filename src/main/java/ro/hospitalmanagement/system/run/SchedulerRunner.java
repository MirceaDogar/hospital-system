package ro.hospitalmanagement.system.run;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ro.hospitalmanagement.system.schedulers.EmailScheduler;
import ro.hospitalmanagement.system.schedulers.ReportScheduler;
import ro.hospitalmanagement.system.schedulers.RoomAvailabilityScheduler;

@Component
public class SchedulerRunner implements CommandLineRunner {
    @Autowired
    ReportScheduler dailyReportScheduler;
    @Autowired
    EmailScheduler emailScheduler;
    @Autowired
    RoomAvailabilityScheduler roomAvailabilityScheduler;

    @Override
    public void run(String... args) throws Exception {
        roomAvailabilityScheduler.roomAvailability();
        dailyReportScheduler.sendReportToDoctor();
        emailScheduler.remainderAppointment();

    }
}