#!/bin/bash
set -x
set -e

echo "Starting compilation of local agent source"

cd automator
mvn clean install

cd ../agent
mvn clean install

cd ../agent-launcher
mvn clean install

cd ..

echo "Completed compilation of local agent source"

cp agent/target/agent.jar runtime/agent.jar
cp agent-launcher/target/agent-launcher.jar runtime/agent-launcher.jar
cp automator/target/automator.jar runtime/automator.jar

cp agent/src/main/scripts/posix/*.sh runtime/

cd runtime
ls -la 

#export DTS_AGENT_JAR=/Users/tiago/DEV/DELTA/nailab/runtime
#export TS_ROOT_DIR=/Users/tiago/DEV/DELTA/nailab/runtime
#export TS_AGENT_JAR_PATH=/Users/tiago/DEV/DELTA/nailab/runtime
#export MAIN_JAR_FILE=/Users/tiago/DEV/DELTA/nailab/runtime/agent-launcher.jar
#export TS_AGENT_JAR=/Users/tiago/DEV/DELTA/nailab/runtime/agent.jar
#export TS_DATA_DIR=/Users/tiago/DEV/DELTA/nailab/runtime/data

# /Users/tiago/DEV/DELTA/nailab/runtime/jre/bin/java -cp "/Users/tiago/DEV/DELTA/nailab/runtime/agent.jar:/Users/tiago/DEV/DELTA/nailab/runtime/lib/*:/Users/tiago/DEV/DELTA/nailab/runtime/data/additional_libs/*" -Dagent.wrapper.port=60972 -Dagent.wrapper.background=false com.testsigma.agent.TestsigmaAgent


exit 0
