package in.erail.notification;

import com.google.common.base.Optional;
import in.erail.notification.model.Endpoint;
import in.erail.notification.model.EndpointId;
import io.reactivex.Maybe;
import io.reactivex.Single;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerHelper;

/**
 *
 * @author vinay
 */
public class DefaultPushNotificationService implements PushNotificationService {

  EntityManagerHelper mEntityManagerHelper;

  protected Maybe<Endpoint> checkEndpointExists(Endpoint pEndpoint) {
    return getEntityManagerHelper()
            .getEM()
            .flatMapMaybe(em -> Maybe.fromCallable(() -> em.find(Endpoint.class, new EndpointId(pEndpoint))));
  }

  protected Maybe<Endpoint> checkEndPointExists(Endpoint pEndpoint, EntityManager pEM) {
    return Maybe.fromCallable(() -> pEM.find(Endpoint.class, pEndpoint));
  }

  @Override
  public Single<Endpoint> add(Endpoint pEndpoint) {
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

}
