#!/bin/bash
set -x
set -e

echo "Starting compilation of local agent source"
#cd ..
cd automator
mvn clean install

cd ../agent
mvn clean install

cd ../agent-launcher
mvn clean install

cd ../server
mvn clean install

cd ..

echo "Completed compilation of local agent source"

cp agent/target/agent.jar runtime/agent.jar
cp agent-launcher/target/agent-launcher.jar runtime/agent-launcher.jar
cp automator/target/automator.jar runtime/automator.jar
cp server/target/testsigma-server.jar runtime/testsigma-server.jar

cp agent/src/main/scripts/posix/*.sh runtime/

cd runtime
ls -la 

export DTS_AGENT_JAR=/Users/tiago/DEV/DELTA/nailab/runtime
export TS_ROOT_DIR=/Users/tiago/DEV/DELTA/nailab/runtime
export TS_AGENT_JAR_PATH=/Users/tiago/DEV/DELTA/nailab/runtime
export TS_DATA_DIR=/Users/tiago/DEV/DELTA/nailab/runtime/data
export TS_AGENT_JAR=/Users/tiago/DEV/DELTA/nailab/agent/target/classes

export MYSQL_HOST_NAME=testsigma.cwvzli6qzugn.us-east-1.rds.amazonaws.com
export MYSQL_DB_NAME=testsigma
export MYSQL_USER=admin
export MYSQL_PASSWORD=y9ZDktZIoYf0wppDq1dm

export TESTSIGMA_SERVER_PORT=9090

#export MAIN_JAR_FILE=/Users/tiago/DEV/DELTA/nailab/runtime/agent-launcher.jar
#export TS_AGENT_JAR=/Users/tiago/DEV/DELTA/nailab/runtime/agent.jar

# /Users/tiago/DEV/DELTA/nailab/runtime/jre/bin/java -cp "/Users/tiago/DEV/DELTA/nailab/runtime/agent.jar:/Users/tiago/DEV/DELTA/nailab/runtime/lib/*:/Users/tiago/DEV/DELTA/nailab/runtime/data/additional_libs/*" -Dagent.wrapper.port=60972 -Dagent.wrapper.background=false com.testsigma.agent.TestsigmaAgent
#cd /Users/tiago/DEV/DELTA/nailab ; /usr/bin/env /Library/Java/JavaVirtualMachines/adoptopenjdk-15.jdk/Contents/Home/bin/java -agentlib:jdwp=transport=dt_socket,server=n,suspend=y,address=localhost:50491 -XX:+ShowCodeDetailsInExceptionMessages @/var/folders/vz/82y984kd1hl1yxxt9mfr619c0000gn/T/cp_5yij55mv7zycpyfdd4elbv3qq.argfile com.testsigma.TestsigmaWebApplication

exit 0
