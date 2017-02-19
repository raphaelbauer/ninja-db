package ninja.flyway;

import com.google.inject.Inject;
import ninja.jdbc.NinjaDatasourceConfig;
import ninja.jdbc.NinjaDatasourceConfigs;
import ninja.jdbc.NinjaDatasources;
import org.flywaydb.core.Flyway;

public class NinjaFlywayMigrator {

    @Inject
    public NinjaFlywayMigrator(NinjaDatasourceConfigs ninjaDatasourceConfigs) {

        for (NinjaDatasourceConfig datasourceConfig: ninjaDatasourceConfigs.getDatasources()) {
            // FIXME => check if this datasource should be migrated

            // FIXME => check where to find the migrations for that datasource...
            if (datasourceConfig.migrationEnabled) {
                Flyway flyway = new Flyway();
                flyway.setDataSource(datasourceConfig.jdbcUrl, datasourceConfig.migrationUsername, datasourceConfig.migrationPassword);
                flyway.setLocations("classpath:db/" + datasourceConfig.name + "/migration");
                flyway.migrate();
            }

        }

    }

}
