package com.hart.overwatch.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.dao.DataAccessException;

@Service
public class DatabaseSetupService {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public DatabaseSetupService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void createTsvectorTrigger() {
        String createFunction = "CREATE OR REPLACE FUNCTION update_search_vector() "
                + "RETURNS trigger AS $$ " + "BEGIN "
                + "NEW.search_vector := to_tsvector('english', NEW.title || ' ' || NEW.description); "
                + "RETURN NEW; " + "END; $$ LANGUAGE plpgsql;";

        String createTrigger = "DO $$ BEGIN "
                + "IF NOT EXISTS (SELECT 1 FROM pg_trigger WHERE tgname = 'trigger_update_search_vector') THEN "
                + "CREATE TRIGGER trigger_update_search_vector "
                + "BEFORE INSERT OR UPDATE ON topic "
                + "FOR EACH ROW EXECUTE FUNCTION update_search_vector(); " + "END IF; " + "END $$;";

        try {
            jdbcTemplate.execute(createFunction);

            jdbcTemplate.execute(createTrigger);
            System.out.println("-----Topic Database Trigger Created------");
        } catch (DataAccessException e) {
            // Log error (consider using a logger)
            System.err.println("Error creating trigger or function: " + e.getMessage());
        }
    }
}
