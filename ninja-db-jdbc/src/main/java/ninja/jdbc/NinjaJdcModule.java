package ninja.jdbc;

import com.google.common.collect.Lists;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import javax.sql.DataSource;
import ninja.utils.NinjaProperties;

public class NinjaJdcModule extends AbstractModule {

    private final String DATASOURCE_PREFIX = "application.datasource";
    private final String DATASOURCE_URL = "url";
    private final String DATASOURCE_USERNAME = "username";
    private final String DATASOURCE_PASSWORD = "password";
    private final String DATASOURCE_DRIVER = "driver";

    private final String DATASOURCE_MIGRATION_ENABLED = "migration.enabled";
    private final String DATASOURCE_MIGRATION_USERNAME = "migration.username";
    private final String DATASOURCE_MIGRATION_PASSWORD = "migration.password";

    @Override
    protected void configure() {
        //nothing to bind...
    }

    @Provides
    @Singleton
    public NinjaDatasources provideNinjaDatasources(NinjaDatasourceConfigs ninjaDatasourceConfigs) {
        List<NinjaDatasource> ninjaDatasources = Lists.newArrayList();

        for (NinjaDatasourceConfig ninjaDatasourceConfig : ninjaDatasourceConfigs.getDatasources()) {
            HikariConfig config = new HikariConfig();

            config.setDriverClassName(ninjaDatasourceConfig.driver);
            config.setJdbcUrl(ninjaDatasourceConfig.jdbcUrl);
            config.setUsername(ninjaDatasourceConfig.username);
            config.setPassword(ninjaDatasourceConfig.password);

            HikariDataSource dataSource = new HikariDataSource(config);

            NinjaDatasource ninjaDatasource = new NinjaDatasource();
            ninjaDatasource.name = ninjaDatasourceConfig.name;
            ninjaDatasource.dataSource = dataSource;

            ninjaDatasources.add(ninjaDatasource);
        }

        return new NinjaDatasourcesImpl(ninjaDatasources);
    }

    @Provides
    @Singleton
    public NinjaDatasourceConfigs provideNinjaDatasourceConfigs(NinjaProperties ninjaProperties) {
        List<NinjaDatasourceConfig> ninjaDatasourceConfigs = getDatasources(ninjaProperties);
        return new NinjaDatasourceConfigsImpl(ninjaDatasourceConfigs);
    }

    private List<NinjaDatasourceConfig> getDatasources(NinjaProperties ninjaProperties) {
        Properties properties = ninjaProperties.getAllCurrentNinjaProperties();

        Set<String> datasourceNames = new HashSet<>();

        // filter datasources from application config
        for (Map.Entry<Object, Object> entrySet : properties.entrySet()) {
            if (((String) entrySet.getKey()).startsWith(DATASOURCE_PREFIX)
                    && ((String) entrySet.getKey()).endsWith(DATASOURCE_URL)) {

                String withoutPrefix = ((String) entrySet.getKey()).split(DATASOURCE_PREFIX + ".")[1];
                String datasourceName = withoutPrefix.split("." + DATASOURCE_URL)[0];

                datasourceNames.add(datasourceName);
            }
        }

        // assemble the datasources in a nice way, so we can build 
        // datasources from it
        List<NinjaDatasourceConfig> ninjaDatasources = Lists.newArrayList();
        for (String datasourceName : datasourceNames) {
            NinjaDatasourceConfig ninjaDatasource = new NinjaDatasourceConfig();
            ninjaDatasource.name = datasourceName;
            
            ninjaDatasource.driver = ninjaProperties.getWithDefault(
                    DATASOURCE_PREFIX + "." + datasourceName + "." + DATASOURCE_DRIVER, "");
            
            // datasource
            ninjaDatasource.jdbcUrl = ninjaProperties.getWithDefault(
                    DATASOURCE_PREFIX + "." + datasourceName + "." + DATASOURCE_URL, "");
            ninjaDatasource.username = ninjaProperties.getWithDefault(
                    DATASOURCE_PREFIX + "." + datasourceName + "." + DATASOURCE_USERNAME, "");
            ninjaDatasource.password = ninjaProperties.getWithDefault(
                    DATASOURCE_PREFIX + "." + datasourceName + "." + DATASOURCE_PASSWORD, "");

            // migrations => special username / password may be configured, but fall back to regular ones if not
            ninjaDatasource.migrationEnabled = ninjaProperties.getBooleanWithDefault(
                    DATASOURCE_PREFIX + "." + datasourceName + "." + DATASOURCE_MIGRATION_ENABLED, Boolean.FALSE);
            ninjaDatasource.migrationUsername = ninjaProperties.getWithDefault(
                    DATASOURCE_PREFIX + "." + datasourceName + "." + DATASOURCE_MIGRATION_USERNAME, ninjaDatasource.username);
            ninjaDatasource.migrationPassword = ninjaProperties.getWithDefault(
                    DATASOURCE_PREFIX + "." + datasourceName + "." + DATASOURCE_MIGRATION_PASSWORD, ninjaDatasource.password);

            ninjaDatasources.add(ninjaDatasource);

        }

        return ninjaDatasources;

    }

}
