#!/usr/bin/env bash

REPOSITORY="/home/ubuntu/heyhi"
TIME_NOW=$(date +%c)

APP_NAME=heyhi-project
JAR_NAME=$(ls $REPOSITORY/build/libs/ | grep 'SNAPSHOT.jar' | tail -n 1)
JAR_PATH=$REPOSITORY/build/libs/$JAR_NAME
CURRENT_PID=$(pgrep -f $APP_NAME)

DEPLOY_LOG="$REPOSITORY/deploy.log"

if [ -z $CURRENT_PID ]
then
  echo "$TIME_NOW > 실행 중인 애플리케이션이 없습니다. <" >> $DEPLOY_LOG
else
  echo "$TIME_NOW > 실행 중인 $CURRENT_PID 종료합니다. <" >> $DEPLOY_LOG
  kill -15 $CURRENT_PID
  sleep 5
fi

echo "$TIME_NOW > $JAR_PATH 배포" >> $DEPLOY_LOG
nohup java -jar $JAR_PATH > /dev/null 2> /dev/null < /dev/null &
