package com.example.timesheet;

import com.example.timesheet.web.dto.EmployeeCreateRequest;
import com.example.timesheet.web.dto.ProjectCreateRequest;
import com.example.timesheet.web.dto.TimesheetEntryDto;
import com.example.timesheet.web.dto.TimesheetUpsertRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TimesheetApiApplicationTests {

    @LocalServerPort
    int port;

    @Autowired
    TestRestTemplate rest;

    String base(String path) {
        return "http://localhost:" + port + "/codex-example/api/v1" + path;
    }

    @Test
    void endToEnd_createAndFetchTimesheet() {
        EmployeeCreateRequest emp = new EmployeeCreateRequest();
        emp.setFirstName("Ada");
        emp.setLastName("Lovelace");
        ResponseEntity<Object> empResp = rest.postForEntity(base("/employees"), emp, Object.class);
        System.out.println("EMP RESP: status=" + empResp.getStatusCode() + ", body=" + empResp.getBody());
        assertThat(empResp.getStatusCode().is2xxSuccessful()).isTrue();

        ProjectCreateRequest proj = new ProjectCreateRequest();
        proj.setName("Project X");
        ResponseEntity<Object> projResp = rest.postForEntity(base("/projects"), proj, Object.class);
        assertThat(projResp.getStatusCode().is2xxSuccessful()).isTrue();

        // Extract ids via simple casts
        Long empId = ((Number)((java.util.Map<?,?>)empResp.getBody()).get("id")).longValue();
        Long projId = ((Number)((java.util.Map<?,?>)projResp.getBody()).get("id")).longValue();

        LocalDate weekStart = LocalDate.now().with(java.time.temporal.TemporalAdjusters.previousOrSame(java.time.DayOfWeek.MONDAY));
        TimesheetEntryDto e1 = new TimesheetEntryDto();
        e1.setProjectId(projId);
        e1.setDayOfWeek(DayOfWeek.MONDAY);
        e1.setHours(8);
        TimesheetEntryDto e2 = new TimesheetEntryDto();
        e2.setProjectId(projId);
        e2.setDayOfWeek(DayOfWeek.TUESDAY);
        e2.setHours(7.5);
        TimesheetUpsertRequest upsert = new TimesheetUpsertRequest();
        upsert.setEmployeeId(empId);
        upsert.setWeekStart(weekStart);
        upsert.setEntries(Arrays.asList(e1, e2));

        ResponseEntity<Object> tsResp = rest.postForEntity(base("/timesheets"), upsert, Object.class);
        assertThat(tsResp.getStatusCode().is2xxSuccessful()).isTrue();

        ResponseEntity<Object> getResp = rest.getForEntity(base("/timesheets") + "?employeeId=" + empId + "&weekStart=" + weekStart, Object.class);
        assertThat(getResp.getStatusCode().is2xxSuccessful()).isTrue();

        ResponseEntity<Object[]> dashResp = rest.getForEntity(base("/dashboard/weekly") + "?weekStart=" + weekStart, Object[].class);
        assertThat(dashResp.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(dashResp.getBody()).isNotNull();
        assertThat(dashResp.getBody().length).isGreaterThan(0);
    }
}
