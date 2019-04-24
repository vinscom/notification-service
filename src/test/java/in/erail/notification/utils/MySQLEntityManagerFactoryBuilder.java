package in.erail.notification.utils;

import in.erail.glue.annotation.StartService;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.testcontainers.containers.MySQLContainer;

/**
 *
 * @author vinay
 */
@SuppressWarnings({"resource", "unchecked", "rawtypes"})
public class MySQLEntityManagerFactoryBuilder {

  private MySQLContainer mMySQL;
  private String mPersistenceUnitName;
  private String mSchemaName;
  private String mUserName;
  private String mPassword;
  private String mPortBinding = "9999:3306";
  private EntityManagerFactory mEntityManagerFactory;

  @StartService
  public void start() {

    mMySQL = new MySQLContainer()
            .withDatabaseName(getSchemaName())
            .withUsername(getUserName())
            .withPassword(getPassword());

    mMySQL.getPortBindings().add(getPortBinding());

    mMySQL.start();

    Map override = new HashMap();
    override.put("javax.persistence.jdbc.url", mMySQL.getJdbcUrl() + "?logger=Slf4JLogger&profileSQL=true&serverTimezone=UTC");
    override.put("javax.persistence.jdbc.user", mMySQL.getUsername());
    override.put("javax.persistence.jdbc.password", mMySQL.getPassword());
    override.put("javax.persistence.schema-generation.database.action", "drop-and-create");
    override.put("javax.persistence.schema-generation.scripts.action", "drop-and-create");
    override.put("javax.persistence.schema-generation.create-source", "metadata");
    override.put("javax.persistence.schema-generation.scripts.create-target", "target/jpa/create.sql");
    override.put("javax.persistence.schema-generation.scripts.drop-target", "target/jpa/drop.sql");
    setEntityManagerFactory(Persistence.createEntityManagerFactory(getPersistenceUnitName(), override));
  }

  public String getPersistenceUnitName() {
    return mPersistenceUnitName;
  }

  public void setPersistenceUnitName(String pPersistenceUnitName) {
    this.mPersistenceUnitName = pPersistenceUnitName;
  }

  public String getSchemaName() {
    return mSchemaName;
  }

  public void setSchemaName(String pSchemaName) {
    this.mSchemaName = pSchemaName;
  }

  public String getUserName() {
    return mUserName;
  }

  public void setUserName(String pUserName) {
    this.mUserName = pUserName;
  }

  public String getPassword() {
    return mPassword;
  }

  public void setPassword(String pPassword) {
    this.mPassword = pPassword;
  }

  public String getPortBinding() {
    return mPortBinding;
  }

  public void setPortBinding(String pPortBinding) {
    this.mPortBinding = pPortBinding;
  }

  public EntityManagerFactory getEntityManagerFactory() {
    return mEntityManagerFactory;
  }

  public void setEntityManagerFactory(EntityManagerFactory pEntityManagerFactory) {
    this.mEntityManagerFactory = pEntityManagerFactory;
  }

}
