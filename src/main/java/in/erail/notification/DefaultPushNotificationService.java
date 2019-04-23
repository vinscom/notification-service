package in.erail.notification;

import in.erail.glue.component.ServiceMap;
import in.erail.notification.model.Endpoint;
import in.erail.notification.model.EndpointId;
import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerHelper;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author vinay
 */
public class DefaultPushNotificationService implements PushNotificationService {

  private ServiceMap mServiceMessageGenerator;
  private EntityManagerHelper mEntityManagerHelper;
  private Logger mLog;

  protected Maybe<Endpoint> checkEndpointExists(Endpoint pEndpoint) {
    return getEntityManagerHelper()
            .getEM()
            .flatMapMaybe(em -> checkEndPointExists(pEndpoint, em));
  }

  protected Maybe<Endpoint> checkEndPointExists(Endpoint pEndpoint, EntityManager pEM) {
    return Maybe.fromCallable(() -> pEM.find(Endpoint.class, new EndpointId(pEndpoint)));
  }

  @Override
  public Single<Endpoint> addDevice(Endpoint pEndpoint) {
    return getEntityManagerHelper()
            .getEMTx()
            .flatMap(em -> addDevice(pEndpoint, em).doOnSuccess(ep -> em.getTransaction().commit()));
  }

  protected Single<Endpoint> addDevice(Endpoint pEndpoint, EntityManager pEM) {
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

  protected Observable<Endpoint> findDevices(String pUser) {
    return getEntityManagerHelper()
            .getEM()
            .flatMapObservable((em) -> {
              List<Endpoint> devices = em
                      .createNamedQuery(Endpoint.QUERY_FIND_USER_DEVICES, Endpoint.class)
                      .setParameter(Endpoint.QUERY_FIND_USER_DEVICES_PARAM_USERID, pUser)
                      .getResultList();
              return Observable.fromIterable(devices);
            });
  }

  @Override
  public Observable<String> send(String pUser, Card pCard) {
    getLog().error("Message is not sent anywhere:" + pUser);
    return Observable.empty();
  }

  public Logger getLog() {
    return mLog;
  }

  public void setLog(Logger pLog) {
    this.mLog = pLog;
  }

  public ServiceMap getServiceMessageGenerator() {
    return mServiceMessageGenerator;
  }

  public void setServiceMessageGenerator(ServiceMap pServiceMessageGenerator) {
    this.mServiceMessageGenerator = pServiceMessageGenerator;
  }

}
