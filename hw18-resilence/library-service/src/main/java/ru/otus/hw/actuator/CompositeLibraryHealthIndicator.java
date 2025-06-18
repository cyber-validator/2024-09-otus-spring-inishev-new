package ru.otus.hw.actuator;


import org.springframework.boot.actuate.health.CompositeHealthContributor;
import org.springframework.boot.actuate.health.HealthContributor;
import org.springframework.boot.actuate.health.NamedContributor;
import org.springframework.boot.actuate.jdbc.DataSourceHealthIndicator;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

@Component("compositeLibraryStorage")
public class CompositeLibraryHealthIndicator implements CompositeHealthContributor {

    private final Map<String, HealthContributor> contributors = new LinkedHashMap<>();


    public CompositeLibraryHealthIndicator(LibraryHealthIndicator libraryHealthContributor, DataSource dataSource) {
        contributors.put("libraryStorage", libraryHealthContributor);
        contributors.put("dataStorage", new DataSourceHealthIndicator(dataSource));
    }

    @Override
    public HealthContributor getContributor(String name) {
        return contributors.get(name);
    }

    @Override
    public Iterator<NamedContributor<HealthContributor>> iterator() {
        return contributors.entrySet().stream()
                .map((entry) -> NamedContributor.of(entry.getKey(), entry.getValue()))
                .iterator();
    }

}
