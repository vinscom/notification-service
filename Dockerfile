FROM openjdk:8-jdk

VOLUME /tmp
ENV VERTX_OPTS -Dvertx.cacheDirBase=/tmp/

COPY target/notification-service-deployment /
EXPOSE 8888

#HEALTHCHECK --interval=5s \
#            --timeout=5s \
#            CMD curl -f http://127.0.0.1:8000 || exit 1

ENTRYPOINT ["sh","-c"]
CMD ["java -Duser.timezone=UTC -Djava.security.egd=file:/dev/./urandom -Dvertx.logger-delegate-factory-class-name=io.vertx.core.logging.Log4j2LogDelegateFactory -Dglue.config=$GLUE_CONFIG -jar lib/notification-service-1.0-SNAPSHOT.jar"]
