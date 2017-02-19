package ninja.jdbc;

import com.google.common.collect.ImmutableList;
import java.util.List;


public class NinjaDatasourcesImpl implements NinjaDatasources {
    
    private final List<NinjaDatasource> datasources;
    
    public NinjaDatasourcesImpl(List<NinjaDatasource> datasources) {
        this.datasources = ImmutableList.copyOf(datasources);
    }

    @Override
    public List<NinjaDatasource> getDatasources() {
        return datasources;
    }
    
}
