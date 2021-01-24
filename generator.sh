export CLASSPATH=$CLASSPATH:~/.m2/repository/org/apache/openjpa/openjpa-all/2.4.0/openjpa-all-2.4.0.jar:~/.m2/repository/mysql/mysql-connector-java/5.1.46/mysql-connector-java-5.1.46.jar
rm -rf ./source
java org.apache.openjpa.jdbc.meta.ReverseMappingTool -md none -ann true -no true -d ./source -p ./persistence.xml
