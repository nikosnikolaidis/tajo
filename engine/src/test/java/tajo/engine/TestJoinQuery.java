package tajo.engine;

import nta.util.FileUtil;
import org.apache.hadoop.thirdparty.guava.common.collect.Maps;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * @author Hyunsik Choi
 */
public class TestJoinQuery extends TpchTestBase {

  public TestJoinQuery() throws IOException {
    super();
  }

  @Test
  public final void testCrossJoin() throws Exception {
    ResultSet res = execute("select n_name, r_name, n_regionkey, r_regionkey from nation, region");

    int cnt = 0;
    while(res.next()) {
      cnt++;
    }
    // TODO - to check their joined contents
    assertEquals(25 * 5, cnt);
  }

  @Test
  public final void testCrossJoinWithExplicitJoinQual() throws Exception {
    ResultSet res = execute("select n_name, r_name, n_regionkey, r_regionkey from nation, region where n_regionkey = r_regionkey");
    int cnt = 0;
    while(res.next()) {
      cnt++;
    }
    // TODO - to check their joined contents
    assertEquals(25, cnt);
  }

  @Test
  public final void testTPCHQ2Join() throws Exception {
    ResultSet res = execute(FileUtil.readTextFile(new File("src/test/queries/tpch_q2_simplified.tql")));

    Object [][] result = new Object[3][3];

    int tupleId = 0;
    int colId = 0;
    result[tupleId][colId++] = 4032.68f;
    result[tupleId][colId++] = "Supplier#000000002";
    result[tupleId++][colId] = "ETHIOPIA";

    colId = 0;
    result[tupleId][colId++] = 4641.08f;
    result[tupleId][colId++] = "Supplier#000000004";
    result[tupleId++][colId] = "MOROCCO";

    colId = 0;
    result[tupleId][colId++] = 4192.4f;
    result[tupleId][colId++] = "Supplier#000000003";
    result[tupleId][colId] = "ARGENTINA";

    Map<Float, Object[]> resultSet =
        Maps.newHashMap();
    for (Object [] t : result) {
      resultSet.put((Float) t[0], t);
    }

    for (int i = 0; i < 3; i++) {
      res.next();
      Object [] resultTuple = resultSet.get(res.getFloat("s_acctbal"));
      assertEquals(resultTuple[0], res.getFloat("s_acctbal"));
      assertEquals(resultTuple[1], res.getString("s_name"));
      assertEquals(resultTuple[2], res.getString("n_name"));
    }

    assertFalse(res.next());
  }
}
