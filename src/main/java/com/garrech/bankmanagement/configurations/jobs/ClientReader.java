package com.garrech.bankmanagement.configurations.jobs;

import com.garrech.bankmanagement.entities.Client;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

@Component
public class ClientReader {

    public ItemReader<Client> reader(String clientCsv) {
        return new FlatFileItemReaderBuilder<Client>()
                .name("clientItemReader")
                .resource(new ClassPathResource(clientCsv))
                .delimited()
                .names(new String[]{"clientName", "password", "clientType"})
                .linesToSkip(1)
                .lineMapper(new DefaultLineMapper<Client>() {{
                    setLineTokenizer(new DelimitedLineTokenizer() {{
                        setNames(new String[]{"clientName", "password", "clientType"});
                    }});
                    setFieldSetMapper(new BeanWrapperFieldSetMapper<Client>() {{
                        setTargetType(Client.class);
                    }});
                }})
                .build();
    }
}
