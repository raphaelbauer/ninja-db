package ninja.jdbc;

import java.util.List;
import java.util.Map;
import javax.sql.DataSource;


public interface NinjaDatasources {
    
    List<NinjaDatasource> getDatasources();
    
}