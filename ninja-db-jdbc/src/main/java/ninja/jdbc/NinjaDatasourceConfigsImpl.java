package ninja.jdbc;

import com.google.common.collect.ImmutableList;
import java.util.List;


public class NinjaDatasourceConfigsImpl implements NinjaDatasourceConfigs {
    
    private final List<NinjaDatasourceConfig> ninjaDatasourceConfigs;
    
    public NinjaDatasourceConfigsImpl(List<NinjaDatasourceConfig> datasources) {
        this.ninjaDatasourceConfigs = ImmutableList.copyOf(datasources);
    }

    @Override
    public List<NinjaDatasourceConfig> getDatasources() {
        return ninjaDatasourceConfigs;
    }
    
}
