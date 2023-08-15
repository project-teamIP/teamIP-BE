#!/usr/bin/env bash

REPOSITORY=/home/ubuntu/heyhi

cd $REPOSITORY

APP_NAME=heyhi-project
JAR_NAME=$(ls $REPOSITORY/build/libs/ | grep 'SNAPSHOT.jar' | tail -n 1)
JAR_PATH=$REPOSITORY/build/libs/$JAR_NAME
CURRENT_PID=$(pgrep -f $APP_NAME)

DEPLOY_LOG="$REPOSITORY/deploy.log"

if [ -z $CURRENT_PID ]
then
  echo "> 실행 중인 애플리케이션이 없습니다. <" >> $DEPLOY_LOG
else
  echo "> 실행 중인 $CURRENT_PID 종료합니다. <" >> $DEPLOY_LOG
  kill -15 $CURRENT_PID
  sleep 5
fi

echo "> $JAR_PATH 배포"
nohup java -jar $JAR_PATH > /dev/null 2> /dev/null < /dev/null &
