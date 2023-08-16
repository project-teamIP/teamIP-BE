#!/usr/bin/env bash

REPOSITORY="/home/ubuntu/heyhi"
TIME_NOW=$(date +%c)
APP_NAME='heyhello'
JAR_NAME=$(ls $REPOSITORY/build/libs/ | grep SNAPSHOT.jar | tail -n 1)
JAR_PATH=$REPOSITORY/build/libs/$JAR_NAME
DEPLOY_LOG="$REPOSITORY/deploy.log"

CURRENT_PID=$(pgrep -f $APP_NAME)
echo "-----------------------------------------" >> $DEPLOY_LOG
echo "[$TIME_NOW] > 현재 실행 중인 .jar PID=$CURRENT_PID" >> $DEPLOY_LOG

if [ -z $CURRENT_PID ]
then
  echo "[$TIME_NOW] > 실행 중인 애플리케이션이 없습니다." >> $DEPLOY_LOG
else
  echo "[$TIME_NOW] > PID=$CURRENT_PID 종료." >> $DEPLOY_LOG
  kill -15 $CURRENT_PID
  sleep 5
  if ps -f $CURRENT_PID > /dev/null
  then
    echo "[$TIME_NOW] > 프로세스가 정상정료되지 않으므로 강제종료 합니다." >> $DEPLOY_LOG
    kill -9 $CURRENT_PID
    fi
fi

echo "[$TIME_NOW] > $JAR_PATH 배포" >> $DEPLOY_LOG
nohup java -jar $JAR_PATH > $REPOSITORY/nohup.log 2> $REPOSITORY/error.log < /dev/null & echo "[$TIME_NOW] > 프로세스 시작, PID=$!" >> $DEPLOY_LOG

sleep 15

CURRENT_PID=$(pgrep -f $APP_NAME)
echo "[$TIME_NOW] > 재배포된 .jar PID=$CURRENT_PID" >> $DEPLOY_LOG
