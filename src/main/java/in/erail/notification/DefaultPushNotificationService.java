package in.erail.notification;

import in.erail.notification.model.Endpoint;
import in.erail.notification.model.EndpointId;
import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Single;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerHelper;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author vinay
 */
public class DefaultPushNotificationService implements PushNotificationService {

  private EntityManagerHelper mEntityManagerHelper;
  private Logger mLog;

  protected Maybe<Endpoint> checkEndpointExists(Endpoint pEndpoint) {
    return getEntityManagerHelper()
            .getEM()
            .flatMapMaybe(em -> checkEndPointExists(pEndpoint,em));
  }

  protected Maybe<Endpoint> checkEndPointExists(Endpoint pEndpoint, EntityManager pEM) {
    return Maybe.fromCallable(() -> pEM.find(Endpoint.class, new EndpointId(pEndpoint)));
  }

  @Override
  public Single<Endpoint> addDevice(Endpoint pEndpoint) {
    return getEntityManagerHelper()
            .getEMTx()
            .flatMap(em -> add(pEndpoint, em).doOnSuccess(ep -> em.getTransaction().commit()));
  }

  protected Single<Endpoint> add(Endpoint pEndpoint, EntityManager pEM) {
    pEM.persist(pEndpoint);
    return Single.just(pEndpoint);
  }

  public EntityManagerHelper getEntityManagerHelper() {
    return mEntityManagerHelper;
  }

  public void setEntityManagerHelper(EntityManagerHelper pEntityManagerHelper) {
    this.mEntityManagerHelper = pEntityManagerHelper;
  }

  @Override
  public Completable removeDevice(Endpoint pEndpoint) {
    return getEntityManagerHelper()
            .getEMTx()
            .flatMapCompletable(em -> removeDevice(pEndpoint, em).doOnComplete(() -> em.getTransaction().commit()));
  }

  protected Completable removeDevice(Endpoint pEndpoint, EntityManager pEM) {
    return checkEndPointExists(pEndpoint, pEM)
            .doOnSuccess(item -> getLog().debug(() -> "Removing Device:" + item.toString()))
            .doOnComplete(() -> getLog().warn(() -> "Device could not be removed as it is not registered:" + pEndpoint.toString()))
            .flatMapCompletable((item) -> {
              pEM.remove(item);
              return Completable.complete();
            });
  }

  public Logger getLog() {
    return mLog;
  }

  public void setLog(Logger pLog) {
    this.mLog = pLog;
  }

}
