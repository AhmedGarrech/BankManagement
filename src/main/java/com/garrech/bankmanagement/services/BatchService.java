package com.garrech.bankmanagement.services;

import com.garrech.bankmanagement.configurations.exceptions.CrudException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
public class BatchService {

    private final JobLauncher jobLauncher;
    private final Job importUserJob;

    public void runSaveClientBatchJob(String clientsFilePath) {
        try {
            JobParameters jobParameters = new JobParametersBuilder().addString("sourceDir", clientsFilePath)
                    .addString("jobId", UUID.randomUUID().toString())
                    .toJobParameters();

            JobExecution jobExecution = jobLauncher.run(importUserJob, jobParameters);
            System.out.println("Batch Job completed with status: " + jobExecution.getStatus());
        } catch (Exception e) {
            String exceptionError = "Batch Job Failed :" + e.getMessage();
            log.error(exceptionError);
            throw new CrudException(exceptionError, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
