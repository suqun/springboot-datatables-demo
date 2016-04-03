package com.larry.repository;

import com.larry.model.App;
import org.springframework.data.jpa.datatables.repository.DataTablesRepository;

public interface AppRepository extends DataTablesRepository<App, Long> {
}
