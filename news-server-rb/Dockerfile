FROM expertsystems/oracle-jdk8

ENV VERTICLE_FILE news-server-rb-1.0-SNAPSHOT-fat.jar

# Set the location of the verticles
ENV VERTICLE_HOME /usr/verticles

#EXPOSE 8080

# Copy your fat jar to the container
COPY target/$VERTICLE_FILE $VERTICLE_HOME/

# Launch the verticle
WORKDIR $VERTICLE_HOME
ENTRYPOINT ["sh", "-c"]
CMD ["java -jar $VERTICLE_FILE -ha"]