FROM myubuntu:1

COPY ./labdocker/target/client-runnable.jar /data/client-runnable.jar

CMD java -cp /data/client-runnable.jar org.ds.java.Client /data/string.txt 2000
