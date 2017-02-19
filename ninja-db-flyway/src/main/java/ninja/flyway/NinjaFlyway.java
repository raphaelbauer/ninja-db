package ninja.flyway;

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

public class NinjaFlyway extends AbstractModule {

    @Override
    protected void configure() {
        // Important: Bind as eager Singleton to run migrations before
        // everything else...
        bind(NinjaFlywayMigrator.class).asEagerSingleton();
    }

}
